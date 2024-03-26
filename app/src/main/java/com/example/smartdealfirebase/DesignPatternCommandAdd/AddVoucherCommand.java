package com.example.smartdealfirebase.DesignPatternCommandAdd;

import android.widget.Toast;

public class AddVoucherCommand implements AddCommand {
    private final AddVoucherActivity activity;
    public AddVoucherCommand(AddVoucherActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {


        // Logic mua hàng được chuyển vào đây
        if (activity.imageUri != null) {
            // Tải hình ảnh lên Firebase Storage
            activity.uploadImageToFirebaseStorage(activity.imageUri);
        } else {
            Toast.makeText(activity, "Hãy chọn một hình ảnh", Toast.LENGTH_SHORT).show();
        }
        // Có thể sao chép toàn bộ logic từ onClick của btBuy vào đây

    }
}
