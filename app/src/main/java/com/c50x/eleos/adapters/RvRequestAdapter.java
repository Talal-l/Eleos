package com.c50x.eleos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.data.GameRequest;
import com.c50x.eleos.data.Request;
import com.c50x.eleos.data.TeamRequest;

import java.util.ArrayList;


public class RvRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int GAME_REQUEST = 1;
    private static final int TEAM_REQUEST = 2;

    private Context mContext;
    private ArrayList<Request> modelList;

    private OnItemClickListener mItemClickListener;
    private OnRequestResponseListener requestResponseListener;


    public RvRequestAdapter(Context context, ArrayList<Request> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<Request> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof TeamRequest)
            return TEAM_REQUEST;
        else if (getItem(position) instanceof GameRequest)
            return GAME_REQUEST;

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = null;
        switch (viewType) {

            case GAME_REQUEST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_game_card, viewGroup, false);
                return new GameRequestVH(view);

            case TEAM_REQUEST:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_team_card, viewGroup, false);
                return new TeamRequestVH(view);
            case 0:
                Log.i("viewHolderCreate", "type invalid");
        }

        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        switch (holder.getItemViewType()) {
            case GAME_REQUEST:
                final GameRequest gameModel = (GameRequest) getItem(position);

                GameRequestVH gameRequestVH = (GameRequestVH) holder;

                gameRequestVH.tvRequestGameCardTeam.setText(gameModel.getTeamName());
                gameRequestVH.tvRequestGameCardAdmin.setText(gameModel.getSender());
                gameRequestVH.tvRequestGameCardChallenged.setText(gameModel.getReceiver());
                gameRequestVH.tvRequestGameCardDateTime.setText(gameModel.getDateTime());
                gameRequestVH.tvRequestGameCardVenue.setText(gameModel.getVenue());

                break;
            case TEAM_REQUEST:
                final TeamRequest teamModel = (TeamRequest) getItem(position);

                TeamRequestVH teamRequestVH = (TeamRequestVH) holder;

                teamRequestVH.tvRequestTeamCardTeam.setText(teamModel.getTeamName());
                teamRequestVH.tvRequestTeamCardAdmin.setText(teamModel.getTeamAdmin());

                break;
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void setOnRequestResponseListener(final OnRequestResponseListener requestResponseListener) {
        this.requestResponseListener = requestResponseListener;
    }

    private Request getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, Request model);
    }

    public interface OnRequestResponseListener {
        void onRequestAcceptListener(View view, int position, Request model);

        void onRequestDeclineListener(View view, int position, Request model);
    }

    public class GameRequestVH extends RecyclerView.ViewHolder {

        private Button btnRequestAccept;
        private Button btnRequestDecline;
        private TextView tvRequestGameCardTitle;
        private TextView tvRequestGameCardTeam;
        private TextView tvRequestGameCardAdmin;
        private TextView tvRequestGameCardChallenged;
        private TextView tvRequestGameCardDateTime;
        private TextView tvRequestGameCardVenue;

        public GameRequestVH(final View itemView) {
            super(itemView);

            this.btnRequestAccept = itemView.findViewById(R.id.btn_request_game_card_accept);
            this.btnRequestDecline = itemView.findViewById(R.id.btn_request_game_card_decline);

            this.tvRequestGameCardTitle = itemView.findViewById(R.id.tv_request_game_card_title);
            this.tvRequestGameCardAdmin = itemView.findViewById(R.id.tv_request_game_card_admin);
            this.tvRequestGameCardTeam = itemView.findViewById(R.id.tv_request_game_card_team);
            this.tvRequestGameCardChallenged = itemView.findViewById(R.id.tv_request_game_card_challenged);
            this.tvRequestGameCardDateTime = itemView.findViewById(R.id.tv_request_game_card_dateTime);
            this.tvRequestGameCardVenue = itemView.findViewById(R.id.tv_request_game_card_venue);

            // setup the listeners

            btnRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestResponseListener.onRequestAcceptListener(itemView, getAdapterPosition(),
                            modelList.get(getAdapterPosition()));

                }
            });

            btnRequestDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestResponseListener.onRequestDeclineListener(itemView, getAdapterPosition(),
                            modelList.get(getAdapterPosition()));
                }
            });
        }
    }

public class TeamRequestVH extends RecyclerView.ViewHolder {

    private Button btnRequestAccept;
    private Button btnRequestDecline;

    private TextView tvRequestTeamCardTitle;
    private TextView tvRequestTeamCardTeam;
    private TextView tvRequestTeamCardAdmin;

    public TeamRequestVH(final View itemView) {
        super(itemView);

        this.btnRequestAccept = itemView.findViewById(R.id.btn_request_team_card_accept);
        this.btnRequestDecline = itemView.findViewById(R.id.btn_request_team_card_decline);

        this.tvRequestTeamCardTitle = itemView.findViewById(R.id.tv_request_team_card_title);
        this.tvRequestTeamCardTeam = itemView.findViewById(R.id.tv_request_team_card_team);
        this.tvRequestTeamCardAdmin = itemView.findViewById(R.id.tv_request_team_card_admin);


        // setup the listeners

        btnRequestAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestResponseListener.onRequestAcceptListener(itemView, getAdapterPosition(),
                        modelList.get(getAdapterPosition()));

            }
        });

        btnRequestDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestResponseListener.onRequestDeclineListener(itemView, getAdapterPosition(),
                        modelList.get(getAdapterPosition()));
            }
        });
    }

}
}

