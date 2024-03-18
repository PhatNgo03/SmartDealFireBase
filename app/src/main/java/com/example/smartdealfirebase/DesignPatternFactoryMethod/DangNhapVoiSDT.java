package com.example.smartdealfirebase.DesignPatternFactoryMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartdealfirebase.DesignPatternSingleton.FireBaseFireStoreSingleton;
import com.example.smartdealfirebase.MainActivity;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DangNhapVoiSDT extends AppCompatActivity {

    EditText edtSdt, edtCodeVerify;
    Button btnRequestVerifyCode, btnConfirmCode;
    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;
    private FireBaseFireStoreSingleton fireBaseFireStoreSingleton;


    // Tạo interface cho Factory Method
    interface LoggingFactory {
       Logging createLogging(Context context);
    }
    // Triển khai Factory Method cho đăng nhập qua số điện thoại
    class PhoneLoggingFactory implements LoggingFactory {
        @Override
        public Logging createLogging(Context context) {
            return new PhoneLogging(context);
        }
    }
    // Tạo interface cho đối tượng đăng ký (Product)
    interface Logging {
        void requestVerificationCode(String phoneNumber);
        void verifyCode(String code);
    }

    // Class xử lý đăng ký bằng số điện thoại
    public class PhoneLogging implements Logging {
        private Context context;
        private FirebaseAuth mAuth;
        private String mVerificationId;

        public PhoneLogging(Context context) {
            this.context = context;
            mAuth = FirebaseAuth.getInstance();
            fireBaseFireStoreSingleton = FireBaseFireStoreSingleton.getInstance();
            firestore = fireBaseFireStoreSingleton.getFirestore();
        }
        @Override
        public void requestVerificationCode(String phoneNumber) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)       // Số điện thoại cần xác minh
                            .setTimeout(60L, TimeUnit.SECONDS) // Thời gian chờ tối đa
                            .setActivity(DangNhapVoiSDT.this)                 // Activity hiện tại
                            .setCallbacks(mCallbacks)          // Callbacks để xử lý kết quả xác minh
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }

        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Xác minh số điện thoại thành công
                        signInWithPhoneAuthCredential(credential);
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(context, "Xác minh số điện thoại thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // Mã xác minh đã được gửi thành công
                        mVerificationId = verificationId;
                    }
                };
        @Override
        public void verifyCode(String code) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }

        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(DangNhapVoiSDT.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = task.getResult().getUser();
                                saveUserInfo(user,mVerificationId);
                                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DangNhapVoiSDT.this, MainActivity.class));
                            } else {
                                Toast.makeText(context, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        private void saveUserInfo(FirebaseUser user, String verificationId) {
            DocumentReference userRef = firestore.collection("NguoiDungPhone").document(user.getUid());
            Map<String, Object> userData = new HashMap<>();
            userData.put("Phone", user.getPhoneNumber());
            userData.put("VerificationId", verificationId); // Thêm verificationId vào dữ liệu người dùng

            userRef.set(userData);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_voi_sdt);
        mAuth = FirebaseAuth.getInstance();

        edtSdt = findViewById(R.id.edtPhoneNumber);
        edtCodeVerify = findViewById(R.id.edtVerificationCode);
        btnRequestVerifyCode = findViewById(R.id.btnRequestVerifyCode);
        btnConfirmCode = findViewById(R.id.btnConfirmVerifyCode);
        // Tạo PhoneLoggingFactory
        LoggingFactory loggingFactory = new PhoneLoggingFactory();

        // Tạo đối tượng đăng nhập qua số điện thoại bằng Factory Method
        Logging logging = loggingFactory.createLogging(this);

        // Xử lý sự kiện khi nhấn nút "Yêu cầu mã xác minh"
        btnRequestVerifyCode.setOnClickListener(v -> {
            String phoneNumber = edtSdt.getText().toString().trim();
            if (!TextUtils.isEmpty(phoneNumber)) {
                logging.requestVerificationCode(phoneNumber);
            } else {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            }
        });
        // Xử lý sự kiện khi nhấn nút "Xác nhận mã"
        btnConfirmCode.setOnClickListener(v -> {
            String code = edtCodeVerify.getText().toString().trim();
            if (!TextUtils.isEmpty(code)) {
                logging.verifyCode(code);
            } else {
                Toast.makeText(this, "Vui lòng nhập mã xác minh", Toast.LENGTH_SHORT).show();
            }
        });
    }
}