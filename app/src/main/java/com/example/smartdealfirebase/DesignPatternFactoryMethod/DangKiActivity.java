package com.example.smartdealfirebase.DesignPatternFactoryMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartdealfirebase.DangNhapActivity;
import com.example.smartdealfirebase.DesignPatternSingleton.FireBaseFireStoreSingleton;
import com.example.smartdealfirebase.R;
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
    EditText edtEmailRegister,edtPaswordRegister, edtPrePass;
    Button btRegisterWithEmail;
    private FirebaseAuth mAuth;

    private FireBaseFireStoreSingleton fireBaseFireStoreSingleton;
    private FirebaseFirestore firestore;

    // ConcreteCretor : các lớp cụ thể triển khai Creator và triển khai Factory Method để tạo ra các đối tượng Product cụ thể.
   public class EmailRegistrationFactory implements RegistrationFactory {
        @Override
        public IRegistration createRegistration() {
            return new EmailRegistration();
        }
    }

    // ConcreteProduct: Lớp cụ thể để thực hiện việc đăng kí
    public  class EmailRegistration implements IRegistration {
        @Override
        public void register() {
            String email, pass, repass;
            email = edtEmailRegister.getText().toString();
            pass = edtPaswordRegister.getText().toString();
            repass = edtPrePass.getText().toString();

            if (TextUtils.isEmpty(email)) {
                showError(edtEmailRegister, "Vui lòng nhập Email!!");
                edtEmailRegister.requestFocus();
                return;
            }
            if (!pass.equals(repass)) {
                showError(edtPrePass, "Mật khẩu không khớp!");
                edtPrePass.requestFocus();
                return;
            }
            if (!email.contains("@gmail.com")) {
                showError(edtEmailRegister, "Email đăng ký không hợp lệ");
                edtEmailRegister.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                showError(edtPaswordRegister, "Vui lòng nhập password");
                return;
            }
            if (!isPasswordValid(pass)) {
                showError(edtPaswordRegister, "Mật khẩu không đủ điều kiện");
                edtPaswordRegister.requestFocus();
                return;
            }
            firestore.collection("NguoiDung").whereEqualTo("Email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null && !task.getResult().isEmpty()) {
                                    Toast.makeText(DangKiActivity.this, "Email đang bị trống!", Toast.LENGTH_SHORT).show();
                                } else {
                                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(DangKiActivity.this, "Tạo tài khoản thành công!!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(DangKiActivity.this, DangNhapActivity.class));
                                                FirebaseUser nguoidung = mAuth.getCurrentUser();
                                                DocumentReference dr = firestore.collection("NguoiDung").document(nguoidung.getUid());
                                                Map<String, Object> nguoidunginfo = new HashMap<>();
                                                nguoidunginfo.put("Email", email);
                                                nguoidunginfo.put("MatKhau", pass);
                                                nguoidunginfo.put("Role", "user");
                                                dr.set(nguoidunginfo);
                                            } else {
                                                Toast.makeText(DangKiActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(DangKiActivity.this, "Lỗi kiểm tra email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki);
        mAuth = FirebaseAuth.getInstance();
        fireBaseFireStoreSingleton = FireBaseFireStoreSingleton.getInstance();
        firestore = fireBaseFireStoreSingleton.getFirestore();
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPaswordRegister = findViewById(R.id.edtPasswordRegister);
        edtPrePass = findViewById(R.id.edtRepassRegister);

        btRegisterWithEmail = findViewById(R.id.btRegisterWithEmail);

        btRegisterWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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

    // Sử dụng Factory
    public void register(){
        //lấy instance của factory dựa trên loại concreteCreator
        RegistrationFactory factory = new EmailRegistrationFactory();
        IRegistration registration = factory.createRegistration();
        registration.register();
    }

}