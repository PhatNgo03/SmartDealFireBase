package com.example.smartdealfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartdealfirebase.Admin.NhaCungCapActivity;
import com.example.smartdealfirebase.DesignPatternFactoryMethod.DangKiActivity;
import com.example.smartdealfirebase.DesignPatternFactoryMethod.DangNhapVoiSDT;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DangNhapActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    Button btLoginEmail, btLoginSDT;
    TextView tvDangKi;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    DocumentReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);


        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmailLogin);
        edtPassword = findViewById(R.id.edtMKLogin);
        btLoginEmail = findViewById(R.id.btdangnhap);
        tvDangKi = findViewById(R.id.tvdangky);
        btLoginSDT = findViewById(R.id.btDangNhapVoiSDT);
        btLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, passAgain;
                email=edtEmail.getText().toString();
                pass=edtPassword.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(DangNhapActivity.this, "Vui long nhap email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(DangNhapActivity.this, "Vui long nhap password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        checkRoleUser(authResult.getUser().getUid());
                    }
                });
            }
        });

        tvDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangKiActivity.class);
                startActivity(intent);
            }
        });
        btLoginSDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhapActivity.this, DangNhapVoiSDT.class);
                startActivity(intent);
            }
        });
    }
    private void checkRoleUser(String uid) {
        firestore = FirebaseFirestore.getInstance();
        dr = firestore.collection("NguoiDung").document(uid);
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("Tag", "onSucess" + documentSnapshot.getData());

                if (documentSnapshot.getString("Role").equals("ncc")) {
                    Toast.makeText(DangNhapActivity.this, "Tài khoản của nhà cung cấp", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DangNhapActivity.this, NhaCungCapActivity.class));
                    finish();
                } else if (documentSnapshot.getString("Role").equals("user")) {

                    Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    }