package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


//Adapter : cung cấp cách để hiển thị dữ liệu lên recyclerView
//Adaptee : Voucher : đại diện cho các dữ liệu của voucher cần hiển thị
public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherVH> {
    private List<Voucher> vouchers; // Adapteee

    Listener listener;
    public VoucherAdapter(List<Voucher> vouchers, Listener listener) {
        this.vouchers = vouchers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VoucherVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_voucher, parent, false);
        return new VoucherVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherVH holder, @SuppressLint("RecyclerView") int position) {

        Voucher voucher = vouchers.get(position);
        holder.tvVoucherName.setText(voucher.getVoucherName());
        holder.tvDiscountPrice.setText(String.valueOf(voucher.getDiscountPrice()));
        holder.tvslnguoimua.setText(String.valueOf(voucher.getSlnguoimua() + " " +"lượt mua"));
        holder.tvPrice.setText(String.valueOf(voucher.getPrice()));
        Glide.with(holder.itemView.getContext()).load(Uri.parse(voucher.getHinhvc())).into(holder.ivVoucherImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.setOnInfoClick(vouchers.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    static class VoucherVH extends RecyclerView.ViewHolder {
        TextView tvVoucherName, tvDiscountPrice, tvPrice, tvslnguoimua;
        ImageView ivVoucherImage;

        public VoucherVH(View itemView) {
            super(itemView);
            tvVoucherName = itemView.findViewById(R.id.tvVoucherName);
            tvDiscountPrice = itemView.findViewById(R.id.tvDiscountPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvslnguoimua = itemView.findViewById(R.id.tvslnguoimua);
            ivVoucherImage = itemView.findViewById(R.id.ivVoucherImage);
            tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
    }

    // Listener ( target) : định nghĩa 1 hành động khi voucher được nhấn
    public interface Listener {
        void setOnInfoClick(Voucher voucher);
    }
}
