package com.project42.iplanner.Groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.project42.iplanner.Chats.ChatActivity;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.TextUtils;
import com.sendbird.android.User;

public class CreateGroupNameFragment extends Fragment {

    private Button btn_newGrpChat;
    private EditText et_newGrpName;
    OnGroupNameEntered grpName;

    interface OnGroupNameEntered {
        public void onGroupEntered(String grpName);
    }

    public static Fragment newInstance() {
        CreateGroupNameFragment mFragment = new CreateGroupNameFragment();
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_grp_channel, container, false);
        btn_newGrpChat = rootView.findViewById(R.id.newGrpChat_btn);
        // ToDo: use interface to pass data back to Activity
        btn_newGrpChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_newGrpName.getText().toString())) {
                    et_newGrpName.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                else {
                    passGrpName(et_newGrpName.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        ((CreateGroupChannelActivity) getActivity()).setState(CreateGroupChannelActivity.STATE_ENTER_GROUP_NAME);

        et_newGrpName = rootView.findViewById(R.id.newGrpName_et);
        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        grpName = (OnGroupNameEntered) context;
    }

    public void passGrpName(String _grpName) {
        grpName.onGroupEntered(_grpName);
    }
}
