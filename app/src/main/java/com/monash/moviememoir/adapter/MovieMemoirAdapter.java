package com.monash.moviememoir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Memoir;
import com.monash.moviememoir.entity.MemoirView;
import com.monash.moviememoir.entity.MovieDetail;
import com.monash.moviememoir.entity.MovieInfo;

import java.util.ArrayList;
import java.util.List;

public class MovieMemoirAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int number;
    List<MemoirView> memoirs = new ArrayList<>();
    private int userid;

    public MovieMemoirAdapter(Context context){
        this.memoirs = memoirs;
    }

    public MovieMemoirAdapter(Context context, List<MemoirView> memoirs,int userid){
        this.memoirs = memoirs;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.userid = userid;
    }

    public void setMemoirs(List<MemoirView> memoirs) {
        this.memoirs = memoirs;
    }

    @Override
    public int getCount() {
        return memoirs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        public ImageView imageView;
        public TextView mMovieName, mReleaseTime,mWatchdate, mpostcode,mComment;
        public RatingBar mRatingbar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.movie_memoir_one_item,null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.mma_image);
            holder.mMovieName = convertView.findViewById(R.id.mma_moviename);
            holder.mReleaseTime = convertView.findViewById(R.id.mma_releasedate);
            holder.mWatchdate = convertView.findViewById(R.id.mma_watchdate);
            holder.mpostcode = convertView.findViewById(R.id.mma_poscode);
            holder.mComment = convertView.findViewById(R.id.mma_comment);
            holder.mRatingbar = convertView.findViewById(R.id.mma_ratingbar);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }

        Glide.with(mContext).load(memoirs.get(position).getImageUrl()).into(holder.imageView);
        holder.mMovieName.setText(memoirs.get(position).getMoviename());
        holder.mReleaseTime.setText("release date: " + memoirs.get(position).getReleasedate());
        holder.mWatchdate.setText("watch date: " + memoirs.get(position).getWatchdate());
        holder.mRatingbar.setRating((float) memoirs.get(position).getUserscore()/20);
        holder.mComment.setText(memoirs.get(position).getComment());
        holder.mpostcode.setText("postcode: " + memoirs.get(position).getPostcode());

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, com.monash.moviememoir.activity.MovieDetail.class);
                intent.putExtra("id", memoirs.get(position).getId());
                intent.putExtra("url",memoirs.get(position).getImageUrl());
                intent.putExtra("userid", userid);
                intent.putExtra("light",0);
                mContext.startActivity(intent);

            }
        });



        return convertView;
    }
}
