package com.thunder.lifecare.util;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thunder.lifecare.GreenDao.daomodel.CountryCode;
import com.thunder.lifecare.R;
import com.thunder.lifecare.constant.AppConstants;
import com.thunder.lifecare.constant.CollectionObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ist on 25/8/16.
 */


public class AppUtills {

    private static char[] chunkBuffer = new char[1024];
    private static Context mContext;
    // IN BYTES
    private static double targetImageFileSize = 409600, currentImageFileSize;
    private static String mCurrentImagePath;


    public synchronized static String readData(InputStreamReader rd) {
        try {
            StringBuffer sb = new StringBuffer();
            while (true) {
                int read = rd.read(chunkBuffer, 0, chunkBuffer.length);
                if (read == -1) {
                    break;
                }
                sb.append(chunkBuffer, 0, read);
            }
            return sb.toString();
        } catch (IOException e) {
        } finally {
            try {
                rd.close();
            } catch (IOException e) {
            }
        }
        return "";
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void loadFragment(Fragment loadFragment, FragmentActivity activity, int id) {
        if (loadFragment != null) {
            if (!loadFragment.isAdded()) {
                activity.getSupportFragmentManager().beginTransaction().addToBackStack("tag")
                        .replace(id, loadFragment)
                        .commitAllowingStateLoss();
            }
        }
    }


    public static void removeFragment(Fragment fragment,FragmentActivity activity){

        if(fragment != null && fragment.isAdded())
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public static void hideShowFrag(Fragment hideFrag,Fragment loadFrag, FragmentActivity activity, int container_id){

        if(!loadFrag.isAdded()) {
            activity.getSupportFragmentManager().beginTransaction().hide(hideFrag)
                    .add(container_id, loadFrag)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }

    }

    public static void loadChildFragment(Fragment parentFrag,Fragment childFrag, FragmentActivity activity, int container_id) {
        FragmentManager childFragMan = parentFrag.getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        childFragTrans.add(container_id, childFrag);
        childFragTrans.addToBackStack(""+childFrag.getTag());
        childFragTrans.commit();
    }

    public static void loadImage(Context mContext,ImageView view, String url){
        Glide.with(mContext).load(url).into(view);
    }




    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static void hideKeybord(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ProgressDialog showProgressDialog(Context context, String title, String msg) {
        ProgressDialog _progress = ProgressDialog.show(context, title, msg);
        return _progress;
    }

    /**
     * method for dismissing progress dialog
     *
     * @param _progress
     */
    public static void hideProgressDialog(ProgressDialog _progress) {
        if (_progress !=null && _progress.isShowing())
            _progress.dismiss();

    }

    public static ProgressDialog showProgressDialog(Activity activity){
        ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        return  mProgressDialog;
    }

    public static void cancelProgressDialog(ProgressDialog mProgressDialog){

        if(mProgressDialog!=null){
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        }

        mProgressDialog = null;
    }

    public static void loadTextOnWebView(String text, TextView textView, WebView webView){

        String summary = "<html><FONT style='font-size:14px; font-weight:700; color:#FFFFFF;'>" +
                "<marquee behavior='scroll' direction='left' scrollamount='3'>"
                + text + "</marquee></FONT></html>";
        textView.setVisibility(View.GONE);
        textView.setText(text);
        webView.setBackgroundColor(Color.GRAY);
        webView.loadData(summary, "text/html", "utf-8");
    }



    public static String loadJsonFromAssets(Activity activity,String fileName){
        String json = null;
        try {
//            InputStream is = activity.getAssets().open(fileName);
            InputStream is = activity.getResources().openRawResource(
                    activity.getResources().getIdentifier(fileName,
                            "raw", activity.getPackageName()));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static ArrayList<CountryCode> initCountyList(String json){

        Type listType = new TypeToken<List<CountryCode>>() {}.getType();
        ArrayList<CountryCode> parseList = new Gson().fromJson(json, listType);
        for (int i=0;i<parseList.size();i++){
            CollectionObject.countryName.add(i,parseList.get(i).getName());
            CollectionObject.counrtyCode.put(parseList.get(i).getName(),parseList.get(i).getCode());
        }
        return parseList;
    }

    private static final int REQUEST_WRITE_STORAGE = 112;

    public static void exportDatabase(Context context,Activity activity) {
        try {
            boolean hasPermission = (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            }
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+context.getPackageName()+"//databases//"+"life-care-db";
                String backupDBPath = "lifeCareDB.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);


                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }else{
                System.out.println("Can not write in SD card");
            }

        } catch (Exception e) {

        }
    }

    public static String checkNull(String item) {
        if (item == null || "null".equalsIgnoreCase(item) || "".equals(item.trim()) ||
                "Please Select".equalsIgnoreCase(item.trim())) {
            item = " ---";
        }
        return item.trim();
    }


    public static String checkNullLatLong(String item) {
        if (item == null || "null".equalsIgnoreCase(item) || "".equals(item.trim())) {
            return  null;
        }
        return item.trim();
    }

    public static int toIntExact(long value) {
        if ((int)value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)value;
    }

    public static void showInformativePopup(final Context context,String message, final FragmentActivity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(""/*R.string.info*/);
        builder.setMessage(message)

                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        activity.onBackPressed();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        final Button neutral = alert.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) neutral.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        neutral.setLayoutParams(positiveButtonLL);
    }

    public void setUnderLine(TextView textView, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);

        textView.setText(content);
    }

    public String getAppVersion(Context ctx) {
        PackageInfo pInfo;
        try {
            pInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);

            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static void setActionBarTitle(String title,String subTitle, ActionBar actionBar, Activity activity, boolean isBackEnable) {
        if(isBackEnable) {
            try {

                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);

                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.action_toolbar);
                toolbar.bringToFront();

                ImageView icon = (ImageView) toolbar.findViewById(R.id.icon);
                icon.setVisibility(View.GONE);
                TextView heading = (TextView) toolbar.findViewById(R.id.action_heading);
                TextView sub_heading = (TextView) toolbar.findViewById(R.id.sub_heading);
                heading.setText("  " + title.trim());
                sub_heading.setText("  " + CollectionObject.LOCATION_ADDRESS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            try {
                actionBar.setIcon(R.drawable.doctor_one);
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayUseLogoEnabled(true);


                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.action_toolbar);
                toolbar.bringToFront();

                ImageView icon = (ImageView) toolbar.findViewById(R.id.icon);
                icon.setVisibility(View.VISIBLE);
                TextView heading = (TextView) toolbar.findViewById(R.id.action_heading);
                TextView sub_heading = (TextView) toolbar.findViewById(R.id.sub_heading);
                heading.setText("  " + title.trim());
                sub_heading.setText("  " + CollectionObject.LOCATION_ADDRESS);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void openDocFolder(Activity activity, int REQUEST_ID){
        if (Build.VERSION.SDK_INT <19){
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activity.startActivityForResult(Intent.createChooser(intent, "Choose File"),REQUEST_ID);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath());
            intent.setDataAndType(uri, "*/*");
            activity.startActivityForResult(intent, REQUEST_ID);
        }
    }

    public static void sendSms(Context context) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"
                +  AppConstants.MOBILE_NO));
        smsIntent.putExtra("sms_body", "Feedback @ Jio Fiber+ :-");
        context.startActivity(smsIntent);

    }

    public static void sendEmail(Context context) {
        final Intent emailIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{AppConstants.EMAIL_ID});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Jio Fiber+ Feedback");
        context.startActivity(Intent.createChooser(emailIntent,
                "Jio Fiber+ Feedback"));

    }

