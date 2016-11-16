package com.yuyh.easydao.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * get all the fields of the class and the super class
     *
     * @param clazz
     * @return
     */
    public static Field[] getDeclaredField(Class clazz) {
        if (clazz == null)
            return null;

        Field[] superFields = clazz.getSuperclass().getDeclaredFields();
        Field[] fields = clazz.getDeclaredFields();

        Field[] fullFields = new Field[superFields.length + fields.length];

        System.arraycopy(superFields, 0, fullFields, 0, superFields.length);
        System.arraycopy(fields, 0, fullFields, superFields.length, fields.length);

        return fullFields;
    }

    /**
     * Get property value.<br>
     * By default, you will get the property value directly.<br>
     * If you do not have access, you will get the property value by getter/setter.
     *
     * @param entity
     * @param field
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends BaseEntity> Object getPropValue(T entity, Field field, Class<T> clazz) {
        Object obj = null;
        try {
            field.setAccessible(true);
            obj = field.get(entity);
            return obj;
        } catch (IllegalAccessException e) {
            String fieldName = toUpperCaseFirstOne(field.getName());
            try {
                Class type = field.getType();
                if (type == Boolean.class || type == boolean.class) {
                    obj = clazz.getMethod("is" + fieldName).invoke(entity);
                } else {
                    obj = clazz.getMethod("get" + fieldName).invoke(entity);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * Capitalizes the first letter
     *
     * @param name
     * @return
     */
    public static String toUpperCaseFirstOne(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }
        StringBuffer sb = new StringBuffer(name);
        String firstChar = name.substring(0, 1).toUpperCase();
        sb.replace(0, 1, firstChar);
        return sb.toString();
    }

    public static <T extends BaseEntity> List<T> cursor2Entity(Class<T> clazz, Cursor cursor) throws DBException {
        List<T> objList = new ArrayList<>();
        Field[] fields = getDeclaredField(clazz);
        try {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    T obj = clazz.newInstance();

                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        String strColName = cursor.getColumnName(i);
                        for (Field field : fields) {
                            if (field.getName().equals(strColName)) {
                                strColName = toUpperCaseFirstOne(strColName);
                                if (cursor.getType(i) == Cursor.FIELD_TYPE_NULL) {
                                    continue;
                                } else if (cursor.getType(i) == Cursor.FIELD_TYPE_FLOAT) {
                                    clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                            cursor.getFloat(i));
                                } else if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                                    if (field.getGenericType().toString().equals("class java.lang.Boolean")
                                            || field.getGenericType().toString().equals("boolean")) {
                                        // e.g. boolean isOk; public boolean isOk(){ return isOk; }   public void setOk(){}
                                        clazz.getMethod("set" + strColName.replaceFirst("Is", ""), field.getType()).invoke(obj,
                                                cursor.getInt(i) == 1 ? true : false);
                                    } else if (field.getGenericType().toString().equals("class java.lang.Integer")
                                            || field.getGenericType().toString().equals("int")) {
                                        clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                                cursor.getInt(i));
                                    } else if (field.getGenericType().toString().equals("class java.lang.Long")
                                            || field.getGenericType().toString().equals("long")) {
                                        clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                                (long) cursor.getInt(i));
                                    } else if (field.getGenericType().toString().equals("class java.lang.Short")
                                            || field.getGenericType().toString().equals("short")) {
                                        clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                                (short) cursor.getInt(i));
                                    } else if (field.getGenericType().toString().equals("class java.lang.Byte")
                                            || field.getGenericType().toString().equals("byte")) {
                                        clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                                (byte) cursor.getInt(i));
                                    }
                                } else if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
                                    clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                            cursor.getString(i));
                                } else if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                                    clazz.getMethod("set" + strColName, field.getType()).invoke(obj,
                                            cursor.getBlob(i));
                                } else {
                                    throw new DBException(null);
                                }
                                break;
                            }
                        }
                    }
                    objList.add(obj);
                    cursor.moveToNext();
                }
                return objList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(null);
        }
        return objList;
    }

    public static <T> ContentValues putValue(Field[] fields, T entity) throws DBException {
        ContentValues values = new ContentValues();

        for (Field field : fields) {
            if (!field.isAccessible())
                field.setAccessible(true);

            String strColName = ORMUtils.getColumnName(field);
            if (strColName == null)
                continue;

            try {
                if (field.getGenericType().toString().equals("class java.lang.String")) {
                    values.put(strColName, (String) (field.get(entity)));
                } else if (field.getGenericType().toString().equals("class java.lang.Boolean")
                        || field.getGenericType().toString().equals("boolean")) {
                    values.put(strColName, (((Boolean) (field.get(entity))) ? 1 : 0));
                } else if (field.getGenericType().toString().equals("class java.lang.Byte")
                        || field.getGenericType().toString().equals("byte")) {
                    values.put(strColName, (Byte) field.get(entity));
                } else if (field.getGenericType().toString().equals("class [B")) {
                    values.put(strColName, (byte[]) field.get(entity));
                } else if (field.getGenericType().toString().equals("class java.lang.Double")
                        || field.getGenericType().toString().equals("double")) {
                    values.put(strColName, (Double) field.get(entity));
                } else if (field.getGenericType().toString().equals("class java.lang.Float")
                        || field.getGenericType().toString().equals("float")) {
                    values.put(strColName, (Float) field.get(entity));
                } else if (field.getGenericType().toString().equals("class java.lang.Integer")
                        || field.getGenericType().toString().equals("int")) {
                    values.put(strColName, (Integer) field.get(entity));
                } else if (field.getGenericType().toString().equals("class java.lang.Long")
                        || field.getGenericType().toString().equals("long")) {
                    values.put(strColName, (Long) field.get(entity));
                } else if (field.getGenericType().toString().equals("class java.lang.Short")
                        || field.getGenericType().toString().equals("short")) {
                    values.put(strColName, (Short) field.get(entity));
                } else {
                    throw new DBException(null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new DBException(null);
            }
        }
        return values;
    }
}
