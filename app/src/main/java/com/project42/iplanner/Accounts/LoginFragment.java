package com.project42.iplanner.Accounts;

import android.content.Context;
import android.net.Uri;
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

public class LoginFragment extends Fragment {

    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_reg;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        et_username = view.findViewById(R.id.login_username_et);
        et_password = view.findViewById(R.id.login_password_et);
        btn_login = view.findViewById(R.id.login_btn);
        btn_reg = view.findViewById(R.id.reg_btn);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            // Login button on click
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Please fill in both username and password", Toast.LENGTH_LONG).show();
                }
                else {
                    AccountController ac = new AccountController(username, password, getActivity());
                    ac.loginUser(username, password);
                }
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(LoginFragment.class.getSimpleName());
                ft.replace(R.id.login_container, new RegisterFragment());
                ft.commit();
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
