package com.project42.iplanner.Home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.project42.iplanner.POIs.POISearchFragment;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Groups.CreateGroupChannelFragment;
import com.project42.iplanner.R;

import com.project42.iplanner.Accounts.ProfileFragment;
import com.project42.iplanner.Bookmarks.BookmarkFragment;
import com.project42.iplanner.Itineraries.ItineraryFragment;
import com.project42.iplanner.Chats.ChatFragment;
import com.sendbird.android.SendBird;
import com.project42.iplanner.Settings.SettingsFragment;


public class HomeActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private DrawerLayout mDrawer;
    private Toolbar tBar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = getSupportActionBar();

//        tBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(tBar);
//
//        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerToggle = setupDrawerToggle();
//        mDrawer.addDrawerListener(drawerToggle);
//
//        nvDrawer = (NavigationView) findViewById(R.id.nvView);
//        setupDrawerContent(nvDrawer);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());

        // Initialise Sendbird SDK
        SendBird.init(AppConfig.SBAPP_ID, this.getApplicationContext());

    }

//    private ActionBarDrawerToggle setupDrawerToggle()
//    {
//        return new ActionBarDrawerToggle(this,mDrawer,tBar,R.string.drawer_open,R.string.drawer_close);
//    }
//
//    private void setupDrawerContent(NavigationView navigationView)
//    {
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        selectDrawerItem(menuItem);
//                        return true;
//                    }
//                });
//    }
//
//    public void selectDrawerItem(MenuItem menuItem) {
//        // Create a new fragment and specify the fragment to show based on nav item clicked
//        Fragment fragment = null;
//        Class fragmentClass;
//        switch(menuItem.getItemId()) {
//            case R.id.drawer_home:
//                fragmentClass = HomeFragment.class;
//                break;
//            case R.id.drawer_itinerary:
//                fragmentClass = ItineraryFragment.class;
//                break;
//            case R.id.drawer_bookmark:
//                fragmentClass = BookmarkFragment.class;
//                break;
//            case R.id.drawer_search:
//                fragmentClass = POISearchFragment.class;
//                break;
//            case R.id.drawer_profile:
//                fragmentClass = ProfileFragment.class;
//                break;
//            case R.id.drawer_chat:
//                fragmentClass = ChatFragment.class;
//                break;
//            case R.id.drawer_settings:
//                fragmentClass = SettingsFragment.class;
//                break;
//            default:
//                fragmentClass = HomeFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
//
//        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);
//        // Set action bar title
//        setTitle(menuItem.getTitle());
//        // Close the navigation drawer
//        mDrawer.closeDrawers();
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        if(drawerToggle.onOptionsItemSelected(item))
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_itinerary:
                    fragment = new ItineraryFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_bookmark:
                    fragment = new BookmarkFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_chats:
                    //fragment = new ChatFragment();
                    fragment = new CreateGroupChannelFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        drawerToggle.syncState();
//    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        // Pass any configuration change to the drawer toggles
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
}
