package com.example.gurpartap.skip_and_buy.Model;

/**
 * Created by OWNER on 10/19/2016.
 */

public class Product {

    @com.google.gson.annotations.SerializedName("productId")
    private String productId;
    @com.google.gson.annotations.SerializedName("productName")
    private String productName;
    @com.google.gson.annotations.SerializedName("productWeight")
    private String productWeight;
    @com.google.gson.annotations.SerializedName("productPrice")
    private String productPrice;
    @com.google.gson.annotations.SerializedName("productDescription")
    private String productDescription;


    public Product(String productId, String productName, String productWeight, String productPrice, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productWeight = productWeight;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
