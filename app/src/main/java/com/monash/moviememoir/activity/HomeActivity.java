package com.monash.moviememoir.activity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Users;
import com.monash.moviememoir.fragment.HomeFragment;
import com.monash.moviememoir.fragment.MapFragment;
import com.monash.moviememoir.fragment.MovieMemoirFragment;
import com.monash.moviememoir.fragment.MovieSearchFragment;
import com.monash.moviememoir.fragment.ReportsFragment;
import com.monash.moviememoir.fragment.WatchListFragment;
import com.monash.moviememoir.networkconnect.NetworkConnection;


import org.jetbrains.annotations.NotNull;

import java.util.Stack;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private int userid;
    private Intent intent;
    private TextView title;
    private NetworkConnection networkConnection = null;
    private Users user = null;
    private int start = 0;


    public  static Stack<Fragment> stack = new Stack<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        intent = getIntent();
        //todo
        userid = intent.getIntExtra("userid",0);
        start = intent.getIntExtra("start",0);

        initView();
        initData();
        initAction();
        replaceFragment(new HomeFragment());
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nv);
        title = findViewById(R.id.home_title);
    }

    private void initData(){
        networkConnection = new NetworkConnection();
        setSupportActionBar(toolbar);
    }

    private void initAction(){
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                title.setText("Home");
                replaceFragment(new HomeFragment());
                break;
            case R.id.movie_search:
                title.setText("Movie Search");
                replaceFragment(new MovieSearchFragment());
                break;
            case R.id.movie_memoir:
                title.setText("Movie Memoir");
                replaceFragment(new MovieMemoirFragment());
                break;
            case R.id.watchlist:
                title.setText("Watch List");
                replaceFragment(new WatchListFragment());
                break;
            case R.id.reports:
                title.setText("Reports");
                replaceFragment(new ReportsFragment());
                break;
            case R.id.map:
                title.setText("Map");
                replaceFragment(new MapFragment());
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment nextFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent intent = new Intent();
        intent.putExtra("userid", userid);
        System.out.println("Home activity userid " + userid);
        HomeActivity.stack.push(new HomeFragment());
        fragmentTransaction.replace(R.id.content_frame, nextFragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }



    private class GetUser extends AsyncTask<String, Void, Users> {
        @Override
        protected Users doInBackground(String... strings) {
            return networkConnection.getUser(strings[0]);
        }

        @Override
        protected void onPostExecute(Users result) {
            if(result == null){
                inforProvide("Not connect database");
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
