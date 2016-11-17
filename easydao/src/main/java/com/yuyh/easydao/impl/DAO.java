/**
 * Copyright (C) 2016 smuyyh
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuyh.easydao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;
import com.yuyh.easydao.interfaces.IDAO;
import com.yuyh.easydao.utils.LogUtils;
import com.yuyh.easydao.utils.ORMUtils;
import com.yuyh.easydao.utils.Utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author yuyh.
 * @date 2016/11/16.
 */
public class DAO<T extends BaseEntity> extends SQLiteOpenHelper implements IDAO<T> {

    private Context mContext;

    private String mDBName;
    private String mTableName;
    private int mVersion;

    private SQLiteDatabase db;
    private Field[] fields;
    private Class<T> entityClass;

    public DBUpdateInfo info = new DBUpdateInfo();

    /**
     * constructor. only for operate the database and cannot operate the specified table.
     *
     * @param context to use to open or create the database
     * @param dbName  of the database name, it will be stored in /data/data/package_name/files
     * @param version number of the database (starting at 1)
     */
    public DAO(Context context, String dbName, int version) {
        this(context, dbName, null, null, version);
    }

    /**
     * constructor
     *
     * @param context     to use to open or create the database
     * @param dbName      of the database name, it will be stored in /data/data/package_name/files
     * @param tableName   the name of table to needs to be operated
     * @param entityClass class for entity
     * @param version     number of the database (starting at 1)
     */
    public DAO(Context context, String dbName, String tableName, Class<T> entityClass, int version) {
        super(context, dbName, null, version);

        this.mContext = context;
        this.mDBName = dbName;
        this.mTableName = tableName;
        this.mVersion = version;

        this.db = getWritableDatabase();
        this.fields = Utils.getDeclaredField(entityClass);
        this.entityClass = entityClass;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        info.isUpdate = true;
        info.from = oldVersion;
        info.to = newVersion;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        info.isUpdate = true;
        info.from = oldVersion;
        info.to = newVersion;
    }

    @Override
    public void initTable(String tableName, Class<T> clazz) {
        this.mTableName = tableName;
        this.entityClass = clazz;

        this.fields = Utils.getDeclaredField(entityClass);
    }

    @Override
    public SQLiteDatabase getDatabase() {
        return db == null ? (db = getWritableDatabase()) : db;
    }

