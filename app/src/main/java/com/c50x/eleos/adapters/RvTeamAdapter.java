package com.c50x.eleos.adapters;

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
import com.c50x.eleos.models.RvTeamModel;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RvTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<RvTeamModel> modelList;
    private RvTeamModel selectedModel;
    private  int currentSelection = -1;
    private  int prevSelection = -1;





    private OnItemClickListener mItemClickListener;


    public RvTeamAdapter(Context context, ArrayList<RvTeamModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;

    }

    public void updateList(ArrayList<RvTeamModel> modelList) {
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
            final RvTeamModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtTitle.setText(model.getTitle());
            genericViewHolder.itemTxtMessage.setText(model.getMessage());


            if (currentSelection == position){
                holder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.DARKEN);
                selectedModel = model;
                Log.i("TeamAdapter", "current selection: " + currentSelection);
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
    public void resetSelection(){
        currentSelection = -1;
        selectedModel = null;
    }
    public int getCurrentSelectionPosition(){
        return currentSelection;
    }
    public RvTeamModel getSelection(){

        return selectedModel;
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private RvTeamModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, RvTeamModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private RvTeamModel lastChecked;


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

                        currentSelection = getAdapterPosition();
                        if (currentSelection == prevSelection)
                            resetSelection();
                        prevSelection = currentSelection;

                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                    notifyDataSetChanged();

                }
            });

        }
    }

}

