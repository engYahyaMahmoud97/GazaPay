package com.gazapay.gazapay;

public class User {
    private boolean isBin;
    private String name;
    private String id;
    private String key;
    private boolean isBay;

    public User() {
    }

    public User(boolean isBin, String name, String id, String key, boolean isBay) {
        this.isBin = isBin;
        this.name = name;
        this.id = id;
        this.key = key;
        this.isBay = isBay;
    }

    public boolean isBay() {
        return isBay;
    }

    public void setBay(boolean bay) {
        isBay = bay;
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
