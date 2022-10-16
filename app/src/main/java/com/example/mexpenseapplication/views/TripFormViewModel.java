package com.example.mexpenseapplication.views;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenseapplication.entity.Trip;

public class TripFormViewModel extends ViewModel {
    MutableLiveData<Trip> trip = new MutableLiveData<>();
}