package com.thunder.lifecare.fragment.DetailFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thunder.lifecare.R;

/**
 * Created by ist-150 on 27/10/16.
 */
public class SubCategoryDetailFragment extends Fragment {

    private View mainView;
    private CoordinatorLayout detailsLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private MyImageViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] images = new int[]{
                R.drawable.chat_three,
                R.drawable.diagnosis,
                R.drawable.doctor_one,
                R.drawable.doctors,
                R.drawable.find,
                R.drawable.hospital,};
    private int position = images.length;

    public enum Single {
        INSTANCE;
        SubCategoryDetailFragment s = new SubCategoryDetailFragment();

        public SubCategoryDetailFragment getInstance() {
            if (s == null)
                return new SubCategoryDetailFragment();
            else return s;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (ViewGroup)inflater.inflate(R.layout.subcategory_detail_layout, container, false);
        initView(mainView);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            if( position >= 4){
                position = 0;
            }else{
                position = position+1;
            }
            viewPager.setCurrentItem(position, true);
            handler.postDelayed(runnable, 2000);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (handler!= null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        handler.postDelayed(runnable, 2000);
    }

    private void initView(View mainView) {
        detailsLayout = (CoordinatorLayout) mainView.findViewById(R.id.details_layout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) mainView.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) mainView.findViewById(R.id.action_toolbar);

        collapsingToolbarLayout.setTitle("Dr. M K Rathore");

        viewPager = (ViewPager) mainView.findViewById(R.id.image_view_pager);
        dotsLayout = (LinearLayout) mainView.findViewById(R.id.imageLayoutDots);

        addBottomDots(0);
        changeStatusBarColor();

        myViewPagerAdapter = new MyImageViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[images.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyImageViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyImageViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.full_image_view, container, false);
            ImageView full_image = (ImageView) view.findViewById(R.id.full_image);
            full_image.setImageResource(images[position]);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
