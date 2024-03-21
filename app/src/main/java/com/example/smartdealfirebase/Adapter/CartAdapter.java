package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    private List<ItemCart> selectedItems = new ArrayList<>();
    private List<ItemCart> itemCarts;

    private OnSelectedItemsChangedListener onSelectedItemsChangedListener;
    public interface OnSelectedItemsChangedListener {
        void onSelectedItemsChanged(List<ItemCart> selectedItems);
    }
    public void setOnSelectedItemsChangedListener(OnSelectedItemsChangedListener listener) {
        this.onSelectedItemsChangedListener = listener;
    }


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
        holder.tvVoucherName.setText(itemCart.getVoucherName());
        holder.tvDiscountPriceCart.setText(String.valueOf(itemCart.getDiscountPrice()));
        holder.tvPriceCart.setText(String.valueOf(itemCart.getPrice()));
        holder.tvQuantityCart.setText(String.valueOf(itemCart.getQuantity()));

        Glide.with(holder.itemView.getContext()).load(itemCart.getHinhAnh()).into(holder.imgVoucher);

        holder.cbCartItem.setChecked(selectedItems.contains(itemCart));
        holder.cbCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật trạng thái checked của mục
                itemCart.setChecked(holder.cbCartItem.isChecked());

                // Thông báo cho Fragment rằng trạng thái đã thay đổi
                if (onSelectedItemsChangedListener != null) {
                    onSelectedItemsChangedListener.onSelectedItemsChanged(selectedItems);
                }
            }
        });
//        holder.cbCartItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // Nếu checkbox được chọn, thêm item vào danh sách các voucher được chọn
//                    selectedItems.add(itemCart);
//                } else {
//                    // Nếu checkbox không được chọn, loại bỏ item khỏi danh sách các voucher được chọn
//                    selectedItems.remove(itemCart);
//                }
//
//                // Thông báo cho listener về sự thay đổi của danh sách các voucher được chọn
//                if (onSelectedItemsChangedListener != null) {
//                    onSelectedItemsChangedListener.onSelectedItemsChanged(selectedItems);
//                }
//            }
//        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(itemCarts.get(position));
                }
                return true;
            }
        });

        holder.btnIncreaseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = itemCart.getQuantity();
                itemCart.setQuantity(currentQuantity + 1);
                notifyItemChanged(position);
                updateTotalPrice();

            }
        });
        holder.btnDecreaseCart.setOnClickListener(new View.OnClickListener() {
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
        holder.tvDeleteCart.setOnClickListener(new View.OnClickListener() {
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
        ImageView imgVoucher;
        TextView tvVoucherName, tvPriceCart, tvDiscountPriceCart, tvQuantityCart,  tvDeleteCart;
        ImageButton btnIncreaseCart, btnDecreaseCart;
        CheckBox cbCartItem;

        public GioHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVoucher = itemView.findViewById(R.id.imgVoucherCart);
            tvVoucherName = itemView.findViewById(R.id.tvtenvoucherCart);
            tvDiscountPriceCart = itemView.findViewById(R.id.tvDiscountPriceCart);
            tvPriceCart= itemView.findViewById(R.id.tvPriceCart);
            tvQuantityCart = itemView.findViewById(R.id.tvQuantityCart);
            btnIncreaseCart = itemView.findViewById(R.id.btnIncreaseCart);
            btnDecreaseCart = itemView.findViewById(R.id.btnDecreaseCart);
            tvDeleteCart = itemView.findViewById(R.id.btnDeleteCart);
            cbCartItem = itemView.findViewById(R.id.cbCart);
        }
    }
}
