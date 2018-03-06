package com.c50x.eleos.activities;

import com.c50x.eleos.data.User;

import java.util.ArrayList;

public class AbstractModel2 {

    private String title;

    private String message;


    public AbstractModel2(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public AbstractModel2(User user) {
        this.title = user.getHandle();
        this.message = user.getName();

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
