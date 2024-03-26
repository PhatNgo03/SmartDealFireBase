package com.example.smartdealfirebase.CommandPattern;

import android.net.Uri;
import android.util.Log;

import com.example.smartdealfirebase.Model.Voucher;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class UppDelVoucherCommand implements IUppDelCommand{

    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private Voucher voucher;
    private Uri imageUri;
    private EditVoucherActivity activity;

    public  UppDelVoucherCommand(EditVoucherActivity activity, FirebaseFirestore firestore, StorageReference storageReference, Voucher voucher, Uri imageUri) {
        this.activity = activity;
        this.firestore = firestore;
        this.storageReference = storageReference;
        this.voucher = voucher;
        this.imageUri = imageUri;
    }


    @Override
    public void excecute() {
        Voucher voucher = (Voucher) activity.getIntent().getSerializableExtra("Voucher");;
        if (voucher != null) {
            String MaVoucher = voucher.getMaVoucher();
            String TenVoucher = activity.edtTenVoucher.getText().toString();
            String GiaGiam = activity.edtGiaGiam.getText().toString();
            String GiaGoc = activity.edtGiaGoc.getText().toString();
            String MoTa = activity.edtMoTa.getText().toString();
            String DanhMuc = activity.edtDanhMuc.getText().toString();
            String SLNguoiMua = activity.edtSoLuongNguoiMua.getText().toString();
            String img = String.valueOf(activity.imageUri);
            Log.d("mavocher", MaVoucher);
            activity.uploadImageToFirebaseStorage(activity.imageUri, MaVoucher, TenVoucher, GiaGiam, GiaGoc, MoTa, DanhMuc, SLNguoiMua, img);
        }
        else {
            // Xử lý khi voucher là null
            Log.e("UppDelVoucherCommand", "Voucher is null");
        }
    }
}
