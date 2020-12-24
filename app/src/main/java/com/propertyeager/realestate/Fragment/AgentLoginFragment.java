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
import com.propertyeager.realestate.Wrapper.GetAgentLogin.GetAgentLoginWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentLoginFragment extends Fragment {



    EditText ed_username, ed_password;
    CheckBox remembercheckBox;
    LinearLayout ly_btn_login;
    TextView not_register;

    ProgressLoader progressBar;
    SessionManager sessionManager;
    ConnectionDetector cd;
    private boolean isinternetpresent = false;

    public static AgentLoginFragment newInstance() {
        return new AgentLoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agent_login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager(getContext());
        cd = new ConnectionDetector(getContext());
        isinternetpresent = cd.isConnectingToInternet();

        ed_username = getView().findViewById(R.id.ed_username);
        ed_password = getView().findViewById(R.id.ed_password);
       //
        remembercheckBox = getView().findViewById(R.id.remembercheckBox);
        //
        not_register = getView().findViewById(R.id.not_register);
        progressBar = getView().findViewById(R.id.progressBar);
        //
        ly_btn_login = getView().findViewById(R.id.ly_btn_login);

        if(sessionManager.getAgent()!=null){
            ed_username.setText(sessionManager.GetRememberAgentEmail());
            ed_password.setText(sessionManager.GetRememberAgentPassword());
        }

        not_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment agentSignupFragment = new AgentSignupFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, agentSignupFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        ly_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ed_username.getText().toString().isEmpty() || ed_username.getText().toString() == null) {
                    Toast.makeText(getContext(), "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else if (ed_password.getText().toString().isEmpty() || ed_password.getText().toString() == null) {
                    Toast.makeText(getContext(), "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (isinternetpresent) {
                        if (remembercheckBox.isChecked()){
                            sessionManager.RememberAgentCredencial(ed_username.getText().toString(), ed_password.getText().toString());
                        }

                        agentLogin();
                    } else {
                        Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
    private void agentLogin() {
        progressBar.setVisibility(View.VISIBLE);
        WebServiceFactory.getInstance().GET_AGENT_LOGIN_WRAPPER_CALL(ed_username.getText().toString().trim(), ed_password.getText().toString().trim())
                .enqueue(new Callback<GetAgentLoginWrapper>() {
                    @Override
                    public void onResponse(Call<GetAgentLoginWrapper> call, Response<GetAgentLoginWrapper> response) {
                        try {
                            if (response.body() != null) {
                                // JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                if (response.body().getResult().getMsg().equalsIgnoreCase("OK")) {
                                    // JSONObject responseobj = object.getJSONObject("data");
                                    sessionManager.logoutAgent();
                                    sessionManager.logoutClient();
                                    sessionManager.CreateAgentSession(

                                            response.body().getResult().getData().getId(),
                                            response.body().getResult().getData().getName(),
                                            response.body().getResult().getData().getEmail(),
                                            response.body().getResult().getData().getNumber(),
                                            response.body().getResult().getData().getPassword(),
                                            response.body().getResult().getData().getAgencyLogo(),
                                            response.body().getResult().getData().getAgencyName(),
                                            response.body().getResult().getData().getDiscription()
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
                    public void onFailure(Call<GetAgentLoginWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Invalid Email or Password provided", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}