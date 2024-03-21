package com.example.smartdealfirebase.Prototype;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.smartdealfirebase.Adapter.NCCAdapter;
import com.example.smartdealfirebase.AddVoucherActivity;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DanhMucVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DanhMucVoucherFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DanhMucVoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DanhMucVoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DanhMucVoucherFragment newInstance(String param1, String param2) {
        DanhMucVoucherFragment fragment = new DanhMucVoucherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ArrayList<VoucherPrototype> vouchersDanhMuc;
    RecyclerView rvDanhMucVoucher;
    NCCAdapter nccAdapter;
    ImageButton btVoucher;
    FirebaseFirestore db;

    VoucherPrototype voucherPrototype; // Đối tượng "prototype"
    VoucherFactory voucherFactory; // Factory để tạo voucher


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_danh_muc_voucher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo đối tượng "prototype"
        voucherPrototype = new VoucherPrototype("MaVoucher", "TenVoucher", 0, 0, 0, "MoTa", "DanhMuc", "HinhAnh");

        // Khởi tạo Factory với "prototype"
        voucherFactory = new VoucherFactory(voucherPrototype);

        rvDanhMucVoucher =view.findViewById(R.id.rvDanhMucVoucher);
        btVoucher = view.findViewById(R.id.btThemVoucher);
        vouchersDanhMuc = new ArrayList<>();
        nccAdapter =new NCCAdapter(getContext(),vouchersDanhMuc);
        rvDanhMucVoucher.setAdapter(nccAdapter);
        rvDanhMucVoucher.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        btVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddVoucherActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        vouchersDanhMuc.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("Voucher").orderBy("MaVoucher")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Sử dụng factory để tạo mới voucher từ prototype
                            VoucherPrototype newVoucher = voucherFactory.createVoucher();
                            newVoucher.setMaVoucher(document.get("MaVoucher").toString());
                            newVoucher.setTenVoucher(document.get("TenVoucher").toString());
                            newVoucher.setGiaGiam(Integer.parseInt(document.get("GiaGiam").toString()));
                            newVoucher.setGiaGoc(Integer.parseInt(document.get("GiaGoc").toString()));
                            newVoucher.setSlNguoiMua(Integer.parseInt(document.get("SLNguoiMua").toString()));
                            newVoucher.setMoTa(document.get("MoTa").toString());
                            newVoucher.setDanhMuc(document.get("DanhMuc").toString());
                            newVoucher.setHinhAnh(document.get("HinhAnh").toString());
                            vouchersDanhMuc.add(newVoucher);

//                            String MaVoucher = document.get("MaVoucher").toString();
//                            String TenVoucher = document.get("TenVoucher").toString();
//                            Integer GiaGiam = Integer.parseInt(document.get("GiaGiam").toString());
//                            Integer GiaGoc = Integer.parseInt(document.get("GiaGoc").toString());
//                            Integer SlNguoimua = Integer.parseInt(document.get("SLNguoiMua").toString());
//                            String Mota = document.get("MoTa").toString();
//                            String DanhMuc = document.get("DanhMuc").toString();
//                            String Hinh = document.get("HinhAnh").toString();
//
//                                Voucher voucher = new Voucher(MaVoucher, TenVoucher, GiaGiam, GiaGoc, Mota, DanhMuc, SlNguoimua, Hinh);

                        }
                        nccAdapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}