package com.propertyeager.realestate.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Utils.ConnectionDetector;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Views.ProgressLoader;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {


    ProgressLoader progressBar;
    private SmartMaterialSpinner spinnerCities, spinnerPropertyOn, spinnerPropertyType;
    //drawer
    private ConstraintLayout ly_onRent, ly_dealersLists, ly_societyAds, ly_videosAds, ly_featureAds, ly_highRise, ly_dealerLogin,
            ly_clientLogin, ly_generalAds, ly_profile, ly_my_ads, ly_logout;

    private RelativeLayout btnSearch;
    public String strCity = "", strPropertyOn = "", strPropertyType = "";
    //
    private ImageView menu;
    public DrawerLayout drawer;
    SessionManager sessionManager;

    CircleImageView profile_pic;
    ConnectionDetector cd;

    private TextView ly_email;
    String user_type = "";

    private boolean isinternetpresent = false;
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //drawer
        drawer = getView().findViewById(R.id.drawer);
        profile_pic = getView().findViewById(R.id.profile_pic);
        ly_email = getView().findViewById(R.id.ly_email);
        ly_generalAds = getView().findViewById(R.id.ly_generalAds);
        ly_onRent = getView().findViewById(R.id.ly_onRent);
        ly_dealersLists = getView().findViewById(R.id.ly_dealersLists);
        ly_societyAds = getView().findViewById(R.id.ly_societyAds);
        ly_videosAds = getView().findViewById(R.id.ly_videosAds);
        ly_featureAds = getView().findViewById(R.id.ly_featureAds);
        ly_highRise = getView().findViewById(R.id.ly_highRise);
        ly_dealerLogin = getView().findViewById(R.id.ly_dealerLogin);
        ly_clientLogin = getView().findViewById(R.id.ly_clientLogin);
        ly_profile = getView().findViewById(R.id.ly_profile);
        ly_my_ads = getView().findViewById(R.id.ly_my_ads);
        ly_logout = getView().findViewById(R.id.ly_logout);
        menu = getView().findViewById(R.id.menu);

        //
        progressBar = getView().findViewById(R.id.progressBar);
        sessionManager = new SessionManager(getContext());

        spinnerCities = getView().findViewById(R.id.spinner_cities);
        spinnerPropertyOn = getView().findViewById(R.id.spinner_property_on);
        spinnerPropertyType = getView().findViewById(R.id.spinner_property_type);
        //

        btnSearch = getView().findViewById(R.id.btn_search);

        //drawer


        //

        ArrayAdapter<CharSequence> citiesAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.Cities,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        citiesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerCities.setAdapter(citiesAdapter);

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                strCity = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> propertyOnAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.property_on,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        propertyOnAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerPropertyOn.setAdapter(propertyOnAdapter);

        spinnerPropertyOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                strPropertyOn = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> propertyTypeAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.property_type,
                        R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        propertyTypeAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerPropertyType.setAdapter(propertyTypeAdapter);

        spinnerPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("item", (String) parent.getItemAtPosition(position));
                strPropertyType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment ();
                Bundle args = new Bundle();
                args.putString("from", "searchfragment");
                args.putString("city", strCity);
                args.putString("property_type", strPropertyType);
                args.putString("property_on", strPropertyOn);

                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        // profile_image = getView().findViewById(R.id.profile_image);

        //

        //
        // registerFirebase();
        // CheckTokenValidity();

        if (sessionManager.isClientLoggedIn()) {

            user_type = "Client";
            ly_email.setText(sessionManager.getClient().get("email"));
            ly_clientLogin.setVisibility(View.GONE);

        } else if (sessionManager.isAgentLoggedin()) {
            user_type = "Dealer";
            ly_email.setText(sessionManager.getAgent().get("email"));
            ly_dealerLogin.setVisibility(View.GONE);
            Picasso.with(getContext())
                    .load("https://propertyeager.com/"
                            + sessionManager.getAgent().get("image"))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.profile_pic)
                    .into(profile_pic, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(getContext())
                                    .load("https://propertyeager.com/"
                                            + sessionManager.getAgent().get("image"))

                                    .into(profile_pic, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
        } else {
            ly_logout.setVisibility(View.GONE);
            ly_profile.setVisibility(View.GONE);
            ly_my_ads.setVisibility(View.GONE);

        }
        setUpDrawer();

        ly_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ly_generalAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment();
                Bundle args = new Bundle();
                args.putString("from", "drawerHomeFragment");
                args.putString("catagory", "general");


                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_my_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAdsListFragment fragment = new MyAdsListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_onRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment();
                Bundle args = new Bundle();
                args.putString("from", "drawerHomeFragment");
                args.putString("catagory", "rent");


                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_dealersLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgentListFragment fragment = new AgentListFragment();
              /*  Bundle args = new Bundle();
                args.putString("from", "drawerHomeFragment");
                args.putString("catagory", "society");*/


                //fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_societyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment();
                Bundle args = new Bundle();
                args.putString("from", "drawerHomeFragment");
                args.putString("catagory", "society");


                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_videosAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoAdsListFragment fragment = new VideoAdsListFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_featureAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment();
                Bundle args = new Bundle();
                args.putString("from", "drawerHomeFragment");
                args.putString("catagory", "featured");


                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_highRise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment();
                Bundle args = new Bundle();
                args.putString("from", "drawerHomeFragment");
                args.putString("catagory", "high rise");


                fragment.setArguments(args);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_dealerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgentLoginFragment fragment = new AgentLoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ly_clientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLoginFragment fragment = new UserLoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ly_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutClient();
                sessionManager.logoutAgent();
                ly_logout.setVisibility(View.GONE);
                ly_profile.setVisibility(View.GONE);
                ly_my_ads.setVisibility(View.GONE);
                ly_clientLogin.setVisibility(View.VISIBLE);
                ly_dealerLogin.setVisibility(View.VISIBLE);
                profile_pic.setImageResource(R.drawable.profile_pic);
                ly_email.setText("Property Eager");
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.setVisibility(View.VISIBLE);
                drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
            }
        });
    }


    private void setUpDrawer() {
        //Setup Titles and Icons of Navigation Drawer

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawer.setVisibility(View.VISIBLE);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawer.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
}