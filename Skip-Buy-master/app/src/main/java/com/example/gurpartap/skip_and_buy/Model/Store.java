package com.example.gurpartap.skip_and_buy.Model;

/**
 * Created by OWNER on 10/19/2016.
 */

public class Store {

    @com.google.gson.annotations.SerializedName("storeName")
    private String storeName;
    @com.google.gson.annotations.SerializedName("storeDescription")
    private String storeDescription;
    @com.google.gson.annotations.SerializedName("storeAddress")
    private String storeAddress;


    public Store(String storeName, String storeDescription, String storeAddress) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.storeAddress = storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }
}
