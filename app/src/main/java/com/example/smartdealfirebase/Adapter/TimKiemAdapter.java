package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TimKiemAdapter extends RecyclerView.Adapter<TimKiemAdapter.TimKiemVH> implements Filterable  {

    private ArrayList<Voucher> vouchers;
    private ArrayList<Voucher> filteredVouchers; // Danh sách tạm thời sau khi tìm kiếm
    private Listener listener;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public TimKiemAdapter(ArrayList<Voucher> vouchers, Listener listener) {
        this.vouchers = vouchers;
        this.filteredVouchers = vouchers;
        this.listener = listener;
    }


    @NonNull
    @Override
    public TimKiemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search,parent,false);
        TimKiemVH timKiemVH =new TimKiemVH(view);
        return timKiemVH;
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiemVH holder, @SuppressLint("RecyclerView") int position) {
        Voucher voucher = vouchers.get(position);
        holder.tvTenvoucher.setText(voucher.getVoucherName());
        holder.tvGiagiam.setText(String.valueOf(voucher.getDiscountPrice()));
        holder.tvGiagoc.setText(String.valueOf(voucher.getPrice()));
        holder.tvslngmua.setText(String.valueOf(voucher.getSlnguoimua()));
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                    String strSearch = charSequence.toString().toLowerCase();
                    ArrayList<Voucher> list = new ArrayList<>();
                    for (Voucher voucher : vouchers) {
                        // Lọc các voucher theo tên
                        if (voucher.getVoucherName().toLowerCase().contains(strSearch)) {
                            list.add(voucher);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = list;
                    return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                vouchers = (ArrayList<Voucher>) filterResults.values;
                notifyDataSetChanged();
            }
        }
            ;
    }

    public interface Listener {
        boolean onCreateOptionsMenu(Menu menu, MenuInflater inflater);

        void setOnInfoClick(Voucher voucher);
    }

    class TimKiemVH extends RecyclerView.ViewHolder {
        ImageView ivVoucherImage;
        TextView tvTenvoucher,tvGiagoc,tvGiagiam,tvslngmua;

        public TimKiemVH(@NonNull View itemView) {
            super(itemView);
            ivVoucherImage=itemView.findViewById(R.id.ivVoucherImage);
            tvTenvoucher=itemView.findViewById(R.id.tvVoucherName);
            tvGiagiam=itemView.findViewById(R.id.tvDiscountPrice);
            tvGiagoc=itemView.findViewById(R.id.tvPrice);
            tvslngmua=itemView.findViewById(R.id.tvslnguoimua);
        }
    }


}
