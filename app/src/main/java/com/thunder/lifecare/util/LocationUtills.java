package com.thunder.lifecare.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.EditText;

import com.thunder.lifecare.R;

/**
 * Created by ist on 14/10/16.
 */

public class LocationUtills {

    /**
     * Checks if GPS is enabled or not
     *
     * @param context
     * @return
     */
    public static boolean isGPSEnabled(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean latitudeValidation(EditText latText) {
        if (isValidDecimalNumber(latText.getText().toString())) {
            Double latValue = Double.parseDouble(latText.getText().toString());
            if (latValue >= 6 && latValue <= 37) {
                latText.setError(null);
                return true;
            } else {
                latText.setFocusableInTouchMode(true);
                latText.requestFocus();
                latText.setError("Latitude is out of Indian territory");
            }
        } else {
            latText.setFocusableInTouchMode(true);
            latText.requestFocus();
            latText.setError("Invalid latitude");
        }
        return false;
    }

    public static boolean longitudeValidation(EditText longText) {
        if (isValidDecimalNumber(longText.getText().toString())) {
            Double latValue = Double.parseDouble(longText.getText().toString());
            if (latValue >= 68 && latValue <= 98) {
                longText.setError(null);
                return true;
            } else {
                longText.setFocusableInTouchMode(true);
                longText.requestFocus();
                longText.setError("Longitude is out of Indian territory");
            }
        } else {
            longText.setFocusableInTouchMode(true);
            longText.requestFocus();
            longText.setError("Invalid longitude");
        }
        return false;
    }

    public static boolean isValidDecimalNumber(String input){
        if(input.matches("^[0-9]+(\\.[0-9]*)?$"))
            return true;
        return false;
    }

    public static void turnGPSOn(Context context){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }

        provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.contains("gps")){
            createLocationServiceError(context);
        }
    }

    public static void turnGPSOff(Context context){
        try {
            String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if(provider.contains("gps")){ //if gps is enabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void createLocationServiceError(final Context activityObj) {

        // show alert dialog if Internet is not connected
        AlertDialog.Builder builder = new AlertDialog.Builder(activityObj);

        builder.setMessage(activityObj.getResources().getString(R.string.gps_off_alert_msg))
                .setTitle(activityObj.getResources().getString(R.string.gps_off_alert_title))
                .setCancelable(false)
                .setPositiveButton(activityObj.getString(R.string.action_settings),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activityObj.startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(activityObj.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        builder.create();
        builder.show();
    }

}
