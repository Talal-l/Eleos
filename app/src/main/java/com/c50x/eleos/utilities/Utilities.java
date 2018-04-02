package com.c50x.eleos.utilities;


import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.c50x.eleos.data.Request;
import com.c50x.eleos.models.RvRequestModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Utilities {
    private static HashMap<Integer, EditText> originalViews = new HashMap<>();

    private Utilities() {

    }

    public static void enableViews(View... views) {

        for (View view : views) {
            if (view instanceof EditText) {
                ((EditText) view).setInputType(InputType.TYPE_CLASS_TEXT);

            } else if (view instanceof Spinner) {


            } else if (view instanceof TextView) {
                view.setClickable(true);

            }

        }
    }

    public static void disableViews(View... views) {

        for (View view : views) {
            if (view instanceof EditText) {
                ((EditText) view).setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (view instanceof Spinner) {

            } else if (view instanceof TextView) {
                view.setClickable(false);
            }

        }
    }
}
