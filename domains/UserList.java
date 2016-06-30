package com.thoughtworks.pos.domains;

/**
 * Created by Administrator on 2016/6/30.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserList {
    private String user = null;
    private String[] items = null;

    public UserList() {
    }

    public UserList(String user, String[] items) {
        this.setUser(user);
        this.setItems(items);
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
