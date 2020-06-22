package com.monash.moviememoir.networkconnect;

import android.util.JsonToken;
import android.util.Log;
import android.view.accessibility.AccessibilityRecord;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.monash.moviememoir.entity.Cinema;
import com.monash.moviememoir.entity.Credentials;
import com.monash.moviememoir.entity.Memoir;
import com.monash.moviememoir.entity.Users;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
    private Gson gson;

    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {

        client = new OkHttpClient();
        gson = new Gson();

    }

    private static final String BASE_URL =
            "http://192.168.31.91:8080/UserMovieMemoir/webresources/";

    public String checkEmail(String email) {
        final String methodPath = "moviememoir.credentials/findByUsername/" + email;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        String results = null;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            System.out.println("##############"+results);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }


    public Users getUser(String email) {
        final String methodPath = "moviememoir.credentials/findByUsername/" + email;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        Users user = null;
        JSONObject item = null;
        try {
            Response response = client.newCall(request).execute();
            String results = response.body().string();
            JSONArray json = new JSONArray(results);   //String -->JSONArray --> get(index):JSONObject  --> .toString .json
            item = json.getJSONObject(0);

            Credentials credential = gson.fromJson(item.toString(),  new TypeToken<Credentials>(){}.getType());
            user = credential.getUserid();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }



    public String doRegister(Credentials credential) {
        final String methodPath = "moviememoir.credentials";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RequestBody requestBody = new FormBody.Builder()
                .add("username", credential.getUsername())
                .add("password", credential.getPassword())
                .add("signupdate", credential.getSingupdate())
                .add("name", credential.getUserid().getUsername())
                .add("surname", credential.getUserid().getSurname())
                .add("gender", credential.getUserid().getGender())
                .add("dob", credential.getUserid().getDob())
                .add("address", credential.getUserid().getAddress())
                .add("state", credential.getUserid().getState())
                .add("postcode", credential.getUserid().getPostcode())
                .build();
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(requestBody).build();
        System.out.println(credential);
        String strResponse = null;
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(strResponse);
        return strResponse;
    }


    public String doLogin(String username, String password) {
        final String methodPath = "moviememoir.credentials/login";
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String userInfo = gson.toJson(map); //传递json
        RequestBody requestBody = RequestBody.create(userInfo, JSON);
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(requestBody).build();
        String strResponse = null;
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
            System.out.println("result 4444444444 " + strResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }


    public String getTop5(Integer userid) {
        final String methodPath = "moviememoir.memoir/findTask4MethodF/" + userid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        System.out.println(BASE_URL + methodPath);
        Request request = builder.build();
        String results = null;
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
            System.out.println("top5" + results);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public String getUserName(Integer userid) {
        final String methodPath = "moviememoir.users/" + userid;
        System.out.println("URL" + BASE_URL + methodPath);
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        Users  user = null;
        try {
            Response response = client.newCall(request).execute();
            String results = response.body().string();
            System.out.println("get user name : " + results);
            user = gson.fromJson(results,  new TypeToken<Users>(){}.getType());
            System.out.println("user" + results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user.getUsername();
    }

    public Long postCinema(Cinema cinema) {
        final String methodPath = "moviememoir.cinema";
        RequestBody requestBody = new FormBody.Builder()
                .add("cinemaname", cinema.getCinemaname())
                .add("location", cinema.getLocation()).build();
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(requestBody).build();
        Long strResponse = null;
        try {
            Response response = client.newCall(request).execute();
            strResponse = Long.valueOf(response.body().string());
            System.out.println(strResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }



    public List<Cinema> getCinemas() {
        final String methodPath = "moviememoir.cinema/";
        String rs = null;
        Request request = new Request.Builder().url(BASE_URL + methodPath).get().build();
        try {
            Response response = client.newCall(request).execute();
            rs = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Cinema> cinemas = new ArrayList<>();
        try {

            cinemas = gson.fromJson(rs, new TypeToken<List<Cinema>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0; i< cinemas.size(); i++)
            System.out.println(cinemas.get(i));
        return cinemas;
    }


    public String postMemoir(Memoir memoir) {
        final String methodPath = "moviememoir.memoir";
        String jsonData = gson.toJson(memoir);
        RequestBody requestBody = RequestBody.create(jsonData, JSON);
        System.out.println("jsonData " + jsonData);
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failed";
    }


    public Users getUserbyid(int userid) {
        final String methodPath = "moviememoir.users/" + userid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        Users user = null;
        JSONObject item = null;
        try {
            Response response = client.newCall(request).execute();
            String results = response.body().string();
            user = gson.fromJson(results,  new TypeToken<Users>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    public List<Double> convertAddress2LL(String address) {
        String url = "http://www.mapquestapi.com/geocoding/v1/address?key=JldaMCy6l5KH77DLnHtmTnV11O6VnJNT&location=" + address;
        String response = get(url);
        try {
            Map<String, Object> map = gson.fromJson(response, new TypeToken<Map<String, Object>>() {
            }.getType());
            List<Map<String, Object>> results = (List<Map<String, Object>>) map.get("results");
            for (Map<String, Object> result : results) {
                List<Map<String, Object>> locations = (List<Map<String, Object>>) result.get("locations");
                for (Map<String, Object> location : locations) {
                    Map<String, Double> latLng = (Map<String, Double>) location.get("latLng");
                    List<Double> rs = new LinkedList<>();
                    rs.add(latLng.get("lat"));
                    rs.add(latLng.get("lng"));
                    return rs;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String get(String url) {
        String rs = "";
        Request request = new Request.Builder().url(url).get().build();
        System.out.println(url);
        try {
            Response response = client.newCall(request).execute();
            rs = response.body().string();
            System.out.println(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public List<Memoir> getMemoirs(Integer param) {
        String  url = BASE_URL + "moviememoir.memoir/" + param;
        String rs = "";
        Request request = new Request.Builder().url(url).get().build();
        System.out.println(url);
        try {
            Response response = client.newCall(request).execute();
            rs = response.body().string();
            rs = null;
            System.out.println(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
