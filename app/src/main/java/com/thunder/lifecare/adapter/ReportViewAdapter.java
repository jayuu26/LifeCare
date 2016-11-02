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
public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.CustomViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private Long inventoryId;
    private ReportActionListener actionListener;

    public ReportViewAdapter(Context context, List<Category> categoryList, ReportActionListener reportActionListener) {
        this.categoryList = categoryList;
        this.context = context;
        this.actionListener = reportActionListener;
    }

    @Override
    public ReportViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View issueRecyclerAdapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(issueRecyclerAdapterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ReportViewAdapter.CustomViewHolder holder, int position) {
//        final Category category = categoryList.get(position);
//        holder.txtCategoryName.setText(category.getCategoryName());
//        holder.txtCategoryIcon.setImageResource(category.getCategoryImage());
//        holder.issueRowItem.setTag(position);
    }

    @Override
    public int getItemCount() {
        return 10;//(null != categoryList ? categoryList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView txtCategoryName;
        //        private LinearLayout  issueRowItem;
        private ImageView txtCategoryIcon;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.txtCategoryName = (TextView) view.findViewById(R.id.cat_name);
            this.txtCategoryIcon = (ImageView) view.findViewById(R.id.cat_logo);
//            this.issueRowItem = (LinearLayout) view.findViewById(R.id.issueRowItem);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Clicked category Position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            int position = (Integer) v.getTag();
//            Category category = categoryList.get(getPosition());
            actionListener.onReportClick(null);
        }
    }

    public interface ReportActionListener {
        void onReportClick(Category category);
    }
}
