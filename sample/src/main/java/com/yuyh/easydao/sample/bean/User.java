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
}
