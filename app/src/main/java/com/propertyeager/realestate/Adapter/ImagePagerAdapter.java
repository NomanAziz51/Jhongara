package com.propertyeager.realestate.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.propertyeager.realestate.R;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class ImagePagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<String> arrayList;

    public ImagePagerAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        Picasso.with(context)
                .load("https://propertyeager.com/admin/" + arrayList.get(position))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load("https://propertyeager.com/admin/" + arrayList.get(position))
                                // .error(R.drawable.placeholder_750x415)
                                .placeholder(R.drawable.progress_animation)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog builder = new Dialog(context, android.R.style.Theme_Light);
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        builder.getWindow().setBackgroundDrawable(
                                new ColorDrawable(Color.BLACK));
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                //nothing;
                            }
                        });

                        final ZoomageView imageView = new ZoomageView (context);

                        Picasso.with(context)
                                .load("https://propertyeager.com/admin/" + arrayList.get(position))
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .fit().centerInside()
                                .placeholder(R.drawable.progress_animation)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        //Try again online if cache failed
                                        Picasso.with(context)
                                                .load("https://propertyeager.com/admin/" + arrayList.get(position))
                                               .fit().centerInside()
                                                .placeholder(R.drawable.progress_animation)
                                                .error(R.drawable.placeholder_750x415)
                                                .into(imageView, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Log.v("Picasso","Could not fetch image");
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
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}