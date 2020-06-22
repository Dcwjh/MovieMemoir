package com.monash.moviememoir.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import com.monash.moviememoir.R;
import com.monash.moviememoir.activity.MovieDetail;
import com.monash.moviememoir.adapter.MovieMemoirAdapter;
import com.monash.moviememoir.entity.Memoir;
import com.monash.moviememoir.entity.MemoirView;
import com.monash.moviememoir.networkconnect.NetworkConnection;
import com.monash.moviememoir.viewmodel.MovieWatchViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MovieMemoirFragment extends Fragment implements  View.OnClickListener{
    private Button mm_releasedate;
    private Button mm_watchdate;
    private Button mm_userscore;
    private Button mm_publicscore;
    private Spinner mm_genre;
    private ListView result;
    private View view;
    private Context context;
    private Intent intent;
    private int userid;
    private NetworkConnection networkConnection;

    private List<MemoirView> allmovies = new ArrayList<>();
    private List<Memoir> newList = new ArrayList<>();

    private static final int RELEASE_DATE_CODE = 1;
    private static final int WATChTIME_DATE_CODE = 2;
    private static final int USERSCORE_CODE = 3;
    private static final int PUBLICSCORE_CODE = 4;

   private MovieMemoirAdapter memoirAdapter;

    private String gener;
    public MovieMemoirFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.movie_memoir, container, false);
        intent = getActivity().getIntent();
        userid = intent.getIntExtra("userid", 0);
        System.out.println("movie memoir userid " + userid);

        initView();
        initData();
        initAction();
        return view;
    }
    private void initView(){
        mm_releasedate = view.findViewById(R.id.mm_releasedate);
        mm_watchdate = view.findViewById(R.id.mm_watchdate);
        mm_userscore = view.findViewById(R.id.mm_userscore);
        mm_publicscore = view.findViewById(R.id.mm_publicscore);
        mm_genre = view.findViewById(R.id.mm_genre);
        result = view.findViewById(R.id.mm_result);
    }
    public void initData(){
        allmovies =  getAllmovies(0);
        context = this.getContext();
        networkConnection = new NetworkConnection();
        GetAllMemoirsTask getAllMemoirsTask = new GetAllMemoirsTask();
        memoirAdapter = new MovieMemoirAdapter(context,allmovies,userid);
    }

    public void initAction(){
        mm_releasedate.setOnClickListener(this);  //release date  sort
        mm_watchdate.setOnClickListener(this);   //watch date 排序
        mm_userscore.setOnClickListener(this);  //
        mm_publicscore.setOnClickListener(this);

        mm_genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    memoirAdapter.setMemoirs(allmovies);
                else {
                    String gener = parent.getItemAtPosition(position).toString();
                    List<MemoirView> movie_gener = new ArrayList<>();
                    allmovies = getAllmovies(0);
                    movie_gener.addAll(allmovies);
                    allmovies.clear();
                    for (MemoirView memoirView : movie_gener) {
                        System.out.println("movieView " + memoirView.getGenre());
                        if(memoirView.getGenre() != null) {
                            if (memoirView.getGenre().contains(gener))
                                allmovies.add(memoirView);
                        }
                    }
                    memoirAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context,"跳转" + position  + "页", Toast.LENGTH_SHORT);
                switch (parent.getId()){
                    case R.id.ms_reslut:
                        Intent intent = new Intent(getActivity(), MovieDetail.class);
                        intent.putExtra("id", allmovies.get(position).getId());
                        intent.putExtra("url",allmovies.get(position).getImageUrl());
                        intent.putExtra("userid",userid);
                        intent.putExtra("light",0);
                        context.startActivity(intent);
                        break;
                }
            }
        });


        result.setAdapter(memoirAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mm_releasedate:
                Collections.sort(newList, new Comparator<Memoir>() {
                            public int compare(Memoir m1, Memoir m2) {
                                return m2.getRelease().compareTo(m1.getRelease());
                            }
                        });
                allmovies.clear();
                allmovies = getAllmovies(RELEASE_DATE_CODE);
                memoirAdapter.notifyDataSetChanged();

                break;
            case R.id.mm_watchdate:
                Collections.sort(newList, new Comparator<Memoir>() {
                    public int compare(Memoir m1, Memoir m2) {
                        return m2.getWatchtime().compareTo(m1.getWatchtime());
                    }
                });
                allmovies.clear();
                allmovies = getAllmovies(WATChTIME_DATE_CODE);
                memoirAdapter.notifyDataSetChanged();

                break;
            case R.id.mm_userscore:
                Collections.sort(newList, new Comparator<Memoir>() {
                    public int compare(Memoir m1, Memoir m2) {
                        return m2.getScore()-m1.getScore();
                    }
                });
                allmovies.clear();
                allmovies = getAllmovies(USERSCORE_CODE);
                memoirAdapter.notifyDataSetChanged();
                break;


            case R.id.mm_publicscore:
                Collections.sort(newList, new Comparator<Memoir>() {
                    public int compare(Memoir m1, Memoir m2) {
                        return m2.getScore()-m1.getScore();
                    }
                });
                allmovies.clear();
                allmovies = getAllmovies(PUBLICSCORE_CODE);
                memoirAdapter.notifyDataSetChanged();

                break;
        }
    }


    private class GetAllMemoirsTask extends AsyncTask<Integer, Void, List<Memoir>> {

        @Override
        protected List<Memoir> doInBackground(Integer... params) {
            return networkConnection.getMemoirs(params[0]);
        }

        @Override
        protected void onPostExecute(List<Memoir> memoirs) {
            if(memoirs!=null){
                for (Memoir memoir : memoirs) {
                    newList.add(memoir);
                }
            }
        }
    }




































































































































































