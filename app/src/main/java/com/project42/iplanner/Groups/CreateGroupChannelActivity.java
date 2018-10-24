package com.project42.iplanner.Groups;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.project42.iplanner.Chats.ChatActivity;
import com.project42.iplanner.R;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupChannelActivity extends AppCompatActivity implements
        SelectUserFragment.UsersSelectedListener, CreateGroupNameFragment.OnGroupNameEntered {

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";
    public static final String EXTRA_NEW_CHANNEL_TITLE = "EXTRA_NEW_CHANNEL_TITLE";

    static final int STATE_SELECT_USERS = 0;
    static final int STATE_ENTER_GROUP_NAME = 1;
    //static final int STATE_SELECT_DISTINCT = 1;

    private Button mNextButton, mCreateButton;

    private List<String> mSelectedIds;
    private boolean mIsDistinct = true;

    private int mCurrentState;

    private Toolbar mToolbar;

    private String grpName;

    private String dummyUser = "hardcoded user1"; // this is used to simulate a dummy user

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_group_channel);

        mSelectedIds = new ArrayList<>();

        if (savedInstanceState == null) {
            Fragment fragment = SelectUserFragment.newInstance();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_create_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        mNextButton = (Button) findViewById(R.id.button_create_group_channel_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_SELECT_USERS && mSelectedIds.size() > 1) {
                    Fragment fragment = CreateGroupNameFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_create_group_channel, fragment)
                            .addToBackStack(null)
                            .commit();
                }
                else {
                    createGroupChannel(mSelectedIds, mIsDistinct);
                }
            }
        });
        mNextButton.setEnabled(false);

        mCreateButton = (Button) findViewById(R.id.button_create_group_channel_create);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentState == STATE_ENTER_GROUP_NAME) {
//                if (mCurrentState == STATE_SELECT_DISTINCT) {
                    //mIsDistinct = PreferenceUtils.getGroupChannelDistinct();
                    createGroupChannel(mSelectedIds, mIsDistinct);
                }
            }
        });
        mCreateButton.setEnabled(false);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_create_group_channel);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setState(int state) {
        if (state == STATE_SELECT_USERS) {
            mCurrentState = STATE_SELECT_USERS;
            mNextButton.setVisibility(View.VISIBLE);
            setActionBarTitle("Select Members");
        } else if (state == STATE_ENTER_GROUP_NAME){
            mCurrentState = STATE_ENTER_GROUP_NAME;
            mNextButton.setVisibility(View.GONE);
            setActionBarTitle("Create Group Name");
        }
    }

    @Override
    public void onUserSelected(boolean selected, String userId) {
        if (selected) {
            mSelectedIds.add(userId);
        } else {
            mSelectedIds.remove(userId);
        }

        if (mSelectedIds.size() > 0) {
            //mCreateButton.setEnabled(true);
            mNextButton.setEnabled(true);
        } else {
            //mCreateButton.setEnabled(false);
            mNextButton.setEnabled(false);
        }
    }

    @Override
    public void onGroupEntered(String newGrpName) {
        grpName = newGrpName;
        createGroupChannel(mSelectedIds, mIsDistinct);
    }

    /*@Override
    public void onDistinctSelected(boolean distinct) {
        mIsDistinct = distinct;
    }*/

    /**
     * Creates a new Group Channel.
     *
     * Note that if you have not included empty channels in your GroupChannelListQuery,
     * the channel will not be shown in the user's channel list until at least one message
     * has been sent inside.
     *
     * @param userIds   The users to be members of the new channel.
     * @param distinct  Whether the channel is unique for the selected members.
     *                  If you attempt to create another Distinct channel with the same members,
     *                  the existing channel instance will be returned.
     */
    private void createGroupChannel(List<String> userIds, boolean distinct) {

        List<String> admins = new ArrayList();
        GroupChannelParams params = new GroupChannelParams();
        if (userIds.size() > 1) {
            admins.add(dummyUser);

           params = new GroupChannelParams()
                    .setPublic(false)
                    .setEphemeral(false)
                    .setDistinct(false)
                    .addUserIds(userIds)
                    .setOperatorUserIds(admins)
                    .setName(grpName)
                    .setCoverImage(null)
                    .setCoverUrl(null)
                    .setData(null)
                    .setCustomType(null);
        }

        else if (userIds.size() == 1) {
            admins.addAll(userIds);

            params = new GroupChannelParams()
                    .setPublic(false)
                    .setEphemeral(false)
                    .setDistinct(false)
                    .addUserIds(userIds)
                    .setOperatorUserIds(admins)
                    .setName(null)
                    .setCoverImage(null)
                    .setCoverUrl(null)
                    .setData(null)
                    .setCustomType(null);
        }



        GroupChannel.createChannel(params, new GroupChannel.GroupChannelCreateHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                Intent intent = new Intent(CreateGroupChannelActivity.this, ChatActivity.class);
                intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                intent.putExtra(EXTRA_NEW_CHANNEL_TITLE, groupChannel.getName());
                String mChannelTitle = null;
                if (groupChannel.getMemberCount() == 2) {
                    if (groupChannel.getMembers().get(0).getUserId().equals(SendBird.getCurrentUser().getUserId())
                            && !groupChannel.getMembers().get(1).getUserId().equals(SendBird.getCurrentUser().getUserId()))
                        mChannelTitle = groupChannel.getMembers().get(1).getUserId();
                    else
                        mChannelTitle = SendBird.getCurrentUser().getUserId();

                    intent.putExtra(EXTRA_NEW_CHANNEL_TITLE, mChannelTitle);
                }
                startActivity(intent);
                finish();
                //setResult(RESULT_OK, intent);
            }
        });
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
