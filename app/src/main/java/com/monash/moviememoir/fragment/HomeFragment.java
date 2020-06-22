package com.monash.moviememoir.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monash.moviememoir.R;
import com.monash.moviememoir.adapter.MyAdapter;
import com.monash.moviememoir.entity.Users;
import com.monash.moviememoir.networkconnect.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView home_firstname;
    private TextView home_date;
    private ListView home_top5;
    private View view;
    private int userid;
    private  Intent intent;
    NetworkConnection networkConnection = null;



    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);
        intent = getActivity().getIntent();
        //todo
        userid = intent.getIntExtra("userid", 0);
        System.out.println("home fragment userid " + userid);

        initView();
        initData();
        initAction();
        return view;
    }

    private void initView(){
        home_firstname =view.findViewById(R.id.home_userfirstname);
        home_date = view.findViewById(R.id.home_date);
        home_top5 = view.findViewById(R.id.home_top5);
    }

    private void initData(){
        networkConnection = new NetworkConnection();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        home_date.setText(sdf.format(date));
        GetUserName getUserName = new GetUserName();
        getUserName.execute(userid);
        GetTopFive getTopFive = new GetTopFive();
        getTopFive.execute(userid);

    }

    private void initAction(){

    }

    private class GetUserName extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            return networkConnection.getUserName(integers[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("############### user" + result);
            if (null != result) {
                home_firstname.setText("Welcome, " + result+ " !");
            } else {
                inforProvide(" Connect database failed");
            }

        }
    }

    private class GetUser extends AsyncTask<String, Void, Users> {
        @Override
        protected Users doInBackground(String... strings) {
            return networkConnection.getUser(strings[0]);
        }

        @Override
        protected void onPostExecute(Users result) {
            System.out.println("############### user" + result);
            if (null != result) {
                home_firstname.setText("Welcome, " + result.getUsername()+ " !");
                GetTopFive getTopFive = new GetTopFive();
                getTopFive.execute(result.getUserid());
            } else {
                inforProvide(" Connect database failed");
            }

        }
    }




    private void inforProvide(String msg){
        Toast toast = Toast.makeText(this.getContext(), "", Toast.LENGTH_SHORT);
        LinearLayout layout = (LinearLayout) toast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(18);
        toast.setText(msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
//        ImageView imageView= new ImageView(this.getContext());
//        imageView.setImageResource(R.drawable.warn);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.VERTICAL);
//        toastView.addView(imageView, 0);
        toast.show();
    }



    private class GetTopFive extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            return networkConnection.getTop5(integers[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("homefragment result" + result);
            if (null == result) {
                inforProvide(" connect database failed!");
            } else {
                try {
                    List<HashMap<String, Object>> movies = new ArrayList<>();
                    JSONArray array = new JSONArray(result);
                    Gson gson = new Gson();
                    for(int i = 0; i < array.length(); i++){
                        HashMap<String, Object> map = new HashMap<>();
                        JSONObject item = array.getJSONObject(i);
                        map = gson.fromJson(item.toString(), new TypeToken<HashMap<String,Object>>(){}.getType() );
                        movies.add(map);
                    }
                    if(movies.size() >= 5)
                         home_top5.setAdapter(new MyAdapter(HomeFragment.this.getContext(),movies,5));
                    else
                        home_top5.setAdapter(new MyAdapter(HomeFragment.this.getContext(),movies,movies.size()));

                } catch (JSONException e) {
                    e.printStackTrace();
                    inforProvide(" Data process failed!");
                }

            }
        }
    }

}
