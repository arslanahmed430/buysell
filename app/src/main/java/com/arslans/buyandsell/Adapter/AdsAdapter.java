package com.arslans.buyandsell.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arslans.buyandsell.Activites.ViewAd;
import com.arslans.buyandsell.Models.AdModel;
import com.arslans.buyandsell.R;
import com.arslans.buyandsell.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.ViewHolder> {
    Context context;
    List<AdModel> itemList;

    public AdsAdapter(Context context, List<AdModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void setItemList(List<AdModel> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ad_item_layout,parent,false);
        AdsAdapter.ViewHolder viewHolder=new AdsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdsAdapter.ViewHolder holder, int position) {
        AdModel model=itemList.get(position);
        Glide.with(context).load(model.getPicUrl()).into(holder.image);
        holder.title.setText(model.getTitle());
        holder.price.setText("Rs "+model.getPrice());
        holder.location.setText(model.getCity());
        holder.time.setText(CommonUtils.getFormattedDate(model.getTime()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ViewAd.class);
                i.putExtra("key",model.getKey());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title,price,location,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            price=itemView.findViewById(R.id.price);
            location=itemView.findViewById(R.id.location);
            time=itemView.findViewById(R.id.time);
        }
    }
}
