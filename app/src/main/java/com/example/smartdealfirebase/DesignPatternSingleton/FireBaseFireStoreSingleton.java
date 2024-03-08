package com.example.smartdealfirebase.DesignPatternSingleton;
import com.google.firebase.firestore.FirebaseFirestore;
public class FireBaseFireStoreSingleton {
    private static FireBaseFireStoreSingleton instance; // lưu trữ instance duy nhất
    private final FirebaseFirestore firestore; // private để ngăn cản việc tạo thêm các instance của các lớp khác ben ngoài
    private FireBaseFireStoreSingleton() {
        // Khởi tạo FirebaseFirestore instance
        firestore = FirebaseFirestore.getInstance();
    }
    public static synchronized FireBaseFireStoreSingleton getInstance(){ // lấy instance duy nhất
        if(instance == null){
            instance = new FireBaseFireStoreSingleton();
        }
        return instance;
    }
    public FirebaseFirestore getFirestore(){return firestore;} // trả về instance đã tạo từ FirebaseFireStoreSingleton
}
