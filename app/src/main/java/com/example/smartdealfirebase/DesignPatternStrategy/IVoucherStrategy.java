package com.example.smartdealfirebase.DesignPatternStrategy;

import com.example.smartdealfirebase.Model.Voucher;

import java.util.List;


//Tạo Interface Voucher : định nghĩa hành động mà các chiến lược sẽ triển khai
public interface IVoucherStrategy{
    void addToVouchersList(Voucher voucher, List<Voucher> voucherList);
}