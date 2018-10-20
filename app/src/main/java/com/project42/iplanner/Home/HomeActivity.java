package com.project42.iplanner.Home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.project42.iplanner.Chats.ConnectionManager;
import com.project42.iplanner.Groups.CreateGroupChannelActivity;
import com.project42.iplanner.Groups.GroupChannelActivity;
import com.project42.iplanner.POIs.POISearchFragment;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Groups.CreateGroupChannelFragment;
import com.project42.iplanner.R;

import com.project42.iplanner.Accounts.ProfileFragment;
import com.project42.iplanner.Bookmarks.BookmarkFragment;
import com.project42.iplanner.Itineraries.ItineraryFragment;
import com.project42.iplanner.Chats.ChatFragment;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.project42.iplanner.Settings.SettingsFragment;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = getSupportActionBar();
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar1);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());

        // Initialise Sendbird SDK
        SendBird.init(AppConfig.SBAPP_ID, this.getApplicationContext());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.drawer_home:
                fragment = new HomeFragment();
                break;
            case R.id.drawer_itinerary:
                fragment = new ItineraryFragment();
                break;
            case R.id.drawer_bookmark:
                fragment = new BookmarkFragment();
                break;
            case R.id.drawer_search:
                fragment = new POISearchFragment();
                break;
            case R.id.drawer_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.drawer_chat:
                fragment = new ChatFragment();
                break;
            case R.id.drawer_settings:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new HomeFragment();
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

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
                    //fragment = new CreateGroupChannelFragment();
                    //loadFragment(fragment);
                    ConnectionManager.login("", new SendBird.ConnectHandler() {
                        @Override
                        public void onConnected(User user, SendBirdException e) {

                            if (e != null) {
                                e.printStackTrace(); // error
                            }
                            showOrCreateChats();
                        }
                    });

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

    private void showOrCreateChats() {
        // If current user has no existing chats, then force user to start chat
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setIncludeEmpty(true);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {

            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {    // Error.
                    e.printStackTrace();
                    return;
                }
                if (list.isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, CreateGroupChannelActivity.class);
                    startActivity(intent);
                }
                else if (!list.isEmpty()) {
                    Intent intent = new Intent(HomeActivity.this, GroupChannelActivity.class);
                    startActivity(intent);
                }
             }
        });
    }
}
