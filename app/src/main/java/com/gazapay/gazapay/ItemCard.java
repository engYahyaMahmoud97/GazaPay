package com.gazapay.gazapay;

public class ItemCard {
    private String name;
    private String fromCuntry;
    private String toCuntry;
    private String cuntryName;
    private boolean isDollar;
    private String price;
    private Long time;
    private String whatsapp;
    private String transfarKey;
    private String myId;

    public ItemCard() {
    }

    public ItemCard(String name, String fromCuntry, String toCuntry, String cuntryName, boolean isDollar, String price, Long time, String whatsapp, String transfarKey, String myId) {
        this.name = name;
        this.fromCuntry = fromCuntry;
        this.toCuntry = toCuntry;
        this.cuntryName = cuntryName;
        this.isDollar = isDollar;
        this.price = price;
        this.time = time;
        this.whatsapp = whatsapp;
        this.transfarKey = transfarKey;
        this.myId = myId;
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

    public boolean isDollar() {
        return isDollar;
    }

    public void setDollar(boolean dollar) {
        isDollar = dollar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromCuntry() {
        return fromCuntry;
    }

    public void setFromCuntry(String fromCuntry) {
        this.fromCuntry = fromCuntry;
    }

    public String getToCuntry() {
        return toCuntry;
    }

    public void setToCuntry(String toCuntry) {
        this.toCuntry = toCuntry;
    }

    public String getCuntryName() {
        return cuntryName;
    }

    public void setCuntryName(String cuntryName) {
        this.cuntryName = cuntryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
