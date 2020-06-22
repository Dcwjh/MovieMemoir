package com.monash.moviememoir.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Cinema;
import com.monash.moviememoir.entity.Memoir;
import com.monash.moviememoir.entity.Users;
import com.monash.moviememoir.networkconnect.IMDbConnect;
import com.monash.moviememoir.networkconnect.NetworkConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddMemoir extends AppCompatActivity implements View.OnClickListener{
    private ImageView aam_image;
    private int userid;
    private Intent intent;
    private Context context;


    private EditText am_watchdate;
    private EditText am_watchtime;
    private Spinner am_cinema;
    private TextView am_postcode;
    private EditText am_comment;
    private Button am_submit;
    private Button am_addcinema;
    private RatingBar am_ratingscore;
    private TextView am_moviename;
    private TextView am_releasedate;


    private String id;  //movie
    private String url;
    private String releasedate;
    private String moviename;

    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private NetworkConnection networkConnection;



    private IMDbConnect imDbConnect;
    private List<Cinema> cinemas;
    private ArrayAdapter<String> cinemaAdapter;

    private static final int REQUEST_CODE = 1;
    private static final int RESULT_CODE = 2;
    private static final int SUBMIT_RESULT_CODE = 3;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memoir);
        setTitle("Add Memoir");


        initView();
        initData();
        initAction();

    }

    private void initView(){
        aam_image = findViewById(R.id.aam_image);
//        Glide.with(this).load("https://imdb-api.com/images/original/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_Ratio0.7273_AL_.jpg).into(holder.imageView").into(aam_image);
        am_watchdate = findViewById(R.id.am_watchdate);
        am_watchtime = findViewById(R.id.am_watchtime);
        am_cinema = findViewById(R.id.am_cinema);
        am_postcode = findViewById(R.id.am_postcode);
        am_comment = findViewById(R.id.am_comment);
        am_submit = findViewById(R.id.am_submit);
        am_addcinema = findViewById(R.id.am_addcinema);
        am_ratingscore = findViewById(R.id.am_ratingscore);
        am_moviename = findViewById(R.id.am_moviename);
        am_releasedate = findViewById(R.id.am_releasedate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData(){


        intent = getIntent();
        userid = intent.getIntExtra("userid", 0);
        id = intent.getStringExtra("id");
        url = intent.getStringExtra("url");
        moviename = intent.getStringExtra("moviename");
        releasedate = intent.getStringExtra("releasedate");
        if(moviename!=null)
            am_moviename.setText(moviename);
        if(releasedate!=null)
            am_releasedate.setText(releasedate);
        if(url!=null)
            if(url != null)
                Glide.with(this).load(url).into(aam_image);



        //todo delete
        System.out.println("movie detail id " + id);
        System.out.println("movie detial url " + url);
        System.out.println("movie detial userid " + userid);


        imDbConnect = new IMDbConnect();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
        }
        calendar.setTime(date);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mSecond = calendar.get(Calendar.SECOND);
        networkConnection = new NetworkConnection();
//        new LoadCinemaTask().execute();
    }


    private void initAction(){
        am_addcinema.setOnClickListener(this);
        am_submit.setOnClickListener(this);
        am_watchdate.setOnClickListener(this);
        am_watchtime.setOnClickListener(this);
        am_cinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                am_postcode.setText(cinemas.get(position).getLocation());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                am_cinema.setSelection(0);
            }
        });
        new LoadCinemaTask().execute();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.am_addcinema:
                Intent intent1 = new Intent(this, AddACinema.class);
                startActivityForResult(intent1, REQUEST_CODE);
                break;
            case R.id.am_submit:


                GetUser getUser = new GetUser();
                getUser.execute(userid);

                break;
            case R.id.am_watchdate:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    new DatePickerDialog(AddMemoir.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int day) {
                                    // TODO Auto-generated method stub
                                    mYear = year;
                                    mMonth = month;
                                    mDay = day;
                                    am_watchdate.setText(new StringBuilder().append(mYear).append("-")
                                            .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                                            .append((mDay < 10) ? "0" + mDay : mDay));
                                }
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                break;
            case R.id.am_watchtime:
                am_watchtime.setFocusable(false);
                am_watchtime.setFocusableInTouchMode(false);
                am_watchtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(AddMemoir.this, AlertDialog.THEME_HOLO_LIGHT, timeListener, mHour, mMinute,true);
                        timePickerDialog.show();
                    }
                });
                break;
        }
    }


    private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            am_watchtime.setText(String.format("%02d:%02d:00", mHour, mMinute));
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_CODE) {
                assert data != null;
                Long cinemaid =data.getLongExtra("cinemaid",0L);
                String cinemaname = data.getStringExtra("cinemaname");
                String postcode = data.getStringExtra("postcode");
                Cinema cinema = new Cinema();
                cinema.setCinemaid(cinemaid);
                cinema.setCinemaname(cinemaname);
                cinema.setLocation(postcode);
                cinemas.add(cinema);
                updateCinemaSpinner(cinemas);
                System.out.println("beijihuo");
            }
        }
    }

    private class LoadCinemaTask extends AsyncTask<Void, String, List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(Void... voids) {
            return networkConnection.getCinemas();
        }

        @Override
        protected void onPostExecute(List<Cinema> response) {
            updateCinemaSpinner(response);
        }
    }



    private void updateCinemaSpinner(List<Cinema> cinemas) {
        this.cinemas = cinemas;
        List<String> list = new ArrayList<>();
        for (Cinema cinema : cinemas) {
            list.add(cinema.getCinemaname());
        }
        cinemaAdapter = new ArrayAdapter<String>(AddMemoir.this, android.R.layout.simple_spinner_item, list);
        am_cinema.setAdapter(cinemaAdapter);
        cinemaAdapter.notifyDataSetChanged();
    }

    private class GetUser extends AsyncTask<Integer, Void, Users> {
        @Override
        protected Users doInBackground(Integer... integers) {
            return networkConnection.getUserbyid(integers[0]);
        }

        @Override
        protected void onPostExecute(Users result) {
            if(result == null){
                inforProvide("Not connect database");
            } else {
                String watchDate = am_watchdate.getText().toString();
                if(watchDate == null || watchDate.length() <3)
                    watchDate = "2020-06-01";
                System.out.println("watch date " + watchDate);
                String watchTime = am_watchtime.getText().toString();
                String comment = am_comment.getText().toString();
                int ratingScore = (int) am_ratingscore.getRating() * 20 - 1;
                Cinema cinema = cinemas.get((int) am_cinema.getSelectedItemId());

                Memoir memoir = new Memoir();
                memoir.setMoviename(moviename);
                memoir.setRelease(releasedate + "T00:00:00+08:00");
                memoir.setWatchtime(watchDate + "T" + watchTime + "+08:00");
                memoir.setComment(comment);
                memoir.setScore(ratingScore);
                memoir.setCinemaid(cinema);
                memoir.setUserid(result);
                PostMemoirTask postMemoirTask = new PostMemoirTask();
                postMemoirTask.execute(memoir);
            }
        }
    }




    private class PostMemoirTask extends AsyncTask<Memoir, Void, String> {

        @Override
        protected String doInBackground(Memoir... params) {
            return networkConnection.postMemoir(params[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            System.out.println("add memoir response"  + response);
            if(response == null){
                inforProvide("Cannot connect database!");
            } else if( response.equals("success")){
                inforProvide("Add memoir to database successfully");
                Intent intent = new Intent(AddMemoir.this, HomeActivity.class);
                intent.putExtra("userid",userid);
                System.out.println("Add Memnir " + userid);
                startActivity(intent);
            }else{
                inforProvide("Failed to add memoir!");
            }
        }
    }

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

