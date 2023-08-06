package com.example.smartdealfirebase.Model;

public class TestLS {
    String tvTenVoucher,DateMuaHang;
    int ToTal;

    public TestLS(String tvTenVoucher, String dateMuaHang, int toTal) {
        this.tvTenVoucher = tvTenVoucher;
        DateMuaHang = dateMuaHang;
        ToTal = toTal;
    }
    public  TestLS(){

    }
    public String getTvTenVoucher() {
        return tvTenVoucher;
    }

    public void setTvTenVoucher(String tvTenVoucher) {
        this.tvTenVoucher = tvTenVoucher;
    }

    public String getDateMuaHang() {
        return DateMuaHang;
    }

    public void setDateMuaHang(String dateMuaHang) {
        DateMuaHang = dateMuaHang;
    }

    public int getToTal() {
        return ToTal;
    }

    public void setToTal(int toTal) {
        ToTal = toTal;
    }
}
