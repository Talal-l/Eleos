package com.c50x.eleos.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.TeamTask;
import com.c50x.eleos.data.Team;

public class PlayerSearchActivity extends AppCompatActivity {

    private TeamTask task;
    private Team newTeam;

    private Context activityContext;

    private EditText playerSearch;
    private Button confirmButton;
    private Button cancelButton;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playersearch);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard upon switching to this Activity

        playerSearch = (EditText) findViewById(R.id.player_search);
        confirmButton = (Button) findViewById(R.id.button_confirm);
        cancelButton = (Button) findViewById(R.id.button_cancel);


        confirmButton.setOnClickListener(new View.OnClickListener() {

            String[] androidStrings = getResources().getStringArray(R.array.dummy1);

            @Override
            public void onClick(View v) {

                if (playerSearch.getText().toString().equals("")) {
                    playerSearch.setError("Empty");
                } else {
                    for (int i = 0; i < androidStrings.length; i++) {
                        if (playerSearch.getText().toString().equals((androidStrings[i]))) {
                            flag = 1;
                        }
                        else
                            continue;
                    }
                    if (flag == 0)
                        playerSearch.setError("Player Does Not Exist");

                    else if (flag == 1)
                        playerSearch.setError("Player Exists");
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
