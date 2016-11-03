package com.thunder.lifecare.fragment.Home.WallPost;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.WallAdapter.PostWallAdapter;
import com.thunder.lifecare.customlayout.ContextMenu;
import com.thunder.lifecare.fragment.SubCategory.MyReport.AddReminderFragment;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist-150 on 15/10/16.
 */
public class WallPostHomeFragment extends Fragment implements PostWallAdapter.WallPostActionListener, View.OnClickListener, OnMenuItemClickListener {

    private String TAG = "WallPostHomeFragment";
    private View mainView;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    private FrameLayout mInterceptorFrame;
    private Context mContext;
    private RelativeLayout topLayout;
    private TextView addPostView;
    private TextView postCount;
    private AppCompatSpinner postCategory;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    private ContextMenu contextMenu;

    public enum Single {
        INSTANCE;
        WallPostHomeFragment s = new WallPostHomeFragment();

        public WallPostHomeFragment getInstance() {
            if (s == null)
                return new WallPostHomeFragment();
            else return s;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Chat-Wall","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Chat-Wall","Sub Chat", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.post_wall_main_layout, container, false);
        mContext =getActivity();
        initView(mainView);
        return mainView;
    }

    private void initView(View view){

        materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setColorNormalResId(R.color.colorPrimary);
        floatingActionButton2.setColorNormalResId(R.color.colorPrimary);
        floatingActionButton3.setColorNormalResId(R.color.colorPrimary);

        mInterceptorFrame = (FrameLayout) mainView.findViewById(R.id.fl_interceptor);
        mInterceptorFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch  " + "");
                if (materialDesignFAM.isOpened()) {
                    materialDesignFAM.close(true);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

    }

    private void loadPostList() {

        String[] itemList = {"Dentist", "Gynecologist/Obstertrician", "Dermatologist", "Homeopath", "Ayurveda", "Cardiologist", "Gastroenterologist", "Neurologist", "ENT", "General Physician", "Physiotherapist", "Pediatrician", "Urologist", "Orthopedist", "Opthalmologist", "Dietitian/Nutritionist"};
        int icons[] = {};//{R.drawable.doctor_one, R.mipmap.ic_get_health_tips, R.mipmap.ic_pharma_banner, R.mipmap.records_banner, R.mipmap.ic_ask_boarding, R.mipmap.ic_get_health_tips, R.mipmap.ic_pharma_banner, R.mipmap.records_banner, R.mipmap.ic_ask_boarding, R.mipmap.ic_get_health_tips, R.mipmap.ic_pharma_banner, R.mipmap.records_banner};

        List<Category> categoryList = new ArrayList<>();
        for(String str : itemList) {
            Category c = new Category();
            c.setCategoryName(str);
//            c.setCategoryImage(icons[0]);
            categoryList.add(c);
        }

        PostWallAdapter adapter = new PostWallAdapter(mContext,categoryList,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topLayout = (RelativeLayout) view.findViewById(R.id.top_layout);
        addPostView = (TextView) view.findViewById(R.id.add_post_view);
        postCount = (TextView) view.findViewById(R.id.post_count);
        postCategory = (AppCompatSpinner) view.findViewById(R.id.post_category);
       
        recyclerView = (RecyclerView) view.findViewById(R.id.wall_post_list);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        addPostView.setOnClickListener(this);
        loadPostList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.material_design_floating_action_menu_item1:
                if (materialDesignFAM.isOpened()) {
                    materialDesignFAM.close(true);
                }
                break;
            case R.id.material_design_floating_action_menu_item2:
                if (materialDesignFAM.isOpened()) {
                    materialDesignFAM.close(true);
                }
                break;
            case R.id.material_design_floating_action_menu_item3:
                if (materialDesignFAM.isOpened()) {
                    materialDesignFAM.close(true);
                }
                break;
            case R.id.add_post_view:
                AppUtills.loadFragment(AddWallPostFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);
                break;
        }
    }

    @Override
    public void onPostClick(Category category) {

    }
}
