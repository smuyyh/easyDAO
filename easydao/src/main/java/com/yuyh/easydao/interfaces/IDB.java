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
package com.yuyh.easydao.interfaces;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;

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
 *
 * @author yuyh.
 * @date 2016/11/16.
 */
public interface IDB {

    <T extends BaseEntity> IDAO<T> getDatabase(int dbVer, String dbName, String tableName,
                                               Class<T> clazz, IDBListener<T> listener) throws DBException;

    <T extends BaseEntity> IDAO<T> getDatabase(int dbVer, String dbName, IDBListener<T> listener) throws DBException;

    boolean isDatabaseExists(String dbName);

    /**
     * delete database file
     *
     * @param dbName database name
     * @return
     */
    boolean deleteDatabase(String dbName);
}
