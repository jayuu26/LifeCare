package com.thunder.lifecare.adapter.WallAdapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ist-150 on 1/7/16.
 */
public class PostWallAdapter extends RecyclerView.Adapter<PostWallAdapter.CustomViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private Long inventoryId;
    private WallPostActionListener actionListener;

    public PostWallAdapter(Context context, List<Category> categoryList, WallPostActionListener wallPostActionListener) {
        this.categoryList = categoryList;
        this.context = context;
        this.actionListener = wallPostActionListener;
    }

    @Override
    public PostWallAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View issueRecyclerAdapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_wall_row_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(issueRecyclerAdapterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PostWallAdapter.CustomViewHolder holder, int position) {
        final Category category = categoryList.get(position);
//          holder.txtCategoryName.setText(category.getCategoryName());
//          holder.txtCategoryIcon.setImageResource(R.drawable.doctor_one);
//        holder.issueRowItem.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != categoryList ? categoryList.size() : 10);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private LinearLayout postCardLayout;
        private CircleImageView drProfileImage;
        private TextView dcrName;
        private TextView postText;
        private ImageView postImage;
        private TextView commentDate;
        private ImageView likeImage;
        private TextView likeCount;
        private TextView replyButton;
        private TextView replyeCount;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            cardView = (CardView) view.findViewById(R.id.card_view);
            postCardLayout = (LinearLayout) view.findViewById(R.id.post_card_layout);
            drProfileImage = (CircleImageView) view.findViewById(R.id.drProfileImage);
            dcrName = (TextView) view.findViewById(R.id.dcrName);
            postText = (TextView) view.findViewById(R.id.post_text);
            postImage = (ImageView) view.findViewById(R.id.post_image);
            commentDate = (TextView) view.findViewById(R.id.comment_date);
            likeImage = (ImageView) view.findViewById(R.id.like_image);
            likeCount = (TextView) view.findViewById(R.id.like_count);
            replyButton = (TextView) view.findViewById(R.id.reply_button);
            replyeCount = (TextView) view.findViewById(R.id.replye_count);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Clicked category Position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            int position = (Integer) v.getTag();
            Category category = categoryList.get(getPosition());
            actionListener.onPostClick(category);
        }
    }

    public interface WallPostActionListener {
        public void onPostClick(Category category);
    }
}
