package com.thunder.lifecare.fragment.Location;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.thunder.lifecare.R;
import com.thunder.lifecare.constant.CollectionObject;
import com.thunder.lifecare.constant.MessageConstant;
import com.thunder.lifecare.rest.RestCall;
import com.thunder.lifecare.rest.RestClient;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.android.gms.internal.co;

public class GetLocationAdd {
	
	private String address;
	private Geocoder geocoder;
	private Context mContext;
	private Activity activity;
	private List<Address> addresses;
	private EditText locationTxt;
	public GetLocationAsync  getLocationAsync;
	JSONObject jsonObject =null;
	String stringToSet;

	  
	public GetLocationAsync getGetLocationAsync() {
		return getLocationAsync;
	}

	public void setGetLocationAsync(GetLocationAsync getLocationAsync) {
		this.getLocationAsync = getLocationAsync;
	}

	public  String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public GetLocationAdd() {
		// TODO Auto-generated constructor stub
	}
 
	public GetLocationAdd(double lat, double log, Context mContext, EditText locationTxt, String stringToSet) {
		this.mContext = mContext;
		this.activity = (Activity) mContext;
		this.locationTxt = locationTxt;
		this.stringToSet = stringToSet;
//		getLocationInfo(lat,log);
		new GetLocationAsync(lat, log).execute();
		}

	private JSONObject getLocationInfo(double lat, double lng) {

		if (AppUtills.isNetworkAvailable(mContext)) {

			RestCall service = RestClient.Single.INSTANCE.getInstance().getRestCallsConnection(mContext);
			final ProgressDialog dialog = AppUtills.showProgressDialog(activity);
			String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+ lat+","+lng +"&sensor=true";
			final Call<ResponseBody> respo = service.sendGetRequest(""+url);
			respo.enqueue(new Callback<ResponseBody>() {
				@Override
				public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
					if (response.code() == HttpURLConnection.HTTP_OK) {
						try {
							String json = "";
							if (response != null) json = response.body().string();{
								//System.out.println("onResponse " + json);

								if (json.contains("errorMsg")) {

								} else {

									 jsonObject = new JSONObject();
									try {
										jsonObject = new JSONObject(json);
										getCurrentLocationViaJSON(jsonObject);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
						Toast.makeText(mContext, MessageConstant.GENERIC_ERROR, Toast.LENGTH_LONG).show();
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

		return jsonObject;
	}


	public  String getCurrentLocationViaJSON(JSONObject jsonObj) {
		AppLog.i("JSON string =>", jsonObj.toString());
		String currentLocation = mContext.getResources().getString(R.string.getting_location);
		String street_address = null;
		String postal_code = null;

		try {
			String status = jsonObj.getString("status").toString();
			AppLog.i("status", status);

			if(status.equalsIgnoreCase("OK")){
				JSONArray results = jsonObj.getJSONArray("results");
				int i = 0;
				AppLog.i("i", i+ "," + results.length() ); //TODO delete this
				do{
					JSONObject r = results.getJSONObject(i);
					JSONArray typesArray = r.getJSONArray("types");
					String types = typesArray.getString(0);

					if(types.equalsIgnoreCase("street_address")){
						street_address = r.getString("formatted_address");//.split(",")[0];
						AppLog.i("formatted_address", street_address);
						currentLocation = street_address;
						break;
					}else{
						if(types.equalsIgnoreCase("route")){
							if(!r.isNull("formatted_address"))
								street_address = r.getString("formatted_address");
							else if(!r.isNull("route"))
								street_address = r.getString("long_name");

							AppLog.i("route", street_address);
							currentLocation = street_address;
						}
						if(types.equalsIgnoreCase("premise")){

							if(!r.isNull("formatted_address"))
								street_address = street_address+r.getString("formatted_address");
							else if(!r.isNull("route"))
								street_address = street_address+r.getString("long_name");

							AppLog.i("premise", street_address);
							currentLocation = street_address;
						}
						if(types.equalsIgnoreCase("sublocality")){

							if(!r.isNull("formatted_address"))
								street_address = street_address+r.getString("formatted_address");
							else if(!r.isNull("route"))
								street_address = street_address+r.getString("long_name");

							AppLog.i("sublocality", street_address);
							currentLocation = street_address;
							break;
						}
					}
					i++;
				}while(i<results.length());

				AppLog.i("JSON Geo Locatoin =>", currentLocation);

				if(currentLocation!=null && !currentLocation.equalsIgnoreCase("Searching...")){
					setAddress(currentLocation + " ");
					try {
						locationTxt.setText(currentLocation);
						CollectionObject.LOCATION_ADDRESS = currentLocation;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					locationTxt.setText(stringToSet);
				}
				AppUtills.setActionBarTitle("Location ",CollectionObject.LOCATION_ADDRESS, ((AppCompatActivity)activity).getSupportActionBar(), activity, true);
				return currentLocation;
			}

		} catch (JSONException e) {
			AppLog.e("testing","Failed to load JSON");
			e.printStackTrace();
		}
		return null;
	}

	private class GetLocationAsync extends AsyncTask<String, Void, String> {
		 
        // boolean duplicateResponse; 
        double x, y;
        StringBuilder str;
        String cityName;
 
        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub 
 
            x = latitude;
            y = longitude;
        } 
 
        @Override 
        protected void onPreExecute() { 
          //  Address.setText(" Getting location "); 
        	try {
        		locationTxt.setText(stringToSet); 
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        } 
 
        @Override 
        protected String doInBackground(String... params) {
            try { 
            	int count = 0;
                geocoder = new Geocoder(mContext,Locale.getDefault());
                addresses = geocoder.getFromLocation(x, y, 5);
                str = new StringBuilder();
                if (geocoder.isPresent()) {
                    Address returnAddress = addresses.get(0);
 
                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();
 
                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");
 
                } else { 
                } 
                    while(count<20){
                    	count++;
                    	if(addresses!=null && addresses.size()>0){
                    		break;
                    	}
                    }
                
            } catch (Exception e) {
                //System.out.println(" doInBackground "+e.getMessage());
            } 
            return null; 
        } 
 
        @Override 
        protected void onPostExecute(String result) {
            try { 
            	if(addresses!=null){
	                setAddress(addresses.get(0).getAddressLine(0)
	                        + addresses.get(0).getAddressLine(1) + " ");
	            	try {
	            		locationTxt.setText(getAddress());
						CollectionObject.LOCATION_ADDRESS = stringToSet;
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
            	}else{
//					getLocationInfo(x,y);
					locationTxt.setText(stringToSet);
            	}
				AppUtills.setActionBarTitle("Location ",CollectionObject.LOCATION_ADDRESS, ((AppCompatActivity)activity).getSupportActionBar(), activity, true);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        } 
 
        @Override 
        protected void onProgressUpdate(Void... values) {
 
        } 
    }

	
}
