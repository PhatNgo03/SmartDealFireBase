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

    EditText mSDT, memail, edten, mngaysinh, mdiachi;
    FirebaseFirestore db;
    Button btnluu;
    ImageView back;

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
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfoInFirestore();
            }
        });

        // Fetch the user information from Firestore using userEmail
        fetchUserInfoFromFirestore();

        // ... (rest of the code)
    }

    private void initView() {
        memail = findViewById(R.id.edtEmail);
        edten = findViewById(R.id.edthovaten);
        mSDT = findViewById(R.id.edtSDT);
        mngaysinh = findViewById(R.id.edtngaysinh);

        mdiachi = findViewById(R.id.edtdiachi);
        btnluu = findViewById(R.id.btnsuatt);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mngaysinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog1();
            }
        });
    }

    private void fetchUserInfoFromFirestore() {
        DocumentReference docRef = db.collection("KhachHang").document(userEmail);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String hoten = documentSnapshot.getString("HoTen");
                    String ngaysinh = documentSnapshot.getString("NgaySinh");
                    String diachi = documentSnapshot.getString("DiaChi");
                    String sdt = documentSnapshot.getString("SDT");
                    String gioiTinh = documentSnapshot.getString("GioiTinh");
                    int gioiTinhIndex = gioiTinhAdapter.getPosition(String.valueOf(gioiTinh));
                    spinnerGioiTinh.setSelection(gioiTinhIndex);
                    // Set the fetched data to the EditText fields
                    edten.setText(hoten);
                    mngaysinh.setText(ngaysinh);
                    spinnerGioiTinh.setTag(gioiTinh);
                    mdiachi.setText(diachi);
                    mSDT.setText(sdt);
                    memail.setText(userEmail);
                    memail.setEnabled(false);
                } else {
                    // Document does not exist, leave EditText fields empty
                    memail.setText("");

                }
            }
        });
    }

//    private void updateUserInfoInFirestore() {
//        // Get the updated user information from EditText fields
//        String hoten = edten.getText().toString();
//        String ngaysinh = mngaysinh.getText().toString();
//        String gioitinh = spinnerGioiTinh.getSelectedItem().toString();
//        String diachi = mdiachi.getText().toString();
//        String sdt = mSDT.getText().toString();
//
//
//        if (sdt.length() < 8 || sdt.length() > 10) {
//           showError(mSDT,"Số điện thoại phải có từ 8 đến 10 ký tự.");
//            return;
//        }
//        // Create a map to hold the user information
//        Map<String, Object> userMap = new HashMap<>();
//        userMap.put("HoTen", hoten);
//        userMap.put("NgaySinh", ngaysinh);
//        userMap.put("GioiTinh", gioitinh);
//        userMap.put("DiaChi", diachi);
//        userMap.put("SDT", sdt);
//
//        // Update the user information in Firestore
//        db.collection("KhachHang").document(userEmail)
//                .set(userMap)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(ThongTinCaNhanActivity.this, "User information updated successfully.", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ThongTinCaNhanActivity.this, "Failed to update user information. Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private void updateUserInfoInFirestore() {
        // Get the updated user information from EditText fields
        String hoten = edten.getText().toString();
        String ngaysinh = mngaysinh.getText().toString();
        String gioitinh = spinnerGioiTinh.getSelectedItem().toString();
        String diachi = mdiachi.getText().toString();
        String sdt = mSDT.getText().toString();

        if (sdt.length() < 8 || sdt.length() > 10) {
            showError(mSDT, "Số điện thoại phải có từ 8 đến 10 ký tự.");
            return;
        }

        // Create a query to search for the phone number in Firestore
        Query query = db.collection("KhachHang").whereEqualTo("SDT", sdt);

        // Perform the query
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    if (task.getResult().size() > 0) {
//                        showError(mSDT, "Số điện thoại đã tồn tại.");
                      {

                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("HoTen", hoten);
                        userMap.put("NgaySinh", ngaysinh);
                        userMap.put("GioiTinh", gioitinh);
                        userMap.put("DiaChi", diachi);
                        userMap.put("SDT", sdt);


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
                mngaysinh.setText(selectedDate);

                // Kiểm tra ràng buộc tuổi từ 18 đến 55
                int age = getAge(year, month, day);
                if (age < 18 || age > 55) {
                    Toast.makeText(ThongTinCaNhanActivity.this, "Tuổi không hợp lệ", Toast.LENGTH_SHORT).show();
                    mngaysinh.setText(null);
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


