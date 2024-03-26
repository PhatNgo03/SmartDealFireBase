package com.example.smartdealfirebase.DesignPatternCommandUpDel;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartdealfirebase.DesignPatternCommandAdd.AddVoucherActivity;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
public class UppDelVoucherCommand implements IUppDelCommand{
    private  FirebaseFirestore firestore;
    private  StorageReference storageReference;
    private  Voucher voucher;
    private  Uri imageUri;
    private  EditVoucherActivity activity;

    public  UppDelVoucherCommand(EditVoucherActivity activity, FirebaseFirestore firestore, StorageReference storageReference, Voucher voucher, Uri imageUri) {
        this.activity = activity;
        this.firestore = firestore;
        this.storageReference = storageReference;
        this.voucher = voucher;
        this.imageUri = imageUri;
    }

    @Override
    public void execute() {
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
