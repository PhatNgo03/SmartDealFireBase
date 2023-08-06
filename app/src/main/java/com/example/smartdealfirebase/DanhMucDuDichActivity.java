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
import com.example.smartdealfirebase.Adapter.VoucherDuLichAdapter;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DanhMucDuDichActivity extends AppCompatActivity implements VoucherDuLichAdapter.Listener {

    RecyclerView rvDanhMucDuLich;

    VoucherDuLichAdapter voucherDuLichAdapter;

    ArrayList<Voucher> vouchersDuLich;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_du_dich);


        rvDanhMucDuLich=findViewById(R.id.rvDanhmucDuLich);
        vouchersDuLich=new ArrayList<>();
        voucherDuLichAdapter=new VoucherDuLichAdapter( vouchersDuLich,this);
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
                            if (DanhMuc.equalsIgnoreCase("DuLich")) {
//                                vouchers.clear();
                                vouchersDuLich.add(voucher);}


                        }
                        voucherDuLichAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        LinearLayoutManager l = new LinearLayoutManager(DanhMucDuDichActivity.this,RecyclerView.VERTICAL,false);
        rvDanhMucDuLich.addItemDecoration(new DividerItemDecoration(DanhMucDuDichActivity.this,DividerItemDecoration.VERTICAL));
        rvDanhMucDuLich.setLayoutManager(l);
        rvDanhMucDuLich.setAdapter(voucherDuLichAdapter);
    }

    @Override
    public void setOnInfoClick(Voucher voucher) {
        Intent intent = new Intent(DanhMucDuDichActivity.this, ThongTinVoucherActivity.class);
        intent.putExtra("ThongTinVoucher",voucher);
        startActivity(intent);
    }
}