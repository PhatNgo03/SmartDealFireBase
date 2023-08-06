package com.example.smartdealfirebase.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartdealfirebase.Adapter.CartAdapter;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    private TextView tvTenNguoiDung;
    private TextView tvSDT;
    private TextView GhiChu;
    private TextView tvDiaChi;
    private TextView tvDiscountPrice;
    private TextView tvPrice;
    private TextView tvQuantity;
    private TextView tvTotal;
    private Button btThanhToan;
    private ItemCart itemCart;

    private CartAdapter cartAdapter;
    private FirebaseFirestore db;

    // Thêm phương thức newInstance để truyền thông tin hóa đơn vào fragment
    public static OrderFragment newInstance(ItemCart itemCart) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemCart", itemCart);
        orderFragment.setArguments(bundle);
        return orderFragment;
    }

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
        tvPrice = view.findViewById(R.id.tvGiaGocOrder);
        tvDiscountPrice = view.findViewById(R.id.tvGiaGiamOrder);
        tvTotal = view.findViewById(R.id.tvTongTienOrder);

        // Lấy thông tin hóa đơn từ bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            ItemCart itemCart = (ItemCart) bundle.getSerializable("itemCart");
            if (itemCart != null) {
                // Hiển thị thông tin hóa đơn
                tvDiscountPrice.setText(String.valueOf(itemCart.getDiscountPrice()));
                tvPrice.setText(String.valueOf(itemCart.getPrice()));
                int discount = itemCart.getDiscountPrice();
                int quantity = itemCart.getQuantity();
                int total = discount * quantity;
                tvTotal.setText(String.valueOf(total));
//                tvTotal.setText(String.valueOf(itemCart.getQuantity()));
            }
        }
        btThanhToan = view.findViewById(R.id.btThanhToan);
        btThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addOrderToFirestore(itemCart);
//                        if(itemCart != null){
//                            showOrderDialog(itemCart);
//
//                        }else {
//                            Toast.makeText(requireContext(), "Không tìm thấy thông tin đơn hàng.", Toast.LENGTH_SHORT).show();
//                        }
                Toast.makeText(requireContext(), "Đơn hàng đã được thanh toán", Toast.LENGTH_SHORT).show();
                    }
        });

    }
private void addOrderToFirestore(ItemCart itemCart) {
    if (itemCart == null) {
        // Xử lý nếu itemCart là null, ví dụ: hiển thị thông báo hoặc xử lý sai lầm
        Toast.makeText(requireContext(), "Không tìm thấy thông tin đơn hàng.", Toast.LENGTH_SHORT).show();
        return;
    }
    db = FirebaseFirestore.getInstance();
    // Tạo một Map chứa thông tin đặt hàng
    Map<String, Object> order = new HashMap<>();
    order.put("voucherName", itemCart.getVoucherName());
    order.put("discountPrice", itemCart.getDiscountPrice());
    order.put("price", itemCart.getPrice());
    order.put("quantity", itemCart.getQuantity());
    order.put("ToTal", itemCart.getToTal());
    order.put("userAddress", itemCart.getUserId());

    // Lưu thông tin đặt hàng vào Firestore
    db.collection("orders").add(order)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // Xử lý khi lưu thông tin đặt hàng thành công
                    Toast.makeText(requireContext(), "Đơn hàng đang được xử lý!", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Xử lý khi có lỗi xảy ra khi lưu thông tin đặt hàng
                    Toast.makeText(requireContext(), "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
}
    private void showOrderDialog(ItemCart itemCart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Đặt hàng");
        builder.setMessage("Bạn muốn đặt hàng sản phẩm này?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addOrderToFirestore(itemCart);
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}