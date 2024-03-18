package com.example.smartdealfirebase.DesignPatternStrategy;

import com.example.smartdealfirebase.Model.Voucher;

import java.util.List;

public  class DuLichVoucherStrategy implements IVoucherStrategy {

    @Override
    public void addToVouchersList(Voucher voucher, List<Voucher> vouchersList) {
        if (voucher.getDanhMuc().equalsIgnoreCase("DuLich")) {
            vouchersList.add(voucher);
        }
    }
}