package com.example.smartdealfirebase.Model;

import java.util.List;

public class Cart {
    private String cartID;
    private String userID;

    private List<ItemCart> cartItems;
    public Cart(){

    }
    public Cart(String cartID, String userID, List<ItemCart> cartItems) {
        this.cartID = cartID;
        this.userID = userID;
        this.cartItems = cartItems;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<ItemCart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<ItemCart> cartItems) {
        this.cartItems = cartItems;
    }
}
