package com.yuyh.easydao;

import android.content.Context;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;
import com.yuyh.easydao.impl.DAO;
import com.yuyh.easydao.interfaces.IDAO;
import com.yuyh.easydao.interfaces.IDB;
import com.yuyh.easydao.interfaces.IDBListener;

/**
 * Database interface.
 * <p/>
 * 1. Declare a public entity class inherit from {@link BaseEntity}.
 * 2. Use notation '@{@link com.yuyh.easydao.annotation.Column}' to make an element a column in the database,
 * the column name will be the same as the element.
 * 3. Every element with the '@Column' notation MUST have getter/setter.
 * 4. Call {@link IDB#getDatabase(int, String, String, Class, IDBListener)} to create the database and table,
 * and use the returned value to operate the database.
 * <p/>
 * NOTICES:
 * 1. The name of getter/setter method MUST strictly follow the rule: getElement, setElement, setOk, isOk,
 * (i.e. get/set + element name with the first character in upper case),NOTE that the auto generated getters/setters of the Boolean/boolean
 * element is following this rule(e.g. boolean isOk; getter:boolean isOk(){return isOk}  setter:void setOk(isOk){this.isOk = isOk})
 * 2. For elements with Boolean/boolean type, the internal type in database is INTEGER, 1 means true, 0 means false.
 * You may notice this if you want to find with condition.
 * 3. By default, the value of the column can NOT be null, so that it would NOT save to database if the value is NULL.
 * use (nullable = true) to allow this.
 */
public class DB implements IDB {

    private static DB insatnce;

    private Context mContext;

    private DB() {

    }

    private DB(Context context) {
        this.mContext = context;
    }

    public static DB getInstance(Context context) {
        return insatnce == null ? (insatnce = new DB(context)) : insatnce;
    }


    @Override
    public <T extends BaseEntity> IDAO<T> getDatabase(int dbVer, String dbName,
                                                      String tableName, Class<T> clazz,
                                                      IDBListener<T> listener) throws DBException {
        DAO<T> dao = new DAO<>(mContext, dbName, tableName, clazz, dbVer);
        if (!dao.isTableExist()) {
            dao.createTable();
        }

        if (dao.info.isUpdate) {
            dao.info.isUpdate = false;
            if (listener != null) {
                listener.onUpdate(dao, dao.info.from, dao.info.to);
            }
        }
        return dao;
    }
}
