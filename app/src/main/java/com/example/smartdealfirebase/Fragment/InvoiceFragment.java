package com.example.smartdealfirebase.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoiceFragment extends Fragment {
    private TextView tvVoucherName;
    private TextView tvDiscountPrice;
    private TextView tvPrice;
    private TextView tvQuantity;

    // Thêm phương thức newInstance để truyền thông tin hóa đơn vào fragment
    public static InvoiceFragment newInstance(ItemCart itemCart) {
        InvoiceFragment invoiceFragment = new InvoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemCart", itemCart);
        invoiceFragment.setArguments(bundle);
        return invoiceFragment;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoiceFragment newInstance(String param1, String param2) {
        InvoiceFragment fragment = new InvoiceFragment();
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
        return inflater.inflate(R.layout.fragment_invoice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvVoucherName = view.findViewById(R.id.tvVoucherName);
        tvDiscountPrice = view.findViewById(R.id.tvDiscountPrice);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvQuantity = view.findViewById(R.id.tvQuantity);

        // Lấy thông tin hóa đơn từ bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            ItemCart itemCart = (ItemCart) bundle.getSerializable("itemCart");
            if (itemCart != null) {
                // Hiển thị thông tin hóa đơn
                tvVoucherName.setText(itemCart.getVoucherName());
                tvDiscountPrice.setText(String.valueOf(itemCart.getDiscountPrice()));
                tvPrice.setText(String.valueOf(itemCart.getPrice()));
                tvQuantity.setText(String.valueOf(itemCart.getQuantity()));
            }
        }
    }
}