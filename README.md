# easyDAO

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-8%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=8)<br/>

easyDAO is a light-weight&amp;fast ORM library for Android that maps objects to SQLite , it becomes much easier to operate the SQLite database.

[中文文档](https://github.com/smuyyh/easyDAO/blob/master/README-CN.md)

## Features
1. easyDAO makes the database CRUD easier for Android.
2. maps objects to SQLite by annotation.

## Download
```
dependencies {
    compile 'com.yuyh.easydao:easydao:1.0.0'
}
```

## A Brief Guide
```
1. Declare a public entity class inherit from {@link com.yuyh.easydao.base.BaseEntity}.
2. Use notation '@{@link com.yuyh.easydao.annotation.Column}' to make an element a column in the database,
   the column name will be the same as the element.
3. Every element with the '@Column' anotation MUST have getter/setter.
4. Call {@link IDB#getDatabase(int, String, String, Class, IDBListener)} to create the database and table,
   and use the returned value to operate the database.
```

## Notices(important!!!)

1. The name of getter/setter method MUST strictly follow the rule: getElement, setElement, setOk, isOk.
(i.e. get/set + element name with the first character in upper case),
NOTE that the auto generated getters/setters of the Boolean/boolean element is following this rule. e.g.
    ```java
    @Column
    private boolean isOk;

    public boolean isOk(){
        return isOk;
    }

    public void setOk(boolean isOk){
        this.isOk = isOk;
    }
    ```
2. For elements with Boolean/boolean type, the internal type in database is INTEGER, 1 means true, 0 means false.
You may notice this if you want to find with condition.

3. By default, the value of the column can NOT be null, so that it would NOT save to database if the value is NULL. use (nullable = true) to allow this.

## Quick Setup

### 1. Download module and add to the project or direct add [dependencies](#download)

### 2. get DB singleton object
```java
IDAO<UserBean> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
```
  there is no tableName and entityClass parameter, called```IDAO.initTable(String, Class)``` to init table.

  or

```java
IDAO<UserBean> dao = DB.getInstance(mContext)
                       .getDatabase(1, databaseName, tablename, UserBean.class, listener);
```
### 3. CRUD
IDAO<T> interface provides the following methods for CRUD

```java
/**
 * get the sqlite database object
 *
 * @return
 */
SQLiteDatabase getDatabase();

/**
 * init table name and entityClass
 *
 * @param tableName
 * @param clazz
 */
void initTable(String tableName, Class<T> clazz);

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
```

## License
```
Copyright (c) 2016 smuyyh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
