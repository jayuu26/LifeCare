package com.thunder.lifecare.GreenDao.daodbhelper;

import android.app.Activity;

import com.google.gson.Gson;
import com.thunder.lifecare.GreenDao.daomodel.DaoSession;
import com.thunder.lifecare.GreenDao.daomodel.HomeCategory;
import com.thunder.lifecare.GreenDao.daomodel.HomeCategoryDao;
import com.thunder.lifecare.activity.App;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;

/**
 * Created by ist on 25/10/16.
 */

public class HomeCategoryDBHelper {

    private DaoSession daoSession;
    private HomeCategoryDao homeCategoryDao;
    private Query<HomeCategory> homeCategoryQuery;
    private String TAG = this.getClass().getName();
    static Activity activity;

    public enum single {
        INSTANCE;
        HomeCategoryDBHelper genericDB = new HomeCategoryDBHelper();

        public HomeCategoryDBHelper getInstnce(Activity activity1) {
            activity = activity1;
            if (genericDB == null)
                return new HomeCategoryDBHelper();
            else return genericDB;
        }
    }

    /**
     * Insert items into Database table
     */
    public void insertItem(ArrayList<HomeCategory> homeCategory, Activity activity,long foreignKeyId) {
        try {

            daoSession = ((App) activity.getApplication()).getDaoSession();
            this.homeCategoryDao = daoSession.getHomeCategoryDao();
            for(HomeCategory homeCategory1:homeCategory) {
                homeCategory1.setHome_sub_id(foreignKeyId);
                AppLog.i(TAG, "Inside Insert HomeRootObject " + new Gson().toJson(homeCategory1)+ " foreignKeyId "+foreignKeyId);
                this.homeCategoryDao.insertOrReplace(homeCategory1);
            }
            AppUtills.exportDatabase(activity, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * //     * Fetching items from Database table
     * //
     */
    public ArrayList<HomeCategory> getItemObject(Activity activity, String kind) {
        daoSession = ((App) activity.getApplication()).getDaoSession();
        homeCategoryDao = daoSession.getHomeCategoryDao();
        QueryBuilder qb;
        qb = homeCategoryDao.queryBuilder();

        homeCategoryQuery = qb.build();

        AppLog.i(TAG," getItemObject "+new Gson().fromJson(""+ homeCategoryQuery.unique(), HomeCategory.class));
        return (ArrayList<HomeCategory>) homeCategoryQuery.list();
    }
}
