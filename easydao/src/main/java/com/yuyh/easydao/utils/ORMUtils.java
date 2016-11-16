package com.yuyh.easydao.utils;

import com.yuyh.easydao.annotation.AutoIncrement;
import com.yuyh.easydao.annotation.Column;
import com.yuyh.easydao.annotation.Id;
import com.yuyh.easydao.annotation.Unique;

import java.lang.reflect.Field;

public class ORMUtils {

    /**
     * check the field is annotated by ID or Column
     *
     * @param field
     * @return
     */
    public static boolean hasOrmAnnotation(Field field) {
        boolean isNeedMap = false;

        if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Column.class)) {
            isNeedMap = true;
        }
        return isNeedMap;
    }

    /**
     * get database column name by field name
     *
     * @param field
     * @return
     */
    public static String getColumnName(Field field) {
        if (!hasOrmAnnotation(field)) {
            return null;
        }

        return field.getName();
    }

    /**
     * get primary key form fields
     *
     * @param fields
     * @return
     */
    public static Field getPKField(Field[] fields) {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class))
                return field;
        }
        return null;
    }

    /**
     * generate sql for create table by entity class
     *
     * @param clazz        entity class
     * @param strTableName table name
     * @return
     */
    public static String genCreateTableSQL(Class clazz, String strTableName) {
        boolean isHaveKeyId = false;
        boolean isHaveColumnAnnotation = false;
        String strIdName = null;

        Field[] fields = Utils.getDeclaredField(clazz);
        if (fields.length <= 0)
            return null;

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS " + strTableName + " ( ");
        StringBuilder sbUnique = new StringBuilder("");

        for (Field field : fields) { // add id
            if (field.isAnnotationPresent(Id.class)) {
                // can not have more than one id
                if (isHaveKeyId)
                    continue;

                isHaveColumnAnnotation = true;

                // columnName
                Id id = field.getAnnotation(Id.class);
                strIdName = getColumnName(field);
                sb.append(strIdName);
                sb.append(" ");

                // type
                sb.append(fieldType2DBType(field.getGenericType().toString()));
                sb.append(" ");

                // primary key
                sb.append("NOT NULL PRIMARY KEY ");

                // is auto-increment
                if (field.isAnnotationPresent(AutoIncrement.class)
                        && (field.getGenericType().toString().equals("class java.lang.Integer") || field
                        .getGenericType().toString().equals("int"))) {
                    sb.append("AUTOINCREMENT");
                }
                sb.append(",");

                isHaveKeyId = true;
            } else if (field.isAnnotationPresent(Column.class)) { // add column
                // columnName
                Column col = field.getAnnotation(Column.class);
                String strColName = getColumnName(field);
                sb.append(strColName);
                sb.append(" ");

                // type
                sb.append(fieldType2DBType(field.getGenericType().toString()));

                // nullable
                boolean canBeNull = col.nullable();
                if (!canBeNull) {
                    sb.append(" NOT NULL");
                }
                sb.append(",");

                // is unique
                if (field.isAnnotationPresent(Unique.class)) {
                    sbUnique.append("CONSTRAINT \"u" + strColName.toLowerCase() + "\" UNIQUE (\"" + strColName + "\"),");
                }
                isHaveColumnAnnotation = true;
            }
        }

        // nothing
        if (!isHaveColumnAnnotation && !isHaveKeyId)
            return null;

        // unique
        sb.append("CONSTRAINT \"u" + strIdName.toLowerCase() + "\" UNIQUE (\"" + strIdName + "\")");
        if (sbUnique.length() > 0) {
            sb.append(",");
            sb.append(sbUnique.deleteCharAt(sbUnique.length() - 1));
        }
        // end
        sb.append(" )");

        return sb.toString();
    }

    /**
     * convert field type to a database column type
     *
     * @param fieldGenericTyp
     * @return
     */
    private static String fieldType2DBType(String fieldGenericTyp) {
        if (fieldGenericTyp.equals("class java.lang.String")) {
            return "VARCHAR";
        } else if (fieldGenericTyp.equals("class java.lang.Boolean") || fieldGenericTyp.equals("boolean")
                || fieldGenericTyp.equals("class java.lang.Integer") || fieldGenericTyp.equals("int")
                || fieldGenericTyp.equals("class java.lang.Long") || fieldGenericTyp.equals("long")
                || fieldGenericTyp.equals("class java.lang.Short") || fieldGenericTyp.equals("short")
                || fieldGenericTyp.equals("class java.lang.Byte") || fieldGenericTyp.equals("byte")) {
            return "INTEGER";
        } else if (fieldGenericTyp.equals("class [B")) {
            return "BLOB";
        } else if (fieldGenericTyp.equals("class java.lang.Float") || fieldGenericTyp.equals("float")) {
            return "float";
        } else if (fieldGenericTyp.equals("class java.lang.Double") || fieldGenericTyp.equals("double")) {
            return "double";
        }
        return null;
    }

}
