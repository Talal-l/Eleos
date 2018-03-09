package com.c50x.eleos.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.c50x.eleos.R;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RecyclerViewTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<TeamModel> modelList;

    private static int currentSelection = -1;





    private OnItemClickListener mItemClickListener;


    public RecyclerViewTeamAdapter(Context context, ArrayList<TeamModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;

    }

    public void updateList(ArrayList<TeamModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_recycler_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final TeamModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtTitle.setText(model.getTitle());
            genericViewHolder.itemTxtMessage.setText(model.getMessage());


            if (currentSelection == position){
                holder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.DARKEN);
                Log.i("TeamAdapter", "pos: " + position +"adapter pos: " + holder.getAdapterPosition() + "  current pos " + currentSelection);
                Log.i("TeamAdapter", "title: " + ((ViewHolder) holder).itemTxtTitle.getText().toString());
            }
            else if (currentSelection != position){

                holder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.LIGHTEN);
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

    private TeamModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, TeamModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private TeamModel lastChecked;


        // @BindView(R.id.img_user)
        // ImageView imgUser;
        // @BindView(R.id.item_txt_title)
        // TextView itemTxtTitle;
        // @BindView(R.id.item_txt_message)
        // TextView itemTxtMessage;
        // @BindView(R.id.radio_list)
        // RadioButton itemTxtMessage;
        // @BindView(R.id.check_list)
        // CheckBox itemCheckList;
        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                        currentSelection = getAdapterPosition();


                    notifyDataSetChanged();
                }
            });

        }
    }

}

