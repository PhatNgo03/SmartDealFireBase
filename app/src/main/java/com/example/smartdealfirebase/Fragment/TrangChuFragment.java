package com.example.smartdealfirebase.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.smartdealfirebase.Adapter.VoucherAdapter;
import com.example.smartdealfirebase.DanhMucAmThucActivity;
import com.example.smartdealfirebase.DanhMucDuDichActivity;
import com.example.smartdealfirebase.DanhMucSPaActivity;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.example.smartdealfirebase.ThongTinVoucherActivity;
import com.example.smartdealfirebase.TimKiemActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrangChuFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class TrangChuFragment extends Fragment implements VoucherAdapter.Listener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrangChuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrangChuFragment newInstance(String param1, String param2) {
        TrangChuFragment fragment = new TrangChuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TrangChuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }


    //----------------

    TextView searchbt,tvDMAT,tvDanhMucDuLich,tvDanhMucSpa;
    private RecyclerView rvVoucherMain;
    FirebaseFirestore db;
    private RecyclerView rvVoucherdl;


    private VoucherAdapter voucherAdapter,voucherAdapterdl;
    private ArrayList<Voucher> vouchers,vouchersdl;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        searchbt=view.findViewById(R.id.edtserchTrangChu);
        tvDMAT=view.findViewById(R.id.tvDMAmThuc);
        tvDanhMucDuLich = view.findViewById(R.id.tvDMDuLich);
        tvDanhMucSpa = view.findViewById(R.id.tvDanhMucSpa);

        slideModels.add(new SlideModel(R.drawable.anh1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.anh2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.anh3, ScaleTypes.FIT));


        tvDMAT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DanhMucAmThucActivity.class);
                startActivity(intent);
            }
        });
        tvDanhMucDuLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DanhMucDuDichActivity.class);
                startActivity(intent);
            }
        });
        tvDanhMucSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DanhMucSPaActivity.class);
                startActivity(intent);
            }
        });
        searchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TimKiemActivity.class);
                startActivity(intent);
            }
        });
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
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
                                vouchersdl.add(voucher);
                            } else if (DanhMuc.equalsIgnoreCase("AmThuc")) {
//                                vouchersdl.clear();
                                vouchers.add(voucher);
                            }

                        }
                        voucherAdapter.notifyDataSetChanged();
                        voucherAdapterdl.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        rvVoucherdl=view.findViewById(R.id.rvVoucherdl);


        vouchersdl=new ArrayList<>();
        rvVoucherdl = view.findViewById(R.id.rvVoucherdl);
        LinearLayoutManager G = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rvVoucherdl.setLayoutManager(G);
        voucherAdapterdl=new VoucherAdapter(vouchersdl,this);
        rvVoucherdl.setAdapter(voucherAdapterdl);



        vouchers = new ArrayList<>();
        rvVoucherMain =view.findViewById(R.id.rvVoucherMain);
        LinearLayoutManager l = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        rvVoucherMain.setLayoutManager(l);
        voucherAdapter = new VoucherAdapter((List<Voucher>) vouchers,this);
        rvVoucherMain.setAdapter(voucherAdapter);

        //khoi tao va mo d

        }

    @Override
    public void setOnInfoClick(Voucher voucher) {
        Intent intent = new Intent(getContext(), ThongTinVoucherActivity.class);
        intent.putExtra("ThongTinVoucher",voucher);
       // Bundle b = new Bundle();
//        b.putString("imgcurrent", voucher.getHinhvc());

        startActivity(intent);
    }
}
