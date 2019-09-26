package com.example.b.niredplay.Activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.b.niredplay.ActivityManager.ActivityCollector;
import com.example.b.niredplay.ActivityManager.BaseActivity;
import com.example.b.niredplay.RecyclerView.OnRecyclerViewClickListener;
import com.example.b.niredplay.R;
import com.example.b.niredplay.RecyclerView.Song;
import com.example.b.niredplay.RecyclerView.SongsAdapter;
import com.example.b.niredplay.SQLite.SongsDatabaseHelper;
import com.example.b.niredplay.Service.SongPlayService;
import com.example.b.niredplay.other.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.internal.http1.Http1Codec;

public class MainActivity extends BaseActivity {
    private ImageView net;
    private User user;
    private DrawerLayout drawerLayout;
    private TextView mail;
    private TextView userName;
    NavigationView navView;
    private SongsDatabaseHelper dbHelper;
    private SongPlayService.MyBinder binder;
    SongsAdapter adapter;
    private int position;
    private ImageView songImage;
    private TextView songName;
    private TextView singer;
    private ImageButton button;
    private ImageView ziliaobackground;
    private int play = 0;
    private List<Song> songList = new ArrayList<>();

    //定义一个ServiceConnection对象
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (SongPlayService.MyBinder)service;
            if(songList!=null&&songList.size()!=0) {
                binder.setDatabase(songList);
                try {
                    binder.setData(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                singer.setText(songList.get(0).getSinger());
                songName.setText(songList.get(0).getName());
                try {
                    songImage.setImageBitmap(loadingCover(songList.get(0).getSongId()));
                } catch (Exception e) {
                    songImage.setImageResource(R.drawable.yinle);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    //recyclerview子项菜单
    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.option,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.removeSong:
                        adapter.notifyDataSetChanged();
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete("Song","name = ?",new String[]{songList.get(pos).getName()});
                        songList.remove(pos);
                        break;
                }
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });
        popupMenu.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCollector.removeActivity(ActivityCollector.activities.get(0));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化bmob
        Bmob.initialize(this, "e53cd95a95fc6de0fcbcff9a705ca0dc");
        //修改标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        navView=(NavigationView) findViewById(R.id.nav_view);
        View headerView = navView.getHeaderView(0);
        ziliaobackground = headerView.findViewById(R.id.ziliaobackground);
        dbHelper = new SongsDatabaseHelper(this, "Songs.db", null, 1);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            try {
                initializeSong();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Glide.with(MainActivity.this)

                .load(R.drawable.pet)

                .bitmapTransform(new BlurTransformation(MainActivity.this, 20, 2))

                .into(ziliaobackground);
        final Intent intent = new Intent(this, SongPlayService.class);
        startService(intent);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);
        mail = headerView.findViewById(R.id.mail);
        userName = headerView.findViewById(R.id.username);
        //设置导航图标
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.daohang);
        }
        //给滑动菜单选项添加点击事件
        NavigationView navigationView = findViewById(R.id.nav_view);
        //navigationView.setCheckedItem(R.id.nav_exit);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_exit:
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                        break;
                }
                return true;
            }
        });
        songImage = findViewById(R.id.songImage);
        songName = findViewById(R.id.songName);
        singer = findViewById(R.id.singer);
        button = findViewById(R.id.imageButton);
        //设置通知渠道
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   String channelId = "NiredPlay";
                   String channelName = "音乐";
                   int importance = NotificationManager.IMPORTANCE_HIGH;
                   createNotificationChannel(channelId, channelName, importance);
              }
        //播放和暂停
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play==0)
                {
                    play=1;

                    try {
                        binder.play();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    button.setImageDrawable(getResources().getDrawable(R.drawable.zanting2));
                }
                else
                {
                    play = 0;
                    binder.pause();
                    button.setImageDrawable(getResources().getDrawable(R.drawable.bofang2));
                }
            }
        });
        final RecyclerView recyclerView = findViewById(R.id.songMenu);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SongsAdapter(songList);
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
        net = findViewById(R.id.wangluo);
        net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(MainActivity.this, Apikk.class);
                startActivity(intent1);
            }
        });
        findViewById(R.id.bendi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initializeSong();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //点击reclerview的子项歌曲进入播放器
        adapter.setItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onItemClickListener(View view) throws IOException {
                position = recyclerView.getChildAdapterPosition(view);
                singer.setText(songList.get(position).getSinger());
                songName.setText(songList.get(position).getName());
                try{
                    songImage.setImageBitmap(loadingCover(songList.get(position).getSongId()));
                }
                catch(Exception e){
                    songImage.setImageResource(R.drawable.yinle);
                }
                button.setImageDrawable(getResources().getDrawable(R.drawable.zanting2));
                if(binder.isData()) {
                    binder.resetMediaPlayer();
                }
                binder.setData(position);

                sendMusic(songList.get(position).getName(),songList.get(position).getSinger());
                    binder.play();
                    play = 1;
            }

            @Override
            public void onItemLongClickListener(View view) {
                int pos = recyclerView.getChildAdapterPosition(view);

                showPopMenu(view,pos);
            }
        });


    }
    //请求获得访问权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    try {
                        initializeSong();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }
    //创建菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    //为菜单栏设置点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addSong:
                Cursor cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                    null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                Song mp3Info = null;
                // 将添加进来的歌曲信息放进app数据库
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //查询系统数据库中的歌曲
                while (cursor.moveToNext()){
                    mp3Info = new Song();
                    long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));  //音乐id
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));  //歌曲名
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家（歌手）
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));  //专辑
                    long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));//专辑id
                    long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));//音乐时长
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));        //文件大小
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));     //文件路径
                    int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));  //是否为音乐
                    //查询数据库中是否有该首音乐，无则添加进数据库
                    if(!SongsDatabaseHelper.haveData(db,"Song","name",title)) {
                        if (isMusic != 0) {  //只把音乐添加到集合中
                            //放进recyclerView中
                            mp3Info.setName(title);
                            mp3Info.setSinger(artist);
                            mp3Info.setSongId(url);
                            songList.add(mp3Info);
                            //放进app数据库中
                            values.put("name", title);
                            values.put("artist", artist);
                            values.put("songUri", url);
                            db.insert("Song", null, values);
                            values.clear();
                        }
                    }
                }
                binder.setDatabase(songList);
                cursor.close();
                adapter.notifyDataSetChanged();
                break;
                //点击标题栏出现滑动菜单
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
    //根据app数据库里的内容初始化歌单
    public void initializeSong() throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询Song表中的所有信息
        Song mp3Info = null;
        songList.removeAll(songList);
        Cursor cursor = db.query("Song",null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {
            do{
                mp3Info = new Song();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String uri= cursor.getString(cursor.getColumnIndex("songUri"));
                String artist = cursor.getString(cursor.getColumnIndex("artist"));
                mp3Info.setName(name);
                mp3Info.setSinger(artist);
                mp3Info.setSongId(uri);
                songList.add(mp3Info);
            }while (cursor.moveToNext());
        }
        if(binder!=null)
        {
            binder.setDatabase(songList);
        }
        cursor.close();
        try{
        adapter.notifyDataSetChanged();}
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //创建通知渠道的方法
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
    //创建音乐播放器通知
    public void sendMusic(String title,String artist) {
        Intent intent = new Intent(this,MainActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = manager.getNotificationChannel("NiredPlay");
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                startActivity(intent);
                Toast.makeText(this, "请手动将通知打开", Toast.LENGTH_SHORT).show();
            }
        }
        Notification notification = new NotificationCompat.Builder(this, "NiredPlay")
                .setContentTitle(title)
                .setContentText(artist)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("11123","des");
        unbindService(conn);
        Intent intent = new Intent(this, SongPlayService.class);
        stopService(intent);
    }
    //参数为音频文件路径
    private Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap= BitmapFactory.decodeByteArray(picture,0,picture.length);
        return bitmap;
    }
    //登陆后
    @Override
    protected void onResume() {
        super.onResume();
        //判断音乐是否在播放
        if(binder!=null) {

            if(binder.isData())
            {
                songName.setText( binder.getInfo().getName());
                singer.setText(binder.getInfo().getSinger());
                try{
                    songImage.setImageBitmap(loadingCover(binder.getInfo().getSongId()));
                    if(binder.getInfo().getImageId()!=null)
                    {
                        Glide.with(MainActivity.this)

                                .load(new URL(binder.getInfo().getImageId()))
                                .placeholder(R.drawable.yinle)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)

                                .into(songImage);
                    }
                }
                catch(Exception e){
                    songImage.setImageResource(R.drawable.yinle);
                }
            }
            if (binder.isPlaying()) {
                button.setImageDrawable(getResources().getDrawable(R.drawable.zanting2));
            } else {
                button.setImageDrawable(getResources().getDrawable(R.drawable.bofang2));
            }

        }
        if(user!=null)
        {
            Log.d("11123",user.getUsername());
            mail.setText(user.getEmail());
            userName.setText(user.getUsername());
        }
        else
        {
            //Toast.makeText(MainActivity.this,"还未登陆，请登录",Toast.LENGTH_SHORT).show();
        }
        //修改nav的menu中子项
        Menu menu = navView.getMenu();
        if(user == null)
        {
            menu.findItem(R.id.nav_exit).setTitle("请登录");
            mail.setText("");
            userName.setText("");
        }
        else
        {
            menu.findItem(R.id.nav_exit).setTitle("退出登陆");
        }
   }
    public void test_click(View v){
        if(songList!=null&&songList.size()!=0)
        {
            Intent intent = new Intent(MainActivity.this, MusicPlayer.class);
            intent.putExtra("name", songList.get(position).getName());
            intent.putExtra("artist", songList.get(position).getSinger());
            intent.putExtra("songid", songList.get(position).getSongId());
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        user = (User) intent.getSerializableExtra("user");
    }
}
