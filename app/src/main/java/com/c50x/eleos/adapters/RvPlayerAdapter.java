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
import com.c50x.eleos.models.RvPlayerModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RvPlayerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<RvPlayerModel> modelList;

    private OnItemClickListener mItemClickListener;

    private OnCheckedListener mOnCheckedListener;

    private ViewHolder playerViewHolder;
    private boolean isChecked;

    private Set<Integer> checkSet = new HashSet<>();


    public RvPlayerAdapter(Context context, ArrayList<RvPlayerModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<RvPlayerModel> modelList) {
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
            final RvPlayerModel model = getItem(position);
            playerViewHolder = (ViewHolder) holder;

            playerViewHolder.itemTxtTitle.setText(model.getTitle());
            playerViewHolder.itemTxtMessage.setText(model.getMessage());

            if (getItem(position).isChecked())
                playerViewHolder.itemView.getBackground().setColorFilter(Color.parseColor("#00796B"), PorterDuff.Mode.DARKEN);

            //in some cases, it will prevent unwanted situations
            playerViewHolder.itemCheckList.setOnCheckedChangeListener(null);

            ((ViewHolder) holder).itemCheckList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!checkSet.contains(position)) {
                        checkSet.add(position);
                    } else {
                        checkSet.remove(position);
                    }

                    if (mOnCheckedListener != null){

                        mOnCheckedListener.onChecked(playerViewHolder.itemCheckList, checkSet.contains(position), position, model);
                        notifyDataSetChanged();
                    }

                }
            });

            //if true, your checkbox will be selected, else unselected
            playerViewHolder.itemCheckList.setChecked(checkSet.contains(position));


            playerViewHolder.itemCheckList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });


        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnCheckedListener(final OnCheckedListener onCheckedListener) {
        this.mOnCheckedListener = onCheckedListener;

    }

    private RvPlayerModel getItem(int position) {
        return modelList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, RvPlayerModel model);
    }


    public interface OnCheckedListener {
        void onChecked(View view, boolean isChecked, int position, RvPlayerModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;


        private CheckBox itemCheckList;

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


            this.imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            this.itemTxtTitle = (TextView) itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.item_txt_message);
            this.itemCheckList = (CheckBox) itemView.findViewById(R.id.check_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));

                }
            });

        }
    }
}

