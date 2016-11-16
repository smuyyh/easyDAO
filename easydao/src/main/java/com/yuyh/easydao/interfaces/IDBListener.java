package com.yuyh.easydao.interfaces;

import com.yuyh.easydao.base.BaseEntity;

public interface IDBListener<T extends BaseEntity> {

    /**
     * called on database updated
     *
     * @param dao
     * @param oldVersion
     * @param newVersion
     */
    void onUpdate(IDAO<T> dao, int oldVersion, int newVersion);
}
