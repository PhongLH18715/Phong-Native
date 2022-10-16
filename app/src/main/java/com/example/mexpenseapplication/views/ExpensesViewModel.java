package com.example.mexpenseapplication.views;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mexpenseapplication.entity.Expense;
import com.example.mexpenseapplication.entity.Trip;

import java.util.List;

public class ExpensesViewModel extends ViewModel {
    MutableLiveData<List<Expense>> expenses = new MutableLiveData<>();
    MutableLiveData<Trip> trip = new MutableLiveData<>();

    public int getTotalExpenses() {
        int total = 0;
        List<Expense> expenseList = expenses.getValue();
        for(Expense e : expenseList){
            total += (e.getCost() * e.getAmount());
        }
        return total;
    }
}