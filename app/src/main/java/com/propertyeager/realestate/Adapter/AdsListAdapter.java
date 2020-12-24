package com.propertyeager.realestate.Adapter;

import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.propertyeager.realestate.FragmentCommunication;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Utils.NumberToWords;
import com.propertyeager.realestate.Utils.SessionManager;
import com.propertyeager.realestate.Wrapper.GetAdslistWrapper.Result;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.propertyeager.realestate.Utils.MyApp.getContext;

public class AdsListAdapter extends RecyclerView.Adapter<AdsListAdapter.MyViewHolder> {


    public AdsListAdapter(Context adsListFragment, ArrayList<Result> datumList /*FragmentCommunication communication*/, FragmentCommunication communication2) {
        this.datumList = datumList;
        /*  mCommunicator = communication;*/
        mCommunicator2 = communication2;
        setHasStableIds(false);
    }

    private NumberToWords numberToWords;
    private ArrayList<Result> datumList;
    private FragmentCommunication mCommunicator, mCommunicator2;
    public SessionManager sessionManager;
    public String currency = "", decodedDescription = "";
    public byte[] data;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView price, address, description, bed, bath, stories;
        public Context context;
        public ImageView image;
        public LinearLayout ly_item;
        public SessionManager sessionManager;

        public MyViewHolder(final View view) {
            super(view);
            price = view.findViewById(R.id.tv_price);
            address = view.findViewById(R.id.tv_address);
            description = view.findViewById(R.id.tv_discription);
            bed = view.findViewById(R.id.tv_bed);
            bath = view.findViewById(R.id.tv_bath);
            stories = view.findViewById(R.id.tv_stories);
            image = view.findViewById(R.id.image);
            ly_item = view.findViewById(R.id.ly_item);

            sessionManager = new SessionManager(getContext());

            ly_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommunicator2.respond(
                            getAdapterPosition(),
                            datumList.get(getAdapterPosition()).getId(),"view"

                    );
                }
            });

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.applicant_form_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        String priceInWords = numberToWords.convertNumberToWords(Integer.parseInt(datumList.get(position).getPrice()));

       /* NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        currency = format.format(Integer.parseInt(datumList.get(position).getPrice()));
        System.out.println("Currency in Canada : " + currency);*/


        data = Base64.decode(datumList.get(position).getDiscription(), Base64.DEFAULT);
        try {
            decodedDescription = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.price.setText(datumList.get(position).getPrice());
        holder.address.setText(datumList.get(position).getAddress().toString());

        holder.description.setText(decodedDescription);
        holder.bed.setText(datumList.get(position).getRooms());
        holder.bath.setText(datumList.get(position).getBaths());
        holder.stories.setText(datumList.get(position).getStories());
        //holder.ly_item.setText(datumList.get(position).getCreatedAt().toString());


        Picasso.with(getContext())
                .load("https://propertyeager.com/admin/"
                        + datumList.get(position).getPic1())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getContext())
                                .load("https://propertyeager.com/admin/"
                                        + datumList.get(position).getPic1())

                                .into(holder.image, new com.squareup.picasso.Callback() {
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

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public void setAllFormList(ArrayList<Result> list) {
        this.datumList = list;
        notifyDataSetChanged();
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
