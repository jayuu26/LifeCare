package com.thunder.lifecare.fragment.Home;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunder.lifecare.GreenDao.daodbhelper.HomeCategoryDBHelper;
import com.thunder.lifecare.GreenDao.daodbhelper.HomeRootObjectDBHelper;
import com.thunder.lifecare.Listener.PageSelectedListener;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.HomeListGridAdapter;
import com.thunder.lifecare.constant.MessageConstant;
import com.thunder.lifecare.fragment.BaseHomeFragment;
import com.thunder.lifecare.fragment.SubCategory.Doctor.SubCategoryFragment;
import com.thunder.lifecare.GreenDao.daomodel.HomeCategory;
import com.thunder.lifecare.GreenDao.daomodel.HomeRootObject;
import com.thunder.lifecare.rest.RestCall;
import com.thunder.lifecare.rest.RestClient;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeMajorCategoryFragment extends Fragment implements HomeListGridAdapter.OnClickListner {

    private String TAG = this.getClass().getName();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView myRecyclerView;
    private GridLayoutManager lLayout;
    Context mContext;
    ArrayList<HomeCategory> homeCategoriesList;
    RestCall service ;

    public enum Single {
        INSTANCE;
        HomeMajorCategoryFragment s = new HomeMajorCategoryFragment();

        public HomeMajorCategoryFragment getInstance() {
            if (s == null)
                return new HomeMajorCategoryFragment();
            else return s;
        }
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Home","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        ViewGroup mainView = (ViewGroup) inflater.inflate(R.layout.search_layout_frag, container, false);
        service = RestClient.Single.INSTANCE.getInstance().getRestCallsConnection(mContext);
        return mainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        lLayout = new GridLayoutManager(getActivity(), 2);

        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(lLayout);

        loadList();

//        HomeListGridAdapter rcAdapter = new HomeListGridAdapter(mContext, this,homeCategoriesList);
//        myRecyclerView.setAdapter(rcAdapter);
    }

    @Override
    public void onClick(int position) {
        loadSubCategory(position);
    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Home","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }

    private void loadList() {

//        final ProgressDialog dialog = AppUtills.showProgressDialog(getActivity());
//        RestCall service = RestClient.Single.INSTANCE.getInstance().getRestCallsConnection(mContext);
        final Type listType = new TypeToken<List<HomeCategory>>() {
        }.getType();

        HomeRootObject homeRootObject = null;
        homeRootObject = HomeRootObjectDBHelper.single.INSTANCE.getInstnce(getActivity()).getItemObject();

        if (homeRootObject != null && homeRootObject.getHomeCategory() != null &&
                homeRootObject.getHomeCategory().size()>0){
            homeCategoriesList = (ArrayList<HomeCategory>) homeRootObject.getHomeCategory();
        }else {
            String json = AppUtills.loadJsonFromAssets(getActivity(), "home_category_list");
            if (json != null && !json.equalsIgnoreCase("")) {
                homeRootObject = new Gson().fromJson(json, HomeRootObject.class);

                HomeRootObjectDBHelper.single.INSTANCE.getInstnce(getActivity()).insertItem(homeRootObject, getActivity());
                long foreignKeyId = HomeRootObjectDBHelper.single.INSTANCE.getInstnce(getActivity()).getPrimaryKeyId();

                if (homeRootObject != null && homeRootObject.getHomeCategory() != null) {
                    homeCategoriesList = (ArrayList<HomeCategory>) homeRootObject.getHomeCategory();
                    HomeCategoryDBHelper.single.INSTANCE.getInstnce(getActivity()).insertItem(homeCategoriesList, getActivity(), foreignKeyId);
                }
            }
        }

        HomeListGridAdapter rcAdapter = new HomeListGridAdapter(mContext,this,homeCategoriesList);
                                myRecyclerView.setAdapter(rcAdapter);

//        AppUtills.hideProgressDialog(dialog);
//

//        if (AppUtills.isNetworkAvailable(mContext)) {
//            final ProgressDialog dialog = AppUtills.showProgressDialog(getActivity());
//            final Call<HomeRootObject> respo = service.getMainCategoryByUserId(userId);
//            respo.enqueue(new Callback<HomeRootObject>() {
//                @Override
//                public void onResponse(Call<HomeRootObject> call, Response<HomeRootObject> response) {
//                }
//                @Override
//                public void onFailure(Call<HomeRootObject> call, Throwable t) {
//                }
//            });




//            repos.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                    if (response.code() == HttpURLConnection.HTTP_OK) {
//                        try {
//                            homeCategoriesList.removeAll(homeCategoriesList);
//                            String json = "";
//                            if (response != null) json = response.body().string();
//                            System.out.println("onResponse " + json);
//
//                            if (json.contains("errorMsg")) {
//                                try {
//                                    ErrorModel errorModel = new ErrorModel();
//                                    errorModel = new Gson().fromJson(json, ErrorModel.class);
//                                    AppUtills.showErrorPopUp(getActivity(), errorModel.getErrorMsg());
//                                } catch (JsonSyntaxException e) {
//                                    e.printStackTrace();
//                                    Toast.makeText(mContext, "" + MessageConstant.GENERIC_ERROR, Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                List<HomeRootObject> homeRootObjects = new Gson().fromJson(json, listType);
//                                homeCategoriesList.addAll((ArrayList)homeRootObjects);
//                                HomeListGridAdapter rcAdapter = new HomeListGridAdapter(mContext,this,homeCategoriesList);
//                                myRecyclerView.setAdapter(rcAdapter);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Toast.makeText(mContext, "" + MessageConstant.GENERIC_ERROR, Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getActivity(), MessageConstant.GENERIC_ERROR, Toast.LENGTH_LONG).show();
//                    }
//                    AppUtills.cancelProgressDialog(dialog);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    AppUtills.cancelProgressDialog(dialog);
//                    Toast.makeText(getActivity(), MessageConstant.GENERIC_ERROR, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }

    private void loadSubCategory(int id) {

        if (AppUtills.isNetworkAvailable(mContext)) {
            final ProgressDialog dialog = AppUtills.showProgressDialog(getActivity());
            final Call<ResponseBody> respo =null;// = service.getMainCategoryByUserId("");
            respo.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        try {
                            String json = "";
                            if (response != null) json = response.body().string();{
                            System.out.println("onResponse " + json);

                            if (json.contains("errorMsg")) {

                            } else {

                            }
                        }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getActivity(), MessageConstant.GENERIC_ERROR, Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

            AppUtills.hideProgressDialog(dialog);
        }else{
            Toast.makeText(mContext, ""+ MessageConstant.CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }

        AppUtills.loadFragment(SubCategoryFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);
    }


}
