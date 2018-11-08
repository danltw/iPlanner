package com.project42.iplanner.Accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.SharedManager;
import com.project42.iplanner.Utilities.TextUtils;

public class ProfileFragment extends Fragment {
    private EditText et_username;
    private EditText et_email;
    private EditText et_oldPassword;
    private TextView tv_oldPassword;
    private TextView tv_newPassword;
    private TextView et_newPassword;
    private TextView tv_cfmPassword;
    private EditText et_cfmPassword;
    private Button btn_edit;
    private Button btn_save;
    private Button btn_cancel;

    private String oldEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        oldEmail = SharedManager.getInstance().getEmail();
        et_username = view.findViewById(R.id.username_et);
        et_email = view.findViewById(R.id.email_et);
        tv_oldPassword = view.findViewById(R.id.oldPass_tv);
        et_oldPassword = view.findViewById(R.id.oldPass_et);
        tv_newPassword = view.findViewById(R.id.newPass_tv);
        et_newPassword = view.findViewById(R.id.newPass_et);
        tv_cfmPassword = view.findViewById(R.id.cfmPass_tv);
        et_cfmPassword = view.findViewById(R.id.cfmPass_et);
        btn_edit = view.findViewById(R.id.edit_btn);
        btn_save = view.findViewById(R.id.save_btn);
        btn_cancel = view.findViewById(R.id.cancel_btn);

        et_email.setText(SharedManager.getInstance().getEmail());
        et_username.setText(SharedManager.getInstance().getUser());

        setViewableState();

        // What happens when edit button is pressed
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditableState();

                // What happens when save button is pressed
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String oldPassword = et_oldPassword.getText().toString();
                        String newPassword = et_newPassword.getText().toString();
                        String email = et_email.getText().toString();
                        AccountController ac = new AccountController(getActivity());

                        String cfmPassword = et_cfmPassword.getText().toString();
                        // Only check if password field is not empty
                        // assume password field is empty by default
                        // Password not empty; check if it fulfills requirements

                        if (!TextUtils.isEmpty(oldPassword)) {
                            if (!TextUtils.passReqValidator(newPassword)) {
                                Toast.makeText(getActivity(), "New Password does not meet requirements", Toast.LENGTH_LONG).show();
                            }
                            // Password fulfills requirements
                            else if (TextUtils.passReqValidator(newPassword)) {
                                // Check if confirm password and password are the same
                                if (!newPassword.equals(cfmPassword))
                                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_LONG).show();
                                else if (newPassword.equals(cfmPassword)) {
                                    if (!TextUtils.isEmpty(email)) {
                                        if (!oldEmail.equals(email)) {
                                            SharedManager.getInstance().setEmail(email);
                                            ac.updateUser(SharedManager.getInstance().getUser(), oldPassword, newPassword, email);
                                        }
                                        setViewableState();
                                    }
                                    else {
                                        ac.updateUser(SharedManager.getInstance().getUser(), oldPassword, newPassword, email);
                                        setViewableState();
                                    }
                                }

                            }
                        }

                        else if (TextUtils.isEmpty(oldPassword)) {

                            if (!TextUtils.isEmpty(email)) {
                                if (!oldEmail.equals(email)) {
                                    SharedManager.getInstance().setEmail(email);
                                    ac.updateUser(SharedManager.getInstance().getUser(), null, null, email);
                                }
                                setViewableState();
                            }
                        }

                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setViewableState();
                    }
                });
            }
        });

        et_oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    et_newPassword.setEnabled(true);
                    et_cfmPassword.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void setEditableState() {
        et_email.setEnabled(true);
        tv_oldPassword.setText("Old Password:");
        et_oldPassword.setEnabled(true);
        et_oldPassword.setText("");
        tv_newPassword.setVisibility(View.VISIBLE);
        et_newPassword.setVisibility(View.VISIBLE);
        tv_cfmPassword.setVisibility(View.VISIBLE);
        et_cfmPassword.setVisibility(View.VISIBLE);

        btn_save.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_edit.setVisibility(View.GONE);
    }

    private void setViewableState() {
        et_email.setEnabled(false);
        tv_oldPassword.setText("Password:");
        et_oldPassword.setEnabled(false);
        tv_newPassword.setVisibility(View.GONE);
        et_newPassword.setVisibility(View.GONE);
        et_newPassword.setEnabled(false);
        tv_cfmPassword.setVisibility(View.GONE);
        et_cfmPassword.setVisibility(View.GONE);
        et_cfmPassword.setEnabled(false);

        et_cfmPassword.setText("");
        et_newPassword.setText("");

        btn_save.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        btn_edit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
