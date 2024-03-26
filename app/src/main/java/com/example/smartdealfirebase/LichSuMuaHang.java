package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
    private CartAdapter cartAdapter;
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
    }

    private void loadOrderHistory() {

//        db.collection("orders").orderBy("NgayMua", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
//
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                itemCarts.clear();
//                for(QueryDocumentSnapshot queryDocumentSnapshot: value){
//                    ItemCart itemCart =queryDocumentSnapshot.toObject(ItemCart.class);
//                    itemCarts.add(itemCart);
//                }
//
//                orderAdapter.notifyDataSetChanged();
//            }
//        });
        if (firebaseUser != null) {
            String userEmail = firebaseUser.getEmail();

            db.collection("orders")
                    .whereEqualTo("Email", userEmail)
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
                                    itemCarts.add(itemCart);
                                }
                                orderAdapter.notifyDataSetChanged();
                            } else {
                                // Handle the case when there are no orders for the user
                                // For example, show a message that there are no orders
                            }
                        }
                    });
        }
    }
}
