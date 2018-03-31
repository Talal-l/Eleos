package com.c50x.eleos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.c50x.eleos.R;
import com.c50x.eleos.models.RvRequestModel;

import java.util.ArrayList;


public class RvRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<RvRequestModel> modelList;

    private OnItemClickListener mItemClickListener;
    private OnRequestResponseListener requestResponseListener;


    public RvRequestAdapter(Context context, ArrayList<RvRequestModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<RvRequestModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.request_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final RvRequestModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.itemTxtMessage.setText(model.getMessage());


        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public void setOnRequestResponseListener(final OnRequestResponseListener requestResponseListener){
        this.requestResponseListener = requestResponseListener;
    }

    private RvRequestModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, RvRequestModel model);
    }

    public interface OnRequestResponseListener {
        void onRequestAcceptListener(View view, int position, RvRequestModel model);
        void onRequestDeclineListener(View view, int position, RvRequestModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtMessage;
        private Button btnRequestAccept;
        private Button btnRequestDecline;


        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.itemTxtMessage = (TextView) itemView.findViewById(R.id.tv_request_message);
            this.btnRequestAccept = itemView.findViewById(R.id.btn_request_accept);
            this.btnRequestDecline = itemView.findViewById(R.id.btn_request_decline);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

            btnRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestResponseListener.onRequestAcceptListener(itemView,getAdapterPosition(),
                            modelList.get(getAdapterPosition()));

                }
            });

            btnRequestDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestResponseListener.onRequestDeclineListener(itemView,getAdapterPosition(),
                            modelList.get(getAdapterPosition()));
                }
            });
        }

    }

}

