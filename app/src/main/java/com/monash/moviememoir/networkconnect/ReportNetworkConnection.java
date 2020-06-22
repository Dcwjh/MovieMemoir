package com.monash.moviememoir.networkconnect;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReportNetworkConnection {
    private OkHttpClient client;
    public static final MediaType JSON =
            MediaType.parse("application/json; charset=utf-8");

    private static final String BASE_URL =
            "http://192.168.31.91:8080/UserMovieMemoir/webresources/";

    public ReportNetworkConnection() {
        client = new OkHttpClient();
    }
    private Gson gson = new Gson();


//    //return postcode and the #
//    //userid begindate enddate
//    public String getPostcodeDataByUseridDate(int userid, String beginDate, String endDate) {
//        final String methodPath = "moviememoir.memoir/findTask4MethodA/" + userid + "/" + beginDate + "/" + endDate;
//        String results = null;
//        System.out.println(methodPath);
//        Request.Builder builder = new Request.Builder();
//        builder.url(BASE_URL + methodPath);
//        Request request = builder.build();
//        try {
//            Response response = client.newCall(request).execute();
//            results = response.body().string();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return results;
//    }
//
//    public String getMonthCountByUseridYear(int userid, String year) {
//        String results = null;
//        final String methodPath = "moviememoir.memoir/findTask4MethodB/" + userid + "/" + year;
//        System.out.println(methodPath);
//        Request.Builder builder = new Request.Builder();
//        builder.url(BASE_URL + methodPath);
//        Request request = builder.build();
//        try {
//            Response response = client.newCall(request).execute();
//            results = response.body().string();
//            System.out.println(results);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return results;
//    }


    public List<Map<String, Object>> getPostcodeDataByUseridDate(int userid, String startDate,String endDate) {
        final String methodPath = "moviememoir.memoir/findTask4MethodA/" + userid + "/" + startDate + "/" + endDate;
        String response = get(BASE_URL + methodPath);
        System.out.println(response);
        if (!response.isEmpty()) {
            return gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
            }.getType());
        }
        return null;
    }

    public List<Map<String, Object>> getMonthCountByUseridYear(int userid, String year) {
        final String methodPath = "moviememoir.memoir/findTask4MethodB/" + userid + "/" + year;
        String response = get(BASE_URL + methodPath);
        if (!response.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                return gson.fromJson(String.valueOf(jsonArray), new TypeToken<List<Map<String, Object>>>() {
                }.getType());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String get(String url) {
        String rs = "";
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            rs = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

}
