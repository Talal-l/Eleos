package com.c50x.eleos.data;

/**
 * Created by ugin on 2/16/18.
 */

public class Venue {
    private String venueName;
    private String venueAddress;
    private String venueType;
    private User venueManager;
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

    public User getVenueManager() {
        return venueManager;
    }

    public void setVenueManager(User venueManager) {
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
