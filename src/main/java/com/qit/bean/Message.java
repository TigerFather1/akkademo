/*
 * Copyright (c) 2018 duanxitao All rights reserved.
 */

package com.qit.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ProjectName akkademo
 * @Description TODO
 * @Autohr DuanXiTao
 * @Date 2018/5/20 17:48
 * @Version 1.0
 */
public class Message {
    private String username;
    private String address;
    private String password;
    @JsonIgnore
    private int age;

    public Message() {

    }

    public Message(String username, String password, String address, int age) {
        this.username = username;
        this.address = address;
        this.password = password;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}
