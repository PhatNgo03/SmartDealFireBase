package com.example.smartdealfirebase.DesignPatternCommandUpDel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.DesignPatternCommandAdd.AddCommand;
import com.example.smartdealfirebase.DesignPatternCommandAdd.AddVoucherActivity;
import com.example.smartdealfirebase.DesignPatternCommandAdd.AddVoucherCommand;
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

public class EditVoucherActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ImageView imgChonHinh;
    EditText edtMaVoucher,edtTenVoucher,edtGiaGiam,edtGiaGoc,edtMoTa,edtSoLuongNguoiMua,edtDanhMuc;
    Button btnCapNhat;
    Voucher voucher;
    Uri imageUri;
    FirebaseFirestore firestore;
    StorageReference storageReference;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.d("check", imageUri.toString());
            imgChonHinh.setImageURI(imageUri);
        }
        else {
            Log.d("sai", "fsdkjfs");
        }
    }

    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    public void uploadImageToFirebaseStorage(Uri imageUri, String maVoucher, String TenVoucher, String GiaGiam, String GiaGoc, String MoTa, String DanhMuc, String SLNguoiMua, String img) {

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL tải xuống của hình ảnh
                    fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                Toast.makeText(this, "tải dữ liệu thành công", Toast.LENGTH_SHORT).show();


                                Map<String, Object> voucherData = new HashMap<>();
                                voucherData.put("TenVoucher", TenVoucher);
                                voucherData.put("GiaGiam", GiaGiam);
                                voucherData.put("GiaGoc", GiaGoc);
                                voucherData.put("MoTa", MoTa);
                                voucherData.put("DanhMuc", DanhMuc);
                                voucherData.put("SLNguoiMua", SLNguoiMua);
                                voucherData.put("HinhAnh", uri);

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference voucherCollection = db.collection("Voucher");

                                Query query = voucherCollection.whereEqualTo("MaVoucher", maVoucher);
                                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            String documentId = documentSnapshot.getId();
                                            DocumentReference docRef = db.collection("Voucher").document(documentId);

                                            docRef.update(voucherData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(EditVoucherActivity.this, "Update success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(EditVoucherActivity.this, "Update không thành công: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                });


                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Không lấy được URL tải xuống", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Tải ảnh thất bại", Toast.LENGTH_SHORT).show();
                });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voucher);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/");
        firestore = FirebaseFirestore.getInstance();
        imgChonHinh = findViewById(R.id.imageViewDanhMuc);
        edtMaVoucher = findViewById(R.id.edtMaVoucherSua);
        edtTenVoucher = findViewById(R.id.edtTenVoucherDanhMucSUa);
        edtGiaGiam = findViewById(R.id.edtGiaGiamDanhmUcSua);
        edtGiaGoc = findViewById(R.id.edtGiaGocDanhMucSua);
        edtMoTa = findViewById(R.id.edtMoTaDanhMucSua);
        edtDanhMuc = findViewById(R.id.edtDanhMucVoucherSua);
        edtSoLuongNguoiMua = findViewById(R.id.edtSLNguoiMuaDanhMucSua);
        btnCapNhat = findViewById(R.id.btChinhSua);

        imgChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        Voucher voucher = (Voucher) getIntent().getSerializableExtra("Voucher");
        if (voucher != null) {
            edtMaVoucher.setText(voucher.getMaVoucher());
            edtMaVoucher.setEnabled(false);
            edtTenVoucher.setText(voucher.getVoucherName());
            edtGiaGiam.setText(String.valueOf(voucher.getDiscountPrice()));
            edtGiaGoc.setText(String.valueOf(voucher.getPrice()));
            edtMoTa.setText(voucher.getMoTa());
            edtDanhMuc.setText(voucher.getDanhMuc());
            edtSoLuongNguoiMua.setText(String.valueOf(voucher.getSlnguoimua()));
            Glide.with(EditVoucherActivity.this).load(voucher.getHinhvc()).into(imgChonHinh);
        }

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IUppDelCommand uppDelCommand = new UppDelVoucherCommand(EditVoucherActivity.this, firestore, storageReference, voucher, imageUri);
                uppDelCommand.execute();
            }
        });
    }

    public void UpdateVoucherByMaVoucher(String maVoucher, String TenVoucher, String GiaGiam, String GiaGoc, String MoTa, String DanhMuc, String SLNguoiMua, String img) {


    }
}