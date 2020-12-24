package com.propertyeager.realestate.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.propertyeager.realestate.Adapter.ImagePagerAdapter;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Retrofit.WebServiceFactory;
import com.propertyeager.realestate.Utils.AppConstants;
import com.propertyeager.realestate.Views.ProgressLoader;
import com.propertyeager.realestate.Wrapper.GetAdDetails.GetAdDetailWrapper;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdDetailFragment extends Fragment/* implements YouTubePlayer.OnInitializedListener*/ {


    private YouTubePlayer YPlayer;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private CardView cv_callmsg;
    private ArrayList<String> ImagesArray = new ArrayList<String>();
    private static final Integer[] IMAGES = {};
    private static ViewPager viewPager;
    private static int currentPage = 0;
    private ProgressLoader progressBar;
    private String id, number = "+923319688867";
    ImageView agent_logo;

    ImagePagerAdapter adapter;

    ImageView back_btn;

    FrameLayout frameLayout;

    MapView mMapView;
    private GoogleMap googleMap;
    YouTubePlayerSupportFragmentX youTubePlayerFragment;
    // private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView;
    private static final int RECOVERY_REQUEST = 1;
    private String videoId = "";

    ImageView img_floor_plan;
    public String currency = "", decodedDescription = "";
    LinearLayout ad_admin_ly, ad_agent_ly, ad_client_ly, ly_floor_plan, ly_call, ly_msg;
    TextView ad_price, ad_title, ad_address, ad_bed, ad_bath, ad_stories, ad_area, posted_date, ad_description, ad_amenities,
            ad_agency_name, ad_agent_name, ad_agent_number, agency_description,
            ad_client_name, ad_client_email, ad_client_number,
            ad_near_by;

    public static AdDetailFragment newInstance() {
        return new AdDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ad_detail_fragment, container, false);

        youTubePlayerFragment = (YouTubePlayerSupportFragmentX) getChildFragmentManager().findFragmentById(R.id.youtube_frag);


        youTubePlayerFragment.initialize(AppConstants.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);
                    YPlayer.setShowFullscreenButton(false);

                    // YPlayer.loadVideo("lWA2pjMjpBs");
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {


            }
        });


        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);


            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // youTubeView = getView().findViewById(R.id.youtube_view);
        //   youTubeView.initialize(AppConstants.YOUTUBE_API_KEY, this);


        //
        ad_price = getView().findViewById(R.id.ad_price);
        ad_title = getView().findViewById(R.id.ad_title);
        ad_address = getView().findViewById(R.id.ad_address);
        ad_bed = getView().findViewById(R.id.ad_bed);
        ad_bath = getView().findViewById(R.id.ad_bath);
        ad_stories = getView().findViewById(R.id.ad_stories);
        ad_area = getView().findViewById(R.id.ad_area);
        posted_date = getView().findViewById(R.id.posted_date);
        ad_description = getView().findViewById(R.id.ad_description);
        ad_amenities = getView().findViewById(R.id.ad_amenities);
        ad_agency_name = getView().findViewById(R.id.ad_agency_name);
        agency_description = getView().findViewById(R.id.agency_description);
        ad_agent_name = getView().findViewById(R.id.ad_agent_name);
        ad_agent_number = getView().findViewById(R.id.ad_agent_number);
        ad_client_name = getView().findViewById(R.id.ad_client_name);
        ad_client_email = getView().findViewById(R.id.ad_client_email);
        ad_client_number = getView().findViewById(R.id.ad_client_number);
        ad_near_by = getView().findViewById(R.id.ad_near_by);
        img_floor_plan = getView().findViewById(R.id.img_floor_plan);
        back_btn = getView().findViewById(R.id.back_btn);
        //
        //   youTubePlayerView = getView().findViewById(R.id.youtube_player_view);
        // googleMapView = getView().findViewById(R.id.googleMapView);

        //
        agent_logo = getView().findViewById(R.id.agent_logo);
        //
        ad_admin_ly = getView().findViewById(R.id.ad_admin_ly);
        ad_agent_ly = getView().findViewById(R.id.ad_agent_ly);
        ad_client_ly = getView().findViewById(R.id.ad_client_ly);
        ly_floor_plan = getView().findViewById(R.id.ly_floor_plan);
        //
        ly_call = getView().findViewById(R.id.ly_call);
        ly_msg = getView().findViewById(R.id.ly_msg);
        cv_callmsg = getView().findViewById(R.id.cv_call_msg);

        frameLayout = getView().findViewById(R.id.frame_youtube);
        id = getArguments().getString("id");
        viewPager = (ViewPager) getView().findViewById(R.id.pager);

        adapter = new ImagePagerAdapter(getActivity(), ImagesArray);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getCount() - 1);

       /* getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                youTubePlayer.loadVideo(videoId, 0);

                Log.e("VideoId","ID: " + videoId);
            }
        });
*/
        ly_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
                startActivity(intent);
            }
        });

        ly_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
               // sendIntent.setData(Uri.parse("sms:"));
               // sendIntent.putExtra("sms_body", x);
                startActivity(sendIntent);
            }
        });

        //
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null && getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
            }
        });


        CirclePageIndicator indicator = (CirclePageIndicator)
                getView().findViewById(R.id.indicator);

        progressBar = getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        GetAdDetail(id);
        indicator.setViewPager(viewPager);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


    }

    private void GetAdDetail(String id) {


        // Log.e("TAG_onCheck", sessionManager.GetAccessToken());
        WebServiceFactory.getInstance().GET_AD_DETAIL_WRAPPER_CALL(id)
                .enqueue(new Callback<GetAdDetailWrapper>() {
                    @Override
                    public void onResponse(Call<GetAdDetailWrapper> call, final Response<GetAdDetailWrapper> response) {
                        if (response.body() != null) {
                            // JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                            if (response.body().getStatus() == 1) {


                                ImagesArray.add(response.body().getResponse().getResult().getPic1());
                                ImagesArray.add(response.body().getResponse().getResult().getPic2());
                                ImagesArray.add(response.body().getResponse().getResult().getPic3());
                                ImagesArray.add(response.body().getResponse().getResult().getPic4());
                                ImagesArray.add(response.body().getResponse().getResult().getPic5());
                                ImagesArray.add(response.body().getResponse().getResult().getPic6());
                                adapter.notifyDataSetChanged();
                                videoId = response.body().getResponse().getResult().getVideoId().trim();
                                //YPlayer.loadVideo("1qcWXa2J1T4&list=RDMM1qcWXa2J1T4&start_radio=1");
                                if (!videoId.isEmpty()) {
                                    YPlayer.cueVideo(videoId);
                                    YPlayer.setFullscreen(false);
                                } else {
                                    frameLayout.setVisibility(View.GONE);
                                }
                                ad_price.setText(response.body().getResponse().getResult().getPrice());
                                ad_title.setText(response.body().getResponse().getResult().getTitle());
                                ad_address.setText(response.body().getResponse().getResult().getAddress());
                                String address = (String) ad_address.getText();
                                if (!address.isEmpty()) {
                                    getLocationFromAddress(getActivity(), response.body().getResponse().getResult().getAddress());
                                }
                                ad_bed.setText(response.body().getResponse().getResult().getRooms());
                                ad_bath.setText(response.body().getResponse().getResult().getBaths());
                                ad_stories.setText(response.body().getResponse().getResult().getStories());
                                ad_area.setText(response.body().getResponse().getResult().getArea());
                                ad_near_by.setText(response.body().getResponse().getResult().getNearby());
                                byte[] data = Base64.decode(response.body().getResponse().getResult().getDiscription(), Base64.DEFAULT);
                                try {
                                    decodedDescription = new String(data, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                ad_description.setText(decodedDescription);
                                ad_amenities.setText(response.body().getResponse().getResult().getAnimites());

                                if (response.body().getResponse().getResult().getUploderType().equalsIgnoreCase("Dealer")) {
                                    ad_admin_ly.setVisibility(View.GONE);
                                    ad_agent_ly.setVisibility(View.VISIBLE);
                                    ad_client_ly.setVisibility(View.GONE);
                                    ad_agency_name.setText(response.body().getResponse().getResult().getAgencyName());
                                    ad_agent_name.setText(response.body().getResponse().getResult().getName());
                                    ad_agent_number.setText(response.body().getResponse().getResult().getNumber());
                                    number = response.body().getResponse().getResult().getNumber();
                                    byte[] data2 = Base64.decode(response.body().getResponse().getResult().getDiscription(), Base64.DEFAULT);
                                    try {
                                        decodedDescription = new String(data2, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    agency_description.setText(decodedDescription);

                                    Picasso.with(getActivity())
                                            .load("https://propertyeager.com/" + response.body().getResponse().getResult().getAgencyLogo())
                                            .networkPolicy(NetworkPolicy.OFFLINE)
                                            .into(agent_logo, new com.squareup.picasso.Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                    //Try again online if cache failed
                                                    Picasso.with(getActivity())
                                                            .load("https://propertyeager.com/" + response.body().getResponse().getResult().getAgencyLogo())
                                                            // .error(R.drawable.placeholder_750x415)
                                                            .into(agent_logo, new com.squareup.picasso.Callback() {
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

                                } else if (response.body().getResponse().getResult().getUploderType().equalsIgnoreCase("Client")) {

                                    ad_admin_ly.setVisibility(View.GONE);
                                    ad_agent_ly.setVisibility(View.GONE);
                                    ad_client_ly.setVisibility(View.VISIBLE);
                                    ad_client_email.setText(response.body().getResponse().getResult().getEmail());
                                    ad_client_name.setText(response.body().getResponse().getResult().getName());
                                    ad_client_number.setText(response.body().getResponse().getResult().getNumber());
                                    number = response.body().getResponse().getResult().getNumber();
                                } else {
                                    ad_admin_ly.setVisibility(View.VISIBLE);
                                    ad_agent_ly.setVisibility(View.GONE);
                                    ad_client_ly.setVisibility(View.GONE);
                                }
                                String strfloorplan = response.body().getResponse().getResult().getFloorPlan();
                                if (!strfloorplan.isEmpty()) {
                                    ly_floor_plan.setVisibility(View.VISIBLE);
                                    Picasso.with(getActivity())
                                            .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getFloorPlan())
                                            .networkPolicy(NetworkPolicy.OFFLINE)
                                            .into(img_floor_plan, new com.squareup.picasso.Callback() {
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onError() {
                                                    //Try again online if cache failed
                                                    Picasso.with(getActivity())
                                                            .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getFloorPlan())
                                                            // .error(R.drawable.placeholder_750x415)
                                                            .into(img_floor_plan, new com.squareup.picasso.Callback() {
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
                                    img_floor_plan.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Dialog builder = new Dialog(getActivity(), android.R.style.Theme_Light);
                                            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            builder.getWindow().setBackgroundDrawable(
                                                    new ColorDrawable(Color.BLACK));
                                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    //nothing;
                                                }
                                            });

                                            final ZoomageView imageView = new ZoomageView(getActivity());
                                            Picasso.with(getActivity())
                                                    .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getFloorPlan())
                                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                                    .fit().centerCrop().noFade()
                                                    .placeholder(R.drawable.progress_animation)
                                                    .into(imageView, new com.squareup.picasso.Callback() {
                                                        @Override
                                                        public void onSuccess() {

                                                        }

                                                        @Override
                                                        public void onError() {
                                                            //Try again online if cache failed
                                                            Picasso.with(getActivity())
                                                                    .load("https://propertyeager.com/admin/" + response.body().getResponse().getResult().getFloorPlan())
                                                                    .fit().centerCrop().noFade()
                                                                    .placeholder(R.drawable.progress_animation)
                                                                    .error(R.drawable.placeholder_750x415)
                                                                    .into(imageView, new com.squareup.picasso.Callback() {
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
                                            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.MATCH_PARENT));
                                            builder.show();
                                        }
                                    });
                                } else {
                                    ly_floor_plan.setVisibility(View.GONE);
                                }

                                progressBar.setVisibility(View.GONE);
                                //  Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                //                   Snackbar.make(getView(), response.body().getError(), Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onFailure(Call<GetAdDetailWrapper> call, Throwable t) {
//                                                            Snackbar.make(itemView, "Please Check Your Internet Connection", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        Log.e("TAG_onFailure", t.toString());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    public void getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {

            }

            Address location = null;
            try {
                location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (IOException ex) {

            ex.printStackTrace();
        }

// For dropping a marker at a point on the Map
        try {
            LatLng sydney = new LatLng(p1.latitude, p1.longitude);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("").snippet(""));

            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}