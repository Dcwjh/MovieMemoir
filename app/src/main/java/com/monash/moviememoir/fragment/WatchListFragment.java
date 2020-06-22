package com.monash.moviememoir.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.monash.moviememoir.R;
import com.monash.moviememoir.activity.MovieDetail;
import com.monash.moviememoir.adapter.WatchListAdapter;
import com.monash.moviememoir.entity.MovieWatch;
import com.monash.moviememoir.viewmodel.MovieWatchViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class WatchListFragment extends Fragment {

    private RecyclerView rvWatchlist;

    private List<MovieWatch> watchlists;
    private Intent intent;
    private int userid;
    private View view;
    private Context context;
    private WatchlistAdapter watchlistAdapter;
    private MovieWatchViewModel vm;
    private SimpleDateFormat sdfDate;
    private SimpleDateFormat sdfDatetime;
    public WatchListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.watch_list, container, false);
        initView();
        initData();
        initAction();
        return view;
    }

    private void initView(){

        intent = getActivity().getIntent();

        rvWatchlist = view.findViewById(R.id.watchlist_rv);

        sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        sdfDatetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        watchlists = new ArrayList<>();
        watchlistAdapter = new WatchlistAdapter(watchlists);
        rvWatchlist.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rvWatchlist.setAdapter(watchlistAdapter);
        rvWatchlist.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initData(){
        context = this.getActivity();

        watchlists = new ArrayList<>();

    }

    private void initAction(){
        vm = new ViewModelProvider(getActivity()).get(MovieWatchViewModel.class);
        vm.initalizeVars(getActivity().getApplication());
        vm.getAll().observe(getActivity(), new Observer<List<MovieWatch>>() {
            @Override
            public void onChanged(List<MovieWatch> watchlists) {
                watchlistAdapter.addMovieWatchlist(watchlists);
            }
        });
        }




    private class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {

        private List<MovieWatch> movieWatchList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView movieName;
            public TextView releaseDate;
            public TextView addDatetime;
            public Button view;
            public Button delete;


            public ViewHolder(View itemView) {
                super(itemView);
                movieName = itemView.findViewById(R.id.wla_moviename);
                releaseDate = itemView.findViewById(R.id.wla_release);
                addDatetime = itemView.findViewById(R.id.wla_addtime);
                view = itemView.findViewById(R.id.wla_detail);
                delete = itemView.findViewById(R.id.wla_delete);
            }
        }

        public WatchlistAdapter(List<MovieWatch> movieWatchList) {
            this.movieWatchList = movieWatchList;
        }

        public void addMovieWatchlist(List<MovieWatch> movieWatchList) {
            this.movieWatchList = movieWatchList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public WatchlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_list_one_item, parent, false);
            WatchlistAdapter.ViewHolder holder = new WatchlistAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            final MovieWatch watchlist = movieWatchList.get(position);

            TextView movieName = holder.movieName;
            TextView releaseDate = holder.releaseDate;
            TextView addDatetime = holder.addDatetime;
            Button view = holder.view;
            Button delete = holder.delete;

            movieName.setText(watchlist.getMovieName());
            releaseDate.setText(sdfDate.format(watchlist.getReleaseDate()));
            addDatetime.setText(sdfDatetime.format(watchlist.getAddDatetime()));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MovieDetail.class);
                    intent.putExtra("id", watchlist.getIMDbID());
                    intent.putExtra("userid", userid);
                    intent.putExtra("light",0);
                    intent.putExtra("url",watchlist.getImageurl());
                    System.out.println("watch list adapter movieid " +  watchlist.getIMDbID());
                    System.out.println("watch list adapter  userid " + userid);
                    startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCoverDialog(watchlist);
//                    new AlertDialog.Builder(getActivity()).setTitle("Confirm delete？").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            movieWatchList.remove(watchlist);
//                            addMovieWatchlist(movieWatchList);
//                            vm.delete(watchlist);
//                        }
//                    }).show();
                }
            });
        }



        private void showCoverDialog(final MovieWatch watchlist){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Notice");
            builder.setMessage("Are you sure to delete " + watchlist.getMovieName() + "?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //todo 删除本地数据库

                    movieWatchList.remove(watchlist);
                    addMovieWatchlist(movieWatchList);
                    vm.delete(watchlist);
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

        @Override
        public int getItemCount() {
            return movieWatchList.size();
        }
    }



}


