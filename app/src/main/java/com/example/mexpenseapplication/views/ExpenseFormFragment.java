package com.example.mexpenseapplication.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mexpenseapplication.Constants;
import com.example.mexpenseapplication.R;
import com.example.mexpenseapplication.database.ExpenseDbHelper;
import com.example.mexpenseapplication.databinding.FragmentExpenseFormBinding;
import com.example.mexpenseapplication.entity.Expense;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFormFragment extends Fragment implements View.OnClickListener {

    private ExpenseFormViewModel mViewModel;
    private FragmentExpenseFormBinding binding;

    private Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;

    private int trip_id;
    private int expense_id;

    private TextInputLayout nameLayout;
    private TextInputLayout dateLayout;
    private TextInputLayout costLayout;
    private TextInputLayout amountLayout;

    private AutoCompleteTextView expenseName;
    private TextInputEditText expenseDate;
    private TextInputEditText expenseCost;
    private TextInputEditText expenseAmount;
    private TextInputEditText expenseComment;

    private ExpenseDbHelper dbHelper;

    public static ExpenseFormFragment newInstance() {
        return new ExpenseFormFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ExpenseFormViewModel.class);
        binding = FragmentExpenseFormBinding.inflate(inflater, container, false);
        dbHelper = new ExpenseDbHelper(getContext());

        // L???y trip id v?? expense id t??? bundle
        trip_id = getArguments().getInt("trip_id");
        expense_id = getArguments().getInt("expense_id");

        // Khai b??o c??c bi???n
        nameLayout = binding.expenseTypeLayout;
        dateLayout = binding.expenseDateLayout;
        costLayout = binding.expenseCostLayout;
        amountLayout = binding.expenseAmountLayout;

        expenseName = binding.expenseType;
        expenseDate = binding.expenseDate;
        expenseCost = binding.expenseCost;
        expenseAmount = binding.expenseAmount;
        expenseComment = binding.expenseComment;

        Button saveButton = binding.saveExpenseButton;
        saveButton.setOnClickListener(this);

        Button cancelButton = binding.cancelExpenseButton;
        cancelButton.setOnClickListener(this);

        expenseName.setOnClickListener(this);
        expenseDate.setOnClickListener(this);
        //

        date = new DatePickerDialog.OnDateSetListener() { // Constructor c???a DatePicker
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                setCalendar(year, month, day); // Update l???i calendar
                SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);
                expenseDate.setText(dateFormat.format(calendar.getTime()));
                // Khi ch???n date tr??n datePicker s??? thay ?????i text c???a field theo format ???? ch???n
            }
        };

        // G???n observer v??o liveData c???a expense
        mViewModel.expense.observe(
                getViewLifecycleOwner(),
                expense -> {
                    expenseName.setText(expense.getName());
                    getDropdownItems(); // Kh???i t???o array cho dropdown c???a name
                    expenseDate.setText(expense.getDate());
                    expenseCost.setText(String.valueOf(expense.getCost()));
                    expenseAmount.setText(String.valueOf(expense.getAmount()));
                    expenseComment.setText(String.valueOf(expense.getComment()));
                }
        );
        // L???y expense t??? db d??ng id
        mViewModel.expense.setValue(dbHelper.getExpenseId(expense_id));

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
        if(expense_id != -1){ // N???u expense_id = -1 t???c l?? ??ang t???o m???i, n??t delete ph???i b??? ???n ??i
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
                // T???o dialog ????? confirm
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete expense")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.deleteExpense(expense_id);
                                Bundle b = new Bundle();
                                b.putInt("trip_id", trip_id);
                                Navigation.findNavController(getView()).navigate(R.id.expensesFragment, b);
                            }
                        }).setNegativeButton("No", null).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expenseDate:
                // t???o date picker
                // C???n context, DatePickerListener, n??m, th??ng, ng??y
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.expenseName:
                getDropdownItems();
                break;
            case R.id.saveExpenseButton:
                saveExpense();
                break;
            case R.id.cancelExpenseButton:
                Bundle b = new Bundle();
                b.putInt("trip_id", trip_id);
                Navigation.findNavController(getView()).navigate(R.id.expensesFragment, b);
            default:
                break;
        }
    }

    private void setCalendar(int year, int month, int day) {
        // Update calendar th??nh n??m, th??ng, ng??y
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    private void getDropdownItems() {
        // T???o dropdown adapter t??? 1 array (l???y t??? constants)
        // sau ???? g???n adapter v??o input field
        // input field n??y b???t bu???c ph ??? d???ng AutoCompleteEditText
        // Source: https://material.io/components/menus/android#theming-menus
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, Constants.expensesType);
        expenseName.setAdapter(adapter);
    }

    private void saveExpense() {
        if (formValidate()) {
            // Clear h???t t???t c??? c??c error n???u c??
            nameLayout.setError(null);
            dateLayout.setError(null);
            costLayout.setError(null);
            amountLayout.setError(null);
            //
            new AlertDialog.Builder(getContext())
                    .setTitle("Adding new Expense")
                    .setMessage("Is all information correct?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String name = expenseName.getText().toString();
                            String date = expenseDate.getText().toString();
                            int cost  = Integer.parseInt(expenseCost.getText().toString());
                            int amount  = Integer.parseInt(expenseAmount.getText().toString());
                            String comment = expenseComment.getText().toString();

                            Expense e = new Expense(-1, name, date, cost, amount, comment, trip_id);

                            if (expense_id == -1) { // N???u expense id l?? -1 th?? t???o m???i, k th?? update.
                                dbHelper.addExpense(e);
                            } else {
                                dbHelper.updateExpense(expense_id, e);
                            }

                            Bundle b = new Bundle();
                            b.putInt("trip_id", trip_id);

                            hideKeyboard();
                            Navigation.findNavController(getView()).navigate(R.id.expensesFragment, b);
                        }
                    }).setNegativeButton("No", null).show();
        }
    }

    private boolean formValidate() {
        boolean b = true;
        if(expenseName.getText().toString().equals("")){
            nameLayout.setError("Select the expense's type"); // ?????i layout th??nh error
            b = false;
        }

        if(expenseDate.getText().toString().equals("")){
            dateLayout.setError("Select the expense's date");
            b = false;
        }

        if(Integer.parseInt(expenseCost.getText().toString()) == 0){
            costLayout.setError("Please enter the cost");
            b = false;
        }

        if(Integer.parseInt(expenseAmount.getText().toString()) == 0){
            amountLayout.setError("Please enter the amount");
            b = false;
        }

        return b;
    }

    private void hideKeyboard(){
        // Hide keyboard th??? c??ng, code v???y v?? n?? v???y
        // Source: https://stackoverflow.com/questions/1109022/how-to-close-hide-the-android-soft-keyboard-programmatically
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getView();
        if(view == null){
            view = new View(getActivity());
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}