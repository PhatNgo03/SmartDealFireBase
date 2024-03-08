package com.example.smartdealfirebase.DesignPatternStrategy;

import com.example.smartdealfirebase.Model.Voucher;

import java.util.List;

public class strategies {
    //Tạo Interface Voucher
    public interface IVoucherStrategy{
        void addToVouchersList(Voucher voucher, List<Voucher> voucherList);
    }

    //Triển khai các lớp cụ thể của voucher

    public static class AmThucVoucherStrategy implements IVoucherStrategy {
        @Override
        public void addToVouchersList(Voucher voucher, List<Voucher> vouchersList) {
            if (voucher.getDanhMuc().equalsIgnoreCase("AmThuc")) {
                vouchersList.add(voucher);
            }
        }
    }
    public static class DuLichVoucherStrategy implements IVoucherStrategy {

        @Override
        public void addToVouchersList(Voucher voucher, List<Voucher> vouchersList) {
            if (voucher.getDanhMuc().equalsIgnoreCase("DuLich")) {
                vouchersList.add(voucher);
            }
        }
    }
    public static class VoucherSpaStrategy implements IVoucherStrategy{
        @Override
        public void addToVouchersList(Voucher voucher, List<Voucher> voucherList) {
            if(voucher.getDanhMuc().equalsIgnoreCase("Spa")){
                voucherList.add(voucher);
            }
        }


    }

// Tương tự cho các loại danh mục khác nếu cần


}
