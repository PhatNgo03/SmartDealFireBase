package com.example.smartdealfirebase.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Order implements Serializable {
    private String tenNguoiDung;

    private Integer giaGiam;
    private Integer giaGoc;
    private String diaChiGiaoHang;
    private String sdt;
    private String ghiChu;

    public Order(String tenNguoiDung,  Integer giaGiam, Integer giaGoc, String diaChiGiaoHang, String sdt, String ghiChu) {
        this.tenNguoiDung = tenNguoiDung;
        this.giaGiam = giaGiam;
        this.giaGoc = giaGoc;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.sdt = sdt;
        this.ghiChu = ghiChu;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }
    public Integer getGiaGiam() {
        return giaGiam;
    }

    public void setGiaGiam(Integer giaGiam) {
        this.giaGiam = giaGiam;
    }

    public Integer getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(Integer giaGoc) {
        this.giaGoc = giaGoc;
    }

    public String getDiaChiGiaoHang() {
        return diaChiGiaoHang;
    }

    public void setDiaChiGiaoHang(String diaChiGiaoHang) {
        this.diaChiGiaoHang = diaChiGiaoHang;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("tenNguoiDung", tenNguoiDung);
        map.put("giaGiam", giaGiam);
        map.put("giaGoc", giaGoc);
        map.put("diaChiGiaoHang", diaChiGiaoHang);
        map.put("sdt", sdt);
        map.put("ghiChu", ghiChu);
        return map;
    }

}
