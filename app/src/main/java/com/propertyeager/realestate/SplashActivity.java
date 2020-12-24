package com.propertyeager.realestate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Wrapper.GetTokenUpdateWrapper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity {

    private final int SPLASH_DELAY_TIME = 2000;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

       // registerFirebase();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY_TIME);

        // Enables Always-on

    }

    private void registerFirebase() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("theaquaticmall");
        agent_token(refreshedToken);
        Log.e("TAG", "FCM ID : " + refreshedToken);
    }

    private void agent_token(final String token) {

        WebServiceFactory.getInstance().Agent_Token(sessionManager.GetAgentId())
                .enqueue(new Callback<GetTokenUpdateWrapper>() {
                    @Override
                    public void onResponse(Call<GetTokenUpdateWrapper> call, Response<GetTokenUpdateWrapper> response) {
                        try {
                            if (response.body() != null) {
                                if(response.body().getStatusCode()==200)
                                    Log.e("TAG_onSuccess", "token update successfully");
                                else {
                                    Log.e("TAG_onSuccess", "token update failed");
                                }
                            } else {
                                Log.e("TAG_onSuccess", "token update failed");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<GetTokenUpdateWrapper> call, Throwable t) {
                        //Snackbar.make(getView(), "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(SplashActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                    }
                });
    }
}