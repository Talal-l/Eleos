package com.c50x.eleos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Game;

import java.util.ArrayList;

import static com.c50x.eleos.data.Request.ACCEPTED;
import static com.c50x.eleos.data.Request.PENDING;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RvGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Game> modelList;

    private OnItemClickListener mItemClickListener;


    public RvGameAdapter(Context context, ArrayList<Game> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<Game> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final Game model = getItem(position);
            ViewHolder gameViewHolder = (ViewHolder) holder;

            if (model.getState() == PENDING)
                gameViewHolder.tv_game_card_state.setText("Pending");
            else if (model.getState() == ACCEPTED)
                gameViewHolder.tv_game_card_state.setText("ACCEPTED");
            else
                gameViewHolder.tv_game_card_state.setText("Canceled");


            gameViewHolder.tv_game_card_dateTime.setText(model.getDateTime());
            gameViewHolder.tv_game_card_venue.setText(model.getVenueAddress());
            gameViewHolder.tv_game_card_team1.setText(model.getTeam1());
            gameViewHolder.tv_game_card_team2.setText(model.getTeam2());

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private Game getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, Game model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_game_card_dateTime;
        private TextView tv_game_card_venue;
        private TextView tv_game_card_team1;
        private TextView tv_game_card_team2;
        private TextView tv_game_card_state;


        public ViewHolder(final View itemView) {
            super(itemView);

            this.tv_game_card_dateTime = itemView.findViewById(R.id.tv_game_card_dateTime);
            this.tv_game_card_venue = itemView.findViewById(R.id.tv_game_card_venue);
            this.tv_game_card_team1 = itemView.findViewById(R.id.tv_game_card_team1);
            this.tv_game_card_team2 = itemView.findViewById(R.id.tv_game_card_team2);
            this.tv_game_card_state = itemView.findViewById(R.id.tv_game_card_state);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

        }
    }

}

