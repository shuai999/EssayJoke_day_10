package com.example.administrator.framelibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Email: 2185134304@qq.com
 * Created by JackChen 2018/4/6 19:42
 * Version 1.0
 * Params:
 * Description:  接口实现类
*/
public class DaoSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSqliteDatabase ;
    private Class<T> mClazz ;

    private static final Object[] mPutMethodArgs = new Object[2];

    private static final Map<String, Method> mPutMethods
            = new ArrayMap<>();


    public void init(SQLiteDatabase sqLiteDatabase , Class<T> clazz){
        this.mSqliteDatabase = sqLiteDatabase ;
        this.mClazz = clazz ;

        // 创建表  这个是完整的创建表语句
        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/


        // 下边是通过 StringBuffer去动态的拼接表语句，目的就是用append拼接出和上边一样一样的 结构

        StringBuffer sb = new StringBuffer() ;
        sb.append("create table if not exists ")    // 如果表不存在
                .append(DaoUtil.getTableName(mClazz)) // 通过 class创建 表名
                .append("(id integer primary key autoincrement, ") ;


        // 获取Person中所有属性，有多少就可以获取多少

        // name就代表 Person中所有的 值，比如是 name、age等等；
        // type就代表的是该值对应的所有类型，比如是String、int等等；

        Field[] fields = mClazz.getDeclaredFields() ;
        for (Field field : fields) {
            // 设置权限，private、public、protected都可以
            field.setAccessible(true);

            // 注意：这里是通过反射，取出 Person中的 name text,
            // 首先获取 name，然后再获取name对应的类型 text，这里String 就对应的是 text

            // 获取的name
            String name = field.getName() ;
            // 获取的name的类型type（其实就是text类型，在数据库中 text类型就对应的是String）
            String type = field.getType().getSimpleName();

            // type需要转换 ：int -> integer String -> text
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ") ;
        }

        // 这里是把最后的 ", " 替换成 ")"
        sb.replace(sb.length() - 2 , sb.length()  ,")") ;
        Log.e("TAG" , "表语句 --> "+sb.toString()) ;

        // 执行创建表
        mSqliteDatabase.execSQL(sb.toString());

    }


    /**
     * 插入数据库，t 是任意对象
     */
    @Override
    public long insert(T obj) {
        /*ContentValues values = new ContentValues();
        values.put("name",person.getName());
        values.put("age",person.getAge());
        values.put("flag",person.getFlag());
        db.insert("Person",null,values);*/


        // 这里使用的其实还是原生的方式，只是把 obj转成ContentValues
        ContentValues values = contentValuesByObj(obj) ;

        return mSqliteDatabase.insert(DaoUtil.getTableName(mClazz) , null , values);
    }


    /**
     * 批量插入
     */
    @Override
    public void insert(List<T> datas) {
        // 批量插入采用事务优化
        mSqliteDatabase.beginTransaction();
        for (T data : datas) {
            // 调用单条数据插入
            long number = insert(data) ;
//            Log.e("TAG" , "number -> " + number) ;
        }
        mSqliteDatabase.setTransactionSuccessful();
        mSqliteDatabase.endTransaction();
    }

    @Override
    public List<T> query() {
        Cursor cursor = mSqliteDatabase.query(DaoUtil.getTableName(mClazz), null, null, null, null, null, null);
        return cursorToList(cursor) ;
    }


    /**
     * 查询所有数据
     */
    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            // 不断的从游标里面获取数据
            do {
                try {
                    // 通过反射new对象
                    T instance = mClazz.newInstance();
                    Field[] fields = mClazz.getDeclaredFields();


                    for (Field field : fields) {
                        // 遍历属性
                        field.setAccessible(true);
                        String name = field.getName();
                        // 获取角标  获取在第几列
                        int index = cursor.getColumnIndex(name);
                        if (index == -1) {
                            continue;
                        }

                        // 通过反射获取 游标的方法  field.getType() -> 获取的类型
                        Method cursorMethod = cursorMethod(field.getType());
                        if (cursorMethod != null) {
                            // 通过反射获取了 value
                            Object value = cursorMethod.invoke(cursor, index);
                            if (value == null) {
                                continue;
                            }

                            // 处理一些特殊的部分
                            if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                                if ("0".equals(String.valueOf(value))) {
                                    value = false;
                                } else if ("1".equals(String.valueOf(value))) {
                                    value = true;
                                }
                            } else if (field.getType() == char.class || field.getType() == Character.class) {
                                value = ((String) value).charAt(0);
                            } else if (field.getType() == Date.class) {
                                long date = (Long) value;
                                if (date <= 0) {
                                    value = null;
                                } else {
                                    value = new Date(date);
                                }
                            }

                            // 通过反射注入
                            field.set(instance, value);
                        }
                    }
                    // 加入集合
                    list.add(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // 获取游标的方法
    private Method cursorMethod(Class<?> type) throws Exception {
        String methodName = getColumnMethodName(type);
        // type String getString(index); int getInt; boolean getBoolean
        Method method = Cursor.class.getMethod(methodName, int.class);
        return method;
    }

    private String getColumnMethodName(Class<?> fieldType) {
        String typeName;
        if (fieldType.isPrimitive()) {
            typeName = DaoUtil.capitalize(fieldType.getName());
        } else {
            typeName = fieldType.getSimpleName();
        }
        String methodName = "get" + typeName;
        if ("getBoolean".equals(methodName)) {
            methodName = "getInt";
        } else if ("getChar".equals(methodName) || "getCharacter".equals(methodName)) {
            methodName = "getString";
        } else if ("getDate".equals(methodName)) {
            methodName = "getLong";
        } else if ("getInteger".equals(methodName)) {
            methodName = "getInt";
        }
        return methodName;
    }


    /**
     *  把 obj 转成 ContentValues 利用反射
     *  反射其实就是获取的是属性、方法等东西
     */
    public ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues() ;

        // 封装values
        Field[] fields = mClazz.getDeclaredFields() ;
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                // 这里的key 就指的是获取的是 Person中的  name、age等所有字段，通过for循环有多少拿多少
                String key = field.getName() ;
                // 获取value
                Object value = field.get(obj) ;

                mPutMethodArgs[0] = key ;
                mPutMethodArgs[1] = value ;


                String filedTypeName = field.getType().getName() ;
                Method putMethod = mPutMethods.get(filedTypeName) ;
                if (putMethod == null){
                    // 获取put()方法
                    putMethod = ContentValues.class.getMethod("put", String.class, value.getClass());
                    mPutMethods.put(filedTypeName , putMethod) ;
                }

                // 通过反射执行
                putMethod.invoke(values , mPutMethodArgs) ;

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                mPutMethodArgs[0] = null ;
                mPutMethodArgs[1] = null ;
            }

        }
        return values;
    }



    /**
     * 删除
     */
    public int delete(String whereClause, String[] whereArgs) {
        return mSqliteDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    /**
     * 更新  这些你需要对  最原始的写法比较明了 extends
     */
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = contentValuesByObj(obj);
        return mSqliteDatabase.update(DaoUtil.getTableName(mClazz),
                values, whereClause, whereArgs);
    }

    // 查询
    // 修改
    // 删除
}
