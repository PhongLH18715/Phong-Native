package com.example.mexpenseapplication.views;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;

import com.example.mexpenseapplication.Constants;
import com.example.mexpenseapplication.R;
import com.example.mexpenseapplication.database.TripDbHelper;
import com.example.mexpenseapplication.databinding.FragmentTripFormBinding;
import com.example.mexpenseapplication.entity.Trip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TripFormFragment extends Fragment implements View.OnClickListener {

    private TripFormViewModel mViewModel;
    private FragmentTripFormBinding binding;
    private TripDbHelper dbHelper;

    private int trip_id;

    private DatePickerDialog.OnDateSetListener startListener;
    private DatePickerDialog.OnDateSetListener endListener;
    private Calendar calendar = Calendar.getInstance();

    private TextInputLayout nameLayout;
    private TextInputLayout destinationLayout;
    private TextInputLayout startLayout;
    private TextInputLayout endLayout;

    private AutoCompleteTextView tripName;
    private TextInputEditText tripDestination;
    private TextInputEditText tripStart;
    private TextInputEditText tripEnd;
    private Switch tripRisk;
    private TextInputEditText tripDescription;

    public static TripFormFragment newInstance() {
        return new TripFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(TripFormViewModel.class);
        binding = FragmentTripFormBinding.inflate(inflater, container, false);
        dbHelper = new TripDbHelper(getContext());

        // Khai báo các fields
        nameLayout = binding.tripNameLayout;
        destinationLayout = binding.tripDestinationLayout;
        startLayout = binding.tripStartDateLayout;
        endLayout = binding.tripEndDateLayout;

        tripName = binding.tripName;
        tripName.setOnClickListener(this);

        tripDestination = binding.tripDestination;
        tripStart = binding.tripStartDate;
        tripEnd = binding.tripEndDate;
        tripRisk = binding.tripRisk;
        tripDescription = binding.tripDescription;
        // Khai báo các fields

        Button saveButton = binding.saveTripButton;
        saveButton.setOnClickListener(this); // Bind listener

        Button cancelButton = binding.cancelTripButton;
        cancelButton.setOnClickListener(this); // Bind listener

        try {
            trip_id = getArguments().getInt("trip_id");
        } catch (Exception e) {
            trip_id = -1;
        }

        // Khởi tạo listener cho DatePicker của ngày bắt đầu
        startListener = new DatePickerDialog.OnDateSetListener() {

            // Khi chọn date trên datePicker sẽ thay đổi text của field
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                setCalendar(year, month, day);
                formatDate(tripStart);
            }
        };
        // Bind listener
        tripStart.setOnClickListener(this);

        // Khởi tạo listener cho DatePicker của ngày kết thúc
        endListener = new DatePickerDialog.OnDateSetListener() {

            // Khi chọn date trên datePicker sẽ thay đổi text của field
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                setCalendar(year, month, day);
                formatDate(tripEnd);
            }
        };
        // Bind listener
        tripEnd.setOnClickListener(this);

        mViewModel.trip.observe(
                getViewLifecycleOwner(),
                trip -> {
                    tripName.setText(trip.getName());

                    // initialize the array
                    getTripArray();

                    tripDestination.setText(trip.getDestination());
                    tripStart.setText(trip.getStartDate());
                    tripEnd.setText(trip.getEndDate());
                    tripRisk.setChecked(trip.getRiskAssessment());
                    tripDescription.setText(trip.getDescription());
                }
        );

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_home);
        setHasOptionsMenu(true);

        requireActivity().invalidateOptionsMenu();

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.action_bar_layout, menu);
        if(trip_id != -1){
            menu.findItem(R.id.action_delete).setVisible(true);
        } else menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_edit).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(getView()).navigate(R.id.tripsFragment);
                return true;
            case R.id.action_delete:
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete trip")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteTrip(trip_id);
                                Navigation.findNavController(getView()).navigate(R.id.tripsFragment);
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void formatDate(TextInputEditText view){ // format lại date cho input field
        String format = Constants.DATE_FORMAT;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        view.setText(dateFormat.format(calendar.getTime()));
    }

    private void setCalendar(int year, int month, int day) { // Update năm, tháng, ngày cho calendar
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    @Override
    public void onClick(View view) { // Listener của view
        switch (view.getId()){
            case R.id.tripName:
                getTripArray();
                break;
            case R.id.tripStartDate:
                // tạo date picker
                // Cần context, DatePickerListener, năm, tháng, ngày
                // Đầu ra trả về sẽ target vào listener tương ứng.
                // Listener đã đc khai báo ở dòng 110 phía trên.
                new DatePickerDialog(getContext(), startListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.tripEndDate:
                new DatePickerDialog(getContext(), endListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.saveTripButton:
                saveTrip();
                break;
            case R.id.cancelTripButton:
                Navigation.findNavController(getView()).navigate(R.id.tripsFragment);
            default:
                break;
        }
    }

    private void getTripArray() {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, Constants.trips);
        tripName.setAdapter(adapter);
    }

    private void saveTrip() {
        if(tripValidate()){
            // Clear hết các error của input fields nếu có
            nameLayout.setError(null);
            destinationLayout.setError(null);
            startLayout.setError(null);
            endLayout.setError(null);
            // Tạo dialog để confirm
            new AlertDialog.Builder(getContext())
                    .setTitle("Adding new Expense") // Title của dialog
                    .setMessage("Is all information correct?") // Message của dialog
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() { // Nút yes của dialog
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String name = tripName.getText().toString();
                            String destination = tripDestination.getText().toString();
                            String start = tripStart.getText().toString();
                            String end = tripEnd.getText().toString();
                            boolean risk = tripRisk.isChecked();
                            String description = tripDescription.getText().toString();
                            Trip t = new Trip(-1, name, destination, start, end, risk, description, 0);

                            if (trip_id == -1) {
                                dbHelper.addTrip(t);
                            } else {
                                dbHelper.updateTrip(trip_id, t);
                            }

                            Log.i("TESTING", "onClick: " + t.toString());

                            hideKeyboard();
                            Navigation.findNavController(getView()).navigate(R.id.tripsFragment);
                        }
                    }).setNegativeButton("No", null).show(); // Nút no của dialog
        }
    }

    private boolean tripValidate(){
        boolean b = true;

        if(tripName.getText().toString().equals("")){
            nameLayout.setError("Select a trip");
            b = false;
        }

        if(tripStart.getText().toString().equals("")){
            startLayout.setError("Select the trip start date");
            b = false;
        }

        if(tripStart.getText().toString().equals("")){
            endLayout.setError("Select the trip end date");
            b = false;
        }

        if(tripDestination.getText().toString().equals("")){
            destinationLayout.setError("Please enter the destination");
            b = false;
        }

        return b;
    }

    // Hide keyboard thủ công, code vậy vì nó vậy
    // Source: https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
    private void hideKeyboard(){
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getView();
        if(view == null){
            view = new View(getActivity());
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}