package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ThongTinCaNhanActivity extends AppCompatActivity {


    String userEmail; // The userEmail will store the currently logged-in user's email

    EditText mSDT, mEmail, mTen, mNgaysinh, mDiachi;
    FirebaseFirestore db;
    Button btnLuu;
    ImageView imgBack;



    private Spinner spinnerGioiTinh;
    private ArrayAdapter<String> gioiTinhAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_thong_tin_ca_nhan);
        spinnerGioiTinh = findViewById(R.id.spinnerGioiTinh);
        gioiTinhAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        gioiTinhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGioiTinh.setAdapter(gioiTinhAdapter);
        String[] gioiTinhOptions = new String[]{"Nam", "Nữ", "Khác"};
        gioiTinhAdapter.addAll(gioiTinhOptions);

        db = FirebaseFirestore.getInstance();

        // Get the currently logged-in user's email using Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userEmail = user.getEmail(); // Get the email of the currently logged-in user
        } else {
            // If the user is not logged in, you may handle this situation or prompt the user to log in.
            // For example, you can redirect the user to the login screen.
            startActivity(new Intent(ThongTinCaNhanActivity.this, DangNhapActivity.class));
            finish();
            return;
        }

        // Initialize views and set click listener for "Lưu" (Save) button
        initView();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfoInFirestore();
            }
        });

        // Fetch the user information from Firestore using userEmail
        fetchUserInfoFromFirestore();

        // ... (rest of the code)
    }




    // Gọi các phương thức từ trạng thái hiện tại

    private void initView() {
        mEmail = findViewById(R.id.edtEmail);
        mTen = findViewById(R.id.edtHoVaTen);
        mSDT = findViewById(R.id.edtSDT);
        mNgaysinh = findViewById(R.id.edtNgaySinh);

        mDiachi = findViewById(R.id.edtDiaChi);
        btnLuu = findViewById(R.id.btnLuu);
        imgBack = findViewById(R.id.back);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mNgaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog1();
            }
        });
    }

