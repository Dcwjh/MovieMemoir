package com.monash.moviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.monash.moviememoir.R;

import java.util.HashMap;
import java.util.List;


public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int number;
    List<HashMap<String,Object>> movies;

    public MyAdapter(Context context, List<HashMap<String,Object>> movies, int number){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.number = number;
        this.movies = movies;
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
        public TextView mMovieName, mReleaseTime,mRating;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.home_one_item_top5,null);
            holder = new ViewHolder();
            holder.mMovieName = convertView.findViewById(R.id.moviename);
            holder.mReleaseTime = convertView.findViewById(R.id.releasetime);
            holder.mRating = convertView.findViewById(R.id.tv_ratingsore);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.mMovieName.setText(movies.get(position).get("movie name").toString());
        holder.mReleaseTime.setText(movies.get(position).get("release date").toString());
        System.out.println(movies.get(position).get("rating score") + "  " + movies.get(position).get("rating score").getClass());
        holder.mRating.setText(movies.get(position).get("rating score").toString());

        return convertView;
    }
}