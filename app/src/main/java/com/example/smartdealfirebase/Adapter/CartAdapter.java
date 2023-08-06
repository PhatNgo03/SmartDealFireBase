package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.GioHangViewHolder> {
    ImageView imgAnhSP;
    TextView tvTenSp,tvGiaGiam,tvGiaGoc,tvSoLuong;
    ImageButton btTang, btGiam, btXoa;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    private List<ItemCart> itemCarts;

//    private List<String> imageUrls;
//    public void setImageUrls(List<String> imageUrls) {
//        this.imageUrls = imageUrls;
//    }
//    Voucher voucher;
    private OnItemClickListener listener;

    public interface OnItemLongClickListener {
        void onItemLongClick(ItemCart itemCart);
    }
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public CartAdapter(List<ItemCart> cartItems) {
        this.itemCarts = cartItems;
    }
    public void addItem(ItemCart itemCart){
        itemCarts.add(itemCart);
    }
   public static Voucher voucher;

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemCart itemCart = itemCarts.get(position);
        holder.tvTenSanPham.setText(itemCart.getVoucherName());
        holder.tvGiaGiam.setText(String.valueOf(itemCart.getDiscountPrice()));
        holder.tvGiaGoc.setText(String.valueOf(itemCart.getPrice()));
        holder.tvSoLuong.setText(String.valueOf(itemCart.getQuantity()));

        StorageReference storageRef = storage.getReference();
        int targerWidth = 119;
        int targetHeight = 70;
        StorageReference imamgeRef = storageRef.child(String.valueOf(itemCart.getHinhAnh()));
        imamgeRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Bitmap resizeBitMap = Bitmap.createScaledBitmap(bitmap,targerWidth,targetHeight,false);
                holder.imgAnhSP.setImageBitmap(resizeBitMap);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(itemCarts.get(position));
                }
                return true;
            }
        });

//
//        Voucher voucher = null;
//        tvTenSp.setText(voucher.getVoucherName());
//        tvGiaGoc.setText(String.valueOf(voucher.getPrice()) + "đ");
//        tvGiaGiam.setText(String.valueOf(voucher.getDiscountPrice()) + "đ");
//        tvSoLuong.setText(String.valueOf(itemCart.getQuantity()));
//        StorageReference storageRef = storage.getReference();
//        int targerWidth = 186;
//        int targetHeight = 114;
//        StorageReference imamgeRef = storageRef.child(String.valueOf(voucher.getHinhvc()));
//        imamgeRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                Bitmap resizeBitMap = Bitmap.createScaledBitmap(bitmap,targerWidth,targetHeight,false);
//                holder.imgAnhSP.setImageBitmap(resizeBitMap);
//            }
//        });
        holder.btTangSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = itemCart.getQuantity();
                itemCart.setQuantity(currentQuantity + 1);
                notifyItemChanged(position);
                updateTotalPrice();

            }
        });
        holder.btGiamSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = itemCart.getQuantity();
                if (currentQuantity > 1) {
                    itemCart.setQuantity(currentQuantity - 1);
                    updateTotalPrice();
                    notifyItemChanged(position);

                }
            }
        });
        holder.tvXoaCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức onDeleteCartItemClicked khi người dùng nhấn nút xóa
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemCarts.size();
    }

    public void removeItem(int position){
        if(position >= 0 && position < itemCarts.size()){
            itemCarts.remove(position);
            notifyDataSetChanged();
        }
    }
    //update tổng tiền
    private void updateTotalPrice() {
        int totalPrice = 0;
        for (ItemCart item : itemCarts) {
            totalPrice += (item.getDiscountPrice() * item.getQuantity());
        }
        // Notify the Fragment to update the total price
        if (listener != null) {
            listener.onTotalPriceUpdated(totalPrice);
        }
    }
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setItems(List<ItemCart> itemCarts) {
        this.itemCarts = itemCarts;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onTotalPriceUpdated(int totalPrice);
    }
    public List<ItemCart> getCartItems() {
        return itemCarts;
    }

    public void setCartItems(List<ItemCart> cartItems) {
        this.itemCarts = cartItems;
//        notifyDataSetChanged();
    }

    public class GioHangViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAnhSP;
        TextView tvTenSanPham, tvGiaGiam, tvGiaGoc, tvSoLuong,tvXoaCart;
        ImageButton btTangSL, btGiamSL;

        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhSP = itemView.findViewById(R.id.imgVoucherCart);
            tvTenSanPham = itemView.findViewById(R.id.tvtenvoucherCart);
            tvGiaGiam = itemView.findViewById(R.id.tvgiagiamCart);
            tvGiaGoc= itemView.findViewById(R.id.tvgiagocCart);
            tvSoLuong = itemView.findViewById(R.id.tvslCart);
            btTangSL = itemView.findViewById(R.id.btnTangSoLuong);
            btGiamSL = itemView.findViewById(R.id.btnGiamSoLuong);
            tvXoaCart = itemView.findViewById(R.id.btnxoavoucherCart);

        }
    }
}
