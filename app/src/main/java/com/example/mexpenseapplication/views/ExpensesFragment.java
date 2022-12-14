package com.example.mexpenseapplication.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mexpenseapplication.R;
import com.example.mexpenseapplication.database.ExpenseDbHelper;
import com.example.mexpenseapplication.database.TripDbHelper;
import com.example.mexpenseapplication.databinding.FragmentExpensesBinding;
import com.example.mexpenseapplication.views.adapters.ExpenseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class ExpensesFragment extends Fragment implements ExpenseAdapter.ExpenseItemListener, View.OnClickListener {

    private ExpensesViewModel mViewModel;
    private FragmentExpensesBinding binding;
    private ExpenseAdapter adapter;
    private ExpenseDbHelper expenseDbHelper;
    private TripDbHelper tripDbHelper;

    private int trip_id;

    private TextView tripName;
    private TextView tripStart;
    private TextView tripEnd;
    private TextView tripDestination;
    private TextView tripTotal;
    private TextView tripDescription;
    private TextView tripAssessment;

    public static ExpensesFragment newInstance() {
        return new ExpensesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);
        binding = FragmentExpensesBinding.inflate(inflater, container, false);

        // Database connection
        expenseDbHelper = new ExpenseDbHelper(getContext());
        tripDbHelper = new TripDbHelper(getContext());

        // L???y trip ip t??? bundle
        trip_id = getArguments().getInt("trip_id");

        // Khai b??o c??c bi???n element trong xml
        tripName = binding.expenseTripName;
        tripStart = binding.expenseTripStart;
        tripEnd = binding.expenseTripEnd;
        tripDestination = binding.expenseTripDestination;
        tripTotal = binding.expenseTripTotal;
        tripDescription = binding.expenseTripComment;
        tripAssessment = binding.expenseTripAssessment;

        FloatingActionButton fab = binding.addExpenseButton;
        fab.setOnClickListener(this);

        // Khai b??o recycler view
        RecyclerView rv = binding.recyclerView2;
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation()));

        // Fixed code, ph???i l??m nh?? n??y v?? android n?? th???
        mViewModel.expenses.observe(
                getViewLifecycleOwner(),
                expenses -> {
                    adapter = new ExpenseAdapter(expenses, this);
                    binding.recyclerView2.setAdapter(adapter);
                    binding.recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.expenseCount.setText("Total expenses: " + expenses.size());
                }
        );
        // V?? c?? observer n??n thay ?????i th???ng mutable live data
        mViewModel.expenses.setValue(expenseDbHelper.getExpenses(trip_id));

        mViewModel.trip.observe(
                getViewLifecycleOwner(),
                trip -> {
                    tripName.setText(trip.getName());
                    tripStart.setText(trip.getStartDate());
                    tripEnd.setText(trip.getEndDate());
                    tripDestination .setText(trip.getDestination());
                    tripTotal.setText(mViewModel.getTotalExpenses() + " VND");
                    tripDescription.setText(trip.getDescription().equals("") ? "No additional comment" : trip.getDescription());
                    tripAssessment.setText(trip.getRiskAssessment() ? "Assessment Required" : "Assessment Not Required");
                }
        );
        // V?? c?? observer n??n thay ?????i th???ng mutable live data
        mViewModel.trip.setValue(tripDbHelper.getTrip(trip_id));

        // Update t???ng ti???n c???a c??c expenses cho t???ng trip
        tripDbHelper.updateTotal(trip_id, mViewModel.getTotalExpenses());

        // Khai b??o thanh action bar, fixed code
        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_home);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.action_bar_layout, menu);
        // T??y trang m?? set l???i visbility c???a n??
        menu.findItem(R.id.action_delete).setVisible(true);
        menu.findItem(R.id.action_edit).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(getView()).navigate(R.id.tripsFragment);
                return true;
            case R.id.action_edit:
                // Kh???i t???o bundle ????? pass tham s??? v??o
                Bundle b = new Bundle();
                b.putInt("trip_id", trip_id);
                Navigation.findNavController(getView()).navigate(R.id.tripFormFragment, b);
                return true;
            case R.id.action_delete:
                // T???o alert dialog ????? confirm
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete trip")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() { // C?? th??? thay = lambda n???u hi???u
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                tripDbHelper.deleteTrip(trip_id);
                                Navigation.findNavController(getView()).navigate(R.id.tripsFragment);
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int expenseId) { // Item click listener c???a adapter
        Bundle b = new Bundle();
        b.putInt("expense_id", expenseId);
        b.putInt("trip_id", trip_id);
        Navigation.findNavController(getView()).navigate(R.id.expenseFormFragment, b);
    }

    @Override
    public void onClick(View view) { // Item click listener c???a view
        switch (view.getId()){
            case R.id.addExpenseButton:
                Bundle b = new Bundle();
                b.putInt("expense_id", -1);
                b.putInt("trip_id", trip_id);
                Navigation.findNavController(getView()).navigate(R.id.expenseFormFragment, b);
            default:break;
        }
    }
}