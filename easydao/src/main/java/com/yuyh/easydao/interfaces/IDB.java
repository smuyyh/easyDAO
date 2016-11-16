package com.yuyh.easydao.interfaces;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;

/**
 * @author yuyh.
 * @date 2016/11/16.
 */
public interface IDB {

    <T extends BaseEntity> IDAO<T> getDatabase(int dbVer, String dbName, String tableName,
                                               Class<T> clazz, IDBListener<T> listener) throws DBException;
}
