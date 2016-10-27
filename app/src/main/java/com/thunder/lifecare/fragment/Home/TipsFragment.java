package com.thunder.lifecare.fragment.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thunder.lifecare.Listener.PageSelectedListener;
import com.thunder.lifecare.R;
import com.thunder.lifecare.fragment.BaseHomeFragment;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

/**
 * Created by ist-150 on 15/10/16.
 */
public class TipsFragment extends Fragment{

    private String TAG = this.getClass().getName();
    private View mainView;

    public enum Single {
        INSTANCE;
        TipsFragment s = new TipsFragment();

        public TipsFragment getInstance() {
            if (s == null)
                return new TipsFragment();
            else return s;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Health Tips","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup)inflater.inflate(R.layout.search_layout_frag, container, false);
        return  mainView;
    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Health Tips","Sub - Health Tips", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }
}
