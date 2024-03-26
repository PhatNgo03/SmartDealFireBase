package com.example.smartdealfirebase.DesignPatternCommandAdd;

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

import com.example.smartdealfirebase.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddVoucherActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST =1 ;
    ImageView imgVoucherDanhMuc;
    EditText edtMavoucher;
    EditText edtTenVoucher;
    EditText edtGiaGoc;
    EditText edtGiaGiam;
    EditText edtMoTa;
    EditText edtSLNguoiMua;
    EditText edtDanhMuc;
    Button btAddVoucher;
    Uri imageUri;
    StorageReference storageReference;
    FirebaseFirestore fireStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_voucher);

        imgVoucherDanhMuc=findViewById(R.id.imageViewDanhMuc);
        edtMavoucher = findViewById(R.id.edtMaVoucher);
        edtTenVoucher=findViewById(R.id.edtTenVoucherDanhMuc);
        edtGiaGoc=findViewById(R.id.edtGiaGocDanhMuc);
        edtGiaGiam=findViewById(R.id.edtGiaGiamDanhmUc);
        edtMoTa=findViewById(R.id.edtMoTaDanhMuc);
        edtDanhMuc=findViewById(R.id.edtDanhMucVoucher);
        edtSLNguoiMua = findViewById(R.id.edtSLNguoiMuaDanhMuc);
        btAddVoucher =findViewById(R.id.btAddVoucher);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images/");
        fireStore = FirebaseFirestore.getInstance();

        btAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddCommand addCommand = new AddVoucherCommand(AddVoucherActivity.this);
                addCommand.execute();

            }
        });

        imgVoucherDanhMuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

    }

//    public  void AddVoucher()
//    {
//        if (imageUri != null) {
//            // Tải hình ảnh lên Firebase Storage
//            uploadImageToFirebaseStorage(imageUri);
//        } else {
//            Toast.makeText(AddVoucherActivity.this, "Hãy chọn một hình ảnh", Toast.LENGTH_SHORT).show();
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.d("check", imageUri.toString());
            imgVoucherDanhMuc.setImageURI(imageUri);
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
    public void uploadImageToFirebaseStorage(Uri imageUri) {

        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Lấy URL tải xuống của hình ảnh
                    fileReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                Toast.makeText(this, "tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                saveDataToFirestore(uri.toString());
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Không lấy được URL tải xuống", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Tải ảnh thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveDataToFirestore(String imageUri) {
        String MaVoucher = edtMavoucher.getText().toString();
        String voucherName = edtTenVoucher.getText().toString();
        String GiaGiam = edtGiaGiam.getText().toString();
        String GiaGoc = edtGiaGoc.getText().toString();
        String moTa = edtMoTa.getText().toString();
        String DanhMuc = edtDanhMuc.getText().toString();
        String SLNguouMua = edtSLNguoiMua.getText().toString();

        Map<String, Object> voucher = new HashMap<>();
        voucher.put("MaVoucher",MaVoucher);
        voucher.put("TenVoucher",voucherName);
        voucher.put("GiaGiam",GiaGiam);
        voucher.put("GiaGoc",GiaGoc);
        voucher.put("MoTa",moTa);
        voucher.put("DanhMuc",DanhMuc);
        voucher.put("SLNguoiMua",SLNguouMua);
        voucher.put("HinhAnh",imageUri);

        // Thêm dữ liệu vào Firestore
        fireStore.collection("Voucher")
                .add(voucher)
                .addOnSuccessListener(documentReference -> {
                    // Dữ liệu đã được lưu trữ thành công vào Firestore
                    Toast.makeText(this, "Tải lên thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu việc lưu dữ liệu vào Firestore thất bại
                    Toast.makeText(this, "Tải lên thất bại", Toast.LENGTH_SHORT).show();
                });


    }
}