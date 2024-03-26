package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.Model.Voucher;
import com.example.smartdealfirebase.R;
import com.example.smartdealfirebase.ThongTinVoucherActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VoucherDaThichAdapter extends RecyclerView.Adapter<VoucherDaThichAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Voucher> favoriteVoucher = new ArrayList<>();
    ArrayList<Voucher> voucherlikes;
    public VoucherDaThichAdapter(Context context, ArrayList<Voucher> voucherlikes) {
        this.context = context;
        this.voucherlikes = voucherlikes;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView namevoucher, pricevoucherGiam,pricevoucherGoc;
        ImageView imagevoucher,img_liked;
        public ViewHolder(View itemView) {
            super(itemView);

            namevoucher = itemView.findViewById(R.id.tvIFVoucherName);
            pricevoucherGiam = itemView.findViewById(R.id.tvIFDiscountPrice);
            pricevoucherGoc=itemView.findViewById(R.id.tvIFPrice);
            imagevoucher = itemView.findViewById(R.id.ivVoucherInfo1);
            img_liked = itemView.findViewById(R.id.img_liked);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_voucherdathich,parent,false);
        ViewHolder VoucherYeuThichVH =new ViewHolder(view);
        return VoucherYeuThichVH;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Voucher currentVoucher = voucherlikes.get(position);
        holder.pricevoucherGiam.setText(String.format("%,d", Math.round(100000)) + " VNĐ");
        holder.pricevoucherGoc.setText(String.format("%,d", Math.round(100000)) + " VNĐ");

        holder.namevoucher.setText(currentVoucher.getVoucherName());
        Glide.with(context).load(currentVoucher.getHinhvc()).into(holder.imagevoucher);
        holder.imagevoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ThongTinVoucherActivity.class);
                intent.putExtra("ThongTInVoucher",currentVoucher);
                context.startActivity(intent);
            }
        });
        holder.img_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.img_liked.setBackgroundResource(R.drawable.heart_24);
                deleteSachYeuThich(currentVoucher ,position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return voucherlikes.size();
    }

    public void deleteSachYeuThich(Voucher voucher,int position)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docref = db.collection("favorites").document(voucher.get_id());
        docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                voucherlikes.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Đã xóa khỏi danh sách !!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Xóa không thành công !!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
