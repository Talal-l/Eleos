package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.AsyncResponse;
import com.c50x.eleos.controllers.LoginTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    public static final int EXPECTED_MIN_RESPONSE_LENGTH = 6;
    private static final String TAG = "LoginActivity";
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private Button sign_in_button;
    private Button register_button;
    private String userEmail;
    private String userPassword;
    private LoginTask loginTask;
    private Button btnRegisterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_login);


        // init variables
        loginTask = new LoginTask(this);


        // set up the login form.
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        sign_in_button = findViewById(R.id.signIn_button);
        register_button = findViewById(R.id.Register_button);
        btnRegisterManager = findViewById(R.id.btn_register_manager);

        sign_in_button.setOnClickListener(new View.OnClickListener() // handles sign in button click
        {
            @Override
            public void onClick(View v) // checks if user input was correct by verifying email and password
            {
                userEmail = emailView.getText().toString();
                userPassword = passwordView.getText().toString();

                if (!validateEmail(userEmail)) // if not a valid email address
                {
                    emailView.setError("Invalid Email");
                    emailView.requestFocus();
                } else if (!validatePassword(userPassword))// if not a valid password
                {
                    passwordView.setError("Invalid Password");
                    passwordView.requestFocus();
                } else { // syntax is correct for password and email

                    Log.i(TAG, "user email: " + userEmail + " user password: " + userPassword);
                    // Check if user has an account (in database)
                    loginTask.authUsingEmail(userEmail, userPassword);
                }
            }
        });


        register_button.setOnClickListener(new View.OnClickListener() // handles register button click
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ManagerRegistrationActivity.class);
                startActivity(intent);


            }
        });
    }

    protected boolean validateEmail(String strEmail) // grants the user access if the email is matched from the database
    {
        String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + // android default pattern

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    protected boolean validatePassword(String strpass) // return true if password is valid and false is invalid
    {
        if (strpass != null && strpass.length() > 6) // if user entered password that is 6 bytes long
            return true;
        else // if he did not
            return false;
    }

    @Override
    public void taskFinished(String output) {
        Log.i(TAG, "login server response: " + output);

        // handle login
        if (!output.contains("null") && !output.contains("error")) { // user is valid
            loginTask.setToken(output);

            Intent intent = new Intent(LoginActivity.this, LoadingActivity.class);

            // prevent back button from returning to this activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

            finish();
            startActivity(intent);

        } else {
            // auth failed
            passwordView.setError("Incorrect email or password");
            passwordView.requestFocus();

        }
    }
}

// managers
