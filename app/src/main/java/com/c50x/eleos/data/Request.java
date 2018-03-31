package com.c50x.eleos.data;

public class Request {
    public static final int PENDING = 0;
    public static final int ACCEPTED = 1;
    public static final int DECLINED = 2;

    private Object type;
    private String sender;
    private String receiver;
    private int state;

    public Request(Object type, String sender, String receiver, int state) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.state = state;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
