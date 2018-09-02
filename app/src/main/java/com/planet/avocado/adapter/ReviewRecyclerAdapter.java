package com.planet.avocado.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planet.avocado.R;
import com.planet.avocado.data.CardviewItem;
import com.planet.avocado.data.Comment;

import java.util.ArrayList;

/**
 * Created by yoonc on 2018-09-01.
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewRecyclerAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Comment> items;

    public ReviewRecyclerAdapter(Context context, ArrayList<Comment> items){
        this.mContext = context;
        this.items = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comment item = items.get(position);

        holder.userImage.setImageResource(R.mipmap.ic_launcher_round);
        holder.userIdTV.setText(item.userId);
        Glide.with(mContext).load(item.imgPath).into(holder.stuffImage);
        holder.pointTV.setText(item.score+"");
        holder.reviewTV.setText(item.content);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView userIdTV;
        ImageView stuffImage;
        TextView pointTV;
        TextView reviewTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = (ImageView)itemView.findViewById(R.id.user_view);
            userIdTV = (TextView)itemView.findViewById(R.id.user_id_tv);
            stuffImage = (ImageView)itemView.findViewById(R.id.stuff_image_recy);
            pointTV = (TextView)itemView.findViewById(R.id.point_tv_recy);
            reviewTV = (TextView)itemView.findViewById(R.id.review_tv);
        }
    }
}
