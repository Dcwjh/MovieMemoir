package com.monash.moviememoir.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.monash.moviememoir.R;
import com.monash.moviememoir.entity.Cinema;
import com.monash.moviememoir.entity.Users;
import com.monash.moviememoir.networkconnect.NetworkConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment  implements OnMapReadyCallback {
    private View view;
    private Context context;
    private Intent intent;
    private int userid;

    private GoogleMap mMap;
    private NetworkConnection networkConnection;
    private Map<String, LatLng> map;

    private static final int HOME_LOCATION = 1;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_maps, container, false);
        intent = getActivity().getIntent();
        userid = intent.getIntExtra("userid", 0);
        System.out.println("movie memoir userid " + userid);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();
        initData();
        initAction();
        return view;
    }
    private void initView(){

    }
    private  void initData(){
        networkConnection = new NetworkConnection();
        map = new HashMap<>();

    }
    private void initAction(){
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GetAllLocation getAllLocation = new GetAllLocation();
        getAllLocation.execute();

        GetHome getHome = new GetHome();
        getHome.execute(userid);


    }

    private class GetAllLocation extends AsyncTask<Integer, Void, List<Cinema>> {

        @Override
        protected List<Cinema> doInBackground(Integer... integers) {
            return networkConnection.getCinemas();
        }
        @Override
        protected void onPostExecute(List<Cinema> cinemas) {
            if(cinemas == null){
                //todo
            } else{
                for(Cinema cinema : cinemas) {
                    new DoConvertTask(cinema.getCinemaname()).execute(cinema.getCinemaname()                          + " Sydney, Australia");
                    System.out.println("cinema " + cinema.getCinemaname() + ", Australia");
                }
            }
        }

    }

    private class GetHome extends AsyncTask<Integer,Void, Users>{

        @Override
        protected Users doInBackground(Integer... integers) {
            return networkConnection.getUserbyid(integers[0]);
        }
        @Override
        protected void onPostExecute(Users user) {
            if(user == null){
                //todo
            }else {
                String location = user.getAddress() + ", " + user.getPostcode() + ", " + user.getState() + " Sydney Australia";
                System.out.println( "home " + location);
                new DoConvertTask("Home").execute(location);
            }
        }
    }


    private class DoConvertTask extends AsyncTask<String, Void, List<Double>> {
        private String name;
        public DoConvertTask(String name){
            this.name = name;
        }

        @Override
        protected List<Double> doInBackground(String... params) {
            return networkConnection.convertAddress2LL(params[0]);
        }

        @Override
        protected void onPostExecute(List<Double> response) {
            if (null != response) {
                if (response.size() == 2) {
                    Double latitude = response.get(0);
                    Double longitude = response.get(1);
                    if(name.equals("Home")) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11.0f ));
                    } else {
                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).position(new LatLng(latitude, longitude)).title(name));
                    }
                    map.put(name, new LatLng(latitude,longitude));
                }
            }
        }
    }





}
