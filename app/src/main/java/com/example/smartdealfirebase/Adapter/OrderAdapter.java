package com.example.smartdealfirebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartdealfirebase.Model.ItemCart;
import com.example.smartdealfirebase.Model.TestLS;
import com.example.smartdealfirebase.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private List<ItemCart> itemCarts;
    public OrderAdapter(Context context,List<ItemCart> itemCarts) {
        this.context = context;
        if (itemCarts == null) {
            this.itemCarts = new ArrayList<>(); // Tạo danh sách mới nếu testLSMuahang là null
        } else {
            this.itemCarts = itemCarts;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlichsumuahang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCart itemCart = itemCarts.get(position);
        if (itemCarts != null){
            holder.tvVoucherName.setText(itemCart.getVoucherName());
             holder.tvDate.setText(itemCart.getNgayMua());
             holder.tvTotal.setText(String.valueOf(itemCart.getToTal()));
    }

    }

    @Override
    public int getItemCount() {
        return itemCarts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvVoucherName;
        private TextView tvTotal;
        private TextView tvDate;
        private TextView tvTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVoucherName = itemView.findViewById(R.id.tvVoucherNameLS);
            tvTotal = itemView.findViewById(R.id.tvTongTienLS);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTrangThai = itemView.findViewById(R.id.tvHuy);
        }
    }
}
