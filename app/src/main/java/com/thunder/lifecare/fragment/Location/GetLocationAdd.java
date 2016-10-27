package com.thunder.lifecare.fragment.Location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

//import com.google.android.gms.internal.co;

public class GetLocationAdd {
	
	private String address;
	private Geocoder geocoder;
	private Context context;
	private List<Address> addresses;
	private EditText locationTxt;
	public GetLocationAsync  getLocationAsync;
	String stringToSet;

	  
	public GetLocationAsync getGetLocationAsync() {
		return getLocationAsync;
	}

	public void setGetLocationAsync(GetLocationAsync getLocationAsync) {
		this.getLocationAsync = getLocationAsync;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public GetLocationAdd() {
		// TODO Auto-generated constructor stub
	}
 
	public GetLocationAdd(double lat,double log,Context context,EditText locationTxt,String stringToSet) {
		this.context = context;
		this.locationTxt = locationTxt;
		this.stringToSet = stringToSet;
		new GetLocationAsync(lat, log).execute();
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
                geocoder = new Geocoder(context, Locale.getDefault());
                addresses = geocoder.getFromLocation(x, y, 10);
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
                
                /*Geocoder gcd = new Geocoder(context, Locale.getDefault());
                List<Address> addresses;
                    addresses = gcd.getFromLocation(x, y, 1);
                    if (addresses != null) {
                        if (addresses.size() > 0)
                             cityName = addresses.get(0).getLocality();
                        	//System.out.println(" cityName  "+cityName +" addresses "+addresses.size());
                    } */
                    
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
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
            	}else{
            		 locationTxt.setText(stringToSet); 
            	}
            } catch (Exception e) {
                e.printStackTrace();
            } 
        } 
 
        @Override 
        protected void onProgressUpdate(Void... values) {
 
        } 
    }   
}
