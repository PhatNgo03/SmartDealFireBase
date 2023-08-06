package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class VoucherSpaAdapter extends RecyclerView.Adapter<VoucherSpaAdapter.VoucherVH>{
    private List<Voucher> vouchers;

    Listener listener;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public VoucherSpaAdapter(List<Voucher> vouchers, Listener listener) {
        this.vouchers = vouchers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VoucherVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voucheramthuc, parent, false);
        return new VoucherSpaAdapter.VoucherVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherVH holder, @SuppressLint("RecyclerView") int position) {
        Voucher voucher = vouchers.get(position);
        holder.tvVoucherName.setText(voucher.getVoucherName());
        holder.tvDiscountPrice.setText(String.valueOf(voucher.getDiscountPrice()));
        holder.tvslnguoimua.setText(String.valueOf(voucher.getSlnguoimua() + " " +"lượt mua"));
        holder.tvPrice.setText(String.valueOf(voucher.getPrice()));
        StorageReference storageRef = storage.getReference();
        int targerWidth = 290;
        int targetHeight = 150;
        StorageReference imamgeRef = storageRef.child(String.valueOf(voucher.getHinhvc()));
        imamgeRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                Bitmap resizeBitMap = Bitmap.createScaledBitmap(bitmap,targerWidth,targetHeight,false);
                holder.ivVoucherImage.setImageBitmap(resizeBitMap);
            }
        });
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
    public interface Listener {
        void setOnInfoClick(Voucher voucher3);
    }
}
