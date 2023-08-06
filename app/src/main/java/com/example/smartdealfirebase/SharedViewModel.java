package com.example.smartdealfirebase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartdealfirebase.Model.Order;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Order> selectedVoucher = new MutableLiveData<>();

    public void setSelectedVoucher(Order order) {
        selectedVoucher.setValue(order);
    }

    public LiveData<Order> getSelectedVoucher() {
        return selectedVoucher;
    }

}
