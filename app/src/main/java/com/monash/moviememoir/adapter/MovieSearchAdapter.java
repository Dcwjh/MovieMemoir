package com.monash.moviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.MovieInfo;

import java.util.List;


public class MovieSearchAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int number;
    private List<MovieInfo> movies;

    public MovieSearchAdapter(Context context, List<MovieInfo> movies, int number){
        this.movies = movies;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.number = number;
    }

    @Override
    public int getCount() {
        return number;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView imageView;
        public TextView mMovieName, mReleaseTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.movie_search_one_item,null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image);
            holder.mMovieName = convertView.findViewById(R.id.moviename);
            holder.mReleaseTime = convertView.findViewById(R.id.releasetime);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mMovieName.setText(movies.get(position).getMovieName());
        //todo
//        holder.mReleaseTime.setText(movies.get(position).getReleaseYear() + "-" + (int)(Math.random()*12 +1) +"-" + (int)(Math.random()*29 +1));

        holder.mReleaseTime.setText(movies.get(position).getReleaseYear());

        Glide.with(mContext).load(movies.get(position).getImageURL()).into(holder.imageView);
        return convertView;
    }
}