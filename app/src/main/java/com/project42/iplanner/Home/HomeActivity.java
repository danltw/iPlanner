package com.project42.iplanner.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Groups.CreateGroupChannelFragment;
import com.project42.iplanner.R;

import com.project42.iplanner.Accounts.ProfileFragment;
import com.project42.iplanner.Bookmarks.BookmarkFragment;
import com.project42.iplanner.Itineraries.ItineraryFragment;
import com.project42.iplanner.Chats.ChatFragment;
import com.sendbird.android.SendBird;


public class HomeActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new HomeFragment());

        // Initialise Sendbird SDK
        SendBird.init(AppConfig.SBAPP_ID, this.getApplicationContext());

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
}
