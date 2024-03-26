package com.example.smartdealfirebase;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.smartdealfirebase.Adapter.VoucherCategoryAdapter;
import com.example.smartdealfirebase.DesignPatternCommand.ThongTinVoucherActivity;
import com.example.smartdealfirebase.DesignPatternSingleton.FireBaseFireStoreSingleton;
import com.example.smartdealfirebase.DesignPatternStrategy.strategies;
import com.example.smartdealfirebase.Model.Voucher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DanhMucDuDichActivity extends AppCompatActivity implements VoucherCategoryAdapter.Listener {

    RecyclerView rvDanhMucDuLich;
    VoucherCategoryAdapter voucherDuLichAdapter;
    ArrayList<Voucher> vouchersDuLich;
    private strategies.IVoucherStrategy iVoucherStrategy;

   private FireBaseFireStoreSingleton fireBaseFireStoreSingleton;
   private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_du_dich);


        rvDanhMucDuLich=findViewById(R.id.rvDanhmucDuLich);
        vouchersDuLich=new ArrayList<>();
        voucherDuLichAdapter = new VoucherCategoryAdapter( vouchersDuLich,this);

        fireBaseFireStoreSingleton = FireBaseFireStoreSingleton.getInstance();
        firestore = fireBaseFireStoreSingleton.getFirestore();

        // Sử dụng Strategy cho việc thêm các voucher vào danh sách ( chiến lược DuLichVoucherStrategy)
        iVoucherStrategy = new strategies.DuLichVoucherStrategy();
        loadDataFromFireStore();

        LinearLayoutManager l = new LinearLayoutManager(DanhMucDuDichActivity.this,RecyclerView.VERTICAL,false);
        rvDanhMucDuLich.addItemDecoration(new DividerItemDecoration(DanhMucDuDichActivity.this,DividerItemDecoration.VERTICAL));
        rvDanhMucDuLich.setLayoutManager(l);
        rvDanhMucDuLich.setAdapter(voucherDuLichAdapter);
    }
    private void loadDataFromFireStore() {
        vouchersDuLich.clear();

        firestore.collection("Voucher")
                .orderBy("MaVoucher")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String MaVoucher = document.getString("MaVoucher");
                                String TenVoucher = document.getString("TenVoucher");
                                Integer GiaGiam= Integer.parseInt(document.get("GiaGiam").toString());
                                Integer GiaGoc= Integer.parseInt(document.get("GiaGoc").toString());
                                Integer SlNguoimua=Integer.parseInt(document.get("SLNguoiMua").toString());
                                String Mota = document.getString("MoTa");
                                String DanhMuc = document.getString("DanhMuc");
                                String Hinh = document.getString("HinhAnh");

                                Voucher voucher = new Voucher(MaVoucher, TenVoucher, GiaGiam, GiaGoc, Mota, DanhMuc, SlNguoimua, Hinh);

                                // Thêm voucher vào danh sách sử dụng chiến lược
                                iVoucherStrategy.addToVouchersList(voucher, vouchersDuLich);
                            }

                            // Cập nhật adapter sau khi đã thêm vào danh sách
                            voucherDuLichAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void setOnInfoClick(Voucher voucher) {
        Intent intent = new Intent(DanhMucDuDichActivity.this, ThongTinVoucherActivity.class);
        intent.putExtra("ThongTinVoucher",voucher);
        startActivity(intent);
    }
}