package com.project42.iplanner.Groups;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project42.iplanner.AppConfig;
import com.project42.iplanner.Chats.ChatActivity;
import com.project42.iplanner.Chats.ConnectionManager;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.ListUtils;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.android.volley.VolleyLog.TAG;

public class GroupChannelListFragment extends Fragment {

    private static final int INTENT_REQUEST_NEW_GROUP_CHANNEL = 302;
    public static final String EXTRA_GROUP_CHANNEL_URL = "GROUP_CHANNEL_URL";
    public static final String EXTRA_GROUP_TITLE = "GROUP_CHANNEL_TITLE";
    private static final String CONNECTION_HANDLER_ID = "CONNECTION_HANDLER_GROUP_CHANNEL_LIST";
    private static final String CHANNEL_HANDLER_ID = "CHANNEL_HANDLER_GROUP_CHANNEL_LIST";
    private static final int CHANNEL_LIST_LIMIT = 15;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GroupChannelListAdapter mChannelListAdapter;
    private FloatingActionButton mCreateChannelFab;
    private GroupChannelListQuery mChannelListQuery;
    private SwipeRefreshLayout mSwipeRefresh;


    public GroupChannelListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("LIFECYCLE", "GroupChannelListFragment onCreateView()");

        View rootView = inflater.inflate(R.layout.fragment_group_channel_list, container, false);

        setRetainInstance(true);

