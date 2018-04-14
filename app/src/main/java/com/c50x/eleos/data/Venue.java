package com.c50x.eleos.data;

import android.arch.persistence.room.Entity;

@Entity
public class Venue {

    private String venueAddress;
    private String venueId;
    private String venueCoordinate;
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

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueCoordinate() {
        return venueCoordinate;
    }

    public void setVenueCoordinate(String venueCoordinate) {
        this.venueCoordinate = venueCoordinate;
    }
}
