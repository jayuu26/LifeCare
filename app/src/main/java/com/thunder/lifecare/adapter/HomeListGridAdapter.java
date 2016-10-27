package com.thunder.lifecare.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thunder.lifecare.R;
import com.thunder.lifecare.GreenDao.daomodel.HomeCategory;

import java.util.ArrayList;

/**
 * Created by ist on 9/10/16.
 */

public class HomeListGridAdapter extends RecyclerView.Adapter<HomeListGridAdapter.RecyclerViewHolders> {

    private OnClickListner listner;
//    String[] homeOptionLMenu;
    TypedArray icons, selected_icons;
    private Context mContext;
    private ArrayList<HomeCategory> homeCategories;


    public HomeListGridAdapter(Context context, OnClickListner onClickListner, ArrayList<HomeCategory> homeCategories) {
        this.mContext = context;
        listner = onClickListner;
        this.homeCategories = homeCategories;
        icons = mContext.getResources().obtainTypedArray(R.array.home_icon);
        selected_icons = mContext.getResources().obtainTypedArray(R.array.home_sel_icon);
//        homeOptionLMenu = mContext.getResources().getStringArray(R.array.home_options);
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.optName.setText(homeCategories.get(position).getName());
        if(previousPosition == position){
            holder.optPhoto.setImageResource(selected_icons.getResourceId(position, -1));
        }else{
            holder.optPhoto.setImageResource(icons.getResourceId(position, -1));
        }

    }

    @Override
    public int getItemCount() {
        return homeCategories.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {

        public TextView optName;
        public ImageView optPhoto;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
           // itemView.setOnTouchListener(this);

            optName = (TextView) itemView.findViewById(R.id.opt_name);
            optPhoto = (ImageView) itemView.findViewById(R.id.opt_photo);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            optPhoto.setImageResource(selected_icons.getResourceId(getPosition(), -1));
            return false;
        }

        @Override
        public void onClick(View view) {
            previousPosition = getPosition();
            notifyDataSetChanged();
            listner.onClick(getPosition());
        }
    }

    public interface OnClickListner {
        void onClick(int position);
    }

    int previousPosition = -1;

}