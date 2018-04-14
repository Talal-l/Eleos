package com.c50x.eleos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.data.Venue;

import java.util.ArrayList;


public class RvVenueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Venue> modelList;
    private Venue selectedModel;
    private int currentSelection = -1;
    private int prevSelection = -1;


    private OnItemClickListener mItemClickListener;


    public RvVenueAdapter(Context context, ArrayList<Venue> modelList) {
        this.mContext = context;
        this.modelList = modelList;

    }

    public void updateList(ArrayList<Venue> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.venue_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final Venue model = getItem(position);
            ViewHolder venueViewHolder = (ViewHolder) holder;

            venueViewHolder.tv_venue_card_name.setText(model.getVenueName());
            venueViewHolder.tv_venue_card_local.setText(model.getVenueAddress());


            if (currentSelection == position) {
                holder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.DARKEN);
                selectedModel = model;
                Log.i("TeamAdapter", "current selection: " + currentSelection);
            } else if (currentSelection != position) {
                holder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.LIGHTEN);
            }


        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void resetSelection() {
        currentSelection = -1;
        selectedModel = null;
    }

    public int getCurrentSelectionPosition() {
        return currentSelection;
    }

    public Venue getSelection() {

        return selectedModel;
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private Venue getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, Venue model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_venue_card_name;
        private TextView tv_venue_card_local;

        public ViewHolder(final View itemView) {
            super(itemView);


            this.tv_venue_card_name = itemView.findViewById(R.id.tv_venue_card_name);
            this.tv_venue_card_local = itemView.findViewById(R.id.tv_venue_card_local);


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

