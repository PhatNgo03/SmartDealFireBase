package com.example.smartdealfirebase.DesignPatternState;

class LoggedOutState implements AuthenticationState {
    @Override
    public void handleLoginSuccess() {
        // Người dùng đăng nhập thành công và không cần xử lý
    }
}
