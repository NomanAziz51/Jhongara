package com.propertyeager.realestate.Fragment;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Utils.SessionManager;


public class SplashFragment extends Fragment {



    private final int SPLASH_DELAY_TIME = 2000;

    SessionManager sessionManager;

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionManager = new SessionManager(getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isAgentLoggedin() || sessionManager.isClientLoggedIn()) {
                    Fragment homefragment = new HomeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.container, homefragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Fragment welcomeFragment = new WelcomeFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.container, welcomeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        }, SPLASH_DELAY_TIME);
    }

}