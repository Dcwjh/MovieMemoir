package com.monash.moviememoir.fragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.utils.Utils;
import com.monash.moviememoir.R;
import com.monash.moviememoir.activity.HomeActivity;
import com.monash.moviememoir.database.MovieWatchDatabase;
import com.monash.moviememoir.entity.MovieDetail;
import com.monash.moviememoir.entity.MovieWatch;
import com.monash.moviememoir.networkconnect.IMDbConnect;
import com.monash.moviememoir.viewmodel.MovieWatchViewModel;

import java.text.ParseException;
import java.util.Date;

public class MovieView extends Fragment implements View.OnClickListener{
    private ImageView mv_image;
    private TextView mv_moviename;
    private TextView mv_director;
    private TextView mv_releasedate;
    private TextView mv_genre;
    private TextView mv_country;
    private TextView mv_numberscore;
    private RatingBar mv_ratingbar;
    private TextView mv_cast;
    private TextView mv_plotsummary;
    private Button mv_addtolist;
    private Button mv_addtomem;
    private TextView title;
    private ImageView mv_list_image;
    private ImageView mv_mem_image;

    private Intent intent;
    private String id;
    private IMDbConnect imDbConnect = null;
    private Context context;
    private String url;
    private boolean flag = false;
    private View view;
    private int userid;
    private  AppCompatActivity activity;

    private MovieWatchDatabase mwd;
    private MovieWatchViewModel mwv;

    public MovieView() {
    }

    //todo no use
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_movie_view, container, false);
        initView();
        initData();
        initAction();
        return view;
    }

    private void initView(){
        mv_image = view.findViewById(R.id.mv_image);
        mv_moviename = view.findViewById(R.id.mv_moviename);
        mv_director = view.findViewById(R.id.mv_director);
        mv_releasedate = view.findViewById(R.id.mv_releasedate);
        mv_genre = view.findViewById(R.id.mv_genre);
        mv_country = view.findViewById(R.id.mv_country);
        mv_numberscore = view.findViewById(R.id.mv_numberscore);
        mv_ratingbar = view.findViewById(R.id.mv_ratingbar);
        mv_cast = view.findViewById(R.id.mv_cast);
        mv_plotsummary = view.findViewById(R.id.mv_plotsummary);
        mv_addtolist = view.findViewById(R.id.mv_addtolist);
        mv_addtomem = view.findViewById(R.id.mv_addtomem);
        mv_list_image = view.findViewById(R.id.mv_list_image);
        mv_mem_image = view.findViewById(R.id.mv_mem_image);

    }

    private void initData(){

        activity = (AppCompatActivity) getActivity();
        intent = getActivity().getIntent();
        context = getActivity();
        userid = intent.getIntExtra("userid", 0);
        id = intent.getStringExtra("id");
        url = intent.getStringExtra("url");
        System.out.println("picture url " + url);

        imDbConnect = new IMDbConnect();
        title = activity.findViewById(R.id.home_title);
        title.setText("Movie detail");

        System.out.println("movie detail id " + id);
        System.out.println("movie detial url " + url);

    }

    private void initAction(){
        mv_addtolist.setOnClickListener(this);
        mv_addtomem.setOnClickListener(this);
        mv_list_image.setOnClickListener(this);
        GetMovieDetial getMovieDetial = new GetMovieDetial();
        System.out.println("Movie View userid " + userid);
        getMovieDetial.execute(id);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mv_list_image:
                if (flag) {
                    mv_list_image.setImageResource(R.drawable.want);
                    flag = false;
                    //添加逻辑 Todo  添加到moviewatch里面

                } else {
                    mv_list_image.setImageResource(R.drawable.added);
                    flag = true;
                    //添加逻辑 Todo  取消添加到moviewatch里面
                }
                break;
            case R.id.mv_addtolist:  //button
                //添加逻辑 todo 和上面一样
                break;
            case R.id.mv_addtomem:
                //todo 跳转到add memoir里面
//
                break;
            default:
                inforProvide(" Movie View Unkown Error");
                break;

        }
    }


    private class GetMovieDetial extends AsyncTask<String, Void, MovieDetail> {
        private Context mContext;

        public GetMovieDetial(){

        }

        public GetMovieDetial(Context mContext){
            this.mContext = mContext;
        }

        @Override
        protected  MovieDetail doInBackground(String... strings) {
            return imDbConnect.getMovieDetail(strings[0]);
        }

        @Override
        protected void onPostExecute(final MovieDetail result) {
            if (result == null) {
                inforProvide(" connect immwd.com failed");
            } else{
                System.out.println("result Movie search" + result);
                if(url != null && context!=null)
                    Glide.with(context).load(url).into(mv_image);
                else if(result.getImageUrl() != null)
                    Glide.with(context).load(url).into(mv_image);
                mv_cast.setText(result.getCast());
                mv_country.setText(result.getCountries());
                mv_director.setText(result.getDirectors());
                mv_genre.setText(result.getGenres());
                mv_moviename.setText(result.getName());
                mv_plotsummary.setText(result.getPlot());
                float score = 0;
                if(result.getRatingScores() == null ||  "".equals( result.getRatingScores()))
                    score = 75;
                else
                    score = Float.valueOf(result.getRatingScores()) * 10;

                mv_numberscore.setText(String.valueOf((int)score));
                mv_ratingbar.setRating((score/20));
                mv_releasedate.setText(result.getReleaseDate());
            }

        }
    }

    private class CheckMovieWatchTask extends AsyncTask<String, Void, MovieWatch> {

        @Override
        protected MovieWatch doInBackground(String... params) {
            return mwd.moviewatchDao().findByIMDBID(params[0]);
        }

        @Override
        protected void onPostExecute(MovieWatch moviewatch) {
            if (moviewatch != null) {
                mv_addtolist.setEnabled(false);
                mv_list_image.setEnabled(false);
                mv_list_image.setImageResource(R.drawable.added);
                mv_addtolist.setBackgroundColor(Color.parseColor("#C0C0C0"));
                mv_list_image.setBackgroundColor(Color.parseColor("#C0C0C0"));
            } else {

//                MovieWatch moviewatch = new MovieWatch();
//                moviewatch.setMovieName(tvMovieName.getText().toString());
//                try {
//                    Date releaseDate = sdf.parse(tvReleaseDate.getText().toString());
//                    moviewatch.setReleaseDate(releaseDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                moviewatch.setAddDatetime(new Date());
//                moviewatch.setIMDbID(IMDbID);
//                vm.insert(moviewatch);
//                sendMessage("successfully add to moviewatch");
//                btnMovieWatch.setEnabled(false);
//                btnMovieWatch.setBackgroundColor(Color.parseColor("#C0C0C0"));

            }
        }
    }

    private void inforProvide(String msg){
        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(18);
        toast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ImageView imageView= new ImageView(context);
        imageView.setImageResource(R.drawable.warn);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.HORIZONTAL);
        toastView.addView(imageView, 0);
        toast.show();
    }


}
