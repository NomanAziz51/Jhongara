package com.propertyeager.realestate.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.propertyeager.realestate.Adapter.MyAdsListAdapter;
import com.propertyeager.realestate.FragmentCommunication;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetAdslistWrapper.GetAdsListWrapper;
import com.propertyeager.realestate.Wrapper.GetAdslistWrapper.Result;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAdsListFragment extends Fragment {

    private ProgressLoader progressBar;

    private RecyclerView rv_applicant_form;

    private LinearLayout ly_upload_ad;

    String agent_id;

    SessionManager sessionManager;

    MyAdsListAdapter adsListAdapter;

    TextView tv_no_record_found;

    private RecyclerView rv_ads_list;

    CircleImageView profile_pic;

    String user_type = "";

    ArrayList<Result> myAdsList = new ArrayList<>();

    public static MyAdsListFragment newInstance() {
        return new MyAdsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_ads_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        progressBar = getView().findViewById(R.id.progressBar);
        rv_ads_list = getView().findViewById(R.id.rv_video_ads);

        tv_no_record_found = getView().findViewById(R.id.tv_no_record_found);
        profile_pic = getView().findViewById(R.id.profile_image);

        ly_upload_ad = getView().findViewById(R.id.ly_upload_ad);

        sessionManager = new SessionManager(getContext());
        adsListAdapter = new MyAdsListAdapter(getContext(), myAdsList /*communication*/, communication2, communication3);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_ads_list.setLayoutManager(mLayoutManager);
        rv_ads_list.setItemAnimator(new DefaultItemAnimator());

        ly_upload_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUploadFragment fragment = new AdUploadFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                args.putString("from", "edit_ad");
                fragment.setArguments(args);
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        if (sessionManager.isClientLoggedIn()) {
            user_type = "Client";
        } else if (sessionManager.isAgentLoggedin()) {
            user_type = "Dealer";

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
        adsListAdapter.clear();
        GetMyAdsList();
    }

    private void GetMyAdsList() {
        progressBar.setVisibility(View.VISIBLE);

        // Log.e("TAG_onCheck", sessionManager.GetAccessToken());
        WebServiceFactory.getInstance().GET_MY_ADS_LIST_WRAPPER_CALL(sessionManager.getAgent().get("id"), user_type)
                .enqueue(new Callback<GetAdsListWrapper>() {
                    @Override
                    public void onResponse(Call<GetAdsListWrapper> call, Response<GetAdsListWrapper> response) {
                        /*    try {*/

                            // JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 1) {
                                //  JSONObject responseobj = object.getJSONObject("response");
                                myAdsList.addAll(response.body().getResponse().getResult());
                                // allapplicantlist.notify();
                                rv_ads_list.setAdapter(adsListAdapter);
                                adsListAdapter.setAllFormList(myAdsList);
                                adsListAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                //  Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();

                        }


                      /*  } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        if (adsListAdapter.getItemCount() == 0) {
                            tv_no_record_found.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetAdsListWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                      /*  try {
                            //Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                        tv_no_record_found.setVisibility(View.VISIBLE);
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    FragmentCommunication communication2 = new FragmentCommunication() {
        @Override
        public void respond(int adapterPosition, String id, String from) {
            AdDetailFragment fragmentB = new AdDetailFragment();
            Bundle args = new Bundle();
            args.putString("id", id);
            fragmentB.setArguments(args);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, fragmentB).commit();
        }

    };

    FragmentCommunication communication3 = new FragmentCommunication() {
        @Override
        public void respond(int adapterPosition, String id, String from) {
            AdEditFragment fragmentB = new AdEditFragment();
            Bundle args = new Bundle();
            args.putString("from", from);
            args.putString("id", id);
            fragmentB.setArguments(args);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, fragmentB).commit();
        }

    };


}

