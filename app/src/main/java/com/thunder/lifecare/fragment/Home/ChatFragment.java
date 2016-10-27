package com.thunder.lifecare.fragment.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thunder.lifecare.R;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

/**
 * Created by ist-150 on 15/10/16.
 */
public class ChatFragment extends Fragment {

    private String TAG = "ChatFragment";
    private View mainView;

    public enum Single {
        INSTANCE;
        ChatFragment s = new ChatFragment();

        public ChatFragment getInstance() {
            if (s == null)
                return new ChatFragment();
            else return s;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Chat","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Chat","Sub Chat", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.chat_fragment, container, false);
        AppUtills.setActionBarTitle("Chat","", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);

        return mainView;
    }


}
