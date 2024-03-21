package com.example.smartdealfirebase.Prototype;

public class VoucherFactory {
    private VoucherPrototype voucherPrototype;

    public VoucherFactory(VoucherPrototype voucherPrototype) {
        this.voucherPrototype = voucherPrototype;
    }

    // Phương thức để tạo ra 1 bản sao
    public VoucherPrototype createVoucher(){
        try {
            return voucherPrototype.clone();
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }
}
