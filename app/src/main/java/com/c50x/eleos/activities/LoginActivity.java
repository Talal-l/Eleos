package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.c50x.eleos.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View loginFormView;
    private Button sign_in_button;
    private Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        setContentView(R.layout.activity_login);

        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        sign_in_button = (Button) findViewById(R.id.signIn_button);
        register_button = (Button) findViewById(R.id.Register_button);


        register_button.setOnClickListener(new View.OnClickListener() // handles register button click
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
        sign_in_button.setOnClickListener(new View.OnClickListener() // handles sign in button click
        {
            @Override
            public void onClick(View v) // checks if user input was correct by verifying email and password
            {
                if (!validateEmail(emailView.getText().toString())) // if not a valid email address
                {
                    emailView.setError("Invalid Email");
                    emailView.requestFocus();
                }
                else if (!validatePassword(passwordView.getText().toString()))// if not a valid password
                {
                    passwordView.setError("Invalid Password");
                    passwordView.requestFocus();
                }
                else // valid email and password
                {
                    Toast.makeText(LoginActivity.this, "LogIn successful !", Toast.LENGTH_LONG).show(); // prints on the screen that log in was successful
                    Intent intent2 = new Intent(v.getContext(), MainActivity.class);
                    intent2.putExtra("from","login"); // To tell what screen we came from
                    // To load the logged in player when they move to the main screen
                    intent2.putExtra("email",emailView.getText().toString());
                    startActivity(intent2); // switch to main activity
                }
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
}


