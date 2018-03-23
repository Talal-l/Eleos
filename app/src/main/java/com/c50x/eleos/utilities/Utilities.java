package com.c50x.eleos.utilities;


import android.text.InputType;
import android.widget.EditText;

public final class Utilities {
   private Utilities(){

   }

       public static void enableEditText(EditText ... editTexts ) {
        for (EditText editText: editTexts ) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public static void disableEditText(EditText ... editTexts ) {
        for (EditText editText: editTexts ) {
            editText.setInputType(InputType.TYPE_NULL);
        }
    }
}
