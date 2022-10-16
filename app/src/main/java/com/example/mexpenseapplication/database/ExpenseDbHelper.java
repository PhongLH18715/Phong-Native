package com.example.mexpenseapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mexpenseapplication.entity.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "expenses";
    public static final String C_ID = "id";
    public static final String C_NAME = "name";
    public static final String C_DATE = "date";
    public static final String C_COST = "cost";
    public static final String C_AMOUNT = "amount";
    public static final String C_COMMENT = "comment";
    public static final String C_TRIP_ID = "trip_id";

    private SQLiteDatabase database;

    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ( "
                    + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + C_NAME + " TEXT, "
                    + C_DATE + " TEXT, "
                    + C_COST + " INTEGER, "
                    + C_AMOUNT + " INTEGER, "
                    + C_COMMENT + " TEXT,"
                    + C_TRIP_ID + " INTEGER, "
                    + "FOREIGN KEY(trip_id) REFERENCES trips(id) ON DELETE CASCADE )";

    // Source: https://developer.android.com/training/data-storage/sqlite#DefineContract
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.database = sqLiteDatabase;
        database.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public ExpenseDbHelper(Context context){
        super(context, TABLE_NAME, null, 1);
        database = getWritableDatabase();
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#ReadDbRow
    public List<Expense> getExpenses(int tripId){
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while(c.moveToNext()){
            if(Integer.parseInt(c.getString(6)) == tripId){
                int id = Integer.parseInt(c.getString(0));
                String name = c.getString(1);
                String date = c.getString(2);
                int cost = Integer.parseInt(c.getString(3));
                int amount = Integer.parseInt(c.getString(4));
                String comment = c.getString(5);
                int trip_id = Integer.parseInt(c.getString(6));
                expenses.add(new Expense(id, name, date, cost, amount, comment, trip_id));
            }
        }

        c.close(); // closing cursor to release memory
        return expenses;
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#ReadDbRow
    public Expense getExpenseId(int id){
        // If the expense is not found then return an empty expense object
        Expense expense = new Expense();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + C_ID + "=" + id, null);
        while(c.moveToNext()){
            expense.setId(Integer.parseInt(c.getString(0)));
            expense.setName(c.getString(1));
            expense.setDate(c.getString(2));
            expense.setCost(Integer.parseInt(c.getString(3)));
            expense.setAmount(Integer.parseInt(c.getString(4)));
            expense.setComment(c.getString(5));
            expense.setTrip_id(Integer.parseInt(c.getString(6)));
        }
        c.close(); // closing cursor to release memory

        return expense;
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#WriteDbRow
    public void addExpense(Expense expense){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(C_NAME, expense.getName());
        values.put(C_DATE, expense.getDate());
        values.put(C_COST, expense.getCost());
        values.put(C_AMOUNT, expense.getAmount());
        values.put(C_COMMENT, expense.getComment());
        values.put(C_TRIP_ID, expense.getTrip_id());

        // insert( TABLE NAME, nullColumnHack, insert values)
        // nullColumnHack = allows null values in a column => used in optional columns
        db.insert(TABLE_NAME, C_COMMENT, values);
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#UpdateDbRow
    public void updateExpense(int expense_id, Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(C_NAME, expense.getName());
        values.put(C_DATE, expense.getDate());
        values.put(C_COST, expense.getCost());
        values.put(C_AMOUNT, expense.getAmount());
        values.put(C_COMMENT, expense.getComment());
        values.put(C_TRIP_ID, expense.getTrip_id());

        // table name -> update values -> where ... -> value of where column
        db.update(TABLE_NAME, values, C_ID + "=?", new String[]{String.valueOf(expense_id)});
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#DeleteDbRow
    public void deleteExpense(int expense_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, C_ID + "=?", new String[]{String.valueOf(expense_id)});
    }
}
