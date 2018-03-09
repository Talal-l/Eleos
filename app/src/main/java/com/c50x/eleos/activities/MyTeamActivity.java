package com.c50x.eleos.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import com.c50x.eleos.R;

import android.widget.Toast;


public class MyTeamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    // @BindView(R.id.recycler_view)
    // RecyclerView recyclerView;


    private RecyclerViewTeamAdapter mAdapter;

    private ArrayList<TeamModel> modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        // ButterKnife.bind(this);
        findViews();
        setAdapter();


    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_team_list);
    }


    private void setAdapter() {


        modelList.add(new TeamModel("Android", "Hello " + " Android"));
        modelList.add(new TeamModel("Beta", "Hello " + " Beta"));
        modelList.add(new TeamModel("Cupcake", "Hello " + " Cupcake"));
        modelList.add(new TeamModel("Donut", "Hello " + " Donut"));
        modelList.add(new TeamModel("Eclair", "Hello " + " Eclair"));
        modelList.add(new TeamModel("Froyo", "Hello " + " Froyo"));
        modelList.add(new TeamModel("Gingerbread", "Hello " + " Gingerbread"));
        modelList.add(new TeamModel("Honeycomb", "Hello " + " Honeycomb"));
        modelList.add(new TeamModel("Ice Cream Sandwich", "Hello " + " Ice Cream Sandwich"));
        modelList.add(new TeamModel("Jelly Bean", "Hello " + " Jelly Bean"));
        modelList.add(new TeamModel("KitKat", "Hello " + " KitKat"));
        modelList.add(new TeamModel("Lollipop", "Hello " + " Lollipop"));
        modelList.add(new TeamModel("Marshmallow", "Hello " + " Marshmallow"));
        modelList.add(new TeamModel("Nougat", "Hello " + " Nougat"));
        modelList.add(new TeamModel("Android O", "Hello " + " Android O"));


        mAdapter = new RecyclerViewTeamAdapter(MyTeamActivity.this, modelList);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(MyTeamActivity.this, R.drawable.divider_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(mAdapter);


        mAdapter.SetOnItemClickListener(new RecyclerViewTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, TeamModel model) {

                //handle item click events here
                Toast.makeText(MyTeamActivity.this, "Hey " + model.getTitle(), Toast.LENGTH_SHORT).show();


            }
        });


    }


}
