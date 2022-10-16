package com.example.mexpenseapplication.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mexpenseapplication.R;
import com.example.mexpenseapplication.databinding.ExpenseListItemBinding;
import com.example.mexpenseapplication.entity.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    // Code thầy dạy nên dùng, k hiểu thì xem lại video
    // Cơ bản thì là tạo 1 viewHolder để adapt từng objects trong list thành các hàng trong recycler view.
    // Trong đó thì có gắn các listener và bind data lại thành 1 cái layout mình đã tạo.
    // Source: https://developer.android.com/develop/ui/views/layout/recyclerview

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private final ExpenseListItemBinding binding;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ExpenseListItemBinding.bind(itemView);
        }

        public void bindData(Expense expense){
            // TODO
            binding.expenseName.setText(expense.getName());
            binding.expenseAmount.setText(String.valueOf(expense.getAmount()));
            binding.expenseCost.setText(String.valueOf(expense.getCost()));
            binding.expenseComment.setText(expense.getComment().equals("") ? "No additional information" : expense.getComment());
            binding.expenseDate.setText(expense.getDate());
            binding.expenseIcon.setImageResource(getExpenseIcon(expense.getName()));
            binding.expenseTotal.setText(expense.getCost() * expense.getAmount() + " VND");
            binding.getRoot().setOnClickListener( view -> listener.onItemClick(expense.getId()));
        }

        private int getExpenseIcon(String name) {
            return binding.getRoot().getResources().getIdentifier("ic_" + name.toLowerCase(), "drawable", binding.getRoot().getContext().getPackageName());
        }
    }

    public interface ExpenseItemListener{
        void onItemClick(int expenseId);
    }

    private List<Expense> expenses;

    private ExpenseItemListener listener;

    public ExpenseAdapter(List<Expense> expenses, ExpenseItemListener listener){
        this.expenses = expenses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bindData(expense);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
