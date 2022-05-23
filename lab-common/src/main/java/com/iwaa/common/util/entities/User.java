package com.iwaa.common.util.entities;

import java.io.Serializable;

public class User implements Serializable {

    private long id;
    private final String login;
    private  final String password;

    public User() {
        this.login = "";
        this.password = "";
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

}
