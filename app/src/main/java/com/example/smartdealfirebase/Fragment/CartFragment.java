package com.example.smartdealfirebase.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartdealfirebase.Adapter.CartAdapter;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CartFragment extends Fragment {
    private RecyclerView rvGioHang;
    private CartAdapter cartAdapter;
    private List<ItemCart> itemCarts;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    public interface OnOrderButtonClickListener {
        void onOrderButtonClick(ItemCart itemCart);
    }
    // Biến listener để lưu đối tượng nghe sự kiện từ Activity
    private OnOrderButtonClickListener listener;

    public void setOnOrderButtonClickListener(OnOrderButtonClickListener listener) {
        this.listener = listener;
    }
    ItemCart itemCart;
    public interface OnOrderInfoEnteredListener {
        void onOrderInfoEntered();
    }

    private OnOrderInfoEnteredListener orderInfoEnteredListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemCarts = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ImageView imageView = view.findViewById(R.id.backcart);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        rvGioHang = view.findViewById(R.id.rvGioHang);
        rvGioHang.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartAdapter = new CartAdapter(itemCarts);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("itemCart")) {
            ItemCart itemCart = (ItemCart) bundle.getSerializable("itemCart");
            if (itemCart != null) {
                // Thêm sản phẩm vào RecyclerView
                addItemToCart(itemCart);
            }
        }

        Button btnNhapThongTinGiaoHang = view.findViewById(R.id.btGiaoHang);
        btnNhapThongTinGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // Gọi đến interface để chuyển sang OrderFragment
//                if (listener != null) {
//                    // Thực hiện hành động khi nút đặt hàng được click
//                    listener.onOrderButtonClick(itemCart); // Truyền dữ liệu itemCart nếu cần
//                navigateToOrderFragment();
              navigateToOrderFragment(itemCart);
//                }
            }
        });


        cartAdapter.setListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onDeleteCartItemClicked(position);
            }

            @Override
            public void onTotalPriceUpdated(int totalPrice) {
                TextView tvTongtien = view.findViewById(R.id.tvTongTien);
                tvTongtien.setText("Tổng tiền: " + totalPrice + "đ");
            }
        });
        cartAdapter.setOnItemLongClickListener(new CartAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(ItemCart itemCart) {
                // Hiển thị dialog đặt hàng với dữ liệu của itemCart
                showOrderDialog(itemCart);
//                navigateToOrderFragment();
                navigateToOrderFragment(itemCart);
                cartAdapter.notifyDataSetChanged();

            }
        });
        rvGioHang.setAdapter(cartAdapter);

        loadCartItemsFromFirestore();


    }


//    private void navigateToInvoiceFragment(ItemCart itemCart) {
//        InvoiceFragment invoiceFragment = InvoiceFragment.newInstance(itemCart);
//        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.FrameOrder, invoiceFragment); // Thay "R.id.FrameOrder" bằng ID của FrameLayout hiện tại
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
    private void navigateToInvoiceFragmentTest(ItemCart itemCart) {
        OrderFragment orderFragment = OrderFragment.newInstance(itemCart);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameOrder, orderFragment); // Thay "R.id.FrameOrder" bằng ID của FrameLayout hiện tại
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void showOrderDialog(ItemCart itemCart) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Đặt hàng");
        builder.setMessage("Bạn muốn đặt hàng sản phẩm này?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                navigateToInvoiceFragmentTest(itemCart);
                cartAdapter.notifyDataSetChanged();
//                addOrderToFirestore(itemCart);
                getUserInfoFromFirestore(itemCart);
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void getUserInfoFromFirestore(ItemCart itemCart) {
        String userId = firebaseUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("NguoiDung").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String Email = documentSnapshot.getString("Email");

                    // Thêm trường "Email" vào đối tượng order
                    itemCart.setUserId(Email);
                    addOrderToFirestore(itemCart);
                } else {
                    // Xử lý nếu không tìm thấy thông tin người dùng
                    Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý nếu có lỗi khi lấy thông tin người dùng từ Firestore
                Toast.makeText(requireContext(), "Đã xảy ra lỗi khi lấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addOrderToFirestore(ItemCart itemCart) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một Map chứa thông tin đặt hàng
        Map<String, Object> order = new HashMap<>();
        order.put("voucherName", itemCart.getVoucherName());
        order.put("discountPrice", itemCart.getDiscountPrice());
        order.put("price", itemCart.getPrice());
        order.put("quantity", itemCart.getQuantity());
        order.put("ToTal",itemCart.getToTal());
        order.put("NgayMua",getCurrentDate());
        order.put("Email", itemCart.getUserId());

        // Lưu thông tin đặt hàng vào Firestore
        db.collection("orders").add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        // Xử lý khi lưu thông tin đặt hàng thành công
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

    public void onDeleteCartItemClicked(int position) {
        // Gọi hàm deleteCartItem và truyền vào vị trí của item cần xóa
        deleteCartItem(position);
    }
    public void deleteCartItem(int position) {
        // Xác định item cần xóa trong danh sách itemCarts
        ItemCart itemToDelete = itemCarts.get(position);
        String itemIDToDelete = itemToDelete.getItemId(); // Lấy ID của item trong Firestore

        // Lấy tham chiếu đến tài liệu cần xóa trong Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itemRef = db.collection("cartItems").document(itemIDToDelete);

        // Thêm tài liệu vào Batched write và xóa nó từ Firestore
        WriteBatch batch = db.batch();
        batch.delete(itemRef);

        // Thực hiện Batched write
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xóa thành công
                        // Cập nhật lại RecyclerView nếu cần thiết
                        itemCarts.remove(position);
                        cartAdapter.notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi xóa thất bại
                    }
                });
    }
