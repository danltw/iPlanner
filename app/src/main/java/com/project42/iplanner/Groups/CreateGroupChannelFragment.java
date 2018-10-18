package com.project42.iplanner.Groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


import com.project42.iplanner.Chats.ChatActivity;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.TextUtils;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserListQuery;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class CreateGroupChannelFragment extends Fragment {

    public static final String EXTRA_NEW_CHANNEL_URL = "EXTRA_NEW_CHANNEL_URL";
    private EditText et_newGrpName;
    private Button btn_newGrpChat;

    private String dummyUser = "hardcoded user1"; // this is used to simulate a dummy user

    private ArrayList<String> userIds = new ArrayList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_grp_channel, container, false);

        et_newGrpName = (EditText) view.findViewById(R.id.newGrpName_et);
        btn_newGrpChat = (Button) view.findViewById(R.id.newGrpChat_btn);


        btn_newGrpChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_newGrpName.getText().toString())) {
                    et_newGrpName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    //setUserList();

                    userIds.add(dummyUser);
                    userIds.add("a1");

                    /*UserListQuery userQuery = SendBird.createUserListQuery();
                    userQuery.next(new UserListQuery.UserListQueryResultHandler() {

                                       @Override
                                       public void onResult(List<User> list, SendBirdException e) {
                                            for (User u : list) {
                                                userIds.add(u.getUserId());
                                            }
                                       }
                                   });*/

                    SendBird.connect(
                            "a1", new SendBird.ConnectHandler() {
                                @Override
                                public void onConnected(User user, SendBirdException e) {
                                    if (e != null) {
                                        // Got Error!!!
                                    }

                                    GroupChannel.createChannelWithUserIds(userIds, true, new GroupChannel.GroupChannelCreateHandler() {
                                        @Override
                                        public void onResult(GroupChannel groupChannel, SendBirdException e) {
                                            if (e != null) {
                                                // Error!
                                                Log.d("Create Channel Err", e.getMessage());
                                                return;
                                            }

                                            // remove current fragment such that it will not be shown again if back is pressed
                                            getActivity().getSupportFragmentManager().popBackStack();

                                            Intent intent = new Intent(getActivity(), ChatActivity.class);
                                            intent.putExtra(EXTRA_NEW_CHANNEL_URL, groupChannel.getUrl());
                                            startActivity(intent);
                                        }
                                    });

                                }
                            });
                }
            }
        });

        return view;
    }

    private void setUserList() {
        UserListQuery mUserListQuery;
        mUserListQuery = SendBird.createUserListQuery();

        mUserListQuery.setLimit(5);

        if (mUserListQuery.isLoading()) {
            Log.d("SENDBIRD LOADING DATA", "Loading...");
        }

        mUserListQuery.next(new UserListQuery.UserListQueryResultHandler() {
            @Override
            public void onResult(List<User> list, SendBirdException e) {
                if (e != null) {
                    // Error!
                    return;
                }

                for (User u : list) {
                    userIds.add(u.getUserId());
                }
                //mListAdapter.setUserList(list);
            }
        });
    }
}
