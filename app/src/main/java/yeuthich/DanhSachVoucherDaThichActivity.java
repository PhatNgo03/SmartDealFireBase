package yeuthich;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import yeuthich.VocherDaThichAdapter;

public class DanhSachVoucherDaThichActivity extends AppCompatActivity {
    ArrayList<Voucher> vouchers;
    RecyclerView rvDanhSachYeuThich;
    FirebaseFirestore db;
    VocherDaThichAdapter voucherDaThichAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_voucher_da_thich);
        rvDanhSachYeuThich = findViewById(R.id.rvDanhSachChuyenBayDaThich);
        vouchers = new ArrayList<>();
        VoucherDaThich();
        voucherDaThichAdapter = new VocherDaThichAdapter(this,vouchers);
        rvDanhSachYeuThich.setAdapter(voucherDaThichAdapter);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
        rvDanhSachYeuThich.setLayoutManager(layoutManager);
    }
    private void VoucherDaThich() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            db = FirebaseFirestore.getInstance();
            db.collection("favorites").whereEqualTo("UserId", currentUserId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

//                        String idVoucher=document.getId();
//                        String id = document.get("voucherId").toString();
                        String MaVoucher=document.get("MaVoucher").toString();
                        String TenVoucher=document.get("TenVoucher").toString();
                        Integer GiaGiam= Integer.parseInt(document.get("GiaGiam").toString());
                        Integer GiaGoc= Integer.parseInt(document.get("GiaGoc").toString());
                        Integer SlNguoimua=Integer.parseInt(document.get("SLNguoiMua").toString());
                        String Mota=document.get("MoTa").toString();
                        String DanhMuc=document.get("DanhMuc").toString();
                        String Hinh=(String)document.get("HinhAnh");
                        Boolean isYeuThich = (Boolean) document.get("isYeuThich");
                        Voucher voucher = new Voucher(document.getId(),MaVoucher,TenVoucher,GiaGiam,GiaGoc,Mota,DanhMuc,SlNguoimua,Hinh,isYeuThich);

                        vouchers.add(voucher);
                    }
                    voucherDaThichAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}