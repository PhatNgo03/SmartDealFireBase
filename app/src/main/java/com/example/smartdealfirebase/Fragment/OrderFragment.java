package com.example.smartdealfirebase.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Adapter.CartAdapter;
import com.example.smartdealfirebase.Adapter.SelectedVoucherAdapter;
import com.example.smartdealfirebase.Adapter.VoucherAdapter;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.PaymentOptionsActivity;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    private List<ItemCart> selectedItems = new ArrayList<>();
    private SelectedVoucherAdapter selectedVoucherAdapter;


    public static OrderFragment newInstance(List<ItemCart> selectedItems) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedItems", (Serializable) selectedItems);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

    private FirebaseFirestore db;


    // Thêm phương thức newInstance để truyền thông tin hóa đơn vào fragment
//    public static OrderFragment newInstance(ItemCart itemCart) {
//        OrderFragment orderFragment = new OrderFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("itemCart", itemCart);
//        orderFragment.setArguments(bundle);
//        return orderFragment;
//    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.rvVoucherOrder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Kiểm tra xem danh sách các mục đã chọn có được truyền từ CartFragment hay không
        if (getArguments() != null && getArguments().containsKey("selectedItems")) {
            selectedItems = (List<ItemCart>) getArguments().getSerializable("selectedItems");
        }

        // Khởi tạo adapter và thiết lập dữ liệu cho RecyclerView
        selectedVoucherAdapter = new SelectedVoucherAdapter(getContext(), selectedItems);
        recyclerView.setAdapter(selectedVoucherAdapter);
        selectedVoucherAdapter.setOnDeleteClickListener(new SelectedVoucherAdapter.IOnDeleteClickListener() {
            @Override
            public void onDeleteClickListener(int position) {
                selectedItems.remove(position);
                selectedVoucherAdapter.notifyDataSetChanged();
            }
        });
        Button btnCheckOutOrder = view.findViewById(R.id.btnCheckout);
        btnCheckOutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo Intent để chuyển từ OrderFragment sang PaymentOptionsActivity
                Intent intent = new Intent(getContext(), PaymentOptionsActivity.class);

                // Đính kèm danh sách các mục đã chọn với Intent (nếu cần)
                intent.putExtra("selectedItems", (Serializable) selectedItems);

                // Chuyển sang trang PaymentOptionsActivity
                startActivity(intent);
            }
        });

    }



}