package com.propertyeager.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.propertyeager.realestate.Fragment.WelcomeFragment;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Fragment.HomeFragment;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            sessionManager = new SessionManager(this);


            if (sessionManager.isAgentLoggedin() || sessionManager.isClientLoggedIn()) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .commitNow();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, WelcomeFragment.newInstance())
                        .commitNow();
            }

        }
    }

}