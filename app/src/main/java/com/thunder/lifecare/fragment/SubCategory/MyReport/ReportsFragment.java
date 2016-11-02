package com.thunder.lifecare.fragment.SubCategory.MyReport;

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

import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.ReportViewAdapter;
import com.thunder.lifecare.util.AppUtills;

/**
 * Created by ist-150 on 15/10/16.
 */
public class ReportsFragment extends Fragment implements ReportViewAdapter.ReportActionListener {

    private View mainView;
    private RecyclerView my_reports_recycler_view;
    private LinearLayoutManager lLayout;
    private Context mContext;

    public enum Single {
        INSTANCE;
        ReportsFragment s = new ReportsFragment();

        public ReportsFragment getInstance() {
            if (s == null)
                return new ReportsFragment();
            else return s;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        mainView = (ViewGroup)inflater.inflate(R.layout.reports_layout, container, false);

        initView(mainView);

        return  mainView;
    }

    private void initView(View mainView) {

        my_reports_recycler_view = (RecyclerView) mainView.findViewById(R.id.reports_recycler_view);

        lLayout = new LinearLayoutManager(mContext);
        my_reports_recycler_view.setHasFixedSize(true);
        my_reports_recycler_view.setLayoutManager(lLayout);

        ReportViewAdapter rcAdapter = new ReportViewAdapter(mContext, null, this);
        my_reports_recycler_view.setAdapter(rcAdapter);
    }

    @Override
    public void onReportClick(Category category) {

    }
}
