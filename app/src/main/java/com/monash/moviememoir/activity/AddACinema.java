package com.monash.moviememoir.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Cinema;
import com.monash.moviememoir.fragment.MovieMemoirFragment;
import com.monash.moviememoir.networkconnect.NetworkConnection;

public class AddACinema extends AppCompatActivity implements View.OnClickListener{
    private EditText ac_cinema;
    private EditText ac_postcode;
    private Button ac_add;

    private Cinema cinema;
    private NetworkConnection networkConnection = null;

    private static final int RETURN_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_cinema);
        setTitle("Add A Cinema");
        initView();
        initData();
        initAction();
    }
    private void initView(){
        ac_cinema = findViewById(R.id.ac_cinemaname);
        ac_postcode = findViewById(R.id.ac_postcode);
        ac_add = findViewById(R.id.ac_add);

    }

    private void initData(){
        cinema = new Cinema();
        networkConnection = new NetworkConnection();

    }
    private void initAction(){
        ac_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ac_add:
                if(ac_cinema == null || ac_cinema.getText().toString().length() == 0){
                    inforProvide("Please input cinema!");
                } else if(ac_postcode == null || ac_postcode.getText().toString().length() == 0) {
                    inforProvide("Please input postcode");
                } else {
                    cinema.setCinemaname(ac_cinema.getText().toString());
                    cinema.setLocation(ac_postcode.getText().toString());
                    PostCinema postCinema = new PostCinema();
                    postCinema.execute(cinema);
                }
        }
    }


    private class PostCinema extends AsyncTask<Cinema, Void, Long> {
        @Override
        protected Long doInBackground(Cinema... cinemas) {
            return networkConnection.postCinema(cinemas[0]);
        }

        @Override
        protected void onPostExecute(Long result) {

            if(result == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddACinema.this);
                builder.setTitle("Notice");
                builder.setMessage("Connect database failed! Check the network.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            } else if(result == 0L){
                AlertDialog.Builder builder = new AlertDialog.Builder(AddACinema.this);
                builder.setTitle("Notice");
                builder.setMessage("Add failed!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            } else{
                Intent intent = new Intent();
                intent.putExtra("cinemaid", result);
                System.out.println("add cinema result " + result);
                intent.putExtra("cinemaname", ac_cinema.getText().toString());
                intent.putExtra("postcode",ac_postcode.getText().toString());
                setResult(RETURN_CODE, intent);
                finish();
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
