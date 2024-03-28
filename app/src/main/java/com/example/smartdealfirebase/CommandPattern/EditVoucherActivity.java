package com.example.smartdealfirebase.CommandPattern;


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
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.Prototype.VoucherPrototype;
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
    Uri imageUri;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    private VoucherPrototype voucher;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.d("check", imageUri.toString());
            imgChonHinh.setImageURI(imageUri);
        }
        else {
            Log.d("Upload Image Success", "Imgage Success");
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String maVoucher = bundle.getString("maVoucher");
            String tenVoucher = bundle.getString("tenVoucher");
            int giaGiam = bundle.getInt("giaGiam");
            int giaGoc = bundle.getInt("giaGoc");
            int slNguoiMua = bundle.getInt("slNguoiMua");
            String moTa = bundle.getString("moTa");
            String danhMuc = bundle.getString("danhMuc");
            String hinhAnh = bundle.getString("hinhAnh");



            voucher = new VoucherPrototype(maVoucher, tenVoucher, giaGiam, giaGoc, slNguoiMua, moTa, danhMuc, hinhAnh);

            edtMaVoucher.setText(maVoucher);
            edtTenVoucher.setText(tenVoucher);
            edtGiaGiam.setText(String.valueOf(giaGiam));
            edtGiaGoc.setText(String.valueOf(giaGoc));
            edtMoTa.setText(moTa);
            edtDanhMuc.setText(danhMuc);
            edtSoLuongNguoiMua.setText(String.valueOf(slNguoiMua));
            Glide.with(EditVoucherActivity.this).load(hinhAnh).into(imgChonHinh);

        }


        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IUppDelCommand uppDelCommand = new UppDelVoucherCommand(EditVoucherActivity.this, firestore, storageReference, voucher, imageUri);
                uppDelCommand.excecute();
            }
        });
    }
}