package com.example.smartdealfirebase.Prototype;

import android.os.Bundle;

public class VoucherPrototype implements Cloneable {
    private String maVoucher;
    private String tenVoucher;
    private int giaGiam;
    private int giaGoc;
    private int slNguoiMua;
    private String moTa;
    private String danhMuc;
    private String hinhAnh;

    public VoucherPrototype(String maVoucher, String tenVoucher, int giaGiam, int giaGoc, int slNguoiMua, String moTa, String danhMuc, String hinhAnh) {
        this.maVoucher = maVoucher;
        this.tenVoucher = tenVoucher;
        this.giaGiam = giaGiam;
        this.giaGoc = giaGoc;
        this.slNguoiMua = slNguoiMua;
        this.moTa = moTa;
        this.danhMuc = danhMuc;
        this.hinhAnh = hinhAnh;
    }
    // Phương thức clone
    @Override
    public VoucherPrototype clone() throws CloneNotSupportedException{
        return (VoucherPrototype) super.clone();
    }

    public String getMaVoucher() {
        return maVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        this.maVoucher = maVoucher;
    }

    public String getTenVoucher() {
        return tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public int getGiaGiam() {
        return giaGiam;
    }

    public void setGiaGiam(int giaGiam) {
        this.giaGiam = giaGiam;
    }

    public int getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(int giaGoc) {
        this.giaGoc = giaGoc;
    }

    public int getSlNguoiMua() {
        return slNguoiMua;
    }

    public void setSlNguoiMua(int slNguoiMua) {
        this.slNguoiMua = slNguoiMua;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("maVoucher", maVoucher);
        bundle.putString("tenVoucher", tenVoucher);
        bundle.putInt("giaGiam", giaGiam);
        bundle.putInt("giaGoc", giaGoc);
        bundle.putInt("slNguoiMua", slNguoiMua);
        bundle.putString("moTa", moTa);
        bundle.putString("danhMuc", danhMuc);
        bundle.putString("hinhAnh", hinhAnh);
        return bundle;
    }

}
