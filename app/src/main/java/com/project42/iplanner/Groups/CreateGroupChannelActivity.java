package com.project42.iplanner.Groups;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Chats.ChatActivity;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.ListUtils;
import com.project42.iplanner.Utilities.TextUtils;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelParams;
import com.sendbird.android.Member;
import com.sendbird.android.OperatorListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

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

    private String dummyUser = SendBird.getCurrentUser().getUserId(); // this is used to simulate a dummy user

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
        admins.add(dummyUser);
        if (userIds.size() > 1) {


           params = new GroupChannelParams()
                    .setPublic(false)
                    .setEphemeral(false)
                    .setDistinct(false)
                    .addUserIds(userIds)
                    .setOperatorUserIds(admins)
                    .setName(grpName)
                    //.setCoverImage(null)
                    //.setCoverUrl(null)
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
                    //.setCoverImage(null)
                    //.setCoverUrl(null)
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

                final String groupName = groupChannel.getName();
                final String groupUrl = groupChannel.getUrl();

                // get list of admins and members
                OperatorListQuery operatorListQuery = groupChannel.createOperatorListQuery();
                operatorListQuery.next(new OperatorListQuery.OperatorListQueryResultHandler() {
                    @Override
                    public void onResult(List<User> list, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            return;
                        }

                        ArrayList<String> members = new ArrayList();
                        ArrayList<String> admins = new ArrayList();

                        for (Member m : groupChannel.getMembers())
                            members.add(m.getUserId());

                        for (User u : list)
                            admins.add(u.getUserId());

                        // add new group into database
                        addGroup(groupName, groupUrl, members, admins);
                    }
                });

                Intent intent = new Intent(CreateGroupChannelActivity.this, ChatActivity.class);
                intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                intent.putExtra(EXTRA_NEW_CHANNEL_TITLE, groupChannel.getName());
                String mChannelTitle = null;
                String me = SendBird.getCurrentUser().getUserId();
                if (groupChannel.getMemberCount() == 2) {
                    if (me.equals(groupChannel.getMembers().get(0).getUserId()))
                        mChannelTitle = groupChannel.getMembers().get(1).getUserId();

                    else if (!me.equals(groupChannel.getMembers().get(0).getUserId()))
                        mChannelTitle = groupChannel.getMembers().get(0).getUserId();

                    else
                        mChannelTitle = me;
                    intent.putExtra(EXTRA_NEW_CHANNEL_TITLE, mChannelTitle);
                }
                startActivity(intent);
                finish();
                //setResult(RESULT_OK, intent);
            }
        });
    }
    // Add a single group record
    private void addGroup(final String groupName, final String channelUrl, final ArrayList<String> memberNames, final ArrayList<String> adminNames) {
        // Tag used to cancel the request
        String tag_string_req = "req_add_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Group Insertion Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Group Insertion Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        // Todo: If successful: display message to inform user that group is created
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group Insertion Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (!TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(channelUrl) && memberNames != null &&
                        adminNames != null) {
                    try {
                        params.put("method", "addGroup");
                        params.put("grpUrl", channelUrl);
                        params.put("grpName", groupName);
                        params.put("memberNames", ListUtils.getStrNames(memberNames));
                        params.put("adminNames", ListUtils.getStrNames(adminNames));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(CreateGroupChannelActivity.this);
        queue.add(strReq);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
