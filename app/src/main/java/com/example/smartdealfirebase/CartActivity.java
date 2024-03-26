package com.example.smartdealfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartdealfirebase.Adapter.CartAdapter;
import com.example.smartdealfirebase.Model.Cart;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

//    private RecyclerView rvGioHang;
//    private CartAdapter cartAdapter;
//
//    private List<ItemCart> itemCarts = new ArrayList<>();
//    private FirebaseFirestore db;
//    private Cart cart;
//
//    private Button btTTGiaoHang;
//
//    private TextView tvTenSP, tvGiaGiam,tvGiaGoc;
//    private FirebaseAuth mAuth;
//    private String userId;
//    private DocumentReference cartRef;

    private  List<ItemCart> itemCarts = new ArrayList<>();
    private  CartAdapter cartAdapter;
    ImageView backCart;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cartItemsRef = db.collection("Cart");
    private RecyclerView rvGioHang;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("itemList")){
           itemCarts = (List<ItemCart>) intent.getSerializableExtra("itemList");

        }

        backCart = findViewById(R.id.backCart);
        backCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvGioHang = findViewById(R.id.rvGioHang);
        cartAdapter = new CartAdapter(itemCarts);

        rvGioHang.setAdapter(cartAdapter);
        rvGioHang.setLayoutManager(new LinearLayoutManager(this));

    }

}




