package com.propertyeager.realestate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Wrapper.GetVideoAdList.Result;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideoAdsListAdapter extends RecyclerView.Adapter<VideoAdsListAdapter.MyViewHolder> {

    private ArrayList<Result> datumList;
    private Context context;
    YouTubePlayerSupportFragmentX youTubePlayerFragment;
    LifecycleObserver lifecycleOwner;

    public VideoAdsListAdapter(Context adsListFragment, ArrayList<Result> datumList) {
        this.datumList = datumList;
        this.context = adsListFragment;

        /*  mCommunicator = communication;*/
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public VideoAdsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_ad_view_item, parent, false);
       // lifecycleOwner.getLifecycle().addObserver(youTubePlayerView);
        return new VideoAdsListAdapter.MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        //     public TextView price, address, description, bed, bath, stories;
        public Context context;
        public YouTubePlayerView youTubePlayerView;
        public TextView ad_price, ad_title, ad_address, ad_bed, ad_bath, ad_stories, ad_area, ad_description;
/*       public ImageView play_button;
       public FrameLayout frameLayout;*/
       /* public LinearLayout ly_item;
        public SessionManager sessionManager;*/

        public MyViewHolder(final View view) {
            super(view);
            youTubePlayerView = view.findViewById(R.id.youtube_frag_item);
            //
            ad_price = view.findViewById(R.id.ad_price);
            ad_title = view.findViewById(R.id.ad_title);
            ad_address = view.findViewById(R.id.ad_address);
            ad_bed = view.findViewById(R.id.ad_bed);
            ad_bath = view.findViewById(R.id.ad_bath);
            ad_stories = view.findViewById(R.id.ad_stories);
            ad_area = view.findViewById(R.id.ad_area);
            ad_description = view.findViewById(R.id.ad_description);

         //   lifecycleOwner.getLifecycle().addObserver(youTubePlayerView);
/*            play_button = view.findViewById(R.id.play_button);
            frameLayout = view.findViewById(R.id.youtube_frag_item);*/

            /*fr = view.findViewById(R.id.play_button);*/
/*
            price = view.findViewById(R.id.tv_price);
            address = view.findViewById(R.id.tv_address);
            description = view.findViewById(R.id.tv_discription);
            bed = view.findViewById(R.id.tv_bed);
            bath = view.findViewById(R.id.tv_bath);
            stories = view.findViewById(R.id.tv_stories);
            image = view.findViewById(R.id.image);
            ly_item = view.findViewById(R.id.ly_item);

            sessionManager = new SessionManager(getContext());
*/

        }
    }


    @Override
    public void onBindViewHolder(@NonNull VideoAdsListAdapter.MyViewHolder holder, int position) {


        holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(datumList.get(position).getVideoId(),0);



            }
        });

        holder.ad_price.setText(datumList.get(position).getPrice());
        holder.ad_title.setText(datumList.get(position).getTitle());
        holder.ad_address.setText(datumList.get(position).getAddress());
        holder.ad_bed.setText(datumList.get(position).getRooms());
        holder.ad_bath.setText(datumList.get(position).getBaths());
        holder.ad_stories.setText(datumList.get(position).getStories());
        holder.ad_area.setText(datumList.get(position).getArea());
        holder.ad_description.setText(datumList.get(position).getDiscription());


                // int containerId = holder.frameLayout.getId();
                // Add the YouTube fragment to view
      /*  youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        ((Activity) context).getFragmentManager().findFragmentById(R.id.youtube_frag_item);*/
                //  holder.play_button.setOnClickListener(new View.OnClickListener() {
        /*    @Override
            public void onClick(View v) {*/

      /*        Fragment oldFragment =  ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(containerId);
                // youTubePlayerFragment = (YouTubePlayerSupportFragmentX)  ((Activity) getContext()).getChildFragmentManager().findFragmentById(R.id.youtube_frag_item);
        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().remove(oldFragment);

        int newContainerId = View.generateViewId();
        holder.frameLayout.setId(newContainerId);

        // Add new fragment
      *//*  MyFragment fragment = MyFragment.newInstance("fragment1");
        fragmentManager.beginTransaction().replace(newContainerId, fragment).commit();*//*
        YouTubePlayerSupportFragmentX  newyouTubePlayerFragment =  (YouTubePlayerSupportFragmentX) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(newContainerId);
        newyouTubePlayerFragment.initialize(AppConstants.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                        //Utils.logDebug(TAG, "onInitializationSuccess");

                        youTubePlayer.cueVideo(datumList.get(position).getVideoId());
                        youTubePlayer.setShowFullscreenButton(false);
                    }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });*/


        /*YouTubePlayerSupportFragmentX  newyouTubePlayerFragment = (YouTubePlayerSupportFragmentX) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentById(newContainerId);
        // youTubePlayerFragment = (YouTubePlayerSupportFragmentX)  ((Activity) getContext()).getChildFragmentManager().findFragmentById(R.id.youtube_frag_item);
        newyouTubePlayerFragment.initialize(AppConstants.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                //Utils.logDebug(TAG, "onInitializationSuccess");

                youTubePlayer.cueVideo(datumList.get(position).getVideoId());
                youTubePlayer.setShowFullscreenButton(false);
            }*/

          /*  @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });*/
    }
      /*  });


    }*/

    public void setAllFormList(ArrayList<Result> list) {
        this.datumList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public void clear() {
        int size = datumList.size();
        datumList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
