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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.ConnectionDetector;

import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetUserSignup.GetUserSignupWrapper;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSignupFragment extends Fragment {



    private ProgressLoader progressBar;
    EditText mName, mNumber, mEmail, mPassword;
    LinearLayout ly_btn_register;
    TextView already_register;

    ConnectionDetector cd;
    private boolean isinternetpresent = false;

    public static UserSignupFragment newInstance() {
        return new UserSignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.client_register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        cd = new ConnectionDetector(getContext());
        isinternetpresent = cd.isConnectingToInternet();
        //
        mName = getView().findViewById(R.id.ed_username);
        mNumber = getView().findViewById(R.id.ed_number);
        mEmail = getView().findViewById(R.id.ed_email);
        mPassword = getView().findViewById(R.id.ed_password);
        progressBar = getView().findViewById(R.id.progressBar);
        already_register = getView().findViewById(R.id.already_register);

        //
        ly_btn_register = getView().findViewById(R.id.ly_btn_register);

        already_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userLoginFragment = new UserLoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, userLoginFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        ly_btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty() ) {
                    Toast.makeText(getContext(), "Email Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else if (mPassword.getText().toString().isEmpty() && mPassword.length()<8) {
                    Toast.makeText(getContext(), "Password Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else if( mPassword.length()<8){
                    Toast.makeText(getContext(), "Password length must be atleast 8 characters.", Toast.LENGTH_SHORT).show();
                    }
                else if( mName.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else if( mNumber.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Number Cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (isinternetpresent) {

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
        WebServiceFactory.getInstance().userSignUp(mName.getText().toString().trim(), mNumber.getText().toString().trim(),
                mEmail.getText().toString().trim(), mPassword.getText().toString().trim())
                .enqueue(new Callback<GetUserSignupWrapper>() {
                    @Override
                    public void onResponse(Call<GetUserSignupWrapper> call, Response<GetUserSignupWrapper> response) {
                        try {
                            if (response.body() != null) {
                                JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                                if (response.body().getResult().getFlag()) {
                                    Fragment userLoginFragment = new UserLoginFragment();
                                    // consider using Java coding conventions (upper first char class names!!!)
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                                    // Replace whatever is in the fragment_container view with this fragment,
                                    // and add the transaction to the back stack
                                    transaction.replace(R.id.container, userLoginFragment);
                                    transaction.addToBackStack(null);

                                    // Commit the transaction
                                    transaction.commit();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_LONG).show();
                                    //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                                }
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), response.body().getResult().getMsg(), Toast.LENGTH_LONG).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetUserSignupWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Invalid Email or Password provided", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}