package com.example.smartdealfirebase.DesignPatternStrategy;

import com.example.smartdealfirebase.Model.Voucher;

import java.util.List;

public class VoucherSpaStrategy implements IVoucherStrategy {
    @Override
    public void addToVouchersList(Voucher voucher, List<Voucher> voucherList) {
        if(voucher.getDanhMuc().equalsIgnoreCase("Spa")){
            voucherList.add(voucher);
        }
    }


}