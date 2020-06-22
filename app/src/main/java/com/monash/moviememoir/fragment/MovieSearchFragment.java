package com.monash.moviememoir.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.monash.moviememoir.R;
import com.monash.moviememoir.activity.HomeActivity;
import com.monash.moviememoir.activity.MovieDetail;
import com.monash.moviememoir.adapter.MovieSearchAdapter;
import com.monash.moviememoir.entity.MovieInfo;
import com.monash.moviememoir.networkconnect.IMDbConnect;

import java.util.List;


public class MovieSearchFragment extends Fragment {
    private EditText ms_search;
    private ListView ms_reslut;
    private Button ms_submit;
    private View view;
    private Context context;
    private Intent intent;
    private int userid;
    private IMDbConnect imDbConnect;
    public MovieSearchFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.movie_search, container, false);

        intent = getActivity().getIntent();
        userid = intent.getIntExtra("userid", 4);
        System.out.println("movie search intent " + intent);
        System.out.println("movie serch userid =" + userid);
        initView();
        initData();
        initAction();

        return view;
    }
    private void initView(){

        ms_search = view.findViewById(R.id.ms_search);
        ms_reslut = view.findViewById(R.id.ms_reslut);
        ms_submit = view.findViewById(R.id.ms_submit);
    }
    public void initData(){
        imDbConnect = new IMDbConnect();
        context = this.getContext();

    }

    public void initAction(){
        ms_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMovies getMovies = new GetMovies();
                getMovies.execute(ms_search.getText().toString());
            }
        });

    }

    private class GetMovies extends AsyncTask<String, Void, List<MovieInfo>> {
        @Override
        protected  List<MovieInfo> doInBackground(String... strings) {
            return imDbConnect.searchMovie(strings[0]);
        }

        @Override
        protected void onPostExecute(final List<MovieInfo> result) {

            if(result == null){
                inforProvide(" connect imdb.com failed");
            } else if(ms_search.getText() == null || ms_search.getText().toString().length() == 0) {
                inforProvide(" Please input movie name");
            }else{
                ms_reslut.setAdapter(new MovieSearchAdapter(context, result,result.size()));
                ms_reslut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (parent.getId()){
                            case R.id.ms_reslut:
                                Intent intent = new Intent(getActivity(), MovieDetail.class);
                                intent.putExtra("id", result.get(position).getId());
                                intent.putExtra("url",result.get(position).getImageURL());
                                intent.putExtra("userid",userid);
                                intent.putExtra("light",1);
                                startActivity(intent);
                                break;
                        }
                    }
                });

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
        ImageView imageView= new ImageView(this.getContext());
        imageView.setImageResource(R.drawable.warn);
        LinearLayout toastView = (LinearLayout) toast.getView();
        toastView.setOrientation(LinearLayout.HORIZONTAL);
        toastView.addView(imageView, 0);
        toast.show();
    }


}
