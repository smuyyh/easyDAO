package com.yuyh.easydao.exception;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        ErrMsg.ERR_INVALID_ID,
        ErrMsg.ERR_GET_COUNT,
        ErrMsg.ERR_IS_TABLE_EXISTS,
        ErrMsg.ERR_CREATE_TABLE,
        ErrMsg.ERR_DROP_TABLE,
        ErrMsg.ERR_SAVE_PARAM,
        ErrMsg.ERR_DEL_PARAM,
        ErrMsg.ERR_GET_PRIMARY_KEY,
        ErrMsg.ERR_DEL,
        ErrMsg.ERR_UPDATE_PARAM,
        ErrMsg.ERR_GET_PRIMARY_KEY_VALUE,
        ErrMsg.ERR_UPDATE,
        ErrMsg.ERR_FIND_CONDITION,
        ErrMsg.ERR_FIND
})
/**
 * database operation error type & message
 *
 * @author yuyh.
 * @date 2016/11/18.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ErrMsg {

    String ERR_INVALID_ID = "invalid entity id";

    String ERR_GET_COUNT = "execute query error during get count operation";

    String ERR_IS_TABLE_EXISTS = "check table exists error";

    String ERR_CREATE_TABLE = "create table execute SQL error";

    String ERR_DROP_TABLE = "drop table execute SQL error";

    String ERR_SAVE_PARAM = "param error during save operation";

    String ERR_DEL_PARAM = "param error during del operation";

    String ERR_GET_PRIMARY_KEY = "get PK(Primary Key) field error";

    String ERR_GET_PRIMARY_KEY_VALUE = "get PK(Primary Key) value error";

    String ERR_DEL = "execute SQL error during delete operation";

    String ERR_UPDATE_PARAM = "param error during update operation";

    String ERR_UPDATE = "execute SQL error during update operation";

    String ERR_FIND_CONDITION = "condition can not be null";

    String ERR_FIND = "execute SQL error during find operation";
}