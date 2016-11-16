package com.yuyh.easydao;

import android.content.Context;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;
import com.yuyh.easydao.impl.DAO;
import com.yuyh.easydao.interfaces.IDAO;
import com.yuyh.easydao.interfaces.IDB;
import com.yuyh.easydao.interfaces.IDBListener;

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
    public <T extends BaseEntity> IDAO<T> getDatabase(int dbVer, String dbName, String tableName, Class<T> clazz, IDBListener<T> listener) throws DBException {

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
