package com.example.smartdealfirebase.Model;

import java.io.Serializable;

public class Voucher implements Serializable {

    private String voucherId;
    private String maVoucher;
    private String voucherName;
    private int discountPrice;
    private int Price;
    private String moTa;
    private String DanhMuc;
    private int slnguoimua;
    private String hinhvc;

    public Voucher(String _id,String maVoucher, String tenVoucher, Integer giaGiam, Integer giaGoc, String mota, String danhMuc, Integer slNguoimua, String hinh, Boolean isYeuThich) {
        this.voucherId = _id;
        this.maVoucher = maVoucher;
        this.voucherName = tenVoucher;
        this.discountPrice = giaGiam;
       Price=giaGoc;
        this.moTa = mota;
        DanhMuc = danhMuc;
        this.slnguoimua = slNguoimua;
        this.hinhvc = hinh;
        this.isYeuThich =isYeuThich;
    }


    public String get_id() {
        return voucherId;
    }

    public void set_id(String _id) {
        this.voucherId = _id;
    }

    public Voucher() {
    }

    public Voucher(String _id) {
        this.voucherId = _id;
    }

    private boolean isYeuThich;
    public boolean isYeuThich() {
        return isYeuThich;
    }
    public void setYeuThich(boolean yeuThich) {
        isYeuThich = yeuThich;
    }


    public Voucher(String maVoucher, String voucherName, int discountPrice, int price, String moTa, String DanhMuc, int slnguoimua, String hinhvc) {
        this.maVoucher = maVoucher;
        this.voucherName = voucherName;
        this.discountPrice = discountPrice;
        Price = price;
        this.moTa = moTa;
        this.DanhMuc = DanhMuc;
        this.slnguoimua = slnguoimua;
        this.hinhvc = hinhvc;
    }




    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
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
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDanhMuc() {
        return DanhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        DanhMuc = danhMuc;
    }

    public int getSlnguoimua() {
        return slnguoimua;
    }

    public void setSlnguoimua(int slnguoimua) {
        this.slnguoimua = slnguoimua;
    }

    public String getHinhvc() {
        return hinhvc;
    }

    public void setHinhvc(String hinhvc) {
        this.hinhvc = hinhvc;
    }
}
