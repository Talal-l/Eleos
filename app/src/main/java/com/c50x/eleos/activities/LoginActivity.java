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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements AsyncResponse
{
    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View loginFormView;
    private Button sign_in_button;
    private Button register_button;
    private String userEmail;
    private String userPassword;
    private LoginTask loginTask;


    public static final int EXPECTED_MIN_RESPONSE_LENGTH = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_login);

        // Set up the login form.
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        sign_in_button = findViewById(R.id.signIn_button);
        register_button = findViewById(R.id.Register_button);

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
                }
                else if (!validatePassword(userPassword))// if not a valid password
                {
                    passwordView.setError("Invalid Password");
                    passwordView.requestFocus();
                }

                else{ // syntax is correct for password and email

                    // Check if user has an account (in database)

                    // pass the activityContext so we can change UI elements from LoginTask
                    Log.i("LoginActivity_signIn","email: " + userEmail + " password: " + userPassword);
                    LoginTask login = new LoginTask(LoginActivity.this);
                    login.authUsingEmail(userEmail,userPassword);


                }
            }
        });

        Button playersearchbutton = findViewById(R.id.PlayerSearch_button);

        playersearchbutton.setOnClickListener(new View.OnClickListener() // handles sign in button click
        {
            @Override
            public void onClick(View view) // checks if user input was correct by verifying email and password
            {
                Intent intent = new Intent(view.getContext(), PlayerSearchActivity.class);
                startActivity(intent);

            }
        });


        register_button.setOnClickListener(new View.OnClickListener() // handles register button click
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
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
        // handle login
        loginTask = new LoginTask(this);
        if (!output.contains("null")) { // user is valid
            loginTask.setToken(output);

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            // prevent back button from returning to this activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            finish();

            startActivity(intent);
        }
        else {
                // Auth failed
                passwordView.setError("Incorrect email or password");
                passwordView.requestFocus();

        }




        // output should include user info and token that can be used to retrieve data from server








    }
}


