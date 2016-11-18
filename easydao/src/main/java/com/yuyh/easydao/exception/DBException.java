/**
 * Copyright (C) 2016 smuyyh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yuyh.easydao.exception;

import com.yuyh.easydao.utils.LogUtils;

/**
 * database excertion
 *
 * @author yuyh.
 * @date 2016/11/16.
 */
public class DBException extends Exception {

    private String errMsg;

    private String desc;

    public DBException(@ErrMsg String errMsg) {
        this(errMsg, null);
    }

    public DBException(String errMsg, Throwable cause) {
        super(errMsg, cause);
        this.errMsg = errMsg;
        if (cause != null) {
            this.desc = cause.toString();
            LogUtils.e(desc);
        }
    }

    @Override
    public String getMessage() {
        return errMsg;
    }

    public String getDesc() {
        return desc;
    }
}
