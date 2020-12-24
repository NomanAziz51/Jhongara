package com.propertyeager.realestate.Fragment;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.propertyeager.realestate.R;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

public class WelcomeFragment extends Fragment {

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    private CardView  cv_guest, cv_continue;
    private String selected_option = "";
    private TextView tv_or;
    private PowerSpinnerView spinner_option;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.welcome_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cv_guest = getView().findViewById(R.id.cv_guest);
        cv_continue = getView().findViewById(R.id.cv_continue);
        spinner_option = getView().findViewById(R.id.spinner_option);
        tv_or = getView().findViewById(R.id.tv_or);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String from = bundle.getString("from");
            if (from.equalsIgnoreCase("to_sell")) {
                cv_guest.setVisibility(View.GONE);
                tv_or.setVisibility(View.GONE);
            }
        }
        spinner_option.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int position, String item) {
               // Toast.makeText(getActivity(), item + "selected", Toast.LENGTH_SHORT).show();
                selected_option = item;
            }
        });
        cv_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_option.dismiss();
                Fragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        cv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(), selected_option + "selected", Toast.LENGTH_SHORT).show();
                if(selected_option.equalsIgnoreCase("sign in as a client")){
                    Fragment userLoginFragment = new UserLoginFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, userLoginFragment);
                    transaction.commit();
                }else if(selected_option.equalsIgnoreCase("sign in as an agent")){
                    Fragment agentLoginFragment = new AgentLoginFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.replace(R.id.container, agentLoginFragment);
                    transaction.commit();
                }else{
                    Toast.makeText(getActivity(),  "Please choose one option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}