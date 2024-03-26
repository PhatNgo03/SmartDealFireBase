package com.example.smartdealfirebase.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartdealfirebase.CommandPattern.EditVoucherActivity;
import com.example.smartdealfirebase.Prototype.VoucherPrototype;
import com.example.smartdealfirebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class NCCAdapter extends RecyclerView.Adapter<NCCAdapter.VoucherVH> {

    Context context;
    ArrayList<VoucherPrototype> vouchersDanhMuc;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public NCCAdapter(Context context, ArrayList<VoucherPrototype> vouchersDanhMuc) {
        this.context = context;
        this.vouchersDanhMuc = vouchersDanhMuc;
    }

    @NonNull
    @Override
    public VoucherVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nccvoucher, parent, false);
        return new NCCAdapter.VoucherVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherVH holder, @SuppressLint("RecyclerView") int position) {
        VoucherPrototype voucher = vouchersDanhMuc.get(position);
        holder.tvTenVoucherNCC.setText(voucher.getTenVoucher());
        holder.tvGiaGiamNCC.setText(String.valueOf(voucher.getGiaGiam()));
        holder.tvGiaGocNCC.setText(String.valueOf(voucher.getGiaGoc()));
        String imageUri= voucher.getHinhAnh();
        holder.tvDanhMucGoc.setText(String.valueOf(voucher.getDanhMuc()));

        Glide.with(holder.itemView.getContext()).load(imageUri).into(holder.img_VoucherNCC);

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maVoucherClicked = voucher.getMaVoucher(); // Lấy MaVoucher từ đối tượng Voucher

                // Gọi phương thức để xóa tài liệu với MaVoucher tương ứng
                deleteVoucherByMaVoucher(maVoucherClicked, position);
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditVoucherActivity.class);
                intent.putExtra("Voucher", (CharSequence) voucher);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return vouchersDanhMuc.size();
    }

    class VoucherVH extends RecyclerView.ViewHolder{
        ImageView img_VoucherNCC;
        TextView tvTenVoucherNCC,tvGiaGiamNCC,tvGiaGocNCC,tvDanhMucGoc;
        ImageButton imgXoa, imgEdit;
        public VoucherVH(@NonNull View itemView) {
            super(itemView);
            img_VoucherNCC =itemView.findViewById(R.id.ivVoucherImageNCC);
            tvTenVoucherNCC =itemView.findViewById(R.id.tvVoucherNameNCC);
            tvGiaGiamNCC = itemView.findViewById(R.id.tvDiscountPriceNCC);
            tvGiaGocNCC = itemView.findViewById(R.id.tvPriceNCC);
            tvDanhMucGoc = itemView.findViewById(R.id.tvDanhMucGoc);
            imgXoa = itemView.findViewById(R.id.imgbtnxoa);
            imgEdit = itemView.findViewById(R.id.imgbtnsua);




        }
    }

//    public void deleteVoucher(Voucher voucher, int position) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Voucher itemToDelete = vouchersDanhMuc.get(position);
//        DocumentReference docref = db.collection("Voucher").document(itemToDelete.ge);
////        docref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
////            @Override
////            public void onSuccess(Void unused) {
////                vouchersDanhMuc.remove(position);
////                notifyItemRemoved(position);
////                Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
////            }
////        }).addOnFailureListener(new OnFailureListener() {
////            @Override
////            public void onFailure(@NonNull Exception e) {
////                Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
////            }
////        });
//    }

    public void deleteVoucherByMaVoucher(String maVoucher, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference voucherCollection = db.collection("VoucherDanhMuc");

        Query query = voucherCollection.whereEqualTo("MaVoucher", maVoucher);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String documentId = documentSnapshot.getId();
                    DocumentReference docRef = db.collection("VoucherDanhMuc").document(documentId);

                    docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            vouchersDanhMuc.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}
