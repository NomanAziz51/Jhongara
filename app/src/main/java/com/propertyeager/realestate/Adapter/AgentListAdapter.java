package com.propertyeager.realestate.Adapter;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.propertyeager.realestate.FragmentCommunication;
import com.propertyeager.realestate.FragmentCommunicationAgentData;
import com.propertyeager.realestate.R;
import com.propertyeager.realestate.Utils.NumberToWords;
import com.propertyeager.realestate.Utils.SessionManager;

import com.propertyeager.realestate.Wrapper.GetAgentList.Result;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.propertyeager.realestate.Utils.MyApp.getContext;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.MyViewHolder> {


    public AgentListAdapter(Context agentListFragment, ArrayList<Result> datumList, FragmentCommunicationAgentData communication2) {
        this.datumList = datumList;
        /*  mCommunicator = communication;*/
        mCommunicator2 = communication2;
        setHasStableIds(false);
    }

    private NumberToWords numberToWords;
    private ArrayList<Result> datumList;
    private FragmentCommunication mCommunicator;
    private FragmentCommunicationAgentData mCommunicator2;
    public SessionManager sessionManager;
    public  String currency="", decodedDescription ="";
    public byte[] data;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_agent_name, tv_agency_name, tv_agent_discription;
        public Context context;
        public ImageView agent_logo;
        public LinearLayout ly_item;
        public SessionManager sessionManager;

        public MyViewHolder(final View view) {
            super(view);
            tv_agent_name = view.findViewById(R.id.tv_agent_name);
            tv_agency_name = view.findViewById(R.id.tv_agency_name);
            tv_agent_discription = view.findViewById(R.id.tv_agent_discription);
            //
            agent_logo = view.findViewById(R.id.agent_logo);
            ly_item = view.findViewById(R.id.ly_agent_item);

            sessionManager = new SessionManager(getContext());

            ly_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommunicator2.respond(
                            getAdapterPosition(),
                            datumList.get(getAdapterPosition()).getId(),"view",
                            datumList.get(getAdapterPosition()).getAgencyLogo(),
                            datumList.get(getAdapterPosition()).getAgencyName(),
                            datumList.get(getAdapterPosition()).getDiscription(),
                            datumList.get(getAdapterPosition()).getNumber(),
                            datumList.get(getAdapterPosition()).getName()

                    );}
            });

        }
    }

    @NonNull
    @Override
    public AgentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_list_item, parent, false);
        return new AgentListAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull AgentListAdapter.MyViewHolder holder, int position) {
       data = Base64.decode(datumList.get(position).getDiscription(), Base64.DEFAULT);
        try {
            decodedDescription = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        holder.tv_agent_name.setText(datumList.get(position).getName());
        holder.tv_agency_name.setText(datumList.get(position).getAgencyName());
        holder.tv_agent_discription.setText(decodedDescription);

        //holder.ly_item.setText(datumList.get(position).getCreatedAt().toString());


        Picasso.with(getContext())
                .load("https://propertyeager.com/"
                        + datumList.get(position).getAgencyLogo())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.agent_logo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getContext())
                                .load("https://propertyeager.com/admin/"
                                        + datumList.get(position).getAgencyLogo())

                                .into(holder.agent_logo, new com.squareup.picasso.Callback() {
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
