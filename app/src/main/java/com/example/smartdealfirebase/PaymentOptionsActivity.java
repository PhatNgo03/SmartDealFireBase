package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartdealfirebase.Model.ItemCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.model.DeferredIntentParams;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PaymentOptionsActivity extends AppCompatActivity {
    String PublishableKey = "pk_test_51OwNBGEQrqQPu8QEFcYXNJbVs4H9QYTaWAYDMiZY6oYTNtdG7K2uNXGlvKmmCfEpLEgbUoWgzHTE7zrIEzrFEtuQ00lAoU50ff";
    String SecretKey = "sk_test_51OwNBGEQrqQPu8QEtHYnWnI100l4A9aOfQMZ5bC8wiTAjjI5I2oQIpheFbHsaJmDxhfoTLGqR0XSKXOfcmuq1JHh00Fl4zewYn";
    String CustomerId;
    String EphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;
    LinearLayout linear_thanhtoan;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView backHTTT;
    ItemCart itemCart;
    String voucherName;
    int total, discountPrice,price, quantity;

    private boolean isPaymentCompleted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        linear_thanhtoan=findViewById(R.id.linear_thanhtoan);
        backHTTT=findViewById(R.id.backHTTT);

        itemCart = new ItemCart();
        List<ItemCart> selectedItems = (List<ItemCart>) getIntent().getSerializableExtra("selectedItems");
        // Kiểm tra xem danh sách đã được truyền thành công hay không
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (ItemCart item : selectedItems) {
                // Trích dẫn thông tin từ mỗi mục và lưu vào Firestore
                    voucherName = item.getVoucherName();
                    discountPrice = item.getDiscountPrice();
                    price = item.getPrice();
                    quantity = item.getQuantity();
                    total = item.getToTal();

                // Tạo một Map chứa thông tin đặt hàng
                Map<String, Object> order = new HashMap<>();
                order.put("voucherName", voucherName);
                order.put("discountPrice", discountPrice);
                order.put("price", price);
                order.put("quantity", quantity);
                order.put("ToTal", total);
                order.put("NgayMua", getCurrentDate());
                order.put("Email", item.getUserId());

                // Lưu thông tin đặt hàng vào Firestore
                addOrderToFirestore(order);
            }
        } else {
            // Xử lý trường hợp không có mục nào được truyền
            Toast.makeText(PaymentOptionsActivity.this, "Không có mục nào được chọn!", Toast.LENGTH_SHORT).show();
        }
        PaymentConfiguration.init(this, PublishableKey);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            if (paymentSheetResult != null) {
                onPaymentResult(paymentSheetResult);
            } else {
                Log.d("PaymentOptions", "paymentSheetResult is null");

            }
        });

        backHTTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        linear_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createStripeCustomer();

                if (CustomerId != null) {
                    paymentFlow();
                } else {
                }
            }
        });

    }
private void getUserInfoFromFirestore() {
    // Lấy thông tin của người dùng hiện tại
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser != null) {
        String userId = firebaseUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("NguoiDung").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String email = documentSnapshot.getString("Email");

                    // Thiết lập trường "Email" cho đơn hàng
                    itemCart.setUserId(email);
                } else {
                    // Xử lý nếu không tìm thấy thông tin người dùng
                    Toast.makeText(PaymentOptionsActivity.this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý nếu có lỗi khi lấy thông tin người dùng từ Firestore
                Toast.makeText(PaymentOptionsActivity.this, "Đã xảy ra lỗi khi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    } else {
        // Xử lý nếu không có người dùng đăng nhập
        Toast.makeText(PaymentOptionsActivity.this, "Không tìm thấy người dùng đăng nhập!", Toast.LENGTH_SHORT).show();
    }
}
private void addOrderToFirestore(Map<String, Object> order) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Lưu thông tin đặt hàng vào Firestore
    db.collection("orders").add(order)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // Xử lý khi lưu thông tin đặt hàng thành công
//                    Toast.makeText(PaymentOptionsActivity.this, "Cảm ơn đã mua hàng!", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Xử lý khi có lỗi xảy ra khi lưu thông tin đặt hàng
                    Toast.makeText(PaymentOptionsActivity.this, "Xảy ra lỗi khi thanh toán!", Toast.LENGTH_SHORT).show();
                }
            });
}
    private void createStripeCustomer() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        CustomerId = object.optString("id");
                        Toast.makeText(PaymentOptionsActivity.this, CustomerId, Toast.LENGTH_SHORT).show();
                        getEphericalKey();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(PaymentOptionsActivity.this, "Error creating customer: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentOptionsActivity.this);
        requestQueue.add(request);
    }
    private void getEphericalKey() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        EphericalKey = object.optString("id");
                        Toast.makeText(PaymentOptionsActivity.this, EphericalKey, Toast.LENGTH_SHORT).show();
                        getClientSecret(CustomerId, EphericalKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(PaymentOptionsActivity.this, "Error getting EphericalKey: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2023-10-16");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void getClientSecret(String customerId, String ephericalKey) {
        final int fixedAmount = 1500000;
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        ClientSecret = object.optString("client_secret");

                        if (ClientSecret != null && !ClientSecret.isEmpty()) {
                            paymentFlow(); // Call paymentFlow once ClientSecret is obtained
                        } else {
                            Toast.makeText(PaymentOptionsActivity.this, "Error: Empty or null ClientSecret", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(PaymentOptionsActivity.this, "Error getting ClientSecret: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                params.put("amount", String.valueOf(fixedAmount));
                params.put("currency", "USD"); // Replace with your currency
                // Add any other necessary parameters
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void paymentFlow() {
        if (ClientSecret == null) {
            Toast.makeText(PaymentOptionsActivity.this, "Error: ClientSecret is null", Toast.LENGTH_SHORT).show();
        } else {
            paymentSheet.presentWithPaymentIntent(ClientSecret, new PaymentSheet.Configuration("Learn with Arvind",
                    new PaymentSheet.CustomerConfiguration(CustomerId, EphericalKey)));
        }
    }
    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            isPaymentCompleted = true;
            if(isPaymentCompleted){
                getUserInfoFromFirestore();
                Toast.makeText(PaymentOptionsActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
            }
            else {

            }


        }
    }
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

}