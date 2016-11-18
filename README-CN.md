# easyDAO

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-8%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=8)<br/>

easyDAO是一个快速&轻量级的Android SQLite ORM映射框架，尽可能的简化数据库操作。

[English Doc](https://github.com/smuyyh/easyDAO/blob/master/README.md)

## 特征
1. easyDAO简化了数据库的增删改查操作.
2. 通过注解的方式进行数据库对象映射.

## 依赖
```
dependencies {
    compile 'com.yuyh.easydao:easydao:1.1.0'
}
```

## 简介
```
1. 定义一个公共类，继承于{@link com.yuyh.easydao.base.BaseEntity}.
2. 使用'@Column'注解属性以便在数据库表创建一个列, 列名和属性名会保持一致.
3. 每一个带有'@Column'注解的属性都必须有getter/setter方法.
4. 调用{@link IDb#getDb(int, String, String, Class, IDbListener)}方法来创建数据库和表, 用返回值来操作数据库.
```

## 注意事项（重要！！！）

1. getter/setter方法必须严格遵循以下规则: getElement, setElement (即: get/set + 首字母大写的属性名).这里需要注意的是，通过eclipse或as自动生成的Bool类型的getter/setter方法也是遵循该规律的，请勿手动再改。如下：
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
2. 对于类型为Boolean/boolean的属性来说, 他们在数据库内部表示是用INTEGER类型, 1 代表true, 0 代表false. 所以如果要通过条件语句操作，就要注意这个地方了.

3. 默认情况下，列的值是不能为null的(如果为null，将不会保存到数据库), 必要的话可以用'@Column(canBeNull = true)'注解来允许该字段为null.

## 使用方式

### 1. 下载easydao module并添加到工程或者直接添加[依赖](#依赖)
xxx

### 2. 获取DB单例对象
```java
    IDAO<UserBean> dao = DB.getInstance(mContext).getDatabase(1, database, listener);
```
    
上面这个方法没有传入tableName和entityClass参数, 故无法进行表数据相关操作，比如增删改查。可再调用```IDAO.initTable(String, Class)``` 方法来进行初始化.

或者直接使用下面这个方法。注：数据库和表不存在的时候，会自动创建！

```java
    IDAO<UserBean> dao = DB.getInstance(mContext)
                           .getDatabase(1, databaseName, tablename, UserBean.class, listener);
```
### 3. 增删改查
IDAO<T>提供了以下方法进行数据库的增删改查等操作。

```java
/**
 * 获取数据库操作对象
 *
 * @return
 */
SQLiteDatabase getDatabase();

/**
 * 初始化表映射关系
 *
 * @param tableName
 * @param clazz
 */
void initTable(String tableName, Class<T> clazz);

/**
 * 获取表数据量
 *
 * @return
 * @throws DBException
 */
long getCount() throws DBException;

boolean isTableExist(String tableName) throws DBException;

/**
 * 检查表是否存在
 *
 * @return
 * @throws DBException
 */
boolean isTableExist() throws DBException;

/**
 * 建表
 *
 * @throws DBException
 */
void createTable() throws DBException;

/**
 * 建表
 *
 * @param tableName 表名
 * @throws DBException
 */
<T> void createTable(Class<T> entityClass, String tableName) throws DBException;

/**
 * 删除表
 *
 * @throws DBException
 */
void dropTable() throws DBException;

/**
 * 删除数据库所有的表
 *
 * @throws DBException
 */
void dropAllTable() throws DBException;

/**
 * 保存数据
 *
 * @param entity
 * @throws DBException
 */
void save(T entity) throws DBException;

/**
 * 通过id(主键)删除数据
 *
 * @param id
 * @throws DBException
 */
void delete(int id) throws DBException;

/**
 * 通过多个id(主键)删除数据
 *
 * @param ids
 * @throws DBException
 */
void delete(int[] ids) throws DBException;

/**
 * 删除所有数据
 *
 * @throws DBException
 */
void deleteAll() throws DBException;

/**
 * 更新一条数据
 *
 * @param entity
 * @throws DBException
 */
void update(T entity) throws DBException;

/**
 * 通过条件语句更新数据
 *
 * @param condition 是更新语句在'WHERE'后面的一部分
 *                  (即："UPDATE Person SET age = 35 WHERE condition")
 *                  (举个栗子： condition -- "name = 'Richard' OR name = 'Jefferson'")
 * @param entity
 * @throws DBException
 */
void updateByCondition(String condition, T entity) throws DBException;

/**
 * 通过id查找数据
 *
 * @param id
 * @return
 * @throws DBException
 */
T find(int id) throws DBException;

/**
 * 查找表中最后一条数据
 *
 * @return
 * @throws DBException
 */
T findLastEntity() throws DBException;

/**
 * 通过条件语句查询数据
 *
 * @param condition 是查询语句在'WHERE'后面的这部分
 *                  (即："SELECT * FROM Person WHERE condition")
 *                  (举个栗子： condition -- "name = 'Richard' OR name = 'Jefferson'")
 * @return
 */
List<T> findByCondition(String condition) throws DBException;

/**
 * 查询所有数据
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
