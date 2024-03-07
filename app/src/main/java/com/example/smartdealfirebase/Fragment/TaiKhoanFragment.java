package com.example.smartdealfirebase.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartdealfirebase.DangNhapActivity;
import com.example.smartdealfirebase.DesignPatternSingleton.FireBaseFireStoreSingleton;
import com.example.smartdealfirebase.LichSuMuaHang;
import com.example.smartdealfirebase.R;
import com.example.smartdealfirebase.ThongTinCaNhanActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaiKhoanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaiKhoanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaiKhoanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaiKhoanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaiKhoanFragment newInstance(String param1, String param2) {
        TaiKhoanFragment fragment = new TaiKhoanFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tai_khoan, container, false);
    }



    TextView tvten,tvemail,tvdangxuat,tvttcn,tvLsMuaHang;

    FireBaseFireStoreSingleton fireBaseFireStoreSingleton = FireBaseFireStoreSingleton.getInstance();
    FirebaseFirestore db = fireBaseFireStoreSingleton.getFirestore();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvttcn=view.findViewById(R.id.tvinfo);
        tvLsMuaHang = view.findViewById(R.id.tvLicSuMuaHang);
        tvten=view.findViewById(R.id.tvten);
        tvemail=view.findViewById(R.id.tvemail);
        firebaseAuth=FirebaseAuth.getInstance();
        tvdangxuat=view.findViewById(R.id.btndangxuat);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userUid = firebaseUser.getUid();

            // Tạo reference đến tài liệu Firestore "NguoiDung" tương ứng với user UID
            DocumentReference nguoiDungDocRef = db.collection("NguoiDung").document(userUid);

            nguoiDungDocRef.get().addOnSuccessListener(nguoiDungDocumentSnapshot -> {
                if (nguoiDungDocumentSnapshot.exists()) {
                    String email = nguoiDungDocumentSnapshot.getString("Email");

                    // Tạo reference đến tài liệu Firestore "KhachHang" tương ứng với user email
                    DocumentReference khachHangDocRef = db.collection("KhachHang").document(email);

                    khachHangDocRef.get().addOnSuccessListener(khachHangDocumentSnapshot -> {
                        if (khachHangDocumentSnapshot.exists()) {
                            String hoVaTen = khachHangDocumentSnapshot.getString("HoTen");
                            tvten.setText(hoVaTen);
                        } else {
                            tvten.setText("");
                        }
                    }).addOnFailureListener(e -> {
                        tvten.setText("Bạn chưa chỉnh sửa TTCN");
                    });

                    // Set the email to the email TextView
                    tvemail.setText(email);
                } else {
                    tvemail.setText("Người Dùng");
                    tvten.setText("Nguời dùng");
                }
            }).addOnFailureListener(e -> {
                tvemail.setText("Lỗi khi lấy dữ liệu từ Firestore");
                tvten.setText("Lỗi khi lấy dữ liệu từ Firestore");
            });
        } else {
            // Người dùng chưa đăng nhập, xử lý thông báo tương ứng
            tvemail.setText("Chưa đăng nhập");
            tvten.setText("Chưa đăng nhập");
        }

        tvdangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                signOutUser();
            }
        });

        tvttcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ThongTinCaNhanActivity.class));
            }
        });

        tvLsMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), LichSuMuaHang.class));
            }
        });
        if(tvemail.getText().toString().equals("Chưa đăng nhập")&&tvten.getText().toString().equals("Chưa đăng nhập")){
            tvdangxuat.setText("Đăng nhập");
            tvttcn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(),DangNhapActivity.class));
                }
            });
        }
    }
    private void signOutUser() {
        Intent intent=new Intent(getContext(),DangNhapActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}