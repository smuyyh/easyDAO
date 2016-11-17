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

import android.database.sqlite.SQLiteDatabase;

import com.yuyh.easydao.base.BaseEntity;
import com.yuyh.easydao.exception.DBException;

import java.util.List;

/**
 * Data Access Object interface
 *
 * @param <T>
 * @author yuyh.
 * @date 2016/11/16.
 */
public interface IDAO<T extends BaseEntity> {

    void initTable(String tableName, Class<T> clazz);

    /**
     * get the sqlite database object
     *
     * @return
     */
    SQLiteDatabase getDatabase();

    /**
     * get count of entities
     *
     * @return
     * @throws DBException
     */
    long getCount() throws DBException;

    boolean isTableExist(String tableName) throws DBException;

    /**
     * check table exists
     *
     * @return
     * @throws DBException
     */
    boolean isTableExist() throws DBException;

    /**
     * create table
     *
     * @throws DBException
     */
    void createTable() throws DBException;

    /**
     * create table
     *
     * @param tableName table name
     * @throws DBException
     */
    <T> void createTable(Class<T> entityClass, String tableName) throws DBException;

    /**
     * drop table
     *
     * @throws DBException
     */
    void dropTable() throws DBException;

    /**
     * drop all table
     *
     * @throws DBException
     */
    void dropAllTable() throws DBException;

    /**
     * save database entity
     *
     * @param entity
     * @throws DBException
     */
    void save(T entity) throws DBException;

    /**
     * delete database entity by id(primary key)
     *
     * @param id
     * @throws DBException
     */
    void delete(int id) throws DBException;

    /**
     * delete database entity by ids(primary key)
     *
     * @param ids
     * @throws DBException
     */
    void delete(int[] ids) throws DBException;

    /**
     * delete all data
     *
     * @throws DBException
     */
    void deleteAll() throws DBException;

    /**
     * update entity
     *
     * @param entity
     * @throws DBException
     */
    void update(T entity) throws DBException;

    /**
     * update entity by a condition string
     *
     * @param condition part of the update SQL command after keyword 'WHERE'
     *                  (i.e."UPDATE Person SET age = 35 WHERE condition")
     *                  (e.g. condition -- "name = 'Richard' OR name = 'Jefferson'")
     * @param entity
     * @throws DBException
     */
    void updateByCondition(String condition, T entity) throws DBException;

    /**
     * find entity by id
     *
     * @param id
     * @return
     * @throws DBException
     */
    T find(int id) throws DBException;

    /**
     * find last entity
     *
     * @return
     * @throws DBException
     */
    T findLastEntity() throws DBException;

    /**
     * find entities by a condition string
     *
     * @param condition part of the select SQL command after keyword 'WHERE'
     *                  (i.e."SELECT * FROM Person WHERE condition")
     *                  (e.g. condition -- "name = 'Richard' OR name = 'Jefferson'")
     * @return
     */
    List<T> findByCondition(String condition) throws DBException;

    /**
     * find all entities
     *
     * @return
     * @throws DBException
     */
    List<T> findAll() throws DBException;
}
