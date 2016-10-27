package com.thunder.lifecare.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thunder.lifecare.R;
import com.thunder.lifecare.constant.CollectionObject;
import com.thunder.lifecare.customlayout.SlidingTabLayout;
import com.thunder.lifecare.fragment.BaseHomeFragment;
import com.thunder.lifecare.fragment.Location.GoogleMapFragment;
import com.thunder.lifecare.fragment.Registration.MobileLoginFragment;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.PreferenceHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public static SearchView searchView;
    public static MenuItem searchMenuItem;
    public static ActionBar actionBar;
    Context mcontext;
    CircleImageView userProfileImage;
    PreferenceHelper preferenceHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        mcontext = this;
        preferenceHelper =new PreferenceHelper(mcontext);
        initActionBar();
       if(preferenceHelper.isFirstTimeLaunch()){
           AppUtills.loadFragment(BaseHomeFragment.Single.INSTANCE.getInstance(), this, R.id.container);
       }else {
           AppUtills.initCountyList(AppUtills.loadJsonFromAssets(this, "isocountry"));
           AppUtills.loadFragment(MobileLoginFragment.Single.INSTANCE.getInstance(), this, R.id.container);
       }
    }


    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_toolbar);
        setSupportActionBar(toolbar);

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.setVisibility(View.GONE);
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        TextView tvuserName = (TextView) headerLayout.findViewById(R.id.tvuserName);
        if (tvuserName != null) {
            tvuserName.setText("Jayesh Verma");
        }
        userProfileImage = (CircleImageView) headerLayout.findViewById(R.id.userProfileImage);
        if (userProfileImage != null)
            userProfileImage.setImageResource(R.drawable.avatar);
        */
        actionBar = getSupportActionBar();
        AppUtills.setActionBarTitle("Home", CollectionObject.LOCATION_ADDRESS,actionBar,this,false);
    }

    private void initSearch(Menu menu) {
        searchMenuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setBackgroundResource(0);
        searchView.setVisibility(View.VISIBLE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (SlidingTabLayout.getSelectedPageIndex()) {
                    case 0:

                        break;

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
//        initSearch(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_search:
                AppUtills.loadFragment(GoogleMapFragment.Single.INSTANCE.getInstance(), this, R.id.container);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
