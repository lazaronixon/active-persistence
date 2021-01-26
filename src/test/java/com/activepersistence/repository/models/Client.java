package com.activepersistence.repository.models;

import com.activepersistence.model.BaseIdentity;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

@Entity
public class Client extends BaseIdentity {

    private String name;

    private Integer age;

    private boolean active;

    private Float weight;

    private Double ratio;

    private BigDecimal salary;

    @Enumerated
    private Gender gender;

    private BigInteger sequence;

    public Client() {
    }

    //<editor-fold defaultstate="collapsed" desc="GET/SET">
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigInteger getSequence() {
        return sequence;
    }

    public void setSequence(BigInteger sequence) {
        this.sequence = sequence;
    }
    //</editor-fold>

}
