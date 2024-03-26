package com.example.smartdealfirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.smartdealfirebase.Fragment.CartFragment;
import com.example.smartdealfirebase.Fragment.DanhMucFragment;
import com.example.smartdealfirebase.Fragment.OrderFragment;
import com.example.smartdealfirebase.Fragment.TaiKhoanFragment;
import com.example.smartdealfirebase.Fragment.TrangChuFragment;
import com.example.smartdealfirebase.Model.ItemCart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements CartFragment.OnOrderButtonClickListener {

    BottomNavigationView BottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView = findViewById(R.id.bnv);
        BottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                display(item.getItemId());
                return true;
            }
        });
        display(R.id.home);
    }
    void display(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.home:
                fragment = new TrangChuFragment();
                break;
            case R.id.category:
                fragment = new DanhMucFragment();
                break;
            case R.id.cart:
                    fragment = new CartFragment();
                    break;
            case R.id.acc:
                fragment = new TaiKhoanFragment();
                break;
            case R.id.framelayourCart:
                fragment = new CartFragment();
                break;
            case R.id.GiaoHang:
                fragment = new OrderFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frContent, fragment);
        ft.commit();
    }


//    @Override
//    public void setOnInfoClick(Voucher voucher) {
//        Intent intent = new Intent(this,infoVoucherActivity.class);
//        intent.putExtra("voucher", voucher);
//        startActivity(intent);
//    }

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

            }
        });

    @Override
    public void onOrderButtonClick(ItemCart itemCart) {
        OrderFragment orderFragment = new OrderFragment();
        Bundle Bundle = new Bundle();
        Bundle.putSerializable("itemCart", itemCart);
        orderFragment.setArguments(Bundle);

        FragmentTransaction Transaction = getSupportFragmentManager().beginTransaction();
        Transaction.replace(R.id.frContent, orderFragment);
        Transaction.addToBackStack(null); // Để có thể quay lại fragment trước đó bằng nút back
        Transaction.commit();
    }
//    @Override
//    public void onClickInfo(DichVu dichVu) {
//        Intent intent = new Intent(getContext(), ThongTinDichVuActivity.class);
//        intent.putExtra("ThongTinDichVu", dichVu);
//        launcher.launch(intent);
//
//
//    }
}