    public static  boolean isValidMobileNumber (String mobile)
    {

        if(mobile==null || mobile.equalsIgnoreCase("") || mobile.length()<1){
            return false;
        }
        boolean isValid = true;
        try{
            int seriesNum = Integer.parseInt(mobile.substring(0, 4));
            if(seriesNum>= 7000 && seriesNum<= 9999)
            {
                isValid = true;
            }
            else{
                isValid = false;
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return isValid;
    }

    /**
     * @param activity
     *            use to hide keyboard on the device.
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }



    public static boolean pincodeValidation(EditText codeText, String numberErrorMessage) {
        Pattern mobNO = Pattern.compile("\\d{6}");
        Matcher matcher = mobNO.matcher(codeText.getText().toString());
        if (matcher.find()) {
            return true;
        } else if (!"".equalsIgnoreCase(codeText.getText().toString().trim())) {
            codeText.setFocusableInTouchMode(true);
            codeText.requestFocus();
            codeText.setError(numberErrorMessage);
            return false;
        }
        return true;
    }

    public static boolean emailValidation(EditText emailText, String emailErrorMessage) {

        Pattern email = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
        Matcher matcher = email.matcher(emailText.getText().toString());
        if (matcher.find()) {
            return true;
        } else if (!"".equalsIgnoreCase(emailText.getText().toString().trim())) {
            emailText.setFocusableInTouchMode(true);
            emailText.requestFocus();
            emailText.setError(emailErrorMessage);
            return false;
        }
        return true;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
    public static byte[] readBytes(File file ){

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return bytes;
    }
    public static void bytesToFile(File file,byte[] byteArray){
        FileOutputStream fileOuputStream = null;
        try {
            fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(byteArray);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                fileOuputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public static boolean isValidDecimalNumber(String input){
        if(input.matches("^[0-9]+(\\.[0-9]*)?$"))
            return true;
        return false;
    }

    public static boolean isValidOTP(String otp) {

        if(otp==null || otp.equalsIgnoreCase("") || otp.length()<1){
            return false;
        }

        if(otp.length() == 6){
            return true;
        }
        return false;
    }

    public static void showExitPopUp(final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.exit_app);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
        builder.create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static boolean hasPermission(Activity activity){

        boolean hasPermission = (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        return hasPermission;
    }


    public static void setLocationPermission(Activity activity){

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                110);
    }

    public static boolean checkPermission(Activity activity,Context mContext, int PERMISSION_REQUEST_CODE, String permission){
        int result = ContextCompat.checkSelfPermission(mContext, permission);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {
            return requestPermission(activity,mContext,PERMISSION_REQUEST_CODE,permission);
        }
    }

    public static boolean requestPermission(Activity activity,Context mContext, int PERMISSION_REQUEST_CODE,String permission){

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){

            Toast.makeText(mContext,permission+" Allowed.",Toast.LENGTH_LONG).show();
            return true;
        } else {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_SMS},PERMISSION_REQUEST_CODE);
            return false;
        }
    }


    public final static Bitmap getBitmapFromFile(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // options.inSampleSize = 4;
        options.inSampleSize = calculateInSampleSize(options, width, height);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                // bitmap = rotateImage(bitmap, 180);
                bitmap = rotateImage(bitmap, 90);
                break;
        }

        return bitmap;
    }

    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public final static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == 0 && reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
 /**
     * @param pBitmap
     * @param angle
     * @return rotate image in a specific angle.
     */
    private static Bitmap rotateImage(Bitmap pBitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(pBitmap, 0, 0, pBitmap.getWidth(), pBitmap.getHeight(), matrix, true);
    }

    public static void showErrorPopUp(Activity mContext, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
        builder.setTitle(R.string.error);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        builder.create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showErrorPopUpBack(final Activity activity, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setTitle(R.string.error);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.onBackPressed();
                    }
                });
        builder.create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showExitPopUp(final Activity activity, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);
        builder.setTitle(R.string.confirmation);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
        builder.create();
        AlertDialog dialog = builder.create();
        dialog.show();
    }

