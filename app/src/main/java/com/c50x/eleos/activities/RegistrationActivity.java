package com.c50x.eleos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.c50x.eleos.R;

/**
 * Created by eris on 11/19/17.
 */

public class RegistrationActivity extends AppCompatActivity  {

    private EditText name;
    private EditText handle;
    private EditText email;
    private EditText password;
    private EditText confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.c50x.eleos.R.layout.activity_registration);

        name = (EditText) findViewById(R.id.name);
        handle = (EditText) findViewById(R.id.handle);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password = (EditText) findViewById(R.id.confirm_password);


        
    }


}
