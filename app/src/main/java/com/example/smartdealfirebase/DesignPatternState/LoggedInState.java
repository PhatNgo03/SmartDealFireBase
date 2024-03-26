package com.example.smartdealfirebase.DesignPatternState;
import android.content.Context;

public class LoggedInState implements AuthenticationState{
    private Context mContext;


    @Override
    public void handleLoginSuccess() {
        // Người dùng đã đăng nhập thành công

    }
}
