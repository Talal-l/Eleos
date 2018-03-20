package com.c50x.eleos.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Venue {
    @NonNull
    @PrimaryKey
    private String venueAddress;

    private String venueName;
    private String venueType;
    private String venueManager;
    private int openingTime;
    private int closingTime;
    private int numOfGrounds;


    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    public String getVenueManager() {
        return venueManager;
    }

    public void setVenueManager(String venueManager) {
        this.venueManager = venueManager;
    }

    public int getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public int getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    public int getNumOfGrounds() {
        return numOfGrounds;
    }

    public void setNumOfGrounds(int numOfGrounds) {
        this.numOfGrounds = numOfGrounds;
    }

}
