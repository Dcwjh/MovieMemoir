package com.monash.moviememoir.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.monash.moviememoir.R;
import com.monash.moviememoir.database.MovieWatchDatabase;
import com.monash.moviememoir.entity.MovieWatch;
import com.monash.moviememoir.networkconnect.IMDbConnect;
import com.monash.moviememoir.viewmodel.MovieWatchViewModel;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

public class MovieDetail extends AppCompatActivity implements View.OnClickListener, IWXAPIEventHandler {
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
    private ImageView mv_list_image;
    private ImageView mv_mem_image;
    private LinearLayout linearLayout;
    private Intent intent;
    private String id;
    private IMDbConnect imDbConnect = null;
    private TextView share;

    private String url;
    private int userid;
    private boolean flag = false;
    private int light;
    private static final String APP_ID = "gh_8689ff7c71b4";
    private IWXAPI api;

    private MovieWatchDatabase mwd;
    private MovieWatchViewModel mwv;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setTitle("Movie Detial");
        initView();
        initData();
        initAction();
    }

    private void initView(){
        mv_image = findViewById(R.id.mv_image);
        mv_moviename = findViewById(R.id.mv_moviename);
        mv_director = findViewById(R.id.mv_director);
        mv_releasedate = findViewById(R.id.mv_releasedate);
        mv_genre = findViewById(R.id.mv_genre);
        mv_country = findViewById(R.id.mv_country);
        mv_numberscore = findViewById(R.id.mv_numberscore);
        mv_ratingbar = findViewById(R.id.mv_ratingbar);
        mv_cast = findViewById(R.id.mv_cast);
        mv_plotsummary = findViewById(R.id.mv_plotsummary);
        mv_addtolist = findViewById(R.id.mv_addtolist);
        mv_addtomem = findViewById(R.id.mv_addtomem);
        mv_list_image = findViewById(R.id.mv_list_image);
        mv_mem_image = findViewById(R.id.mv_mem_image);
        linearLayout = findViewById(R.id.mv_unable);
        share = findViewById(R.id.share);

    }

    private void initData(){
        intent = getIntent();
        userid = intent.getIntExtra("userid", 0); //userid
        id = intent.getStringExtra("id");  //movie id
        System.out.println("movie detail id " + id);
        url = intent.getStringExtra("url");
        System.out.println("movie detial picture url " + url);
        light = intent.getIntExtra("light",0);
        System.out.println("Movie detial light "+ light);
        imDbConnect = new IMDbConnect();
        if(light == 0){
                mv_list_image.setEnabled(false);
                mv_addtolist.setEnabled(false);
                mv_list_image.setImageResource(R.drawable.added);
                mv_addtolist.setBackgroundColor(Color.parseColor("#666666"));
                mv_list_image.setBackgroundColor(Color.parseColor("#666666"));
                linearLayout.setBackgroundColor(Color.parseColor("#666666"));
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        mwd = MovieWatchDatabase.getInstance(this);
        mwv = new ViewModelProvider(this).get(MovieWatchViewModel.class);
        mwv.initalizeVars(getApplication());
    }

    private void initAction(){
        GetMovieDetial getMovieDetial = new GetMovieDetial();
        getMovieDetial.execute(id); //receive movie data
        CheckWatchlistTask checkWatchlistTask = new CheckWatchlistTask();
        checkWatchlistTask.execute(id); // check movie exist?

        mv_addtolist.setOnClickListener(this);
        mv_addtomem.setOnClickListener(this);
        mv_list_image.setOnClickListener(this);

        System.out.println("Movie Detial userid " + userid);

        share.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //add to watch list
            case R.id.mv_list_image:
            case R.id.mv_addtolist:
                mv_list_image.setImageResource(R.drawable.added);
                MovieWatch watchlist = new MovieWatch();
                watchlist.setMovieName(mv_moviename.getText().toString());
                try {
                    Date releaseDate = sdf.parse(mv_releasedate.getText().toString());
                    watchlist.setReleaseDate(releaseDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                Date date = new Date();
//                String str = sdf.format(date);
                watchlist.setAddDatetime(new Date());
                watchlist.setIMDbID(id);
                watchlist.setImageurl(url);
                mwv.insert(watchlist);
                inforProvide("successfully add to watchlist");
                mv_list_image.setEnabled(false);
                mv_addtolist.setEnabled(false);
                mv_list_image.setImageResource(R.drawable.added);
                mv_addtolist.setBackgroundColor(Color.parseColor("#666666"));
                mv_list_image.setBackgroundColor(Color.parseColor("#666666"));
                linearLayout.setBackgroundColor(Color.parseColor("#666666"));
                break;
             //add to memoir
            case R.id.mv_addtomem:
            case R.id.mv_mem_image:
                Intent intent = new Intent(this, AddMemoir.class);
                intent.putExtra("id",id);  //movie id
                intent.putExtra("userid",userid);
                intent.putExtra("url",url);
                intent.putExtra("moviename",mv_moviename.getText().toString());
                intent.putExtra("releasedate",mv_releasedate.getText().toString());
                startActivity(intent);
                break;
            case R.id.share:
                //share to Wechat
                    regToWx();
                break;
            default:
                inforProvide(" Movie View Unkown Error");
                break;

        }
    }

    private void regToWx() {
        // Get an instance of the IWXAPI through the WXAPIFactory
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);

        // Register the appId of the application to WeChat
        api.registerApp(APP_ID);

        //It is recommended to dynamically listen to WeChat start broadcasting to register to WeChat
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Register the app with WeChat
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

        if (api.getWXAppSupportAPI() >= Build.TIMELINE_SUPPORTED_SDK_INT) {
            //Initializes a WXTextObject object to fill in the Shared text content
            WXTextObject textObj = new WXTextObject();
            textObj.text =  mv_moviename.getText().toString() + " it's googd movie!";

            //Initialize a WXMediaMessage object with a WXTextObject object
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = textObj;
            msg.description = mv_plotsummary.getText().toString();

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());  //transaction字段用与唯一标示一个请求
            req.message = msg;
            req.scene = WXSceneSession;

            //call API interface, send data to WeChat
            api.sendReq(req);
        }

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i("WXTest","onResp OK");

                if(resp instanceof SendAuth.Resp){
                    SendAuth.Resp newResp = (SendAuth.Resp) resp;
                    //
                    String code = newResp.code;
                    Log.i("WXTest","onResp code = "+code);
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i("WXTest","onResp ERR_USER_CANCEL ");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i("WXTest","onResp ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                Log.i("WXTest","onResp default errCode " + resp.errCode);
                //发送返回
                break;
        }
        finish();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class GetMovieDetial extends AsyncTask<String, Void, com.monash.moviememoir.entity.MovieDetail> {
        @Override
        protected com.monash.moviememoir.entity.MovieDetail doInBackground(String... strings) {
            return imDbConnect.getMovieDetail(strings[0]);
        }

        @Override
        protected void onPostExecute(final com.monash.moviememoir.entity.MovieDetail result) {
            if (result == null) {
                inforProvide(" connect immwd.com failed");
            } else{
                System.out.println("result Movie search" + result);
                if(url != null)
                    Glide.with(getApplicationContext()).load(url).into(mv_image);
                else if(result.getImageUrl() != null)
                    Glide.with(getApplicationContext()).load(url).into(mv_image);
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

    private class CheckWatchlistTask extends AsyncTask<String, Void, MovieWatch> {
        @Override
        protected  MovieWatch doInBackground(String... params) {
            System.out.println("params[0] " + params[0]);
            return mwd.moviewatchDao().findByIMDBID(params[0]);
        }

        @Override
        protected void onPostExecute(MovieWatch watchlist) {
            System.out.println("Movie detail watchlist " + watchlist);
            if (watchlist != null) {
                mv_list_image.setEnabled(false);
                mv_addtolist.setEnabled(false);
                mv_list_image.setImageResource(R.drawable.added);
                mv_addtolist.setBackgroundColor(Color.parseColor("#666666"));
                mv_list_image.setBackgroundColor(Color.parseColor("#666666"));
                linearLayout.setBackgroundColor(Color.parseColor("#666666"));
            }
        }
    }

//
//    private class CheckWatchlistTask extends AsyncTask<Void, Void, List<MovieWatch>> {
//
//
//        @Override
//        protected  List<MovieWatch> doInBackground(Void... voids) {
//            return (List<MovieWatch>) mwd.moviewatchDao().getAll();
//        }
//
//        @Override
//        protected void onPostExecute(List<MovieWatch> watchlist) {
//            for(MovieWatch watch : watchlist)
//                System.out.println("Movie detail watchlist " + watch);
//            if (watchlist != null) {
//                mv_list_image.setEnabled(false);
//                mv_addtolist.setEnabled(false);
//                mv_list_image.setImageResource(R.drawable.added);
//                mv_addtolist.setBackgroundColor(Color.parseColor("#666666"));
//                mv_list_image.setBackgroundColor(Color.parseColor("#666666"));
//                linearLayout.setBackgroundColor(Color.parseColor("#666666"));
//            }
//        }
//    }

//    private void bindWatchlistEvent() {
//       mv_addtolist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MovieWatch watchlist = new MovieWatch();
//                watchlist.setMovieName(mv_moviename.getText().toString());
//                watchlist.setReleaseDate(mv_releasedate.getText().toString());
//                Date date = new Date();
//                String str = sdf.format(date);
//                watchlist.setAddDatetime(str);
//                watchlist.setIMDbID(id);
//                mwv.insert(watchlist);
//                inforProvide("successfully add to watchlist");
//                mv_list_image.setEnabled(false);
//                mv_addtolist.setEnabled(false);
//                mv_list_image.setImageResource(R.drawable.added);
//                mv_addtolist.setBackgroundColor(Color.parseColor("#666666"));
//                mv_list_image.setBackgroundColor(Color.parseColor("#666666"));
//                linearLayout.setBackgroundColor(Color.parseColor("#666666"));
//            }
//        });
//    }




    private void inforProvide(String msg){
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(18);
        toast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.VERTICAL);
        toast.show();
    }


}
