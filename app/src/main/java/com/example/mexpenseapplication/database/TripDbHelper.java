package com.example.mexpenseapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mexpenseapplication.entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "trips";
    public static final String T_ID = "id";
    public static final String T_NAME = "name";
    public static final String T_DESTINATION = "destination";
    public static final String T_START_DATE = "start_date";
    public static final String T_END_DATE = "end_date";
    public static final String T_RISK = "risk";
    public static final String T_DESCRIPTION = "description";
    public static final String T_TOTAL = "total";

    private SQLiteDatabase database;

    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_NAME + " ( "
                    + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + T_NAME + " TEXT, "
                    + T_DESTINATION + " TEXT, "
                    + T_START_DATE + " TEXT, "
                    + T_END_DATE + " TEXT, "
                    + T_RISK + " INTEGER,"
                    + T_DESCRIPTION + " TEXT, "
                    + T_TOTAL + " INTEGER)";

    public TripDbHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
        database = getWritableDatabase();
    }

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

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#ReadDbRow
    public List<Trip> getTrips() {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (c.moveToNext()) {
            int id = Integer.parseInt(c.getString(0));
            String name = c.getString(1);
            String destination = c.getString(2);
            String startDate = c.getString(3);
            String endDate = c.getString(4);
            boolean risk = Integer.parseInt(c.getString(5)) == 1 ? true : false;
            String description = c.getString(6);
            int total = Integer.parseInt(c.getString(7));
            trips.add(new Trip(id, name, destination, startDate, endDate, risk, description, total));
        }

        c.close(); // closing cursor to release memory
        return trips;
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#ReadDbRow
    public Trip getTrip(int id) {
        // If the expense is not found then return an empty expense object
        Trip t = new Trip();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + T_ID + "=" + id, null);
        while (c.moveToNext()) {
            t.setId(Integer.parseInt(c.getString(0)));
            t.setName(c.getString(1));
            t.setDestination(c.getString(2));
            t.setStartDate(c.getString(3));
            t.setEndDate(c.getString(4));
            t.setRiskAssessment(Integer.parseInt(c.getString(5)) == 1 ? true : false);
            t.setDescription(c.getString(6));
            t.setTotal(Integer.parseInt(c.getString(7)));
        }
        c.close(); // closing cursor to release memory

        return t;
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#WriteDbRow
    public void addTrip(Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(T_NAME, trip.getName());
        values.put(T_DESTINATION, trip.getDestination());
        values.put(T_START_DATE, trip.getStartDate());
        values.put(T_END_DATE, trip.getEndDate());
        values.put(T_RISK, trip.getRiskAssessment());
        values.put(T_DESCRIPTION, trip.getDescription());
        values.put(T_TOTAL, 0);

        // insert( TABLE NAME, nullColumnHack, insert values)
        // nullColumnHack = allows null values in a column => used in optional columns
        Log.i("DATABASE", "addTrip: " + values.toString());
        db.insert(TABLE_NAME, T_DESCRIPTION, values);
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#UpdateDbRow
    public void updateTrip(int trip_id, Trip trip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(T_NAME, trip.getName());
        values.put(T_DESTINATION, trip.getDestination());
        values.put(T_START_DATE, trip.getStartDate());
        values.put(T_END_DATE, trip.getEndDate());
        values.put(T_RISK, trip.getRiskAssessment());
        values.put(T_DESCRIPTION, trip.getDescription());

        // insert( TABLE NAME, nullColumnHack, insert values)
        // nullColumnHack = allows null values in a column => used in optional columns
        db.update(TABLE_NAME, values, T_ID + "=?", new String[]{String.valueOf(trip_id)});
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#UpdateDbRow
    public void updateTotal(int trip_id, int total){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T_TOTAL, total);
        db.update(TABLE_NAME, values, T_ID + "=?", new String[]{String.valueOf(trip_id)});
    }

    // Nguồn chính thức từ Android
    // Source: https://developer.android.com/training/data-storage/sqlite#DeleteDbRow
    public void deleteTrip(int trip_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, T_ID + "=?", new String[]{String.valueOf(trip_id)});
    }

    public List<Trip> searchTripsByName(String s) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + T_NAME + " LIKE '%" + s + "%'" ,null);
        while (c.moveToNext()) {
            int id = Integer.parseInt(c.getString(0));
            String name = c.getString(1);
            String destination = c.getString(2);
            String startDate = c.getString(3);
            String endDate = c.getString(4);
            boolean risk = Integer.parseInt(c.getString(5)) == 1 ? true : false;
            String description = c.getString(6);
            int total = Integer.parseInt(c.getString(7));
            trips.add(new Trip(id, name, destination, startDate, endDate, risk, description, total));
        }

        c.close(); // closing cursor to release memory
        return trips;
    }

    public List<Trip> searchTripsByDestination(String s) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + T_DESTINATION + " LIKE '%" + s + "%'" ,null);
        while (c.moveToNext()) {
            int id = Integer.parseInt(c.getString(0));
            String name = c.getString(1);
            String destination = c.getString(2);
            String startDate = c.getString(3);
            String endDate = c.getString(4);
            boolean risk = Integer.parseInt(c.getString(5)) == 1 ? true : false;
            String description = c.getString(6);
            int total = Integer.parseInt(c.getString(7));
            trips.add(new Trip(id, name, destination, startDate, endDate, risk, description, total));
        }

        c.close(); // closing cursor to release memory
        return trips;
    }

    public List<Trip> searchTripsByStartDate(String s) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + T_START_DATE + " LIKE '%" + s + "%'" ,null);
        while (c.moveToNext()) {
            int id = Integer.parseInt(c.getString(0));
            String name = c.getString(1);
            String destination = c.getString(2);
            String startDate = c.getString(3);
            String endDate = c.getString(4);
            boolean risk = Integer.parseInt(c.getString(5)) == 1 ? true : false;
            String description = c.getString(6);
            int total = Integer.parseInt(c.getString(7));
            trips.add(new Trip(id, name, destination, startDate, endDate, risk, description, total));
        }

        c.close(); // closing cursor to release memory
        return trips;
    }

    public List<Trip> searchTripsByEndDate(String s) {
        List<Trip> trips = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + T_END_DATE + " LIKE '%" + s + "%'" ,null);
        while (c.moveToNext()) {
            int id = Integer.parseInt(c.getString(0));
            String name = c.getString(1);
            String destination = c.getString(2);
            String startDate = c.getString(3);
            String endDate = c.getString(4);
            boolean risk = Integer.parseInt(c.getString(5)) == 1 ? true : false;
            String description = c.getString(6);
            int total = Integer.parseInt(c.getString(7));
            trips.add(new Trip(id, name, destination, startDate, endDate, risk, description, total));
        }

        c.close(); // closing cursor to release memory
        return trips;
    }
}
