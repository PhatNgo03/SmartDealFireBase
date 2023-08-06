package com.example.smartdealfirebase.Model;

public class User {

    private String Idnguoidung;
    private String hoten;
    private String ngaysinh;
    private String gioitinh;
    private String diachi;
    private String SDT;
    private String email;

    public User() {
    }

    public User(String idnguoidung, String hoten, String ngaysinh, String gioitinh, String diachi, String SDT, String email) {
        Idnguoidung = idnguoidung;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.diachi = diachi;
        this.SDT = SDT;
        this.email = email;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdnguoidung() {
        return Idnguoidung;
    }

    public void setIdnguoidung(String idnguoidung) {
        Idnguoidung = idnguoidung;
    }
}
