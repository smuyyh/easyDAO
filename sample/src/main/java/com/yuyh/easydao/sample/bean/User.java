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
package com.yuyh.easydao.sample.bean;

import com.yuyh.easydao.annotation.Column;
import com.yuyh.easydao.base.BaseEntity;

/**
 * @author yuyh.
 * @date 2016/11/16.
 */
public class User extends BaseEntity {

    @Column
    public String name;

    @Column
    public int age;

    @Column
    public boolean isMarried;

    @Column
    public double distance;

    public User() {
        super();
    }

    public User(String name, int age, boolean isMarried, double distance) {
        this.name = name;
        this.age = age;
        this.isMarried = isMarried;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "User{\n" +
                "\t\tid = " + (getId() == null ? "auto-increment" : getId()) + ",\n" +
                "\t\tname = '" + name + "\',\n" +
                "\t\tage = " + age + ",\n" +
                "\t\tisMarried = " + isMarried + ",\n" +
                "\t\tdistance = " + distance + "\n" +
                '}';
    }
}
