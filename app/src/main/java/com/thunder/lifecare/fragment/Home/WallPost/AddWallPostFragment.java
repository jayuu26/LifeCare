package com.thunder.lifecare.fragment.Home.WallPost;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.WallAdapter.PostWallAdapter;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist-150 on 15/10/16.
 */
public class AddWallPostFragment extends Fragment implements View.OnClickListener {

    private String TAG = "WallPostHomeFragment";
    private View mainView;
    private Context mContext;


    public enum Single {
        INSTANCE;
        AddWallPostFragment s = new AddWallPostFragment();

        public AddWallPostFragment getInstance() {
            if (s == null)
                return new AddWallPostFragment();
            else return s;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Add Post","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.post_create_layout, container, false);
        mContext =getActivity();
        AppUtills.setActionBarTitle("Add Post","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);
        return mainView;
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

}
