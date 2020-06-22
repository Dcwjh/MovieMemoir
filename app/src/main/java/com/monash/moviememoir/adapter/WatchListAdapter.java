package com.monash.moviememoir.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.WantToWatch;
import com.monash.moviememoir.fragment.MovieView;

import java.util.ArrayList;
import java.util.List;


public class WatchListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private int number;
    private List<WantToWatch> wantToWatches = new ArrayList<>();

    public WatchListAdapter(Context context, int number){
        this.context = context;
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
        public TextView wla_moviename, wla_release, wla_addtime;
        public Button wla_delete, wla_detail;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
//            convertView = mLayoutInflater.inflate(R.layout.watch_list_one_item,null);
           convertView = mLayoutInflater.inflate(R.layout.zhangxiaoming_one_item,null);
            holder = new ViewHolder();
            holder.wla_moviename = convertView.findViewById(R.id.wla_moviename);
            holder.wla_release = convertView.findViewById(R.id.wla_release);
            holder.wla_addtime = convertView.findViewById(R.id.wla_addtime);
            holder.wla_delete = convertView.findViewById(R.id.wla_delete);
            holder.wla_detail = convertView.findViewById(R.id.wla_detail);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.wla_moviename.setText(wantToWatches.get(position).getMoviename());
        holder.wla_release.setText(wantToWatches.get(position).getReleasetdate());
        holder.wla_addtime.setText(wantToWatches.get(position).getAddtime());
        holder.wla_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoverDialog(position);
            }
        });
        holder.wla_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo  Jump and cannot add to watchlist
                Intent intent = new Intent(context, MovieView.class);
                context.startActivity(intent);
            }
        });



//        holder.mMovieName.setText("肖申克的救赎");
//        holder.mReleaseTime.setText("2020-5-12");
//        Glide.with(mContext).load("https://www.imdb.com/title/tt0111161/mediaviewer/rm10105600").into(holder.imageView);
        return convertView;
    }


    private void showCoverDialog(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Notice");
        builder.setMessage("Are you sure to delete " + wantToWatches.get(position).getMoviename() + "?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //todo 删除本地数据库
                Toast toast = Toast.makeText(context,"Successfully delete"  ,Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }
}
