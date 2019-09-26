package com.example.b.niredplay.Activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;


import com.alibaba.fastjson.JSON;
import com.example.b.niredplay.Json.CONFIG;
import com.example.b.niredplay.Json.JsonBean;
import com.example.b.niredplay.Json.QqmusicBean;
import com.example.b.niredplay.Json.TokenBean;
import com.example.b.niredplay.Json.WeiBean;
import com.example.b.niredplay.R;
import com.example.b.niredplay.RecyclerView.OnRecyclerViewClickListener;
import com.example.b.niredplay.RecyclerView.Song;
import com.example.b.niredplay.RecyclerView.SongsAdapter;
import com.example.b.niredplay.Service.SongPlayService;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Apikk extends AppCompatActivity {
    Toolbar toolbar;
    WeiBean weiBean;
    String seach;
    private EditText searchEdit;
    SongsAdapter adapter;
    private List<Song> songList = new ArrayList<>();
    RecyclerView songlist;
    private SongPlayService.MyBinder binder;
    //定义一个ServiceConnection对象
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (SongPlayService.MyBinder) service;
            if (songList != null && songList.size() != 0) {
                binder.setDatabase(songList);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchEdit = findViewById(R.id.searchEdit);
        songlist = findViewById(R.id.songlist);
            final Intent intent = new Intent(Apikk.this, SongPlayService.class);
            startService(intent);
            bindService(intent, conn, Service.BIND_AUTO_CREATE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Apikk.this);
        songlist.setLayoutManager(layoutManager);
        adapter = new SongsAdapter(songList);
        songlist.setAdapter(adapter);
        findViewById(R.id.searchimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatasong("https://api.mlwei.com/music/api/wy/?key=523077333&type=so&cache=0&nu=20&id="+searchEdit.getText().toString());

            }
        });
        adapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onItemClickListener(View view) throws IOException {
                int position = songlist.getChildAdapterPosition(view);
                if(binder.isData()) {
                    binder.resetMediaPlayer();
                }
                binder.setData(position);
                binder.play();
            }

            @Override
            public void onItemLongClickListener(View view) {

            }
        });
        //initDatasong(CONFIG.API.URL+"只想要你知道");

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    //现在用的
        private void initDatasong(String url){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final  Call call = client.newCall(request);
        //同步调用会阻塞主线程,这边在子线程进行
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //同步调用,返回Response,会抛出IO异常
                    Response response = call.execute();

                    // Log.d("asd","as"+response.body().string());
                    Message message = new Message();
                    Log.v("asd",response.toString());
                    String s =  response.body().string();

//                    Gson gson = new Gson();
//                    JsonBean jsonBean = gson.fromJson(s,JsonBean.class);
//                    Log.v("asd",jsonBean.getResult().getToday().getCity());
//                    Log.v("asd",jsonBean.getResult().getSk().getTemp());
//                    String ss = jsonBean.getResult().getSk().getTemp();
                    message.obj = s;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.v("asd","asd");
                }
            }
        }).start();

    }
        Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Gson gson = new Gson();
                    Log.d("asd",msg.obj.toString());
                    songList.removeAll(songList);
                    weiBean = gson.fromJson(msg.obj.toString(), WeiBean.class);
                    for(int k = 0;k < weiBean.getBody().size();k++)
                    {
                        Song mp3Info = new Song();
                        String name = weiBean.getBody().get(k).getTitle();
                        String artist= weiBean.getBody().get(k).getAuthor();
                        String uri = weiBean.getBody().get(k).getUrl();
                        String imageId = weiBean.getBody().get(k).getPic();
                        mp3Info.setName(name);
                        mp3Info.setSinger(artist);
                        mp3Info.setSongId(uri);
                        mp3Info.setImageId(imageId);
                        songList.add(mp3Info);
                        Log.d("123q",name);
                    }
                    adapter.notifyDataSetChanged();
                    binder.setDatabase(songList);
                    break;
                default:
                    break;
            }
        }

    };

//    private void initDatasong(String url){
//        OkHttpClient client = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .build();
//        final  Call call = client.newCall(request);
//        //同步调用会阻塞主线程,这边在子线程进行
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //同步调用,返回Response,会抛出IO异常
//                    Response response = call.execute();
//
//                    // Log.d("asd","as"+response.body().string());
//                    Message message = new Message();
//                    Log.v("asd",response.toString());
//                    String s =  response.body().string();
//
////                    Gson gson = new Gson();
////                    JsonBean jsonBean = gson.fromJson(s,JsonBean.class);
////                    Log.v("asd",jsonBean.getResult().getToday().getCity());
////                    Log.v("asd",jsonBean.getResult().getSk().getTemp());
////                    String ss = jsonBean.getResult().getSk().getTemp();
//                    message.obj = s;
//                    handler.sendMessage(message);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.v("asd","asd");
//                }
//            }
//        }).start();
//
//    }
//    private void initDatatoken(String url){
//        OkHttpClient client = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .get()
//                .build();
//        final  Call call = client.newCall(request);
//        //同步调用会阻塞主线程,这边在子线程进行
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    //同步调用,返回Response,会抛出IO异常
//                    Response response = call.execute();
//
//                    // Log.d("asd","as"+response.body().string());
//                    Message message = new Message();
//                    String s =  response.body().string();
//
//
////                    Gson gson = new Gson();
////                    JsonBean jsonBean = gson.fromJson(s,JsonBean.class);
////                    Log.v("asd",jsonBean.getResult().getToday().getCity());
////                    Log.v("asd",jsonBean.getResult().getSk().getTemp());
////                    String ss = jsonBean.getResult().getSk().getTemp();
//                    message.obj = s;
//                    tokenhandler.sendMessage(message);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    Gson gson = new Gson();
//
//                    Log.d("asd",msg.obj.toString());
//                    bean = gson.fromJson(dealString(msg.obj.toString()), QqmusicBean.class);
//                    Log.d("123q",bean.getData().getSong().getList().get(0).getSongname());
//
//                    initDatatoken(CONFIG.getToken(bean.getData().getSong().getList().get(0).getSongmid()));
//                //    Log.d("123q",bean.getData().)
//                    Log.d("123q",CONFIG.getToken(bean.getData().getSong().getList().get(0).getSongmid()));
//                    //Log.d("123q",CONFIG.getSongURL(bean.getData().getSong().getList().get(0).getSongmid(),tokenBean.getData().getItems().get(0).getVkey()));
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    };
//    Handler tokenhandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    Gson gson = new Gson();
//                    Log.d("asd",msg.obj.toString());
//                    tokenBean = gson.fromJson(msg.obj.toString(), TokenBean.class);
//                    Log.d("123q",tokenBean.getData().getItems().get(0).getVkey());
//                    Log.d("123q",CONFIG.getSongURL(tokenBean.getData().getItems().get(0).getFilename(),tokenBean.getData().getItems().get(0).getVkey()));
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    };
//    private String dealString(String s)
//    {
//        return s.substring(9,s.length()-1);
//    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("网络歌曲");
    }
}