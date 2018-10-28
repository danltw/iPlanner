package com.project42.iplanner.Groups;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sendbird.android.Member;
import com.sendbird.android.OperatorListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class InviteMemberActivity extends AppCompatActivity{

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SelectableUserListAdapter mListAdapter;
    private Toolbar mToolbar;

    private UserListQuery mUserListQuery;
    private GroupChannel groupChannel;
    private String mChannelUrl;
    private Button mInviteButton;

    private List<String> mSelectedUserIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_member);

        mSelectedUserIds = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_invite_member);
        mListAdapter = new SelectableUserListAdapter(this, false, true);
        mListAdapter.setItemCheckedChangeListener(new SelectableUserListAdapter.OnItemCheckedChangeListener() {
            @Override
            public void OnItemChecked(User user, boolean checked) {
                if (checked) {
                    mSelectedUserIds.add((user.getUserId()));
                } else {
                    mSelectedUserIds.remove(user.getUserId());
                }

                // If no users are selected, disable the invite button.
                if (mSelectedUserIds.size() > 0) {
                    mInviteButton.setEnabled(true);
                } else {
                    mInviteButton.setEnabled(false);
                }
            }
        });

        mToolbar = (Toolbar) findViewById(R.id.toolbar_invite_member);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }

        mChannelUrl = getIntent().getStringExtra(ChatActivity.EXTRA_CHANNEL_URL);

        mInviteButton = (Button) findViewById(R.id.button_invite_member);
        mInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedUserIds.size() > 0) {
                    inviteSelectedMembersWithUserIds();
                }
            }
        });
        mInviteButton.setEnabled(false);

        setUpRecyclerView();

        loadInitialUserList(15);
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

    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mListAdapter.getItemCount() - 1) {
                    loadNextUserList(10);
                }
            }
        });
    }

    private void inviteSelectedMembersWithUserIds() {

        // Get channel instance from URL first.
        GroupChannel.getChannel(mChannelUrl, new GroupChannel.GroupChannelGetHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                // Then invite the selected members to the channel.
                groupChannel.inviteWithUserIds(mSelectedUserIds, new GroupChannel.GroupChannelInviteHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            // Error!
                            return;
                        }

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

                                // update current group details in database
                                updateGroup(groupUrl, members, admins);
                            }
                        });
                        finish();
                    }
                });
            }
        });
    }

    // Update a single group record
    public void updateGroup(final String channelUrl, final ArrayList<String> memberNames, final ArrayList<String> adminNames) {
        // Tag used to cancel the request
        String tag_string_req = "req_update_group";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GROUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Group Update Response: " + response.toString());

                // Process the JSON
                try {
                    JSONArray jsonResponse = new JSONArray(response);

                    // If some errors occurred, return
                    JSONObject errResponse = jsonResponse.getJSONObject(0);
                    boolean error = errResponse.has("error");
                    if (error) {
                        Log.d("Group Update Error: ", errResponse.getString("error"));
                        return;
                    }
                    else {
                        // Todo: If successful: display message to inform user that group is updated
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group Update Error: " + error.getMessage());
            }
        }) {
            // method to pass user input to php page
            @Override
            protected Map<String, String> getParams() {
                // Posting params to php page
                Map<String, String> params = new HashMap<String, String>();
                if (!TextUtils.isEmpty(channelUrl) && memberNames != null &&
                        adminNames != null) {
                    try {
                        params.put("method", "updateGroup");
                        params.put("grpUrl", channelUrl);
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
        RequestQueue queue = Volley.newRequestQueue(InviteMemberActivity.this);
        queue.add(strReq);
    }

    /**
     * Replaces current user list with new list.
     * Should be used only on initial load.
     */
    private void loadInitialUserList(int size) {
        mUserListQuery = SendBird.createUserListQuery();

        mUserListQuery.setLimit(size);
        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(final List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                GroupChannel.getChannel(mChannelUrl, new GroupChannel.GroupChannelGetHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            e.printStackTrace();
                            return;
                        }

                        final List<User> users = list;
                        final List<Member> members = groupChannel.getMembers();

                        ArrayList<User> finalList = new ArrayList();
                        finalList = filterMembers(users, members);
                        if (!finalList.isEmpty() && finalList.size()>1)
                            mListAdapter.setUserList(finalList);
                    }
                });
            }

            ;
        });
    }

    /**
     * Loads users and adds them to current user list.
     *
     * A PreviousMessageListQuery must have been already initialized through {@link #loadInitialUserList(int)}
     */
    private void loadNextUserList(int size) {
        mUserListQuery.setLimit(size);

        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                GroupChannel.getChannel(mChannelUrl, new GroupChannel.GroupChannelGetHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            // Error!
                            e.printStackTrace();
                            return;
                        }

                        final List<User> users = list;
                        final List<Member> members = groupChannel.getMembers();

                        ArrayList<User> finalList = new ArrayList();
                        finalList = filterMembers(users, members);
                        if (!finalList.isEmpty() && finalList.size() > 1) {
                            for (User user : finalList) {
                                mListAdapter.addLast(user);
                            }
                        }
                    }
                });


            }
        });
    }

    private ArrayList<User> filterMembers(List<User> adminsIncluded, List<Member> onlyMembers) {
        ArrayList<User> finalList = new ArrayList();

        for (User user : adminsIncluded) {
            boolean match = false;
            for (Member member : onlyMembers) {
                // Check if user is an existing participant
                if (!member.getUserId().equals(user.getUserId()) && !user.getUserId().equals(SendBird.getCurrentUser().getUserId())) {
                    Log.d("Compare names: ", member.getUserId() + "VS" + user.getUserId());
                } else {
                    match = true;
                    break;
                }
            }
            if (!match)
                // only add if current user has been found to not match against all members
                finalList.add(user);
        }
        return finalList;
    }


}
