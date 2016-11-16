package com.yuyh.easydao.exception;

import android.support.annotation.StringDef;

public class DBException extends Exception {

    private String errMsg;

    public DBException(@ErrMsg String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
    }

    public DBException(@ErrMsg String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errMsg = errMsg;
    }

    @StringDef({
            ErrMsg.ERR_INVALID_ID
    })
    public @interface ErrMsg {

        String ERR_INVALID_ID = "";

    }

}
