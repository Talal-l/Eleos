package com.c50x.eleos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.c50x.eleos.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    // UI references.
    private AutoCompleteTextView EmailView;
    private EditText PasswordView;
    private View LoginFormView;
    private Button sign_in_button;
    private Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        EmailView = (AutoCompleteTextView) findViewById(R.id.email);
        PasswordView = (EditText) findViewById(R.id.password);
        sign_in_button = (Button) findViewById(R.id.signIn_button);
        register_button = (Button) findViewById(R.id.Register_button);
        LoginFormView = (ScrollView) findViewById(R.id.login_form);


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
        sign_in_button.setOnClickListener(new View.OnClickListener() // handles button click
        {
            @Override
            public void onClick(View v) // checks if user input was correct by verifying email and password
            {
                if (!validateEmail(EmailView.getText().toString())) // if not a valid email address
                {
                    EmailView.setError("Invalid Email");
                    EmailView.requestFocus();
                }
                else if (!validatePassword(PasswordView.getText().toString()))// if not a valid email address)
                {
                    PasswordView.setError("Invalid Password");
                    PasswordView.requestFocus();
                }
                else
                    Toast.makeText(LoginActivity.this, "LogIn successful !", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected boolean validateEmail(String strEmail) // checks if user entered email into the email textfield
    {
        if (strEmail != null) // if user entered email
            return true;
        else // if did not enter email
            return false;
    }

    protected boolean validatePassword(String strpass) //
    {
        if (strpass != null && strpass.length() > 6) // if user entered password that is 6 bytes long
            return true;
        else // if he did not
            return false;
    }
}


