package com.yuyh.easydao.base;

import com.yuyh.easydao.annotation.AutoIncrement;
import com.yuyh.easydao.exception.DBException;
import com.yuyh.easydao.annotation.Id;

/**
 * the abstract base class for entity in the database
 */
public abstract class BaseEntity {

    @Id
    @AutoIncrement
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws DBException {
        if (id < 1) {
            throw new DBException(DBException.ErrMsg.ERR_INVALID_ID);
        }
        this.id = id;
    }
}