package com.c50x.eleos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import com.c50x.eleos.R;
import com.c50x.eleos.adapters.RvTeamAdapter;
import com.c50x.eleos.models.RvTeamModel;

import android.widget.Toast;


public class TeamSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;


    private RvTeamAdapter mAdapter;

    private ArrayList<RvTeamModel> modelList = new ArrayList<>();
    private Menu mnu_team_select;
    private MenuItem mnut_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);





        // ButterKnife.bind(this);
        findViews();
        setAdapter();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // add menu actions to toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        mnu_team_select = menu;
        mnut_done = mnu_team_select.findItem(R.id.mnut_done);

        // hide done when nothing is selected
        mnut_done.setVisible(false);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        mAdapter.resetSelection();
        finish();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp:
                // User chose the "Settings" item, show the app settings UI...

            case R.id.mnut_done:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }







    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_team_list);
    }


    private void setAdapter() {


        modelList.add(new RvTeamModel("Android", "Hello " + " Android"));
        modelList.add(new RvTeamModel("Beta", "Hello " + " Beta"));
        modelList.add(new RvTeamModel("Cupcake", "Hello " + " Cupcake"));
        modelList.add(new RvTeamModel("Donut", "Hello " + " Donut"));
        modelList.add(new RvTeamModel("Eclair", "Hello " + " Eclair"));
        modelList.add(new RvTeamModel("Froyo", "Hello " + " Froyo"));
        modelList.add(new RvTeamModel("Gingerbread", "Hello " + " Gingerbread"));
        modelList.add(new RvTeamModel("Honeycomb", "Hello " + " Honeycomb"));
        modelList.add(new RvTeamModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
        modelList.add(new RvTeamModel("Jelly Bean", "Hello " + " Jelly Bean"));
        modelList.add(new RvTeamModel("KitKat", "Hello " + " KitKat"));
        modelList.add(new RvTeamModel("Lollipop", "Hello " + " Lollipop"));
        modelList.add(new RvTeamModel("Marshmallow", "Hello " + " Marshmallow"));
        modelList.add(new RvTeamModel("Nougat", "Hello " + " Nougat"));
        modelList.add(new RvTeamModel("Android O", "Hello " + " Android O"));


        mAdapter = new RvTeamAdapter(TeamSelectionActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(TeamSelectionActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RvTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, RvTeamModel model) {

                //handle item click events here

                // show done action when an item is selected
                if (mAdapter.getCurrentSelectionPosition() > -1)
                    mnut_done.setVisible(true);
                else
                    mnut_done.setVisible(false);

                Log.i("teamActivity","pos: " + mAdapter.getCurrentSelectionPosition());

                Toast.makeText(TeamSelectionActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();



            }
        });


    }


}
