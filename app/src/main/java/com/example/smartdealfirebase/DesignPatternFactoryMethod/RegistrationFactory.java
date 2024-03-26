package com.example.smartdealfirebase.DesignPatternFactoryMethod;

// Creator : tạo ra các đối tượng đăng kí mà khong quan tam đến chi tiết
public interface RegistrationFactory {
    IRegistration createRegistration();
}

