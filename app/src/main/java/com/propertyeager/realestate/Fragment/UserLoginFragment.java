package com.propertyeager.realestate.Fragment;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.ConnectionDetector;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Views.ProgressLoader;

import com.propertyeager.realestate.Wrapper.GetUserLogin.GetUserLoginWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginFragment extends Fragment {


    private EditText ed_email,ed_password;
    private LinearLayout btn_login;
    private CheckBox rememberCheckBox;
    private TextView not_register;
    ProgressLoader progressBar;
    SessionManager sessionManager;
    ConnectionDetector cd;
    private boolean isinternetpresent = false;
    public static UserLoginFragment newInstance() {
        return new UserLoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        sessionManager = new SessionManager(getContext());
        cd = new ConnectionDetector(getContext());
        isinternetpresent = cd.isConnectingToInternet();


        ed_email = getView().findViewById(R.id.ed_username);
        ed_password = getView().findViewById(R.id.ed_password);
        progressBar = getView().findViewById(R.id.progressBar);
        not_register = getView().findViewById(R.id.not_register);


        btn_login = getView().findViewById(R.id.ly_btn_login);

        rememberCheckBox = getView().findViewById(R.id.remembercheckBox);

        if(sessionManager.getClient()!=null){
            ed_email.setText(sessionManager.GetRememberClientEmail());
            ed_password.setText(sessionManager.GetRememberClientPassword());
        }

        not_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userSignupFragment = new UserSignupFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, userSignupFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed_email.getText().toString().isEmpty() || ed_email.getText().toString() == null) {
                    Toast.makeText(getContext(), "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else if (ed_password.getText().toString().isEmpty() || ed_password.getText().toString() == null) {
                    Toast.makeText(getContext(), "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (isinternetpresent) {
                        if (rememberCheckBox.isChecked()){
                            sessionManager.RememberClientCredencial(ed_email.getText().toString(), ed_password.getText().toString());
                        }
                        UserLogin();
                    } else {
                        Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
    private void UserLogin() {
        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().getUserLogin(ed_email.getText().toString().trim(), ed_password.getText().toString().trim())
                .enqueue(new Callback<GetUserLoginWrapper>() {
                    @Override
                    public void onResponse(Call<GetUserLoginWrapper> call, Response<GetUserLoginWrapper> response) {
                        try {
                            if (response.body() != null) {
                               // JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                if (response.body().getResult().getMsg().equalsIgnoreCase("OK")) {
                                   // JSONObject responseobj = object.getJSONObject("data");
                                    sessionManager.logoutAgent();
                                    sessionManager.logoutClient();

                                    sessionManager.CreateClientSession(

                                            response.body().getResult().getData().getId(),
                                            response.body().getResult().getData().getName(),
                                            response.body().getResult().getData().getEmail(),
                                            response.body().getResult().getData().getNumber(),
                                            response.body().getResult().getData().getPassword()
                                           /* responseobj.getString("id"),
                                            responseobj.getString("name"),
                                            responseobj.getString("email"),
                                            responseobj.getString("number"),
                                            responseobj.getString("password")*/
                                    );
                                    Fragment homeFragment = new HomeFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, homeFragment);

                                    transaction.commit();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_SHORT).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetUserLoginWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Invalid Email or Password provided", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}