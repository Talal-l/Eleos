package com.c50x.eleos.utilities;


import android.text.InputType;
import android.widget.EditText;

import java.util.HashMap;

public final class Utilities {
    private static HashMap<Integer, EditText> originalViews = new HashMap<>();

    private Utilities() {

    }

    public static void enableEditText(EditText... editTexts) {

        for (EditText editText : editTexts) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);

        }
    }

    public static void disableEditText(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }
}
