package com.example.smartdealfirebase.DesignPatternState;

public class AuthenticationManager {
    public AuthenticationState currentState;

    public AuthenticationManager() {
        currentState = new LoggedOutState(); // Ban đầu là trạng thái đăng xuất
    }

    public void login() {
        // Thực hiện đăng nhập

        // Sau khi đăng nhập thành công, cập nhật trạng thái
        currentState = new LoggedInState();
    }

    // Các phương thức khác để quản lý trạng thái nếu cần
}
