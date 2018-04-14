package com.c50x.eleos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.controllers.LoginTask;
import com.c50x.eleos.data.Game;

import java.util.ArrayList;

import static com.c50x.eleos.data.Request.ACCEPTED;
import static com.c50x.eleos.data.Request.CANCELED;
import static com.c50x.eleos.data.Request.DECLINED;
import static com.c50x.eleos.data.Request.PENDING;
import static com.c50x.eleos.data.Request.WAITING;


public class RvGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Game> modelList;

    private OnItemClickListener mItemClickListener;
    private OnLocationClickListener locationClickListener;
    private OnJoinClickListener joinClickListener;


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

        if (holder instanceof ViewHolder) {
            final Game model = getItem(position);
            ViewHolder gameViewHolder = (ViewHolder) holder;

            gameViewHolder.btn_game_card_join.setEnabled(false);
            switch (model.getState()) {
                case PENDING:
                    gameViewHolder.tv_game_card_state.setText("Pending");
                    break;
                case ACCEPTED:
                    gameViewHolder.tv_game_card_state.setText("Accepted");
                    break;
                case WAITING:
                    gameViewHolder.tv_game_card_state.setText("Waiting");
                    gameViewHolder.btn_game_card_join.setEnabled(true);


                    break;
                case DECLINED:
                    gameViewHolder.tv_game_card_state.setText("Declined");
                    gameViewHolder.btn_game_card_join.setEnabled(true);
                    break;

                case CANCELED:

                    gameViewHolder.tv_game_card_state.setText("Canceled");
                    break;
            }

            if (model.getGameAdmin().equals(LoginTask.currentAuthUser.getHandle()))
                gameViewHolder.btn_game_card_join.setEnabled(false);

            gameViewHolder.tv_game_card_dateTime.setText(model.getDateTime());
            gameViewHolder.tv_game_card_venue.setText(model.getVenue().getVenueName());
            gameViewHolder.tv_game_card_team1.setText(model.getTeam1());
            gameViewHolder.tv_game_card_team2.setText(model.getTeam2());

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private Game getItem(int position) {
        return modelList.get(position);
    }

    public void setOnLocationClickListener(final OnLocationClickListener locationClickListener) {
        this.locationClickListener = locationClickListener;
    }

    public void setOnJoinClickListener(final OnJoinClickListener joinClickListener) {
        this.joinClickListener = joinClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, Game model);
    }


    public interface OnLocationClickListener {
        void onLocationClick(View view, int position, Game model);
    }

    public interface OnJoinClickListener {
        void onJoinClick(View view, int position, Game model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_game_card_dateTime;
        private TextView tv_game_card_venue;
        private TextView tv_game_card_team1;
        private TextView tv_game_card_team2;
        private TextView tv_game_card_state;
        private Button btn_game_card_join;


        public ViewHolder(final View itemView) {
            super(itemView);

            this.tv_game_card_dateTime = itemView.findViewById(R.id.tv_game_card_dateTime);
            this.tv_game_card_venue = itemView.findViewById(R.id.tv_game_card_venue);
            this.tv_game_card_team1 = itemView.findViewById(R.id.tv_game_card_team1);
            this.tv_game_card_team2 = itemView.findViewById(R.id.tv_game_card_team2);
            this.tv_game_card_state = itemView.findViewById(R.id.tv_game_card_state);
            this.btn_game_card_join = itemView.findViewById(R.id.btn_game_card_join);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });


            tv_game_card_venue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationClickListener.onLocationClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });

            btn_game_card_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    joinClickListener.onJoinClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }

}

