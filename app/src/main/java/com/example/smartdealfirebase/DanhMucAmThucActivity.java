package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartdealfirebase.Adapter.VoucherAdapter;
import com.example.smartdealfirebase.Adapter.VoucherAmThucAdapter;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DanhMucAmThucActivity extends AppCompatActivity implements VoucherAmThucAdapter.Listener {

    RecyclerView recyclerViewAmThuc;

    VoucherAmThucAdapter voucherAdapterAmThuc;

    ArrayList<Voucher> vouchersAmThuc;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_am_thuc);

        recyclerViewAmThuc=findViewById(R.id.rvdmat);
        vouchersAmThuc=new ArrayList<>();
        voucherAdapterAmThuc=new VoucherAmThucAdapter(vouchersAmThuc,this);
        db=FirebaseFirestore.getInstance();
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
                            if (DanhMuc.equalsIgnoreCase("AmThuc")) {
//                                vouchers.clear();
                                vouchersAmThuc.add(voucher);}


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