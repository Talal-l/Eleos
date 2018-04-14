package com.c50x.eleos.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.c50x.eleos.R;

public class EditPlayerInfoActivity extends AppCompatActivity
{
    private EditText pn;
    private EditText ph;
    private EditText pe;
    private EditText pdob;
    private EditText pg;
    private ImageView user_img;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player_info);

        Button edit = (Button) findViewById(R.id.btn_edit_info);
        user_img = (ImageView) findViewById(R.id.img_user_edit);

        pn = findViewById(R.id.et_player_name);
        ph = findViewById(R.id.et_player_handle);
        pe = findViewById(R.id.et_player_email);
        pdob = findViewById(R.id.et_player_dob);
        pg = findViewById(R.id.et_player_gender);
        cancel = findViewById(R.id.btn_cancel_edit_info);

        pn.setEnabled(false);
        ph.setEnabled(false);
        pe.setEnabled(false);
        pdob.setEnabled(false);
        pg.setEnabled(false);

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pn.setEnabled(true);
                ph.setEnabled(true);
                pe.setEnabled(true);
                pdob.setEnabled(true);
                pg.setEnabled(true);
            }
        });

        user_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });
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
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE)
        {
            imageUri = data.getData();
            user_img.setImageURI(imageUri);
        }
    }

}
