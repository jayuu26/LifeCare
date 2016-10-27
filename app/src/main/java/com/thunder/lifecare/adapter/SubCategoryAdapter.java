package com.thunder.lifecare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunder.lifecare.GreenDao.daomodel.SubCategoryOld;
import com.thunder.lifecare.R;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.RecyclerViewHolders> {

    private Context context;
    private OnClickListner listner;
    private List<SubCategoryOld> itemList;
    private View detailRecyclerAdapterView;

//    public SubCategoryAdapter(Context context) {
//        super(context);
//        setItemsList(Arrays.asList(SubCategory.getAllPaintings(context.getResources())));
//    }
    public SubCategoryAdapter(Context context, List<SubCategoryOld> itemList, OnClickListner onClickListner) {
        this.itemList = itemList;
        this.context = context;
        listner = onClickListner;
    }

//    @Override
//    protected View createView(SubCategory item, int pos, ViewGroup parent, LayoutInflater inflater) {
//        View view = inflater.inflate(R.layout.list_item, parent, false);
//        ViewHolder vh = new ViewHolder();
//        vh.image = view.findViewById(R.id.list_item_image);
//        vh.image.setOnClickListener(this);
//        vh.title = view.findViewById(R.id.list_item_title);
//        view.setTag(vh);
//
//        return view;
//    }

//    @Override
//    protected void bindView(SubCategory item, int pos, View convertView) {
//        ViewHolder vh = (ViewHolder) convertView.getTag();
//
//        vh.image.setTag(R.id.list_item_image, item);
//        GlideHelper.loadPaintingImage(vh.image, item);
//        vh.title.setText(item.getTitle());
//    }

//    @Override
//    public void onClick(View view) {
//        SubCategory item = (SubCategory) view.getTag(R.id.list_item_image);
////        if (view.getContext() instanceof UnfoldableDetailsActivity) {
//            ((UnfoldableDetailsActivity) view.getContext()).openDetails(view, item);
////        }
////        else if (view.getContext() instanceof FoldableListActivity) {
////            Toast.makeText(view.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
////        }
//    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        detailRecyclerAdapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_list_item, parent, false);
        RecyclerViewHolders viewHolder = new RecyclerViewHolders(detailRecyclerAdapterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

//        holder.photoOne.setTag(R.id.list_item_image, item);
//        GlideHelper.loadPaintingImage(vh.image, item);
//        holder.title.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView drName, drAddress;
        public TextView fee, experience, distance, review;
        public ImageView photoOne, photoTwo, photoThree;
        public Button call;
        public RelativeLayout main_layout;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            main_layout = (RelativeLayout) itemView.findViewById(R.id.main_layout);
            drName = (TextView) itemView.findViewById(R.id.drName);
            drAddress = (TextView) itemView.findViewById(R.id.drAddress);
            fee = (TextView) itemView.findViewById(R.id.fee);
            experience = (TextView) itemView.findViewById(R.id.experience);
            distance = (TextView) itemView.findViewById(R.id.distance);
            review = (TextView) itemView.findViewById(R.id.review);
            photoOne = (ImageView) itemView.findViewById(R.id.photoOne);
            photoTwo = (ImageView) itemView.findViewById(R.id.photoTwo);
            photoThree = (ImageView) itemView.findViewById(R.id.photoThree);
            call = (Button) itemView.findViewById(R.id.call);

            main_layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
                    SubCategoryOld subCategoryOld = itemList.get(getPosition());
                    listner.onDetailClick(v, subCategoryOld);
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public interface OnClickListner{
        public void onDetailClick(View view, SubCategoryOld subCategoryOld);
    }

}