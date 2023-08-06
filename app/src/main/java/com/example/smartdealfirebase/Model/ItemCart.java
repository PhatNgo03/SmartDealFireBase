package com.example.smartdealfirebase.Model;

import java.io.Serializable;

public class ItemCart implements Serializable {
    private String itemId; //
    private String voucherName;
    private int discountPrice;
    private int price;
    private int quantity = 1;
    private String UserId;
    private int ToTal;
    private String HinhAnh;
    public ItemCart(){

    }

    private String NgayMua;

    public ItemCart(String voucherName, int toTal, String ngayMua) {
        this.voucherName = voucherName;
        ToTal = toTal;
        NgayMua = ngayMua;
    }
    public String getNgayMua() {
        return NgayMua;
    }

    public void setNgayMua(String ngayMua) {
        NgayMua = ngayMua;
    }
    //    public ItemCart(String itemId, String voucherName, int discountPrice, int price, int quantity) {
//        this.itemId = itemId;
//        this.voucherName = voucherName;
//        this.discountPrice = discountPrice;
//        this.price = price;
//        this.quantity = quantity;
//    }


//    public ItemCart(String itemId, String voucherName, int discountPrice, int price, int quantity, String userId, int toTal) {
//        this.itemId = itemId;
//        this.voucherName = voucherName;
//        this.discountPrice = discountPrice;
//        this.price = price;
//        this.quantity = quantity;
//        UserId = userId;
//        ToTal = toTal;
//    }

    public ItemCart(String itemId, String voucherName, int discountPrice, int price, int quantity, int ToTal,String HinhAnh,String UserId) {
        this.itemId = itemId;
        this.voucherName = voucherName;
        this.discountPrice = discountPrice;
        this.price = price;
        this.quantity = quantity;
        this.ToTal = ToTal;
        this.UserId = UserId;
        this.HinhAnh = HinhAnh;
    }
//

    public ItemCart(String itemId, String voucherName, int discountPrice, int price, int quantity, String userId, int toTal, String hinhAnh, String ngayMua) {
        this.itemId = itemId;
        this.voucherName = voucherName;
        this.discountPrice = discountPrice;
        this.price = price;
        this.quantity = quantity;
        UserId = userId;
        ToTal = toTal;
        HinhAnh = hinhAnh;
        NgayMua = ngayMua;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
        updateTotal();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotal();
    }

    public int getToTal() {
        return ToTal;
    }

        private void updateTotal() {
            ToTal = (int) (quantity * discountPrice);
        }

    public void increaseQuantity() {
        quantity++;
        updateTotal();
    }

    public void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            updateTotal();
        }
    }

}
