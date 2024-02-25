package com.example.smartdealfirebase.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.smartdealfirebase.Fragment.DanhMucVoucherFragment;
import com.example.smartdealfirebase.Fragment.VoucherFragment;
import com.example.smartdealfirebase.R;
import com.example.smartdealfirebase.ThongBaoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NhaCungCapActivity extends AppCompatActivity {

    BottomNavigationView bnvAmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nha_cung_cap);

        bnvAmin = findViewById(R.id.bnvNCC);
        bnvAmin.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                display(item.getItemId());
                return true;
            }
        });
        display(R.id.mnu_Voucher);
    }
    void display(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.mnu_Voucher:
                fragment = new DanhMucVoucherFragment();
                break;
            case R.id.mnu_thongbao:
                fragment = new ThongBaoFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameNhaCungCap, fragment);
        ft.commit();
    }
}