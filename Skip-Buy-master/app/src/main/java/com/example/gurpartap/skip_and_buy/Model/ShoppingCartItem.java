package com.example.gurpartap.skip_and_buy.Model;

/**
 * Created by GURPARTAP on 2016-09-16.
 */

/*
    *   This is model class for shopping cart.
    *   It stores icon, name, amount,weight,productId
    *   and quantity of items in shopping cart
*/

public class ShoppingCartItem {


    public int icon;
    public String name;
    public String amount;
    public String weight;
    public String productId;
    public String productQuantity;

    public ShoppingCartItem(){
        super();
    }

    public ShoppingCartItem(int icon, String name,String amount,String weight,String productId) {
        super();
        this.icon = icon;
        this.name = name;
        this.amount = amount;
        this.weight = weight;
        this.productId = productId;
    }

    public ShoppingCartItem(int icon, String name,String amount,String weight,String productId,String productQuantity){
        super();
        this.icon=icon;
        this.name=name;
        this.amount=amount;
        this.weight=weight;
        this.productId=productId;
        this.productQuantity=productQuantity;
    }

}