    @Override
    public long getCount() throws DBException {
        if (TextUtils.isEmpty(mTableName))
            return 0;

        try {
            openDB(true);

            String sql = "SELECT COUNT(*) FROM " + mTableName;
            LogUtils.d(sql);

            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            long total = cursor.getLong(0);
            return total;
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public boolean isTableExist(String tableName) throws DBException {
        boolean isExist = false;
        if (TextUtils.isEmpty(tableName))
            return isExist;

        try {
            openDB(false);

            String sql = "SELECT COUNT(*) FROM Sqlite_master WHERE TYPE ='table' AND NAME ='" + tableName.trim() + "' ";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    isExist = true;
                }
            }
            return isExist;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public boolean isTableExist() throws DBException {
        return !TextUtils.isEmpty(mTableName) && isTableExist(mTableName);
    }

    @Override
    public void createTable() throws DBException {
        createTable(entityClass, mTableName);
    }

    @Override
    public <T> void createTable(Class<T> entityClass, String tableName) throws DBException {
        try {
            openDB(true);

            String strCreateTable = ORMUtils.genCreateTableSQL(entityClass, tableName);
            LogUtils.i(strCreateTable);
            db.execSQL(strCreateTable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public void dropTable() throws DBException {
        try {
            openDB(true);

            String delSql = "DROP TABLE " + mTableName;
            LogUtils.d(delSql);
            db.beginTransaction();
            db.execSQL(delSql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            db.endTransaction();
            closeDB();
        }
    }

    @Override
    public void dropAllTable() throws DBException {
        String strTabSql = "select name from sqlite_master where type='table' order by name";
        LogUtils.d("dropAllTable:" + strTabSql);

        try {
            openDB(false);

            db.beginTransaction();
            Cursor cursor = db.rawQuery(strTabSql, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                if (name.equals("sqlite_sequence")) {
                    continue;
                }
                String strDelSql = "DROP TABLE " + name;
                LogUtils.d(strDelSql);
                db.execSQL(strDelSql);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            db.endTransaction();
            closeDB();
        }
    }

    @Override
    public void save(T entity) throws DBException {
        ContentValues values;
        try {
            openDB(true);

            values = Utils.putValue(fields, entity);
            if (values == null || values.size() <= 0) {
                throw new DBException(null);
            }

            long flag = db.insert(mTableName, null, values);
            if (flag < 1) {
                throw new DBException(null);
            }
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public void delete(int id) throws DBException {
        delete(new int[]{id});
    }

    @Override
    public void delete(int[] ids) throws DBException {
        int length = ids == null ? 0 : ids.length;
        if (length <= 0) {
            throw new DBException(null);
        }

        Field pkField = ORMUtils.getPKField(fields);
        if (pkField == null) {
            throw new DBException(null);
        }

        Class pkType = pkField.getType();
        String columnName = ORMUtils.getColumnName(pkField);

        try {
            openDB(true);

            StringBuilder sbSql = new StringBuilder("DELETE FROM " + mTableName + " WHERE " + columnName);

            if (ids.length == 1) {
                sbSql.append(" = ");
            } else {
                sbSql.append(" in (");
            }

            String strSep = "";
            if (pkType == String.class) {
                strSep = "'";
            }

            StringBuilder strEntityIds = new StringBuilder("");
            for (Serializable id : ids) {
                strEntityIds.append(strSep);
                strEntityIds.append(id);
                strEntityIds.append(strSep);
                strEntityIds.append(",");
            }
            strEntityIds.deleteCharAt(strEntityIds.length() - 1);
            sbSql.append(strEntityIds.toString());

            if (ids.length > 1) {
                sbSql.append(")");
            }
            LogUtils.d(sbSql.toString());
            db.execSQL(sbSql.toString());
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public void deleteAll() throws DBException {
        try {
            openDB(true);

            String delSql = "DELETE FROM " + mTableName;
            String revSeqSql = "UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='" + mTableName + "'";

            db.beginTransaction();
            db.execSQL(delSql);
            db.execSQL(revSeqSql);
            db.setTransactionSuccessful();

            LogUtils.d(delSql);
            LogUtils.d(revSeqSql);
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            db.endTransaction();
            closeDB();
        }
    }

    @Override
    public void update(T entity) throws DBException {
        if (entity == null) {
            throw new DBException(null);
        }

        Field pkField = ORMUtils.getPKField(fields);
        if (pkField == null) {
            throw new DBException(null);
        }

        Object pkValue = Utils.getPropValue(entity, pkField, entityClass);
        if (pkValue == null) {
            throw new DBException(null);
        }

        ContentValues values;

        try {
            openDB(true);

            values = Utils.putValue(fields, entity);
            if (values.size() <= 0) {
                throw new DBException(null);
            }

            int flag = db.update(mTableName, values, pkField.getName() + "=?",
                    new String[]{String.valueOf(pkValue)});
            if (flag < 1) {
                throw new DBException(null);
            }
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public void updateByCondition(String condition, T entity) throws DBException {
        if (entity == null) {
            throw new DBException(null);
        }

        ContentValues values;

        try {
            openDB(true);

            values = Utils.putValue(fields, entity);
            values.remove("id");
            if (values.size() <= 0) {
                throw new DBException(null);
            }

            int flag = db.update(mTableName, values, condition, null);
            if (flag < 1) {
                throw new DBException(null);
            }

        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
    }

    @Override
    public T find(int id) throws DBException {
        Field pkField = ORMUtils.getPKField(fields);
        if (pkField == null) {
            throw new DBException(null);
        }

        String columnName = ORMUtils.getColumnName(pkField);
        try {
            openDB(true);

            String sql = "SELECT * FROM " + mTableName + " WHERE " + columnName + "=?";
            LogUtils.d(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{id + ""});
            List<T> objList = Utils.cursor2Entity(entityClass, cursor);
            if (objList != null && objList.size() > 0) {
                return objList.get(0);
            }
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
        return null;
    }

    @Override
    public T findLastEntity() throws DBException {
        Field pkField = ORMUtils.getPKField(fields);
        if (pkField == null) {
            throw new DBException(null);
        }

        String columnName = ORMUtils.getColumnName(pkField);
        try {
            openDB(true);

            String sql = "SELECT * FROM " + mTableName + " ORDER BY " + columnName + " desc LIMIT 1";
            LogUtils.d(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            List<T> objList = Utils.cursor2Entity(entityClass, cursor);
            if (objList != null && objList.size() > 0) {
                return objList.get(0);
            }
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
        return null;
    }

    @Override
    public List<T> findByCondition(String condition) throws DBException {
        if (TextUtils.isEmpty(condition)) {
            throw new DBException(null);
        }

        try {
            openDB(true);

            String sql = "SELECT * FROM " + mTableName + " WHERE " + condition;
            LogUtils.d(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            List<T> objList = Utils.cursor2Entity(entityClass, cursor);
            if (objList != null && objList.size() > 0) {
                return objList;
            }
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
        return null;
    }

    @Override
    public List<T> findAll() throws DBException {
        try {
            openDB(true);

            String sql = "SELECT * FROM " + mTableName;
            LogUtils.d(sql);
            Cursor cursor = db.rawQuery(sql, new String[]{});
            List<T> objList = Utils.cursor2Entity(entityClass, cursor);
            if (objList != null && objList.size() > 0) {
                return objList;
            }
        } catch (Exception e) {
            throw new DBException(null);
        } finally {
            closeDB();
        }
        return null;
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
     *
     * @param checkTable True if need check mTableName, false otherwise
     * @throws DBException
     */
    private void openDB(boolean checkTable) throws DBException {
        if (checkTable && TextUtils.isEmpty(mTableName)) {
            throw new DBException(null);
        }
        db = getWritableDatabase();
    }

    /**
     * Releases a reference to the database
     * closing the database if the last reference was released.
     */
    private void closeDB() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    /**
     * database version update information
     */
    public class DBUpdateInfo {
        public boolean isUpdate = false;
        public int from;
        public int to;
    }
}
