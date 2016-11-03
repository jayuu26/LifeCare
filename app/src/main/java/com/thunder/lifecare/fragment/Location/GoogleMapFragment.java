package com.thunder.lifecare.fragment.Location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.thunder.lifecare.R;
import com.thunder.lifecare.service.LocationService;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.LocationUtills;

import java.io.IOException;
import java.util.List;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    final int PERMISSION_REQUEST_CODE = 1;
    final int PERMISSION_REQUEST_CODE1 = 2;

    private Context mContext;
    private LocationManager locationManager;
    private GoogleMapFragment gridRouteActivity;
    private ViewGroup mainView;
    private EditText locationTxt;
    private GoogleMap mGoogleMap;
    private double mLatitude, mLongitude;
    private Marker locationMarker;

    public enum Single {
        INSTANCE;
        GoogleMapFragment s = new GoogleMapFragment();

        public GoogleMapFragment getInstance() {
            if (s == null)
                return new GoogleMapFragment();
            else return s;
        }
    }

    public GoogleMapFragment() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mainView != null) {
            ViewGroup parent = (ViewGroup) mainView.getParent();
            if (parent != null)
                parent.removeView(mainView);
        }
        try {
            mainView = (ViewGroup) inflater.inflate(
                    R.layout.googlemap_view_layout, container, false);
            mContext = getActivity();
            AppUtills.setActionBarTitle("Select Location "," Location Address", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);

            AppUtills.checkPermission(getActivity(), mContext, PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
            initView();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return mainView;
    }

    void initView(){
        location = LocationService.getmCurrentLocation();
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        mapFrag.getMapAsync(this);
        myLocationButton();
//        AppUtills.checkPermission(getActivity(), mContext, PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION);
//        mGoogleMap.setMyLocationEnabled(true);
        locationTxt = (EditText) mainView.findViewById(R.id.findLocationText);
        ImageView search_button = (ImageView) mainView.findViewById(R.id.search_icon);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapSearch(mainView);
            }
        });

        mainView.findViewById(R.id.backToList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(" OnBacl to list click ");
                getActivity().onBackPressed();

            }
        });

        final FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

    }

    private void getMyLocation() {
        LatLng latLng = new LatLng(mLatitude,mLongitude);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
    }


    private void initMap() {

        Log.v("LocationChanged", "IN INIT MAP LOCATION ");
        try {
            if (AppUtills.checkPermission(getActivity(), mContext, PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)) {
                locationManager = (LocationManager) getActivity()
                        .getSystemService(Context.LOCATION_SERVICE);
                mGoogleMap.setMyLocationEnabled(true);
                Criteria criteria = new Criteria();

                location = getLocation();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));

                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location == null) {
                    location = getLocation();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                } else {
                }
                locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        Log.v("LocationChanged", "IN onMapReady ");
        initMap();
    }

    boolean isGPSEnabled, isNetworkEnabled, canGetLocation;
    Location location;
    long MIN_TIME_BW_UPDATES = 1000, MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    public Location getLocation() {
        Log.v("LocationChanged", "IN getLocation ");
        try {
            locationManager = (LocationManager) getActivity()
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                LocationUtills.turnGPSOn(mContext);
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    //Log.d("Network", "Network Enabled");
                    if (AppUtills.checkPermission(getActivity(), mContext, PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();
                            }
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        //Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                mLatitude = location.getLatitude();
                                mLongitude = location.getLongitude();
                            }
                        }
                    }
                }
                if(mGoogleMap!=null)
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude,mLongitude), 16.0f));
            }

            Log.v("LocationChanged", "IN GET LOCATION ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        location = getLocation();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.googleMap));
        FragmentManager childFragmentManager = getChildFragmentManager();
        childFragmentManager.beginTransaction().remove(fragment);
        super.onDestroyView();
    }


    private void showErrorDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title).setMessage(msg).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void addMarker(LatLng latLng) {

        mGoogleMap.clear();
//        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        locationMarker = mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng).title("Center Position"));
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.v("LocationChanged", "IN ON LOCATION CHANGE");

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        mLatitude = latitude;
        mLongitude = longitude;

        LatLng latLng = new LatLng(latitude, longitude);
        Log.v("LocationChanged", "IN ON LOCATION CHANGE, lat=" + latitude + ", lon=" + longitude);
        try {
            mGoogleMap.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setMap();
        mGoogleMap
                .animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location
                                .getLongitude()), 16.0f));
        if (AppUtills.checkPermission(getActivity(), mContext, PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationManager.removeUpdates(this);
        }

    }


    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    private void myLocationButton() {

//         Get the button view
        View locationButton = ((View) mainView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 40, 40);
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) view.findViewById(R.id.findLocationText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addressList = geocoder.getFromLocationName(location, 1);
                if (addressList != null && addressList.size() > 0) {
                    for (Address add : addressList) {
                        System.out.println("locality : " + add.getLocality());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList != null) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
//                addMarker(latLng);
            }
        }
    }

    private void setMap() {
        System.out.println(" Calling  setMap ");
        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                LatLng centerOfMap = mGoogleMap.getCameraPosition().target;
                new GetLocationAdd(centerOfMap.latitude, centerOfMap.longitude, mContext, locationTxt, mContext.getResources().getString(R.string.getting_location));
                mLatitude = centerOfMap.latitude;
                mLongitude = centerOfMap.longitude;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Snackbar.make(mainView, "Permission Granted, Now you can access.", Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(mainView, "Permission Denied, You cannot access.", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
}
