package com.example.smartdealfirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Adapter.CartAdapter;
import com.example.smartdealfirebase.Fragment.CartFragment;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThongTinVoucherActivity extends AppCompatActivity implements  CartFragment.OnOrderInfoEnteredListener{

    private ImageView ivVoucherImage;
    private TextView tvVoucherName, tvDiscountPrice, tvPrice, tvMota;
    private Button btBuy, btAddToCart;

    CartAdapter cartAdapter;
    FirebaseUser firebaseUser;
    Voucher voucher;

    private ActivityResultLauncher<Intent> buyActivityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_voucher);

        ivVoucherImage = findViewById(R.id.ivVoucherInfo);
        tvVoucherName = findViewById(R.id.tvIFVoucherName);
        tvDiscountPrice = findViewById(R.id.tvIFDiscountPrice);
        tvPrice = findViewById(R.id.tvIFPrice);
        tvMota = findViewById(R.id.tvIFMota);
        cartAdapter = new CartAdapter(new ArrayList<ItemCart>());
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ThongTinVoucher")) {
             voucher = (Voucher) intent.getSerializableExtra("ThongTinVoucher");
            if (voucher != null) {
                Glide.with(this).load(voucher.getHinhvc()).into(ivVoucherImage);
                tvVoucherName.setText(voucher.getVoucherName());
                tvDiscountPrice.setText(String.valueOf(voucher.getDiscountPrice()));
                tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvPrice.setText(String.valueOf(voucher.getPrice()));
                tvMota.setText(voucher.getMoTa());
            }
        }
        btBuy = findViewById(R.id.btMuaHang);
        btAddToCart = findViewById(R.id.btThemVaoGioHang);
        btAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voucherName = tvVoucherName.getText().toString();
                int discountPrice = Integer.parseInt(tvDiscountPrice.getText().toString());
                int price = Integer.parseInt(tvPrice.getText().toString());
                int quantity = 1;
                int Total = (quantity * discountPrice);
                List<String> imageUrls = new ArrayList<>();
                // Thực hiện truy vấn Firestore để lấy hình ảnh của voucher
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference voucherRef = db.collection("Voucher");
                voucherRef.whereEqualTo("TenVoucher", voucherName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (firebaseUser == null) {
                                            // Nếu người dùng chưa đăng nhập, không có dữ liệu để hiển thị
                                            return;
                                        }

                                        String userid = firebaseUser.getUid(); // Lấy email của người dùng hiện tại
                                        // Lấy link hình ảnh từ document
                                        String imageUrl = document.getString("HinhAnh");
                                        imageUrls.add(imageUrl);

                                        // Tiếp tục thêm dữ liệu vào Firestore
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> cartItem = new HashMap<>();
                                        cartItem.put("voucherName", voucherName);
                                        cartItem.put("discountPrice", discountPrice);
                                        cartItem.put("price", price);
                                        cartItem.put("quantity", quantity);
                                        cartItem.put("ToTal", Total);
                                        cartItem.put("HinhAnh", voucher.getHinhvc());
                                        cartItem.put("UserId",userid);

                                        db.collection("cartItems").add(cartItem)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        // Tạo một item mới từ dữ liệu được thêm vào Firestore
                                                        ItemCart itemCart = new ItemCart("", voucherName, discountPrice, price, quantity, Total, voucher.getHinhvc(),userid);
                                                        // Gọi phương thức addItemToCart trong CartFragment để cập nhật RecyclerView
                                                        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.framelayourCart);
                                                        if (cartFragment != null) {
                                                            cartFragment.addItemToCart(itemCart);
                                                        }
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinVoucherActivity.this);
                                                        builder.setTitle("Thành công");
                                                        builder.setMessage("Đã thêm vào giỏ hàng");
                                                        builder.setPositiveButton("OK", null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Xử lý khi có lỗi xảy ra khi lưu thông tin món hàng
                                                    }
                                                });

                                        cartAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    // Xử lý khi có lỗi xảy ra khi truy vấn dữ liệu voucher
                                }
                            }
                        });
            }
        });
        buyActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Xử lý kết quả trả về từ CartFragment (nếu cần)
                    }
                }
        );
        btBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String voucherName = tvVoucherName.getText().toString();
                int discountPrice = Integer.parseInt(tvDiscountPrice.getText().toString());
                int price = Integer.parseInt(tvPrice.getText().toString());
                int quantity = 1;
                int Total = (quantity * discountPrice);
                List<String> imageUrls = new ArrayList<>();
                // Thực hiện truy vấn Firestore để lấy hình ảnh của voucher
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference voucherRef = db.collection("Voucher");
                voucherRef.whereEqualTo("TenVoucher", voucherName)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        if (firebaseUser == null) {
                                            // Nếu người dùng chưa đăng nhập, không có dữ liệu để hiển thị
                                            return;
                                        }

                                        String userid = firebaseUser.getUid(); // Lấy email của người dùng hiện tại
                                        // Lấy link hình ảnh từ document
                                        String imageUrl = document.getString("HinhAnh");
                                        imageUrls.add(imageUrl);

                                        // Tiếp tục thêm dữ liệu vào Firestore
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> cartItem = new HashMap<>();
                                        cartItem.put("voucherName", voucherName);
                                        cartItem.put("discountPrice", discountPrice);
                                        cartItem.put("price", price);
                                        cartItem.put("quantity", quantity);
                                        cartItem.put("ToTal", Total);
                                        cartItem.put("HinhAnh", imageUrl);
                                        cartItem.put("UserId",userid);

                                        db.collection("cartItems").add(cartItem)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        // Tạo một item mới từ dữ liệu được thêm vào Firestore
                                                        ItemCart itemCart = new ItemCart("", voucherName, discountPrice, price, quantity, Total, imageUrl,userid);
                                                        // Gọi phương thức addItemToCart trong CartFragment để cập nhật RecyclerView
                                                        CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentById(R.id.framelayourCart);
                                                        if (cartFragment != null) {
                                                            cartFragment.addItemToCart(itemCart);
                                                        }
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinVoucherActivity.this);
                                                        builder.setTitle("Thành công");
                                                        builder.setMessage("Đã thêm vào giỏ hàng");
                                                        builder.setPositiveButton("OK", null);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Xử lý khi có lỗi xảy ra khi lưu thông tin món hàng
                                                    }
                                                });

                                        cartAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    // Xử lý khi có lỗi xảy ra khi truy vấn dữ liệu voucher
                                }
                            }
                        });
                CartFragment cartFragment = new CartFragment();
                Bundle bundle = new Bundle();
                Serializable itemCart = null;
                bundle.putSerializable("itemCart", itemCart);
                cartFragment.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frContent, cartFragment);
                transaction.commit();
            }
        });

    }
    @SuppressLint("ResourceType")
    private void navigateToCartFragment(ItemCart itemCart) {
        // Chuyển sang CartFragment và gửi thông tin sản phẩm qua Bundleli
        // Lấy dữ liệu từ Firebase Firestore và cập nhật vào adapter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cartItemsRef = db.collection("cartItems");
        cartItemsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ItemCart> cartItems = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Parse data từ document và thêm vào list cartItems
                        String voucherName = document.getString("voucherName");
                        int discountPrice = document.getLong("discountPrice").intValue();
                        int price = document.getLong("price").intValue();
                        int quantity = document.getLong("quantity").intValue();
                        int ToTal = document.getLong("ToTal").intValue();
                        ItemCart cartItem = new ItemCart("", voucherName, discountPrice, price, quantity,ToTal,"","");
                        cartItems.add(cartItem);
                    }
                    // Cập nhật dữ liệu vào adapter và hiển thị trong RecyclerView
                    cartAdapter.setItems(cartItems);
                    cartAdapter.notifyDataSetChanged();
                } else {
                    // Xử lý khi có lỗi xảy ra khi lấy dữ liệu từ Firestore
                }
            }
        });
        CartFragment cartFragment = new CartFragment();
        Bundle bundle = new Bundle();
        List<ItemCart> itemCarts = new ArrayList<>();
        itemCarts.add(itemCart);
        bundle.putSerializable("itemCarts", (Serializable) itemCarts);
        cartFragment.setArguments(bundle);

        // Thay thế fragment hiện tại bằng CartFragment
      getSupportFragmentManager().beginTransaction()
                .replace(R.id.frContent, cartFragment)
                .commit();
    }
    @Override
    public void onOrderInfoEntered() {

    }
}
