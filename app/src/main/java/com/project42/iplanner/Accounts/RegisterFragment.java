package com.project42.iplanner.Accounts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.TextUtils;

import org.w3c.dom.Text;

public class RegisterFragment extends Fragment {

    private EditText et_username;
    private EditText et_password;
    private EditText et_email;
    private EditText et_cfmPassword;
    private Button btn_submit;
    private Button btn_back;

    public RegisterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        et_username = view.findViewById(R.id.reg_username_et);
        et_password = view.findViewById(R.id.reg_password_et);
        et_email = view.findViewById(R.id.reg_email_et);
        et_cfmPassword = view.findViewById(R.id.cfm_password_et);
        btn_submit = view.findViewById(R.id.submit_btn);
        btn_back = view.findViewById(R.id.back_btn);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            // Login button on click
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                String cfmPassword = et_cfmPassword.getText().toString();
                String email = "NIL";

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cfmPassword)) {
                    Toast.makeText(getActivity(), "Please fill in both username and password", Toast.LENGTH_LONG).show();
                }
                else if (!password.equals(cfmPassword))
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_LONG).show();
                else {
                    AccountController ac = new AccountController(username, password, getActivity());
                    if (!TextUtils.isEmpty(et_email.getText().toString())) {
                        email = et_email.getText().toString();
                        ac.regUser(username, password, email);
                    }
                    else
                        ac.regUser(username, password, email);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
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
