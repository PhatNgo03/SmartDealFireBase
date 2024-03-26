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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class ThongTinVoucherActivity extends AppCompatActivity {

    private ImageView ivVoucherImage;
    private TextView tvVoucherName, tvDiscountPrice, tvPrice, tvMota;
    private Button btBuy, btAddToCart;

    private CheckBox cbYeuThich;
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
        cbYeuThich=findViewById(R.id.TymButton);
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
                int total = quantity * discountPrice;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference cartItemsRef = db.collection("cartItems");
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null) {
                    // Nếu người dùng chưa đăng nhập, không có dữ liệu để hiển thị
                    return;
                }

                String userid = firebaseUser.getUid();
                cartItemsRef.whereEqualTo("voucherName", voucherName)
                        .whereEqualTo("UserId", userid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean voucherExists = false;
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        voucherExists = true;
                                        String cartItemId = document.getId();
                                        int existingQuantity = document.getLong("quantity").intValue();
                                        int newQuantity = existingQuantity + quantity;
                                        int newTotal = newQuantity * discountPrice;

                                        // Cập nhật số lượng và tổng tiền của voucher trong Firestore
                                        DocumentReference cartItemRef = db.collection("cartItems").document(cartItemId);
                                        cartItemRef.update("quantity", newQuantity);
                                        cartItemRef.update("ToTal", newTotal)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Thông báo cho người dùng rằng số lượng đã được cập nhật thành công
                                                        Toast.makeText(ThongTinVoucherActivity.this, "Đã cập nhật số lượng trong giỏ hàng", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Xử lý khi có lỗi xảy ra khi cập nhật số lượng
                                                    }
                                                });
                                    }

                                    if (!voucherExists) {
                                        // Nếu voucher chưa tồn tại trong giỏ hàng, thêm mới
                                        Map<String, Object> cartItem = new HashMap<>();
                                        cartItem.put("voucherName", voucherName);
                                        cartItem.put("discountPrice", discountPrice);
                                        cartItem.put("price", price);
                                        cartItem.put("quantity", quantity);
                                        cartItem.put("ToTal", total);
                                        cartItem.put("HinhAnh", voucher.getHinhvc());
                                        cartItem.put("UserId", userid);

                                        db.collection("cartItems").add(cartItem)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
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
                // Code hiện tại của bạn để thêm vào giỏ hàng
                // Sau khi thêm vào giỏ hàng thành công, bạn có thể chuyển sang fragment giỏ hàng và truyền thông tin của sản phẩm
                CartFragment cartFragment = new CartFragment();
                Bundle bundle = new Bundle();
                List<ItemCart> itemCarts = new ArrayList<>();
                ItemCart itemCart = new ItemCart("", voucher.getVoucherName(), voucher.getDiscountPrice(), voucher.getPrice(), 1, voucher.getDiscountPrice(), voucher.getHinhvc(), firebaseUser.getUid());
                itemCarts.add(itemCart);
                bundle.putSerializable("itemCarts", (Serializable) itemCarts);
                cartFragment.setArguments(bundle);

                // Thay thế fragment hiện tại bằng CartFragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frContent, cartFragment)
                        .commit();
            }
        });

        cbYeuThich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    voucher.setYeuThich(true);
                    Toast.makeText(ThongTinVoucherActivity.this, "Thành công ", Toast.LENGTH_SHORT).show();
                    putChuyenBayYeuThich(voucher);
                } else {
                    voucher.setYeuThich(false);
                    xoaDulieuTrenFirestore(voucher);
                    cbYeuThich.setBackgroundResource(R.drawable.heart_24);
                }
            }
        });

    }
    private  void putChuyenBayYeuThich(Voucher voucher){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> favoriteItem = new HashMap<>();
        favoriteItem.put("voucherId",voucher.get_id());
        favoriteItem.put("MaVoucher",voucher.getMaVoucher());
        favoriteItem.put("UserId", userID);
        favoriteItem.put("TenVoucher", voucher.getVoucherName());
        favoriteItem.put("HinhAnh",voucher.getHinhvc());
        favoriteItem.put("GiaGiam",voucher.getDiscountPrice());
        favoriteItem.put("GiaGoc",voucher.getPrice());
        favoriteItem.put("MoTa",voucher.getMoTa());
        favoriteItem.put("DanhMuc",voucher.getDanhMuc());
        favoriteItem.put("SLNguoiMua",voucher.getSlnguoimua());
        favoriteItem.put("isYeuThich", voucher.isYeuThich());
        db.collection("favorites")
                .add(favoriteItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        cbYeuThich.setBackgroundResource(R.drawable.heart_red_24);
                        Toast.makeText(ThongTinVoucherActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isYeuThich_" + voucher.getMaVoucher(), voucher.isYeuThich());
                        editor.putInt("checkBoxBackground_" + voucher.getVoucherName(), R.drawable.heart_red_24);
                        editor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        // Xử lý khi thêm item thất bại
                        Toast.makeText(ThongTinVoucherActivity.this, "Lỗi ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void xoaDulieuTrenFirestore(Voucher voucher) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy ID của voucher
        String voucherId = voucher.get_id();

        // Kiểm tra xem ID của voucher có tồn tại không
        if (voucherId != null && !voucherId.isEmpty()) {
            // Thực hiện xóa tài liệu từ collection "favorites" với ID là voucherId
            db.collection("favorites")
                    .document(voucherId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Xóa thành công
                            Toast.makeText(ThongTinVoucherActivity.this, "Đã xóa khỏi danh sách yêu thích!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            // Xảy ra lỗi khi xóa
                            Toast.makeText(ThongTinVoucherActivity.this, "Lỗi khi xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Nếu voucherId không tồn tại hoặc rỗng, hiển thị thông báo lỗi
            Toast.makeText(ThongTinVoucherActivity.this, "Không thể xóa vì thiếu thông tin ID của voucher!", Toast.LENGTH_SHORT).show();
        }
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
//    @Override
//    public void onOrderInfoEntered() {
//
//    }
}