//gener:Comedy,Drama,Romance, Animation, Adventure,Sci-Fi,Animation, Adventure, Drama, Fantasy,Crime, Drama, Thriller,Biography

    private List<MemoirView> getAllmovies(int optiton){
        allmovies.clear();
        MemoirView memoirView0 = new MemoirView("tt0118799","Life is Beautiful","1997-12-20","2008-12-01","This is one of those movies that have a lasting effect on you. After watching it, I found that it has less to do with the Holocaust and more to do with the human feelings and the beautiful relationship of a father and his son. The holocaust provides the ultimate context, that brings and highlights the story and adds yet another deep dimension to the movie.",
                99,87,"2560","https://m.media-amazon.com/images/M/MV5BYmJmM2Q4NmMtYThmNC00ZjRlLWEyZmItZTIwOTBlZDQ3NTQ1XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SY1000_SX670_AL_.jpg");
        memoirView0.setGenre("Comedy,Drama,Romance");   //显示第二个

        MemoirView memoirView1 = new MemoirView("tt7605074","The Wandering Earth","2019-12-20","2019-12-27","I have never in my life seen a movie that so truly symbolizes its name and its message. Roberto Benigni - 'The Italian Treasure House' has probably given to his viewers his best. ",
                99,89,"2560","https://m.media-amazon.com/images/M/MV5BMjE2NzZlMGItMzA4OS00ZjRiLTk3NzItMDRkOGFlZmNhYzJkXkEyXkFqcGdeQXVyNzI1NzMxNzM@._V1_SY1000_CR0,0,714,1000_AL_.jpg");
        memoirView1.setGenre("action,Sci-Fi");

        MemoirView memoirView2 = new MemoirView("tt6105098","The Lion King", "2019-07-12", "2019-07-19", "This is by far and away one of my top movies of all time and definitely a movie that I can say the lines pretty much the whole way through.\n" +
                "After thinking about \"what I was disappointed with\", I would say the following:\n" +
                "1. The broadway voices of Nathan lane, Ernie sambella and Jeremy irons really added that punch to the characters that you \"don''t know that you''re missing till it''s gone\".",
                78,84,"2000","https://m.media-amazon.com/images/M/MV5BMjIwMjE1Nzc4NV5BMl5BanBnXkFtZTgwNDg4OTA1NzM@._V1_SY1000_CR0,0,674,1000_AL_.jpg");
        memoirView2.setGenre(" Animation, Adventure, Drama");

        MemoirView memoirView3 = new MemoirView("tt2527338","Star Wars: Episode IX - The Rise of Skywalker", "2019-12-19","2019-12-22","Where the Mandalorian feels like somebody taking their own beloved Star Wars fantasy and putting it on screen, this feels like a marketing product. room for a kid to look over the setting alien suns of his planet and dream of something more.",
                88,78,"2144","https://m.media-amazon.com/images/M/MV5BMDljNTQ5ODItZmQwMy00M2ExLTljOTQtZTVjNGE2NTg0NGIxXkEyXkFqcGdeQXVyODkzNTgxMDg@._V1_SY1000_CR0,0,675,1000_AL_.jpg");
        memoirView3.setGenre("Action, Adventure, Fantasy");

        MemoirView memoirView4 = new MemoirView("tt1533117","Rang zi dan fei", "2010-12-16","2015-05-10","This is the best Clint Eastwood \"Spaghetti Western-type\" movie ever made, except that it was made in China and Clint had nothing to do with it."
                + "If there was a better film made in 2010, I don't know what it is. I'm sure that I missed a ton of allusions and stuff that a Chinese person would catch instantly, but it doesn''t matter, because even if I didn''t get half of it, the half I got was still over-the-top great.",
                93,60,"2114","https://m.media-amazon.com/images/M/MV5BMjI2NDQ5Mzg0NF5BMl5BanBnXkFtZTgwMDM3NjI2MDE@._V1_SY1000_CR0,0,666,1000_AL_.jpg");
        memoirView4.setGenre("Action, Comedy, Drama ");

        MemoirView memoirView5 = new MemoirView("tt7286456","Joker", "2019-08-04","2019-09-10","I was a person that saw all the hype and claims of masterpiece as overreacting and overblown excitement for another Joker based film. "+
                "I thought this looked solid at best and even a bit too pretentious in the trailer, but in here to say I was incredibly wrong. This is a massive achievement of cinema that''s extremely rare in a day and age of cgi nonsense and reboots." +
                "While this is somewhat of a reboot of sorts, the standalone origin tale is impeccable from start to finish and echoes resemblance to the best joker origin comics from the past. Joaquin bleeds, sweats, and cries his every drop into this magnificently dedicated performance. Heath Ledger would be proud.",
                90,85,"2560","https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SY1000_CR0,0,674,1000_AL_.jpg");
        memoirView5.setGenre("Crime, Drama, Thriller");

        MemoirView memoirView6 = new MemoirView("tt1677720","Ready Player One","2018-03-30","2018-04-01","Coming from the producing mind of James Cameron, Alita: Battle Angel is one of the most visually impressive films of recent memory, though it doesn't necessarily have the script to go along with those dazzling visuals. Rosa Salazar is fantastic as the titular Alita, and once again proves that she deserves more starring roles in Hollywood.",
                88,90,"2000","https://m.media-amazon.com/images/M/MV5BY2JiYTNmZTctYTQ1OC00YjU4LWEwMjYtZjkwY2Y5MDI0OTU3XkEyXkFqcGdeQXVyNTI4MzE4MDU@._V1_SY1000_CR0,0,674,1000_AL_.jpg");
        memoirView6.setGenre("Action, Adventure, Sci-Fi ");

        MemoirView memoirView7 = new MemoirView("tt0437086","Alita: Battle Angel","2019-02-22","2018-04-04","I've noticed quite a few reviews here from book fans complaining that the movie wasn't true to the novel. As a fan of the book, let me just say that's true but it's fine. The overarching story is the same. The fact of the matter is with a nearly 400 page novel packed full of pop culture references, some things would have to be cut to make it onto the big screen. Partially it's an issue of length. Partially it's just the reality that the planets were never going to fully align to allow use of many of the properties from the novel",
                75,84,"2560","https://m.media-amazon.com/images/M/MV5BMTQzYWYwYjctY2JhZS00NTYzLTllM2UtZWY5ZTk0NmYwYzIyXkEyXkFqcGdeQXVyMzgxODM4NjM@._V1_SY1000_CR0,0,675,1000_AL_.jpg");
        memoirView7.setGenre(" Action, Adventure, Sci-Fi");


        MemoirView memoirView8 = new MemoirView("tt3498820","Captain America: Civil War","2016-05-06","2016-05-10","There is no absolute right or wrong position on both sides of the civil war. The United States team is the embodiment of very justice , he believes in truth, goodness and beauty, he believes in the kindness of winter soldiers (in fact, winter soldiers are indeed manipulated by Hydra). Winter soldier is the only person in the world that the United States team has known since before he went to sleep, a spirit that no one can replace.",
                80,78,"2560","https://m.media-amazon.com/images/M/MV5BMjQ0MTgyNjAxMV5BMl5BanBnXkFtZTgwNjUzMDkyODE@._V1_SY1000_CR0,0,674,1000_AL_.jpg");
        memoirView8.setGenre("Action, Adventure, Sci-Fi ");


        MemoirView memoirView9 = new MemoirView("tt6450804","Terminator: Dark Fate","2019-11-01","2020-05-10","We, when we watch it, when we pay a ticket, we encourage these people to keep trowing garbarge at us, we need to stop... Winter soldier is the only person in the world that the United States team has known since before he went to sleep, a spirit that no one can replace.",
                65,67," 2000","https://m.media-amazon.com/images/M/MV5BOWExYzVlZDgtY2E1ZS00NTFjLWFmZWItZjI2NWY5ZWJiNTE4XkEyXkFqcGdeQXVyMTA3MTA4Mzgw._V1_SY1000_CR0,0,666,1000_AL_.jpg");
        memoirView9.setGenre("Action, Adventure, Sci-Fi ");

        MemoirView memoirView10 = new MemoirView("tt6966692","Green Book","2018-11-06","2019-04-10","We loved Green Book along with the sold-out crowd who applauded loudly at the end. Based on true story of piano virtuoso, Don Shirley's road trip through the south during the 60's, the film pays tribute to his genius and courage as a black man who tries hard to soar above the ugliness of the times. The elegant trappings of his home and his suc",
                89,90,"2000","https://m.media-amazon.com/images/M/MV5BYzIzYmJlYTYtNGNiYy00N2EwLTk4ZjItMGYyZTJiOTVkM2RlXkEyXkFqcGdeQXVyODY1NDk1NjE@._V1_SY1000_CR0,0,666,1000_AL_.jpg");
        memoirView10.setGenre("Biography, Comedy, Drama");

        MemoirView memoirView11 = new MemoirView("tt1979376","Toy Story 4","2019-06-20","2020-06-08","good movie",
                99,87,"2000","https://m.media-amazon.com/images/M/MV5BMTYzMDM4NzkxOV5BMl5BanBnXkFtZTgwNzM1Mzg2NzM@._V1_SY1000_CR0,0,674,1000_AL_.jpg");
        memoirView10.setGenre("Animation, Adventure, Comedy");


//        List<MemoirView>  memoir_release = new ArrayList<>();
//        memoir_release.add(memoirView1);
//        memoir_release.add(memoirView9);
//        memoir_release.add(memoirView3);
//        memoir_release.add(memoirView5);
//        memoir_release.add(memoirView2);
//        memoir_release.add(memoirView7);
//        memoir_release.add(memoirView10);
//        memoir_release.add(memoirView6);
//        memoir_release.add(memoirView8);
//        memoir_release.add(memoirView4);
//        memoir_release.add(memoirView0);
//
//        List<MemoirView>  memoir_watch = new ArrayList<>();
//        memoir_watch.add(memoirView9);
//        memoir_watch.add(memoirView1);
//        memoir_watch.add(memoirView3);
//        memoir_watch.add(memoirView5);
//        memoir_watch.add(memoirView2);
//        memoir_watch.add(memoirView10);
//        memoir_watch.add(memoirView7);
//        memoir_watch.add(memoirView6);
//        memoir_watch.add(memoirView8);
//        memoir_watch.add(memoirView4);
//        memoir_watch.add(memoirView0);

//        List<MemoirView>  memoir_userscore = new ArrayList<>();
//        memoir_userscore.add(memoirView1);
//        memoir_userscore.add(memoirView0);
//        memoir_userscore.add(memoirView4);
//        memoir_userscore.add(memoirView5);
//        memoir_userscore.add(memoirView10);
//        memoir_userscore.add(memoirView3);
//        memoir_userscore.add(memoirView6);
//        memoir_userscore.add(memoirView8);
//        memoir_userscore.add(memoirView2);
//        memoir_userscore.add(memoirView7);
//        memoir_userscore.add(memoirView9);
//
//        List<MemoirView>  memoir_publicscore = new ArrayList<>();
//        memoir_publicscore.add(memoirView10);
//        memoir_publicscore.add(memoirView6);
//        memoir_publicscore.add(memoirView1);
//        memoir_publicscore.add(memoirView0);
//        memoir_publicscore.add(memoirView5);
//        memoir_publicscore.add(memoirView7);
//        memoir_publicscore.add(memoirView2);
//        memoir_publicscore.add(memoirView8);
//        memoir_publicscore.add(memoirView3);
//        memoir_publicscore.add(memoirView9);
//        memoir_publicscore.add(memoirView4);

        if(optiton ==RELEASE_DATE_CODE) {
            allmovies.add(memoirView1);
            allmovies.add(memoirView3);
            allmovies.add(memoirView9);
            allmovies.add(memoirView5);
            allmovies.add(memoirView2);
            allmovies.add(memoirView11);
            allmovies.add(memoirView7);
            allmovies.add(memoirView10);
            allmovies.add(memoirView6);
            allmovies.add(memoirView8);
            allmovies.add(memoirView4);
            allmovies.add(memoirView0);
        } else if(optiton == WATChTIME_DATE_CODE){
           allmovies.add(memoirView11);
           allmovies.add(memoirView9);
           allmovies.add(memoirView1);
           allmovies.add(memoirView3);
           allmovies.add(memoirView5);
           allmovies.add(memoirView2);
           allmovies.add(memoirView10);
           allmovies.add(memoirView7);
           allmovies.add(memoirView6);
           allmovies.add(memoirView8);
           allmovies.add(memoirView4);
           allmovies.add(memoirView0);
        } else if(optiton == USERSCORE_CODE){
            allmovies.add(memoirView11);
            allmovies.add(memoirView1);
            allmovies.add(memoirView0);
            allmovies.add(memoirView4);
            allmovies.add(memoirView5);
            allmovies.add(memoirView10);
            allmovies.add(memoirView3);
            allmovies.add(memoirView6);
            allmovies.add(memoirView8);
            allmovies.add(memoirView2);
            allmovies.add(memoirView7);
            allmovies.add(memoirView9);
        } else if(optiton == PUBLICSCORE_CODE){
            allmovies.add(memoirView10);
            allmovies.add(memoirView6);
            allmovies.add(memoirView1);
            allmovies.add(memoirView0);
            allmovies.add(memoirView5);
            allmovies.add(memoirView11);
            allmovies.add(memoirView7);
            allmovies.add(memoirView2);
            allmovies.add(memoirView8);
            allmovies.add(memoirView3);
            allmovies.add(memoirView9);
            allmovies.add(memoirView4);
        } else {
            allmovies.add(memoirView0);
            allmovies.add(memoirView1);
            allmovies.add(memoirView2);
            allmovies.add(memoirView3);
            allmovies.add(memoirView4);
            allmovies.add(memoirView5);
            allmovies.add(memoirView6);
            allmovies.add(memoirView7);
            allmovies.add(memoirView8);
            allmovies.add(memoirView9);
            allmovies.add(memoirView10);
            allmovies.add(memoirView11);
        }
        return allmovies;

    }

}
