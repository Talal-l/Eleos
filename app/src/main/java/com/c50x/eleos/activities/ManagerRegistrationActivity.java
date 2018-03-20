package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.controllers.RegisterTask;
import com.c50x.eleos.data.User;

import java.util.Objects;


public class ManagerRegistrationActivity extends AppCompatActivity implements AsyncResponse {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private Button   confirmButton;
    private Button   cancelButton;
    private User newUser;

    private final static int ERROR_HANDLE_TAKEN = 1;
    private final static int ERROR_EMAIL_TAKEN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        setContentView(R.layout.activity_manager_registration);

        //Assignment statements for fields and buttons

        name = findViewById(R.id.name);
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
                else
                    {

                    // Save user info in a User object so it can be saved in database

                    newUser = new User();
                    newUser.setName(name.getText().toString());
                    newUser.setEmail(email.getText().toString());
                    newUser.setPassword(password.getText().toString());
                    newUser.setGender("male");
                    newUser.setDateOfBirth("10/2/2019");
                    newUser.setHandle("manager");
                    newUser.setManager(true);



                    // Start the Async process that will save into the database
                    RegisterTask regTask = new RegisterTask(ManagerRegistrationActivity.this);
                    regTask.addUser(newUser);
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

    @Override
    public void taskFinished(String output) {

        Log.i("ManagerRegActivity","output: " + output);
        LoginTask loginTask = new LoginTask(this);
        if(!output.contains("null") && !output.contains("error")) {
            loginTask.setToken(output);

            Intent intent = new Intent(ManagerRegistrationActivity.this, LoadingActivity.class);

            // prevent back button from coming back to this screen
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        }
        else if (output.contains("error")){
                // Auth failed because of invalid input

                if (output.contains("email")){

                    email.setError("Email exist");
                }
        }
        else{
              // something is wrong
                    Log.i("RegActivity","Something is wrong with server response "+ "\n" +
                            "response from server: " + output);
        }
    }
}
