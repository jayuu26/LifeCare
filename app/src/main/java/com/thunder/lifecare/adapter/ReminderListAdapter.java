package com.thunder.lifecare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;

import java.util.List;


/**
 * Created by ist-150 on 1/7/16.
 */
public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.CustomViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private Long inventoryId;
    private ReminderActionListener actionListener;

    public ReminderListAdapter(Context context, List<Category> categoryList, ReminderActionListener reminderActionListener) {
        this.categoryList = categoryList;
        this.context = context;
        this.actionListener = reminderActionListener;
    }

    @Override
    public ReminderListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View issueRecyclerAdapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(issueRecyclerAdapterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReminderListAdapter.CustomViewHolder holder, int position) {
        final Category category = categoryList.get(position);
        holder.txtMedicineName.setText(category.getCategoryName());
        holder.txtMedicineDose.setText("");
        holder.txtMedicineImg.setImageResource(category.getCategoryImage());
//        holder.issueRowItem.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != categoryList ? categoryList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView txtMedicineName, txtMedicineDose;
        public ImageView txtMedicineImg;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.txtMedicineName = (TextView) view.findViewById(R.id.medicine_name);
            this.txtMedicineDose = (TextView) view.findViewById(R.id.medicine_dose);
            this.txtMedicineImg = (ImageView) view.findViewById(R.id.medi_img);
//            this.issueRowItem = (LinearLayout) view.findViewById(R.id.issueRowItem);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Clicked category Position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            int position = (Integer) v.getTag();
            Category category = categoryList.get(getPosition());
           actionListener.OnReminderClick(category);
        }
    }

    public interface ReminderActionListener{
        public void OnReminderClick(Category category);
    }
}
