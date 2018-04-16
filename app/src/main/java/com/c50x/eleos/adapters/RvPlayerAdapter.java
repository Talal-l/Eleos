package com.c50x.eleos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.data.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.c50x.eleos.data.Request.ACCEPTED;
import static com.c50x.eleos.data.Request.ADMIN;
import static com.c50x.eleos.data.Request.CANCELED;
import static com.c50x.eleos.data.Request.DECLINED;
import static com.c50x.eleos.data.Request.PENDING;


public class RvPlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<User> modelList;

    private OnItemClickListener mItemClickListener;

    private OnCheckedListener mOnCheckedListener;

    private ViewHolder playerViewHolder;
    private boolean isChecked;

    private Set<Integer> checkSet = new HashSet<>();
    private boolean checkTeamState;

    public RvPlayerAdapter(Context context, ArrayList<User> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<User> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewHolder) {
            final User model = getItem(position);
            playerViewHolder = (ViewHolder) holder;

            playerViewHolder.tv_player_card_name.setText(model.getName());
            playerViewHolder.tv_player_card_handel.setText(model.getHandle());
            playerViewHolder.tv_player_card_team.setText(model.getTeam());
            playerViewHolder.getTv_player_card_teamState.setText("");

            if (getItem(position).isSelected())
                playerViewHolder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.DARKEN);

            //in some cases, it will prevent unwanted situations
            playerViewHolder.chk_player_card_selection.setOnCheckedChangeListener(null);

            ((ViewHolder) holder).chk_player_card_selection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!checkSet.contains(position)) {
                        checkSet.add(position);
                    } else {
                        checkSet.remove(position);
                    }

                    if (mOnCheckedListener != null){

                        mOnCheckedListener.onChecked(playerViewHolder.chk_player_card_selection, checkSet.contains(position), position, model);
                        notifyDataSetChanged();
                    }

                }
            });

            //if true, your checkbox will be selected, else unselected
            playerViewHolder.chk_player_card_selection.setChecked(checkSet.contains(position));


            playerViewHolder.chk_player_card_selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });


            if (checkTeamState){
                String state = "";
                switch (model.getTeamState()){
                    case PENDING:
                        state = "Pending";
                        break;
                    case ACCEPTED:
                        state = "Accepted";
                        break;
                    case DECLINED:
                        state = "Declined";
                        break;
                    case CANCELED:
                        state = "Canceled";
                        break;
                    case ADMIN:
                        state = "Admin";
                        break;
                }

                playerViewHolder.getTv_player_card_teamState.setText(state);

            }
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public void checkState(boolean check){

        this.checkTeamState = check;


    }

    public void SetOnCheckedListener(final OnCheckedListener onCheckedListener) {
        this.mOnCheckedListener = onCheckedListener;

    }

    private User getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, User model);
    }


    public interface OnCheckedListener {
        void onChecked(View view, boolean isChecked, int position, User model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_player_card_profile_image;
        private TextView tv_player_card_name;
        private TextView tv_player_card_handel;
        private TextView tv_player_card_team;
        private TextView getTv_player_card_teamState;
        private CheckBox chk_player_card_selection;

        public ViewHolder(final View itemView) {
            super(itemView);


            this.img_player_card_profile_image = itemView.findViewById(R.id.img_player_card_profile_image);
            this.tv_player_card_name = itemView.findViewById(R.id.tv_player_card_name);
            this.tv_player_card_handel = itemView.findViewById(R.id.tv_player_card_handel);
            this.tv_player_card_team = itemView.findViewById(R.id.tv_player_card_team);
            this.getTv_player_card_teamState = itemView.findViewById(R.id.tv_player_card_teamSate);
            this.chk_player_card_selection = (CheckBox) itemView.findViewById(R.id.chk_player_card_selection);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });

        }
    }
}

