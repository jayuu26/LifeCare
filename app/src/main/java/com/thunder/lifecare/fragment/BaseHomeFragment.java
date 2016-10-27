package com.thunder.lifecare.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;
import com.thunder.lifecare.Listener.PageSelectedListener;
import com.thunder.lifecare.R;
import com.thunder.lifecare.customlayout.RippleView;
import com.thunder.lifecare.fragment.Home.ChatFragment;
import com.thunder.lifecare.fragment.Home.HomeMajorCategoryFragment;
import com.thunder.lifecare.fragment.Home.OrderFragment;
import com.thunder.lifecare.fragment.Home.TipsFragment;
import com.thunder.lifecare.fragment.Home.MyReportsFragment;
import com.thunder.lifecare.fragment.Location.GoogleMapFragment;
import com.thunder.lifecare.service.LocationService;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

import java.util.ArrayList;

/**
 * Created by ist-150 on 14/10/16.
 */
public class BaseHomeFragment extends Fragment {

    private String TAG = this.getClass().getName();
    PageSelectedListener pageSelectedListener;
    private View mainView;
    public static BaseHomeFragment baseHomeFragment;
    private LinearLayout findLocation;
    private RippleView ripple;

    public enum Single {
        INSTANCE;
        BaseHomeFragment s = new BaseHomeFragment();

        public BaseHomeFragment getInstance() {
            if (s == null)
                return new BaseHomeFragment();
            else return s;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        if(menuVisible)
            AppUtills.setActionBarTitle("Chat","Sub Chat", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Chat","Sub Chat", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup)inflater.inflate(R.layout.home_fragment_layout, container, false);

        startLocationService();
        initUI(mainView);

        //ripple = (RippleView) mainView.findViewById(R.id.ripple);
        // Click on find location button
        findLocation = (LinearLayout) mainView.findViewById(R.id.findLocation);
        findLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtills.loadFragment(GoogleMapFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);
            }
        });

        if (mainView != null) {
            mainView.setFocusableInTouchMode(true);
            mainView.requestFocus();
            mainView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AppUtills.showExitPopUp(getActivity());
                    }
                    return false;
                }
            });
        }


        return  mainView;
    }

    public void initListner(PageSelectedListener pageSelectedListener1){
        this.pageSelectedListener = pageSelectedListener1;
    }
    private void startLocationService() {
        Intent locationIntent = new Intent(getActivity(), LocationService.class);
        getActivity().startService(locationIntent);
    }

    private void initUI(View view)
    {
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        final String[] colors = getResources().getStringArray(R.array.bottom_tab_font_colors);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) view.findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.find),
                        Color.parseColor(colors[0]))
                        .title("Home")
                        .badgeTitle("Home")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.chat_three),
                        Color.parseColor(colors[1]))
                        .title("Chat")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.my_report_four),
                        Color.parseColor(colors[2]))
                        .title("My Reports")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tips),
                        Color.parseColor(colors[3]))
                        .title("Tips/Care")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.my_order),
                        Color.parseColor(colors[4]))
                        .title("Order")

                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {

                switch (position){
                    case 0:
                        HomeMajorCategoryFragment.Single.INSTANCE.getInstance().onPageSelected(position);
                        break;
                    case 1:
                        ChatFragment.Single.INSTANCE.getInstance().onPageSelected(position);
                        break;
                    case 2:
                        MyReportsFragment.Single.INSTANCE.getInstance().onPageSelected(position);
                        break;
                    case 3:
                        TipsFragment.Single.INSTANCE.getInstance().onPageSelected(position);
                        break;
                    case 4:
                        OrderFragment.Single.INSTANCE.getInstance().onPageSelected(position);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(final int state) {


            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 200);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return HomeMajorCategoryFragment.Single.INSTANCE.getInstance();
                case 1: return ChatFragment.Single.INSTANCE.getInstance();
                case 2: return MyReportsFragment.Single.INSTANCE.getInstance();
                case 3: return TipsFragment.Single.INSTANCE.getInstance();
                case 4: return OrderFragment.Single.INSTANCE.getInstance();
                default: return HomeMajorCategoryFragment.Single.INSTANCE.getInstance();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