 public static boolean copyStream(InputStream inStream, OutputStream outStream, String imagePath, Context context) {
        mContext = context;
        mCurrentImagePath = imagePath;
        try {
            currentImageFileSize = inStream.available();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        byte[] buffer = new byte[1024];
        try {
            while (true) {
                int len = inStream.read(buffer);
                if (len == -1)
                    break;
                outStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                inStream.close();
                outStream.close();
            } catch (IOException e) {
                return false;
            }
        }
        if (currentImageFileSize > targetImageFileSize) {
            compressImage(currentImageFileSize);
        }
        return true;
    }

    private static void compressImage(double currentImageSize) {
        Bitmap bitmap = AppUtills.getBitmapFromFile(mCurrentImagePath, AppConstants.IMAGE_WIDTH, AppConstants.IMAGE_HEIGHT);
        if (bitmap == null) {
            return;
        }
        try {
            FileOutputStream os = new FileOutputStream(mCurrentImagePath);
            if (currentImageSize > targetImageFileSize && currentImageSize <= 3145728) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, os);
            } else if (currentImageSize > 3145728 && currentImageSize <= 5242880) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, os);
            } else if (currentImageSize > 5242880 && currentImageSize <= 10485760) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, os);
            }
            os.close();
            bitmap.recycle();
            bitmap = null;

            fireMediaScanIntent();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fireMediaScanIntent() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(mCurrentImagePath)));
        mContext.sendBroadcast(intent);
    }

    /**
     * Creates resized Bitmap, then compress it
     */
    public static void compressImage(Context mContext, String imagePath) {
        Bitmap bitmap = AppUtills.getBitmapFromFile(imagePath, AppConstants.IMAGE_WIDTH, AppConstants.IMAGE_HEIGHT);
        if (bitmap == null) {
            return;
        }
        try {
            FileOutputStream os = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.close();
            bitmap.recycle();
            bitmap = null;
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(new File(imagePath)));
            mContext.sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // to show photos in full screen
    public static void showPhoto(Context mContext, Uri photoUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        mContext.startActivity(intent);
    }
}


