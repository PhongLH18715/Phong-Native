package com.example.mexpenseapplication.entity;

import java.util.Calendar;

public class Expense {
    private int id;
    private String name;
    private String date;
    private int cost;
    private int amount;
    private String comment;
    private int trip_id;


    public Expense() {
        this.id = -1;
        this.name = "";
        this.date = "";
        this.cost = 0;
        this.amount = 0;
        this.comment = "";
        this.trip_id = -1;
    }

    public Expense(int id, String name, String date, int cost, int amount, String comment, int trip_id) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.cost = cost;
        this.amount = amount;
        this.comment = comment;
        this.trip_id = trip_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
