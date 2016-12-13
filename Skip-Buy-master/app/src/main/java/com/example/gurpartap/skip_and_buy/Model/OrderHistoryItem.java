package com.example.gurpartap.skip_and_buy.Model;

/**
 * Created by GURPARTAP on 2016-09-16.
 */
public class OrderHistoryItem {


    public int icon;
    public String name;
    public String amount;
    public String weight;
    public String productId;
    public String productQuantity;

    public OrderHistoryItem(){
        super();
    }

    public OrderHistoryItem(int icon, String name, String amount, String weight, String productId){
        super();
        this.icon=icon;
        this.name=name;
        this.amount=amount;
        this.weight=weight;
        this.productId=productId;
    }

    public OrderHistoryItem(int icon, String name,String amount,String weight,String productId,String productQuantity){
        super();
        this.icon=icon;
        this.name=name;
        this.amount=amount;
        this.weight=weight;
        this.productId=productId;
        this.productQuantity=productQuantity;
    }

}
