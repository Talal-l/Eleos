package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.RegisterTask;
import com.c50x.eleos.data.AppDatabase;
import com.c50x.eleos.data.User;

import java.util.Objects;

/**
 * Created by eris on 11/19/17.
 */

public class RegistrationActivity extends AppCompatActivity  {

    private EditText name;
    private EditText handle;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private Button   confirmButton;
    private Button   cancelButton;
    private User newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(com.c50x.eleos.R.layout.activity_registration);

        //Assignment statements for fields and buttons

        name = findViewById(R.id.name);
        handle = findViewById(R.id.handle);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        // TODO: Take input from spinners
        // TODO: Take date of birth

        confirmButton = findViewById(R.id.button_confirm);
        cancelButton = findViewById(R.id.button_cancel);



        // Validate user input when the confirm button is clicked
        // TODO: Better error messages
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nameIsValid(name.getText().toString())) {
                    name.setError("Empty or Incorrect Length (Between 2 and 10 characters)");
                }

                else if (!handleIsValid(handle.getText().toString())) {
                    handle.setError("Empty or Incorrect Length (Between 4 and 10 characters)");
                }

                else if (!emailIsValid(email.getText().toString())) {
                    email.setError("Empty");
                }

                else if (!passwordIsValid(password.getText().toString())) {
                    password.setError("Empty or Incorrect Length (Between 6 and 16 characters)");
                }

                else if (!passwordIsValid(confirm_password.getText().toString())) {
                    confirm_password.setError("Empty or Incorrect Length (Between 6 and 16 characters)");
                }

                else if (!Objects.equals(password.getText().toString(), confirm_password.getText().toString())) {
                    confirm_password.setError("Passwords do not match");
                }
                // All inputs are valid
                else {

                    // Save user info in a User object so it can be saved in database

                    newUser = new User();
                    newUser.setHandle(handle.getText().toString());
                    newUser.setName(name.getText().toString());
                    newUser.setEmail(email.getText().toString());
                    newUser.setPassword(password.getText().toString());



                    // Start the Async process that will save into the database
                    RegisterTask registerTask = new RegisterTask(getApplicationContext(),
                            RegistrationActivity.this);
                    registerTask.execute(newUser);
                }
            }
        });


        // Cancel button takes you back to Login Page

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    // Methods for checking whether the format for the inputs are valid

    public boolean nameIsValid(String name) {

        if(name.isEmpty() || !(name.length() >= 2 && name.length() <= 10)) {
            return false;
        } else
            return true;
    }

    public boolean handleIsValid(String handle) {

        if(handle.isEmpty() || !(handle.length() >= 4 && handle.length() <= 10)) {
            return false;
        } else
            return true;
    }

    public boolean emailIsValid(String email) {

        if(email.isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean passwordIsValid(String password) {

        if(password.isEmpty() || !(password.length() >= 6 && password.length() <= 16)) {
            return false;
        } else
            return true;
    }

}