        // Change action bar title
        ((GroupChannelActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.all_group_channels));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_group_channel_list);
        mCreateChannelFab = (FloatingActionButton) rootView.findViewById(R.id.fab_group_channel_list);
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout_group_channel_list);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                refresh();
            }
        });

        mCreateChannelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateGroupChannelActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_NEW_GROUP_CHANNEL);
            }
        });

        mChannelListAdapter = new GroupChannelListAdapter(getActivity());
        //refresh();
        //mChannelListAdapter.load();

        setUpRecyclerView();
        setUpChannelListAdapter();

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d("LIFECYCLE", "GroupChannelListFragment onResume()");

        ConnectionManager.addConnectionManagementHandler(CONNECTION_HANDLER_ID, new ConnectionManager.ConnectionManagementHandler() {
            @Override
            public void onConnected(boolean reconnect) {
                refresh();
            }
        });

        SendBird.addChannelHandler(CHANNEL_HANDLER_ID, new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
            }

            @Override
            public void onChannelChanged(BaseChannel channel) {
                //mChannelListAdapter.clearMap();
                mChannelListAdapter.updateOrInsert(channel);
            }

            @Override
            public void onTypingStatusUpdated(GroupChannel channel) {
                mChannelListAdapter.notifyDataSetChanged();
            }
        });

        super.onResume();
    }

    @Override
    public void onPause() {
        //mChannelListAdapter.save();

        Log.d("LIFECYCLE", "GroupChannelListFragment onPause()");

        //ConnectionManager.removeConnectionManagementHandler(CONNECTION_HANDLER_ID);
        //SendBird.removeChannelHandler(CHANNEL_HANDLER_ID);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_REQUEST_NEW_GROUP_CHANNEL) {
            if (resultCode == RESULT_OK) {
                // Channel successfully created
                // Enter the newly created channel.
                String newChannelUrl = data.getStringExtra(CreateGroupChannelFragment.EXTRA_NEW_CHANNEL_URL);
                String newChannelTitle = data.getStringExtra(CreateGroupChannelFragment.EXTRA_NEW_CHANNEL_TITLE);
                if (newChannelUrl != null) {
                    enterGroupChannel(newChannelUrl, newChannelTitle);
                }
            } else {
                Log.d("GrChLIST", "resultCode not STATUS_OK");
            }
        }
    }

    // Sets up recycler view
    private void setUpRecyclerView() {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mChannelListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // If user scrolls to bottom of the list, loads more channels.
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mLayoutManager.findLastVisibleItemPosition() == mChannelListAdapter.getItemCount() - 1) {
                    loadNextChannelList();
                }
            }
        });
    }

    // Sets up channel list adapter
    private void setUpChannelListAdapter() {
        mChannelListAdapter.setOnItemClickListener(new GroupChannelListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GroupChannel channel) {
                enterGroupChannel(channel);
            }
        });

        mChannelListAdapter.setOnItemLongClickListener(new GroupChannelListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final GroupChannel channel) {
                showChannelOptionsDialog(channel);
            }
        });
    }

    /**
     * Displays a dialog listing channel-specific options.
     */
    private void showChannelOptionsDialog(final GroupChannel channel) {
        String[] options;
        final boolean pushCurrentlyEnabled = channel.isPushEnabled();

        options = pushCurrentlyEnabled
                ? new String[]{"Leave channel", "Turn push notifications OFF"}
                : new String[]{"Leave channel", "Turn push notifications ON"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Channel options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Show a dialog to confirm that the user wants to leave the channel.
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Leave channel " + channel.getName() + "?")
                                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            leaveChannel(channel);
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .create().show();
                        } else if (which == 1) {
                            setChannelPushPreferences(channel, !pushCurrentlyEnabled);
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * Turns push notifications on or off for a selected channel.
     * @param channel   The channel for which push preferences should be changed.
     * @param on    Whether to set push notifications on or off.
     */
    private void setChannelPushPreferences(final GroupChannel channel, final boolean on) {
        // Change push preferences.
        channel.setPushPreference(on, new GroupChannel.GroupChannelSetPushPreferenceHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                String toast = on
                        ? "Push notifications have been turned ON"
                        : "Push notifications have been turned OFF";

                Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * Enters a Group Channel. Upon entering, a GroupChatFragment will be inflated
     * to display messages within the channel.
     *
     * @param channel The Group Channel to enter.
     */
    void enterGroupChannel(GroupChannel channel) {
        final String channelUrl = channel.getUrl();
        String me = SendBird.getCurrentUser().getUserId();
        String mChannelTitle = channel.getName();
        // If 1-to-1 chat, then display channel title as other party's name
        if (channel.getMemberCount() == 2) {
            if (me.equals(channel.getMembers().get(0).getUserId()))
                mChannelTitle = channel.getMembers().get(1).getUserId();

            else if (!me.equals(channel.getMembers().get(0).getUserId()))
                mChannelTitle = channel.getMembers().get(0).getUserId();

            else
                mChannelTitle = me;
        }
        enterGroupChannel(channelUrl, mChannelTitle);
    }

    /**
     * Enters a Group Channel with a URL.
     *
     * @param channelUrl The URL of the channel to enter.
     */
    void enterGroupChannel(String channelUrl, String channelTitle) {
        /*ChatFragment fragment = ChatFragment.newInstance(channelUrl);
        getFragmentManager().beginTransaction()
                .replace(R.id.container_group_channel, fragment)
                .addToBackStack(null)
                .commit();*/
        Intent intent = new Intent((GroupChannelActivity)getActivity(), ChatActivity.class);
        intent.putExtra(EXTRA_GROUP_CHANNEL_URL, channelUrl);
        intent.putExtra(EXTRA_GROUP_TITLE, channelTitle);
        startActivity(intent);

    }

    private void refresh() {
        refreshChannelList(CHANNEL_LIST_LIMIT);
    }

    /**
     * Creates a new query to get the list of the user's Group Channels,
     * then replaces the existing dataset.
     *
     * @param numChannels The number of channels to load.
     */
    private void refreshChannelList(int numChannels) {
        mChannelListQuery = GroupChannel.createMyGroupChannelListQuery();
        //mChannelListQuery.setLimit(0);
        mChannelListQuery.setIncludeEmpty(true);

        mChannelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                mChannelListAdapter.setGroupChannelList(list);
                mChannelListAdapter.notifyDataSetChanged();
            }
        });

        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    /**
     * Loads the next channels from the current query instance.
     */
    private void loadNextChannelList() {
        mChannelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();
                    return;
                }

                for (GroupChannel channel : list) {
                    mChannelListAdapter.addLast(channel);
                }
            }
        });
    }
    /**
     * Leaves a Group Channel.
     *
     * @param channel The channel to leave.
     */
    private void leaveChannel(final GroupChannel channel) {
        channel.leave(new GroupChannel.GroupChannelLeaveHandler() {
            @Override
            public void onResult(SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                // Re-query message list
                refresh();
            }
        });
    }
}
