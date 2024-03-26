package com.example.smartdealfirebase.DesignPatternStrategy;
import com.example.smartdealfirebase.Model.Voucher;
import java.util.List;

public class AmThucVoucherStrategy implements IVoucherStrategy {
    @Override
    public void addToVouchersList(Voucher voucher, List<Voucher> vouchersList) {
        if (voucher.getDanhMuc().equalsIgnoreCase("AmThuc")) {
            vouchersList.add(voucher);
        }
    }
}