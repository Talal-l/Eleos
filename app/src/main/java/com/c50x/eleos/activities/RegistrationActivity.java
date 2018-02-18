package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.c50x.eleos.R;

/**
 * Created by eris on 11/19/17.
 */

public class RegistrationActivity extends AppCompatActivity  {

    protected static EditText name;
    private EditText handle;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private Button   confirmButton;
    private Button   cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(com.c50x.eleos.R.layout.activity_registration);

        name = (EditText) findViewById(R.id.name);
        handle = (EditText) findViewById(R.id.handle);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        confirmButton = (Button) findViewById(R.id.button_confirm);
        cancelButton = (Button) findViewById(R.id.button_cancel);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (name.getText().toString().contains("[0-9]+") == true || name.getText().toString().isEmpty() && name.getText().toString().length() > 10) {
                if(nameCheck(name.getText().toString())) {
                    name.setError("Empty or Incorrect Length");
                }

                else if (handleCheck(handle.getText().toString())) {
                    handle.setError("Should be less than 10 characters");
                }

                else if (emailCheck(email.getText().toString())) {
                    email.setError("Empty?");
                }

                else if (passwordCheck(password.getText().toString())) {
                    password.setError("Empty or Less than 6 characters");
                }

                else if (passwordCheck(password.getText().toString())) {
                    confirm_password.setError("Empty or Less than 6 characters");
                }

                else {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });




        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean nameCheck (String name) {

        if(name.isEmpty()) {
            return true;
        } else if (name.length() < 10) {
            return false;
        }

        return true;
    }

    public boolean handleCheck (String handle) {

        if(handle.isEmpty()) {
            return true;
        } else if (handle.length() > 4 && handle.length() < 10) {
            return false;
        }

        return true;
    }

    public boolean emailCheck (String email) {

        if(email.isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean passwordCheck (String password) {

        if(password.isEmpty()) {
            return true;
        } else if (password.length() < 6) {
            return false;
        }

        return true;
    }

}