package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.example.smartdealfirebase.Adapter.CartAdapter;
import com.example.smartdealfirebase.Adapter.OrderAdapter;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.Model.TestLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LichSuMuaHang extends AppCompatActivity {
    private RecyclerView rvLsMuaHang;

    private OrderAdapter orderAdapter;
    private List<ItemCart> itemCarts;
    private FirebaseFirestore db;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_mua_hang);
        rvLsMuaHang = findViewById(R.id.rvLSMuaHang);
        rvLsMuaHang.setLayoutManager(new LinearLayoutManager(this));
        itemCarts = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, itemCarts);
        rvLsMuaHang.setAdapter(orderAdapter);
        rvLsMuaHang.setHasFixedSize(true);

        db = FirebaseFirestore.getInstance();
        loadOrderHistory();
        orderAdapter.setOnItemLongClickListener(new OrderAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LichSuMuaHang.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc muốn xóa mục này?");

                // Xử lý khi nhấn nút "Xác nhận"
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xóa item từ Firestore
                        deleteItemFromFirebase(itemCarts.get(position));
                    }
                });

                // Xử lý khi nhấn nút "Hủy"
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Hiển thị dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    private void loadOrderHistory() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            String userEmail = firebaseUser.getEmail();

            db.collection("orders")
                    .whereEqualTo("Email", firebaseUser.getUid())
                    .orderBy("NgayMua", Query.Direction.DESCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                // Handle the error if there's any
                                return;
                            }

                            if (value != null && !value.isEmpty()) {
                                itemCarts.clear();
                                for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                                    ItemCart itemCart = queryDocumentSnapshot.toObject(ItemCart.class);
                                    String voucherName = itemCart.getVoucherName();
                                    int total = itemCart.getToTal();
                                    String date = itemCart.getNgayMua();

                                    // Hiển thị thông tin trong giao diện người dùng, ví dụ:
                                    Log.d("ItemCart", "Voucher Name: " + voucherName);
                                    Log.d("ItemCart", "Total: " + total);
                                    Log.d("ItemCart", "Date: " + date);
                                    itemCarts.add(itemCart);
                                }
                                orderAdapter.notifyDataSetChanged();
                            } else {

                            }
                        }
                    });
        }
    }
    private void deleteItemFromFirebase(ItemCart itemCart) {
        CollectionReference ordersRef = db.collection("orders");

        // Tạo query để tìm các documents có Email và voucherName tương ứng với itemCart
        ordersRef.whereEqualTo("Email", itemCart.getUserId())
                .whereEqualTo("voucherName", itemCart.getVoucherName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Duyệt qua tất cả các documents tìm được
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Xóa document tương ứng với itemCart
                                document.getReference().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Delete", "Item deleted successfully");

                                                    // Sau khi xóa từ Firebase, cập nhật RecyclerView
                                                    itemCarts.remove(itemCart);
                                                    orderAdapter.notifyDataSetChanged();
                                                } else {
                                                    Log.e("Delete", "Error deleting item: " + task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            Log.e("Delete", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}
