package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.R;
import com.google.android.gms.dynamic.OnDelegateCreatedListener;

import java.util.List;

public class SelectedVoucherAdapter extends RecyclerView.Adapter<SelectedVoucherAdapter.OrderVH>{
    private Context context;
    private List<ItemCart> selectedItems;

    public SelectedVoucherAdapter(Context context, List<ItemCart> selectedItems) {
        this.context = context;
        this.selectedItems = selectedItems;
    }
    private IOnDeleteClickListener  onDeleteClickListener;

    public void setOnDeleteClickListener(IOnDeleteClickListener  listener)
    {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public OrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_orderfagment, parent, false);
        return new OrderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderVH holder, @SuppressLint("RecyclerView") int position) {
        ItemCart item = selectedItems.get(position);
        holder.tvVoucherName.setText(item.getVoucherName());
        holder.tvDiscountPriceOrder.setText(String.valueOf(item.getDiscountPrice()));
        holder.tvPriceOrder.setText(String.valueOf(item.getPrice()));
        holder.tvQuantityOrder.setText(String.valueOf(item.getQuantity()));
        holder.tvTotalOrder.setText(String.valueOf(item.getToTal()));
        Glide.with(holder.itemView.getContext()).load(item.getHinhAnh()).into(holder.imgVoucher);

        holder.tvDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedItems.size();
    }

    public class OrderVH extends RecyclerView.ViewHolder {
        ImageView imgVoucher;
        TextView tvVoucherName, tvPriceOrder, tvDiscountPriceOrder, tvQuantityOrder,  tvDeleteOrder, tvTotalOrder;


        public OrderVH(@NonNull View itemView) {
            super(itemView);
            imgVoucher = itemView.findViewById(R.id.imgVoucherOrder);
            tvVoucherName = itemView.findViewById(R.id.tvVoucherNameOder);
            tvDiscountPriceOrder = itemView.findViewById(R.id.tvDiscountPriceOrder);
            tvPriceOrder= itemView.findViewById(R.id.tvPriceOrder);
            tvQuantityOrder = itemView.findViewById(R.id.tvQuantityOrder);
            tvTotalOrder = itemView.findViewById(R.id.tvToTalOrder);
            tvDeleteOrder = itemView.findViewById(R.id.tvDeleteOrder);

        }
    }
    public interface IOnDeleteClickListener{
        void onDeleteClickListener(int position);
    }
}
