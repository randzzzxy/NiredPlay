package com.example.b.niredplay.Activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.b.niredplay.ActivityManager.BaseActivity;
import com.example.b.niredplay.R;
import com.example.b.niredplay.RecyclerView.Song;
import com.example.b.niredplay.Service.SongPlayService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicPlayer extends BaseActivity {
    private Toolbar toolbar;
    private ImageButton shangyishou;
    private ImageButton xiayishou;
    private ImageView background;
    private ImageButton mmmbutton;
    private CircleImageView circleImageView;
    private SongPlayService.MyBinder binder;
    private Song currentSong;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (SongPlayService.MyBinder) service;
            toolbar.setTitle(binder.getInfo().getName());
            toolbar.setSubtitle(binder.getInfo().getSinger());


//背景

            updataBackGround();


//头像


//
//            Glide.with(UserDataActivity.this)
//
//                    .load("http://p1.so.qhmsg.com/t015112ce43bd4a1586.jpg")
//
//                    .into(civ_head);

            try {
                circleImageView.setImageBitmap(loadingCover(binder.getInfo().getSongId()));
            } catch (Exception e) {
                circleImageView.setImageResource(R.drawable.log);
            }
            if (binder.isPlaying()) {
                mmmbutton.setImageDrawable(getResources().getDrawable(R.drawable.zanting2));
            } else {
                mmmbutton.setImageDrawable(getResources().getDrawable(R.drawable.bofang2));
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        toolbar = findViewById(R.id.musicplayertoolbar);
        background = findViewById(R.id.background);
        setSupportActionBar(toolbar);
        shangyishou = findViewById(R.id.shangyishou);
        xiayishou = findViewById(R.id.xiayishou);
        mmmbutton = findViewById(R.id.mmmbutton);
        circleImageView = findViewById(R.id.circleImageView);
        bindService(new Intent(MusicPlayer.this, SongPlayService.class), conn, Service.BIND_AUTO_CREATE);
        mmmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binder.isPlaying()) {
                    binder.pause();
                    mmmbutton.setImageDrawable(getResources().getDrawable(R.drawable.bofang2));
                } else {
                    try {
                        binder.play();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mmmbutton.setImageDrawable(getResources().getDrawable(R.drawable.zanting2));
                }
            }
        });
        shangyishou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binder.lastSong();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentSong = binder.getInfo();
                updataBackGround();
                toolbar.setTitle(currentSong.getName());
                toolbar.setSubtitle(currentSong.getSinger());
                try {
                    circleImageView.setImageBitmap(loadingCover(currentSong.getSongId()));
                } catch (Exception e) {
                    circleImageView.setImageResource(R.drawable.log);
                    Glide.with(MusicPlayer.this)

                            .load(R.drawable.log)

                            .bitmapTransform(new BlurTransformation(MusicPlayer.this, 20, 2))

                            .into(background);
                }
            }
        });
        xiayishou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    binder.nextSong();
                    Log.d("11123", "11");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentSong = binder.getInfo();
                updataBackGround();
                toolbar.setTitle(currentSong.getName());
                toolbar.setSubtitle(currentSong.getSinger());
                try {
                    circleImageView.setImageBitmap(loadingCover(currentSong.getSongId()));
                } catch (Exception e) {
                    circleImageView.setImageResource(R.drawable.log);
                    Glide.with(MusicPlayer.this)

                            .load(R.drawable.log)

                            .bitmapTransform(new BlurTransformation(MusicPlayer.this, 20, 2))

                            .into(background);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override

    public void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

        //Toolbar 必须在onCreate()之后设置标题文本，否则默认标签将覆盖我们的设置

        if (toolbar != null && binder != null) {

            toolbar.setTitle(binder.getInfo().getName());
            toolbar.setSubtitle(binder.getInfo().getSinger());

        }

    }

    //参数为音频文件路径
    private Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        return bitmap;
    }

    public void updataBackGround()
    {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            loadingCover(binder.getInfo().getSongId()).compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes=baos.toByteArray();
            Glide.with(MusicPlayer.this)

                    .load(bytes)

                    .bitmapTransform(new BlurTransformation(MusicPlayer.this, 20, 2))

                    .into(background);
        } catch (Exception e)
        {
            e.printStackTrace();
            Glide.with(MusicPlayer.this)

                    .load(R.drawable.log)

                    .bitmapTransform(new BlurTransformation(MusicPlayer.this, 20, 2))

                    .into(background);
        }
    }
}

