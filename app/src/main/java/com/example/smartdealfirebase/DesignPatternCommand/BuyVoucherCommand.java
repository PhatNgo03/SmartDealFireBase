package com.example.smartdealfirebase.DesignPatternCommand;

import yeuthich.ThongTinVoucherActivity;

public class BuyVoucherCommand implements BuyCommand{
    private final ThongTinVoucherActivity activity;

    public BuyVoucherCommand(ThongTinVoucherActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        // Logic mua hàng được chuyển vào đây
        // Có thể sao chép toàn bộ logic từ onClick của btBuy vào đây
        activity.buyVoucher1();
    }
}