//    private void fetchUserInfoFromFirestore() {
//        DocumentReference docRef = db.collection("KhachHang").document(userEmail);
//
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    // Document exists, retrieve data
//                    String hoten = documentSnapshot.getString("HoTen");
//                    String ngaysinh = documentSnapshot.getString("NgaySinh");
//                    String diachi = documentSnapshot.getString("DiaChi");
//                    String sdt = documentSnapshot.getString("SDT");
//                    String gioiTinh = documentSnapshot.getString("GioiTinh");
//                    int gioiTinhIndex = gioiTinhAdapter.getPosition(String.valueOf(gioiTinh));
//                    spinnerGioiTinh.setSelection(gioiTinhIndex);
//                    // Set the fetched data to the EditText fields
//                    edten.setText(hoten);
//                    mngaysinh.setText(ngaysinh);
//                    spinnerGioiTinh.setTag(gioiTinh);
//                    mdiachi.setText(diachi);
//                    mSDT.setText(sdt);
//                    memail.setText(userEmail);
//                    memail.setEnabled(false);
//                } else {
//                    // Document does not exist, leave EditText fields empty
//                    memail.setText("");
//
//                }
//            }
//        });
//    }
private void fetchUserInfoFromFirestore() {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if (user != null) {
        String userUID = user.getUid(); // Lấy UID của người dùng hiện tại
        String userEmail = user.getEmail(); // Lấy email của người dùng hiện tại
        String userPhoneNumber = user.getPhoneNumber(); // Lấy số điện thoại của người dùng hiện tại

        DocumentReference docRef;
        if (userPhoneNumber != null) {
            // Trường hợp đăng nhập bằng số điện thoại, sử dụng số điện thoại để truy vấn Firestore
            docRef = db.collection("KhachHang").document(userPhoneNumber);
        } else if (userEmail != null) {
            // Trường hợp đăng nhập bằng email, sử dụng email để truy vấn Firestore
            docRef = db.collection("KhachHang").document(userEmail);
        } else {
            // Xử lý khi không thể lấy được số điện thoại hoặc email của người dùng
            return;
        }

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Xử lý khi tài liệu tồn tại
                    String hoTen = documentSnapshot.getString("HoTen");
                    String ngaySinh = documentSnapshot.getString("NgaySinh");
                    String diaChi = documentSnapshot.getString("DiaChi");
                    String SDT = documentSnapshot.getString("SDT");
                    String gioiTinh = documentSnapshot.getString("GioiTinh");
                    int gioiTinhIndex = gioiTinhAdapter.getPosition(String.valueOf(gioiTinh));
                    spinnerGioiTinh.setSelection(gioiTinhIndex);
                    // Set the fetched data to the EditText fields
                    mTen.setText(hoTen);
                    mNgaysinh.setText(ngaySinh);
                    spinnerGioiTinh.setTag(gioiTinh);
                    mDiachi.setText(diaChi);
                    mSDT.setText(SDT);
                    if (userEmail != null) {
                        mEmail.setText(userEmail);
                        mEmail.setEnabled(false);
                    }
                } else {
                    // Xử lý khi tài liệu không tồn tại
                    if (userEmail != null) {
                        mEmail.setText(userEmail);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi truy vấn thất bại
                Toast.makeText(ThongTinCaNhanActivity.this, "Failed to fetch user information. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    } else {
        // Xử lý khi người dùng không đăng nhập
        startActivity(new Intent(ThongTinCaNhanActivity.this, DangNhapActivity.class));
        finish();
    }
}




    private void updateUserInfoInFirestore() {
        // Get the updated user information from EditText fields
        String hoTen = mTen.getText().toString();
        String ngaySinh = mNgaysinh.getText().toString();
        String gioiTinh = spinnerGioiTinh.getSelectedItem().toString();
        String diaChi = mDiachi.getText().toString();
        String SDT = mSDT.getText().toString();

        if (SDT.length() < 8 || SDT.length() > 10) {
            showError(mSDT, "Số điện thoại phải có từ 8 đến 10 ký tự.");
            return;
        }

        // Create a query to search for the phone number in Firestore
        Query query = db.collection("KhachHang").whereEqualTo("SDT", SDT);

        // Perform the query
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    if (task.getResult().size() > 0) {
//                        showError(mSDT, "Số điện thoại đã tồn tại.");
                      {

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("HoTen", hoTen);
                        userMap.put("NgaySinh", ngaySinh);
                        userMap.put("GioiTinh", gioiTinh);
                        userMap.put("DiaChi", diaChi);
                        userMap.put("SDT", SDT);


                        db.collection("KhachHang").document(userEmail)
                                .set(userMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ThongTinCaNhanActivity.this, "User information updated successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ThongTinCaNhanActivity.this, "Failed to update user information. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                } else {
                    // An error occurred while performing the query
                    Toast.makeText(ThongTinCaNhanActivity.this, "Failed to check phone number. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openDialog1() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String thismonth;
                if (month + 1 < 10) {
                    thismonth = "0" + String.valueOf(month + 1);
                } else {
                    thismonth = String.valueOf(month + 1);
                }
                String selectedDate = String.valueOf(year) + "-" + thismonth + "-" + String.valueOf(day);
                mNgaysinh.setText(selectedDate);

                // Kiểm tra ràng buộc tuổi từ 18 đến 55
                int age = getAge(year, month, day);
                if (age < 18 || age > 55) {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Tuổi không hợp lệ", Toast.LENGTH_SHORT).show();
                    mNgaysinh.setText(null);
                    openDialog1();
                }
            }
        }, year, month, day);


        datePickerDialog.show();
    }

    private int getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private void showError(EditText mEdt, String s)
    {
        mEdt.setError(s);
    }
}


