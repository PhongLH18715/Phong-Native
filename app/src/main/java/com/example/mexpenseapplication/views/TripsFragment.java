package com.example.mexpenseapplication.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.mexpenseapplication.R;
import com.example.mexpenseapplication.database.TripDbHelper;
import com.example.mexpenseapplication.databinding.FragmentTripsBinding;
import com.example.mexpenseapplication.views.adapters.TripAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TripsFragment extends Fragment implements View.OnClickListener, TripAdapter.TripItemListener {

    private TripsViewModel mViewModel;
    private TripAdapter adapter;
    private FragmentTripsBinding binding;

    private TripDbHelper dbHelper;

    public static TripsFragment newInstance() {
        return new TripsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TripsViewModel.class);
        binding = FragmentTripsBinding.inflate(inflater, container, false);
        dbHelper = new TripDbHelper(getContext());

        RecyclerView recyclerView = binding.recyclerView;
        // Set fixed height cho tung` item
        recyclerView.setHasFixedSize(true);
        // Set line decoration cho tung hang
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation()));

        SearchView sw = binding.searchTrip;
        sw.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mViewModel.trips.setValue(dbHelper.searchTripsByDestination(s));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mViewModel.trips.setValue(dbHelper.searchTripsByDestination(s));
                return true;
            }
        });

        mViewModel.trips.observe(
                getViewLifecycleOwner(),
                trips -> {
                    adapter = new TripAdapter(trips, this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );
        mViewModel.trips.setValue(dbHelper.getTrips());

        FloatingActionButton fab = binding.tripAddFAB;
        fab.setOnClickListener(this);

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
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // View click listener
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tripAddFAB:
                Bundle b = new Bundle();
                b.putInt("trip_id", -1);
                Navigation.findNavController(getView()).navigate(R.id.tripFormFragment, b);
                break;
            default:
                break;
        }
    }

    // Adapter view click listener
    @Override
    public void onItemClick(int trip_id) {
        Bundle b = new Bundle();
        b.putInt("trip_id", trip_id);
        Navigation.findNavController(getView()).navigate(R.id.expensesFragment, b);
    }

}