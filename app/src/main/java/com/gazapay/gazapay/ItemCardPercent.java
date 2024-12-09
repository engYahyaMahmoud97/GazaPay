package com.gazapay.gazapay;

public class ItemCardPercent {
    private String name;
    private String cuntryName;
    private String percent;
    private String desc;
    private Long time;
    private String transfarKey;
    private String myId;

    public ItemCardPercent() {
    }

    public ItemCardPercent(String name, String cuntryName, String percent, String desc, Long time, String transfarKey, String myId) {
        this.name = name;
        this.cuntryName = cuntryName;
        this.percent = percent;
        this.desc = desc;
        this.time = time;
        this.transfarKey = transfarKey;
        this.myId = myId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuntryName() {
        return cuntryName;
    }

    public void setCuntryName(String cuntryName) {
        this.cuntryName = cuntryName;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTransfarKey() {
        return transfarKey;
    }

    public void setTransfarKey(String transfarKey) {
        this.transfarKey = transfarKey;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }
}
