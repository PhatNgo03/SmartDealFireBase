package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.example.smartdealfirebase.Adapter.TimKiemAdapter;
import com.example.smartdealfirebase.DesignPatternCommand.ThongTinVoucherActivity;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TimKiemActivity extends AppCompatActivity implements TimKiemAdapter.Listener{

    RecyclerView recyclerViewVoucher;

    ArrayList<Voucher> vouchers;

    TimKiemAdapter timKiemAdapter;

    FirebaseFirestore db;


    private EditText edtSearch;
    private ImageButton btSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);

        recyclerViewVoucher = findViewById(R.id.rvTimKiemSach);
        vouchers = new ArrayList<>();

        timKiemAdapter = new TimKiemAdapter(vouchers, this);
        recyclerViewVoucher.setAdapter(timKiemAdapter); // Set the adapter to the RecyclerView
        recyclerViewVoucher.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        edtSearch = findViewById(R.id.edtSearch);
        btSearch=findViewById(R.id.btnSearch);
        db = FirebaseFirestore.getInstance();

//        db.collection("Voucher").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                    String Idvc=(String) documentSnapshot.get("MaVoucher");
//                    String TenVC=(String)documentSnapshot.get("TenVoucher");
//                    int giagiam = Integer.parseInt(documentSnapshot.getString("GiaGiam"));
//                    int giagoc = Integer.parseInt(documentSnapshot.getString("GiaGoc"));
//                    String danhmuc = (String) documentSnapshot.get("DanhMuc");
//                    String MoTa = (String) documentSnapshot.get("MoTa");
//                    String HinhAnh = (String) documentSnapshot.get("HinhAnh");
//                    int slngmua = Integer.parseInt(documentSnapshot.getString("SLNguoiMua"));
//                    Voucher voucher=new Voucher(Idvc,TenVC,giagiam,giagoc,MoTa,danhmuc,slngmua,HinhAnh);
//                    vouchers.add(voucher);
//                }
//                timKiemAdapter.notifyDataSetChanged();
//            }
//        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ten =  edtSearch.getText().toString().trim();
                if(Ten.isEmpty()){
                    return;
                }
                else {
                    searchData(Ten);
                }

            }
        });

        //Add a TextChangeListener to the SearchView
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                timKiemAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                timKiemAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });


    }

    private void searchData(String ten) {
        db = FirebaseFirestore.getInstance();
        db.collection("Voucher").whereGreaterThanOrEqualTo("TenVoucher", ten)
                .whereLessThan("TenVoucher", ten + "\uf8ff")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        vouchers.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String TenVoucher = document.get("TenVoucher").toString();
                            String HinhAnh  = document.get("HinhAnh").toString();
                            Integer GiaGiam =Integer.parseInt(document.get("GiaGiam").toString());
                            Integer GiaGoc =Integer.parseInt(document.get("GiaGoc").toString());
                            Integer SLmua =Integer.parseInt(document.get("SLNguoiMua").toString());
                            String MoTa = document.get("MoTa").toString();
                            String DanhMuc = document.get("DanhMuc").toString();
                            String MaVoucher = document.get("MaVoucher").toString();
                            Voucher voucher = new Voucher(MaVoucher, TenVoucher, GiaGiam,GiaGoc,MoTa,DanhMuc,SLmua,HinhAnh);
                            vouchers.add(voucher);
                        }

                        timKiemAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        return false;

    }

    @Override
    public void setOnInfoClick(Voucher voucher) {
        Intent intent = new Intent(this, ThongTinVoucherActivity.class);
        intent.putExtra("ThongTinVoucher", voucher);
        startActivity(intent);
    }
}