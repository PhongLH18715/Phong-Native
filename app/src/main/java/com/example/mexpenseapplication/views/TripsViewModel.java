package com.example.mexpenseapplication.views;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenseapplication.entity.Trip;

import java.util.List;

public class TripsViewModel extends ViewModel {
    MutableLiveData<List<Trip>> trips = new MutableLiveData<>();
}