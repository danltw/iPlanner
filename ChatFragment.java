package com.project42.iplanner.Chats;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.project42.iplanner.Groups.GroupChannelListFragment;
import com.project42.iplanner.R;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private Button startChatButton;
    private String user = "hardcoded user";

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(@NonNull String channelUrl) {
        ChatFragment fragment = new ChatFragment();

        Bundle args = new Bundle();
        args.putString(GroupChannelListFragment.EXTRA_GROUP_CHANNEL_URL, channelUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        startChatButton = view.findViewById(R.id.startChat_Btn);
        //startChatButton.setOnClickListener(this);

        return view;
    }
}
