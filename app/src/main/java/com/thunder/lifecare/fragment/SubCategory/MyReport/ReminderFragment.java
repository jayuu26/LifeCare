package com.thunder.lifecare.fragment.SubCategory.MyReport;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.thunder.lifecare.R;
import com.thunder.lifecare.util.AppUtills;

/**
 * Created by ist-150 on 15/10/16.
 */
public class ReminderFragment extends Fragment {

    private View mainView;
    private RecyclerView my_reports_recycler_view;
    private LinearLayoutManager lLayout;
    private Context mContext;

    public enum Single {
        INSTANCE;
        ReminderFragment s = new ReminderFragment();

        public ReminderFragment getInstance() {
            if (s == null)
                return new ReminderFragment();
            else return s;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        mainView = (ViewGroup)inflater.inflate(R.layout.reminder_layout, container, false);
//        AppUtills.setActionBarTitle("Reminder","", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);

        initView(mainView);

        return  mainView;
    }

    private void initView(View mainView) {

        my_reports_recycler_view = (RecyclerView) mainView.findViewById(R.id.reminder_recycler_view);

        lLayout = new LinearLayoutManager(mContext);
        my_reports_recycler_view.setHasFixedSize(true);
        my_reports_recycler_view.setLayoutManager(lLayout);

//        ReportViewAdapter rcAdapter = new ReportViewAdapter(mContext, null, this);
//        my_reports_recycler_view.setAdapter(rcAdapter);
    }

}
