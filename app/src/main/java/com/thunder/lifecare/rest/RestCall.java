package com.thunder.lifecare.rest;


import com.thunder.lifecare.GreenDao.daomodel.HomeRootObject;
import com.thunder.lifecare.GreenDao.daomodel.Inventory;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by ist on 3/9/16.
 */
public interface RestCall {

    /**
     * Login POST CALL
     */
    @FormUrlEncoded
    @POST("rest/unauthorize/mobileAppAuth")
    Call<Inventory> sendLoginRequest(@Field("username")String  uname, @Field("password")String  password, @Field("domain")String  domain);

    /**
     * GET Home Category LIST
     */
    @GET("rest/AccountDetail/getAccountDetailByUserId/{userID}")
    Call<HomeRootObject> getMainCategoryByUserId(@Path("userID") String userID);


    /**
     *
     * GET CALL NORMAL
     *
     * */
    @GET
    Call<ResponseBody> sendGetRequest(@Url String url);

    /************* Other Call *************/

    /**
     * POST UPLOAD IMAGE
     */
    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);

}
