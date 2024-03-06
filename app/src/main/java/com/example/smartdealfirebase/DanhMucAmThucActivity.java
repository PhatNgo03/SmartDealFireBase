package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartdealfirebase.Adapter.VoucherCategoryAdapter;
import com.example.smartdealfirebase.DesignPatternStrategy.strategies;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DanhMucAmThucActivity extends AppCompatActivity implements VoucherCategoryAdapter.Listener {

    RecyclerView recyclerViewAmThuc;

    VoucherCategoryAdapter voucherAdapterAmThuc;

    ArrayList<Voucher> vouchersAmThuc;

    FirebaseFirestore db;

    private strategies.IVoucherStrategy iVoucherStrategy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_am_thuc);

        recyclerViewAmThuc=findViewById(R.id.rvdmat);
        vouchersAmThuc=new ArrayList<>();
        voucherAdapterAmThuc=new VoucherCategoryAdapter(vouchersAmThuc,this);
        db=FirebaseFirestore.getInstance();

        // Sử dụng Strategy cho việc thêm các voucher vào danh sách ( chiến lược AmThucVoucherStrategy)
        iVoucherStrategy = new strategies.AmThucVoucherStrategy();
        db.collection("Voucher").orderBy("MaVoucher")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot document : task.getResult()){
                            String MaVoucher=document.get("MaVoucher").toString();
                            String TenVoucher=document.get("TenVoucher").toString();
                            Integer GiaGiam= Integer.parseInt(document.get("GiaGiam").toString());
                            Integer GiaGoc= Integer.parseInt(document.get("GiaGoc").toString());
                            Integer SlNguoimua=Integer.parseInt(document.get("SLNguoiMua").toString());
                            String Mota=document.get("MoTa").toString();
                            String DanhMuc=document.get("DanhMuc").toString();
                            String Hinh=document.get("HinhAnh").toString();

                            Voucher voucher = new Voucher(MaVoucher,TenVoucher,GiaGiam,GiaGoc,Mota,DanhMuc,SlNguoimua,Hinh);
                            // Thêm voucher vào danh sách sử dụng chiến lược
                            iVoucherStrategy.addToVouchersList(voucher, vouchersAmThuc);

                        }
                        voucherAdapterAmThuc.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        LinearLayoutManager l = new LinearLayoutManager(DanhMucAmThucActivity.this,RecyclerView.VERTICAL,false);
        recyclerViewAmThuc.addItemDecoration(new DividerItemDecoration(DanhMucAmThucActivity.this,DividerItemDecoration.VERTICAL));
        recyclerViewAmThuc.setLayoutManager(l);
        recyclerViewAmThuc.setAdapter(voucherAdapterAmThuc);
    }

    @Override
    public void setOnInfoClick(Voucher voucher) {
        Intent intent = new Intent(DanhMucAmThucActivity.this, ThongTinVoucherActivity.class);
        intent.putExtra("ThongTinVoucher",voucher);
        startActivity(intent);
    }
}