//    private void loadCartItemsFromFirestore() {
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            CollectionReference cartItemsRef = db.collection("cartItems");
//
//            // Lấy dữ liệu từ Firestore
//            cartItemsRef.get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                // Xử lý dữ liệu khi lấy thành công
//                                itemCarts.clear();
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    String itemID = document.getId(); // Lấy ID của item trong Firestore
//                                    String voucherName = document.getString("voucherName");
//                                    Long discountPriceLong = document.getLong("discountPrice");
//                                    Long priceLong = document.getLong("price");
//                                    Long quantityLong = document.getLong("quantity");
//                                    Long ToTal = document.getLong("ToTal");
//                                    String HinhAnh = document.getString("HinhAnh");
//                                    String UserID = document.getString("UserId");
//
//                                    // Kiểm tra xem các trường discountPrice, price, quantity có tồn tại và không phải là null
//                                    int discountPrice = (discountPriceLong != null) ? discountPriceLong.intValue() : 0;
//                                    int price = (priceLong != null) ? priceLong.intValue() : 0;
//                                    int quantity = (quantityLong != null) ? quantityLong.intValue() : 0;
//                                    int total = (ToTal != null) ? ToTal.intValue() : 0;
//                                    ItemCart itemCart = new ItemCart(itemID,voucherName,discountPrice,price,quantity,UserID,total,HinhAnh,"");
////                                    ItemCart itemCart = new ItemCart(itemID, voucherName, discountPrice, price, quantity,total,HinhAnh,"",""); // Truyền ID vào ItemCart
//                                    itemCarts.add(itemCart);
//                                }
//                                cartAdapter.notifyDataSetChanged();
//                            } else {
//                            }
//                        }
//                    });
//        }
private void loadCartItemsFromFirestore() {
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser == null) {
        // Nếu người dùng chưa đăng nhập, không có dữ liệu để hiển thị
        return;
    }
    String userid = firebaseUser.getUid();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference cartItemsRef = db.collection("cartItems");

    cartItemsRef.whereEqualTo("UserId", userid)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        itemCarts.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String itemID = document.getId(); // Lấy ID của item trong Firestore
                                    String voucherName = document.getString("voucherName");
                                    Long discountPriceLong = document.getLong("discountPrice");
                                    Long priceLong = document.getLong("price");
                                    Long quantityLong = document.getLong("quantity");
                                    Long ToTal = document.getLong("ToTal");
                                    String HinhAnh = document.getString("HinhAnh");
                                    String UserID = document.getString("UserId");

                                    // Kiểm tra xem các trường discountPrice, price, quantity có tồn tại và không phải là null
                                    int discountPrice = (discountPriceLong != null) ? discountPriceLong.intValue() : 0;
                                    int price = (priceLong != null) ? priceLong.intValue() : 0;
                                    int quantity = (quantityLong != null) ? quantityLong.intValue() : 0;
                                    int total = (ToTal != null) ? ToTal.intValue() : 0;
                                    ItemCart itemCart = new ItemCart(itemID,voucherName,discountPrice,price,quantity,UserID,total,HinhAnh,"");
//                                    ItemCart itemCart = new ItemCart(itemID, voucherName, discountPrice, price, quantity,total,HinhAnh,"",""); // Truyền ID vào ItemCart
                                    itemCarts.add(itemCart);
                        }
                        cartAdapter.notifyDataSetChanged();
                    } else {
                        // Xử lý khi không lấy được dữ liệu
                    }
                }
            });
}
    public void addItemToCart(ItemCart item) {
        if (itemCarts != null) {
            itemCarts.add(item);
            cartAdapter.notifyDataSetChanged();
        }
    }
    private void navigateToOrderFragment(ItemCart itemCart) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemCart", itemCart);
        orderFragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frContent, orderFragment);
        transaction.addToBackStack("TrangChuFragment"); // Để có thể quay lại fragment trước đó bằng nút back
        transaction.commit();
    }
}