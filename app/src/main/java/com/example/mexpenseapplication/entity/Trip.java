package com.example.mexpenseapplication.entity;

import java.util.Calendar;

public class Trip {
    private int id;
    private String name;
    private String destination;
    private String startDate;
    private String endDate;
    private boolean riskAssessment;
    private String description;
    private int total;

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", destination='" + destination + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", riskAssessment=" + riskAssessment +
                ", description='" + description + '\'' +
                ", total=" + total +
                '}';
    }

    public Trip() {
        id = -1;
        name = "";
        destination = "";
        startDate = "";
        endDate = "";
        riskAssessment = false;
        description = "";
        total = 0;
    }

    public Trip(int id, String name, String destination, String startDate, String endDate, boolean riskAssessment, String description, int total) {
        this.id = id;
        this.name = name;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.riskAssessment = riskAssessment;
        this.description = description;
        this.total = total;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(boolean riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
