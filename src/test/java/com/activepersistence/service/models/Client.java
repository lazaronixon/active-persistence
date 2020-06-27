package com.activepersistence.service.models;

import com.activepersistence.model.Base;

public class Client extends Base<Integer> {

    private Integer id;

    private String name;

    private Integer age;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
