package com.example.mexpenseapplication.views;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenseapplication.entity.Expense;

public class ExpenseFormViewModel extends ViewModel {
    MutableLiveData<Expense> expense = new MutableLiveData<>();
}