package com.thunder.lifecare.GreenDao.daodbhelper;

import android.app.Activity;
import android.database.Cursor;

import com.google.gson.Gson;
import com.thunder.lifecare.GreenDao.daomodel.DaoSession;
import com.thunder.lifecare.GreenDao.daomodel.HomeRootObject;
import com.thunder.lifecare.GreenDao.daomodel.HomeRootObjectDao;
import com.thunder.lifecare.activity.App;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;


/**
 * Created by ist on 25/10/16.
 */

public class HomeRootObjectDBHelper {

    private DaoSession daoSession;
    private HomeRootObjectDao homeRootObjectDao;
    private Query<HomeRootObject> homeRootObjectQuery;
    private String TAG = this.getClass().getName();
    private static Activity activity;

    public enum single {
        INSTANCE;
        HomeRootObjectDBHelper genericDB = new HomeRootObjectDBHelper();

        public HomeRootObjectDBHelper getInstnce(Activity activity1) {
            activity = activity1;
            if (genericDB == null)
                return new HomeRootObjectDBHelper();
            else return genericDB;
        }
    }

    /**
     * Insert items into Database table
     */
    public long insertItem(HomeRootObject homeRootObject, Activity activity) {
        try {
            daoSession = ((App) activity.getApplication()).getDaoSession();
            this.homeRootObjectDao = daoSession.getHomeRootObjectDao();
            if(!isExsist(homeRootObjectDao,homeRootObject)) {
                return this.homeRootObjectDao.insertOrReplace(homeRootObject);
            }
            AppUtills.exportDatabase(activity, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

     public int getPrimaryKeyId(){
         daoSession = ((App) activity.getApplication()).getDaoSession();
         homeRootObjectDao = daoSession.getHomeRootObjectDao();
         QueryBuilder qb;
         String query = "SELECT _id from "+homeRootObjectDao.getTablename();
         Cursor c = daoSession.getDatabase().rawQuery(query, null);
         try{
             if (c.moveToFirst()) {
                 do {
                        AppLog.e(TAG," PrimaryKeyId "+c.getInt(0));
                     return c.getInt(0);
                 } while (c.moveToNext());
             }
         } finally {
             c.close();
         }
         return 0;
    }


    /**
     * //     * Fetching items from Database table
     * //
     */
    public HomeRootObject getItemObject() {
        daoSession = ((App) activity.getApplication()).getDaoSession();
        homeRootObjectDao = daoSession.getHomeRootObjectDao();
        QueryBuilder qb;
        qb = homeRootObjectDao.queryBuilder();
        homeRootObjectQuery = qb.build();

        return homeRootObjectQuery.unique();
    }



    private boolean isExsist(HomeRootObjectDao homeRootObjectDao, HomeRootObject homeRootObject) {
        boolean isTrue = false;
        long count =0;
        QueryBuilder qb;

        try {
            qb = homeRootObjectDao.queryBuilder();
            qb.where(HomeRootObjectDao.Properties.Category.eq(homeRootObject.getCategory()));
            count = qb.count();
            if(count>0) {
                isTrue = true;
                homeRootObjectDao.updateInTx(homeRootObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
            isTrue = false;
        }
        System.out.println("homeRootObjectDao isExsist "+isTrue+ "count "+count);
        return isTrue;
    }

}
