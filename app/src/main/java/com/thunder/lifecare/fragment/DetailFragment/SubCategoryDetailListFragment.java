package com.thunder.lifecare.fragment.DetailFragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexvasilkov.android.commons.texts.SpannableBuilder;
import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.thunder.lifecare.GreenDao.daomodel.SubCategoryOld;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.SubCategoryAdapter;
import com.thunder.lifecare.util.AppUtills;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryDetailListFragment extends Fragment implements SubCategoryAdapter.OnClickListner{

    private View mainView;
    private RecyclerView recyclerView;
//    private UnfoldableView unfoldableView;
//    private View mListTouchInterceptor;
    private ImageView image;
    private TextView title, description;

    public enum Single {
        INSTANCE;
        SubCategoryDetailListFragment s = new SubCategoryDetailListFragment();

        public SubCategoryDetailListFragment getInstance() {
            if (s == null)
                return new SubCategoryDetailListFragment();
            else return s;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup)inflater.inflate(R.layout.subcategory_layout, container, false);
        getTouchInterceptorView(mainView);
        initView(mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private View getTouchInterceptorView(View view) {
        return view.findViewById(R.id.touch_interceptor_view);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.subCategoryList);

        image = (ImageView) view.findViewById(R.id.details_image);
//        title = (TextView) view.findViewById(R.id.details_title);
//        description = (TextView) view.findViewById(R.id.details_text);

        List<SubCategoryOld> itemList = new ArrayList<>();

        itemList = SubCategoryOld.getAllPaintings();
        SubCategoryAdapter paintingAdapter = new SubCategoryAdapter(getActivity(), itemList,this);
//        paintingAdapter.setPaintingClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addItemDecoration(new MarginDecoration(this));
        recyclerView.setAdapter(paintingAdapter);

//        mListTouchInterceptor = view.findViewById(R.id.touch_interceptor_view);
//        mListTouchInterceptor.setClickable(false);
//
//        detailsLayout.setVisibility(View.INVISIBLE);

//        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
//            @Override
//            public void onUnfolding(UnfoldableView unfoldableView) {
//                mListTouchInterceptor.setClickable(true);
//                detailsLayout.setVisibility(View.VISIBLE);
//                detailsLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.text_color_white));
//            }
//
//            @Override
//            public void onUnfolded(UnfoldableView unfoldableView) {
//                mListTouchInterceptor.setClickable(false);
//                detailsLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.text_color_white));
//            }
//
//            @Override
//            public void onFoldingBack(UnfoldableView unfoldableView) {
//                mListTouchInterceptor.setClickable(true);
//                detailsLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.text_color_white));
//            }
//
//            @Override
//            public void onFoldedBack(UnfoldableView unfoldableView) {
//                mListTouchInterceptor.setClickable(false);
//                detailsLayout.setVisibility(View.INVISIBLE);
//                detailsLayout.setBackgroundColor(getActivity().getResources().getColor(R.color.text_color_white));
//            }
//        });
    }



    @Override
    public void onDetailClick(View view, SubCategoryOld subCategoryOld) {
//        openDetails(view, subCategoryOld);
        AppUtills.loadFragment(SubCategoryDetailFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);
    }

//    public void openDetails(View coverView, SubCategoryOld subCategoryOld) {
//
////        GlideHelper.loadPaintingImage(image, subCategory);
//        title.setText(subCategoryOld.getTitle());
//
//        SpannableBuilder builder = new SpannableBuilder(getActivity());
//        builder
//                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
//                .append("Year").append(": ")
//                .clearStyle()
//                .append(subCategoryOld.getYear()).append("\n")
//                .createStyle().setFont(Typeface.DEFAULT_BOLD).apply()
//                .append("Location").append(": ")
//                .clearStyle()
//                .append(subCategoryOld.getLocation());
//        description.setText(builder.build());
//
//        unfoldableView.unfold(coverView, detailsLayout);
//    }

}
