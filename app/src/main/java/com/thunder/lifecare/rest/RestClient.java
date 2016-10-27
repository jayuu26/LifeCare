package com.thunder.lifecare.rest;

import android.content.Context;

import com.thunder.lifecare.util.UrlConfig;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ist on 3/9/16.
 */
public class RestClient implements Interceptor {

    Retrofit retrofit =null;
    OkHttpClient okHttp =null;
    Picasso sPicasso;

    public enum Single {
        INSTANCE;
        RestClient s = new RestClient();

        public RestClient getInstance() {
            if (s == null)
                return new RestClient();
            else return s;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        System.out.println("Retrofit@Response ---- "+response.body().string());
        return response;
    }

    public RestCall getRestCallsConnection(Context mContext){
        try {


//            Retrofit.Builder builder = new Retrofit.Builder().baseUrl("https://itunes.apple.com/");
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(UrlConfig.BASE_URL);
//            builder.addConverterFactory(ScalarsConverterFactory.create());
            builder.addConverterFactory(GsonConverterFactory.create());
//            builder.addConverterFactory(new LenientGsonConverter());
            retrofit = builder.client(getOkHttpInstance(mContext)).build();
            setRetrofit(retrofit);
            return retrofit.create(RestCall.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public OkHttpClient getOkHttpInstance(Context mContext){
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        try {
            okHttp = new OkHttpClient.Builder()
                    .addInterceptor(new AddCookiesInterceptor(mContext))
                    .addInterceptor(new ReceivedCookiesInterceptor(mContext))
                    /*.sslSocketFactory(getSSLConfig(mContext).getSocketFactory())*/
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .build();
            setOkHttp(okHttp);
//            getSSLConfig(mContext).getSocketFactory();
            return okHttp;
//        }catch (CertificateException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//            return null;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            return null;
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Picasso getPicasoInstance(Context context) {
        if (sPicasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);
            try {
                OkHttp3Downloader okHttpDownloader = new OkHttp3Downloader(getOkHttpInstance(context));
                builder.downloader(okHttpDownloader);
                sPicasso = builder.build();
                Picasso.setSingletonInstance(sPicasso);
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sPicasso;
    }

    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getAssets().open("siteforge.ril.com.crt");
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public OkHttpClient getOkHttp() {
        return okHttp;
    }

    public void setOkHttp(OkHttpClient okHttp) {
        this.okHttp = okHttp;
    }
}
