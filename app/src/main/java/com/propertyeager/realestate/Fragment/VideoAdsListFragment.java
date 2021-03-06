package com.propertyeager.realestate.Fragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.propertyeager.realestate.Adapter.VideoAdsListAdapter;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetVideoAdList.GetVideoAdListWrapper;
import com.propertyeager.realestate.Wrapper.GetVideoAdList.Result;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAdsListFragment extends Fragment {

    private RecyclerView rv_video_ads_list;

    //drawer
    private ConstraintLayout ly_onRent, ly_dealersLists, ly_societyAds, ly_videosAds, ly_featureAds, ly_highRise, ly_dealerLogin, ly_clientLogin, ly_generalAds,
            ly_profile, ly_my_ads, ly_logout;
    //
    private TextView agent_name,ly_email;
    private ImageView menu;
    public DrawerLayout drawer;

    CircleImageView profile_pic;

    private ProgressLoader progressBar;

    SessionManager sessionManager;

    TextView tv_no_record_found;
    VideoAdsListAdapter videoAdsListAdapter;

    String user_type = "";

    ArrayList<Result> allVideoAdsList = new ArrayList<Result>();

    public static VideoAdsListFragment newInstance() {
        return new VideoAdsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_ads_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        progressBar = getView().findViewById(R.id.progressBar);
        rv_video_ads_list = getView().findViewById(R.id.rv_video_ads);

        tv_no_record_found = getView().findViewById(R.id.tv_no_record_found);

        sessionManager = new SessionManager(getContext());
        videoAdsListAdapter = new VideoAdsListAdapter(getContext(), allVideoAdsList /*communication*/);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_video_ads_list.setLayoutManager(mLayoutManager);
        rv_video_ads_list.setItemAnimator(new DefaultItemAnimator());

        //drawer
        ly_email = getView().findViewById(R.id.ly_email);
        profile_pic = getView().findViewById(R.id.profile_pic);
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
        //
        drawer = getView().findViewById(R.id.drawer);
        menu = getView().findViewById(R.id.menu);



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
        }
        else{
            ly_logout.setVisibility(View.GONE);
            ly_profile.setVisibility(View.GONE);
            ly_my_ads.setVisibility(View.GONE);

        }
        setUpDrawer();
        //  agent_name.setText(sessionManager.GetAgentName());


        /*Picasso.with(getContext())
                .load("https://digitization.albarigoc.com/public/uploads/profilepictures/"
                        )
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.profile_pic)
                .into(profile_image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getContext())
                                .load("https://digitization.albarigoc.com/public/uploads/profilepictures/"
                                        )

                                .into(profile_image, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });*/
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
                AdsListFragment fragment = new AdsListFragment ();
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
                AdsListFragment fragment = new AdsListFragment ();
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

            }
        });

        ly_societyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdsListFragment fragment = new AdsListFragment ();
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
                AdsListFragment fragment = new AdsListFragment ();
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
                AdsListFragment fragment = new AdsListFragment ();
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
                ly_email.setText("Jhonagra");
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.setVisibility(View.VISIBLE);
                drawer.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
            }
        });

        GetVideoAds();
    }


    private void GetVideoAds() {
        progressBar.setVisibility(View.VISIBLE);

        // Log.e("TAG_onCheck", sessionManager.GetAccessToken());
        WebServiceFactory.getInstance().GET_VIDEO_AD_LIST_WRAPPER_CALL()
                .enqueue(new Callback<GetVideoAdListWrapper>() {
                    @Override
                    public void onResponse(Call<GetVideoAdListWrapper> call, Response<GetVideoAdListWrapper> response) {
                        /*    try {*/
                        if (response.body() != null) {
                            // JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 1) {
                                //  JSONObject responseobj = object.getJSONObject("response");
                                videoAdsListAdapter.clear();
                                allVideoAdsList.addAll(response.body().getResult());
                                // allapplicantlist.notify();
                                rv_video_ads_list.setAdapter(videoAdsListAdapter);
                                videoAdsListAdapter.setAllFormList(allVideoAdsList);
                                videoAdsListAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                //  Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }


                      /*  } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        if (videoAdsListAdapter.getItemCount() == 0) {
                            tv_no_record_found.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetVideoAdListWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
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