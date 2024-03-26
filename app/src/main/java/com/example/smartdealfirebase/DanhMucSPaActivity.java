package com.example.smartdealfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartdealfirebase.Adapter.VoucherCategoryAdapter;
import com.example.smartdealfirebase.DesignPatternCommand.ThongTinVoucherActivity;
import com.example.smartdealfirebase.DesignPatternSingleton.FireBaseFireStoreSingleton;
import com.example.smartdealfirebase.DesignPatternStrategy.strategies;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DanhMucSPaActivity extends AppCompatActivity implements VoucherCategoryAdapter.Listener {
    RecyclerView rvDanhMucSpa;

    VoucherCategoryAdapter voucherSpaAdapter;

    ArrayList<Voucher> vouchersSpa;
    private FireBaseFireStoreSingleton fireBaseFireStoreSingleton;
    private FirebaseFirestore firestore;

    private strategies.IVoucherStrategy iVoucherStrategy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_spa);

        rvDanhMucSpa=findViewById(R.id.rvDanhMucSpa);
        vouchersSpa =new ArrayList<>();
        voucherSpaAdapter=new VoucherCategoryAdapter( vouchersSpa,this);

        fireBaseFireStoreSingleton = FireBaseFireStoreSingleton.getInstance();
        firestore = fireBaseFireStoreSingleton.getFirestore();

        // Sử dụng Strategy cho việc thêm các voucher vào danh sách ( chiến lược VoucherSpaStrategy)
        iVoucherStrategy = new strategies.VoucherSpaStrategy();
        firestore.collection("Voucher").orderBy("MaVoucher")
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

                            iVoucherStrategy.addToVouchersList(voucher,vouchersSpa);


                        }
                        voucherSpaAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        LinearLayoutManager l = new LinearLayoutManager(DanhMucSPaActivity.this,RecyclerView.VERTICAL,false);
        rvDanhMucSpa.addItemDecoration(new DividerItemDecoration(DanhMucSPaActivity.this,DividerItemDecoration.VERTICAL));
        rvDanhMucSpa.setLayoutManager(l);
        rvDanhMucSpa.setAdapter(voucherSpaAdapter);
    }

    @Override
    public void setOnInfoClick(Voucher voucher) {
        Intent intent = new Intent(DanhMucSPaActivity.this, ThongTinVoucherActivity.class);
        intent.putExtra("ThongTinVoucher",voucher);
        startActivity(intent);
    }

}