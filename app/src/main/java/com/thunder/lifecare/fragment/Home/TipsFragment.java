package com.thunder.lifecare.fragment.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.thunder.lifecare.R;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.UrlConfig;

/**
 * Created by ist-150 on 15/10/16.
 */
public class TipsFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    private String TAG = this.getClass().getName();
    private View mainView;
    private WebView mWebView;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        mainView = (ViewGroup)inflater.inflate(R.layout.health_tips_layout, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadPage(mainView);
            }
        });


        return  mainView;
    }

    private void loadPage(View view){
        swipeRefreshLayout.setRefreshing(true);
        mWebView = (WebView) view.findViewById(R.id.webview);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
            }
        });

        mWebView.loadUrl(""+ UrlConfig.HEALTH_TIPS_URL);

        if(mWebView.getProgress()>50){
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onRefresh() {

    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Health Tips","Sub - Health Tips", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }
}
