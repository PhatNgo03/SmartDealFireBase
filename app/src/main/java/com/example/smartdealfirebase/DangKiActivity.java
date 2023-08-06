package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DangKiActivity extends AppCompatActivity {
    EditText edtEmailDki,edtPasworDK, edtPrePass;
    Button btdangKi;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        edtEmailDki = findViewById(R.id.edtEmailDki);
        edtPasworDK = findViewById(R.id.edtMKDki);
        edtPrePass = findViewById(R.id.edtnhaplaiMKdki);

        btdangKi = findViewById(R.id.btdangky);
        btdangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regiter();

            }
        });



    }

    private void showError(EditText mEdt, String s)
    {
        mEdt.setError(s);
    }

    private boolean isPasswordValid(String password) {
        // Password should contain at least one letter or one digit, and length should be greater than 8.
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{8,}$");
    }
    private void regiter() {
        String email, pass,repass;
        email = edtEmailDki.getText().toString();
        pass = edtPasworDK.getText().toString();
        repass= edtPrePass.getText().toString();

        if(TextUtils.isEmpty(email)){
            showError(edtEmailDki,"Vui lòng nhập Email!!");
            edtEmailDki.requestFocus();
            return;
        }
        if(!pass.equals(repass)){
            showError(edtPrePass,"Mật khẩu không khớp!");
            edtPrePass.requestFocus();
            return;
        }
        if(!email.contains("@gmail.com")){
            showError(edtEmailDki, "Email đăng ký không hợp lệ");
            edtEmailDki.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            showError(edtPasworDK,"Vui lòng nhập password");
            return;
        }
        if (!isPasswordValid(pass)) {
            showError(edtPasworDK,"Mật khẩu không đủ điều kiện");
            edtPasworDK.requestFocus();
            return;
        }

        firestore.collection("NguoiDung").whereEqualTo("Email",email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if (task.getResult() != null && !task.getResult().isEmpty()){
                                Toast.makeText(DangKiActivity.this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(DangKiActivity.this, "Tạo tài khoản thành công!!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(DangKiActivity.this,DangNhapActivity.class));
                                            FirebaseUser nguoidung=mAuth.getCurrentUser();
                                            DocumentReference dr=firestore.collection("NguoiDung").document(nguoidung.getUid());
                                            Map<String,Object> nguoidunginfo=new HashMap<>();
                                            nguoidunginfo.put("Email",email);
                                            nguoidunginfo.put("MatKhau",pass);
                                            nguoidunginfo.put("Role","user");
                                            dr.set(nguoidunginfo);
                                        }
                                        else {
                                            Toast.makeText(DangKiActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(DangKiActivity.this, "Lỗi kiểm tra email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}