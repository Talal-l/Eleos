package com.c50x.eleos.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.c50x.eleos.R;
import com.c50x.eleos.utilities.Utilities;

public class EditPlayerInfoActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private EditText etUserName;
    private EditText etUserEmail;
    private EditText etUserDateOfBirth;
    private EditText etUserGender;
    private ImageView imgvUserImage;
    private Menu mnuUserProfile;
    private MenuItem mnutDone;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player_info);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity
        imgvUserImage = (ImageView) findViewById(R.id.img_user_edit);

        etUserName = findViewById(R.id.et_player_name);
        etUserEmail = findViewById(R.id.et_player_email);
        etUserDateOfBirth = findViewById(R.id.et_player_dob);
        etUserGender = findViewById(R.id.et_player_gender);

        etUserName.setEnabled(false);
        etUserEmail.setEnabled(false);
        etUserDateOfBirth.setEnabled(false);
        etUserGender.setEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgvUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnut_done: // button pressed

                if (mnutDone.getTitle().equals("Edit")) {

                    mnutDone.setTitle("Save");
                    // enable the EditViews

                    etUserName.setEnabled(true);
                    etUserEmail.setEnabled(true);
                    etUserDateOfBirth.setEnabled(true);
                    etUserGender.setEnabled(true);

                }
                break;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnuUserProfile = menu;
        mnutDone = menu.findItem(R.id.mnut_done);

        mnutDone.setTitle("Edit");
        Utilities.disableViews(etUserName, etUserEmail, etUserDateOfBirth, etUserGender);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public void openGallery() // for changing user image
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)// for changing user image
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imgvUserImage.setImageURI(imageUri);
        }
    }

}
