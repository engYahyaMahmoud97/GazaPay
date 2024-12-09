package com.cat.gazapay;

public class User {
    private boolean isBin;
    private String name;
    private String id;
    private String key;

    public User() {
    }

    public User(boolean isBin, String name, String id, String key) {
        this.isBin = isBin;
        this.name = name;
        this.id = id;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBin() {
        return isBin;
    }

    public void setBin(boolean bin) {
        isBin = bin;
    }
}
