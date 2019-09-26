package com.example.b.niredplay.Service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.b.niredplay.RecyclerView.Song;

import java.io.IOException;
import java.util.List;

public class SongPlayService extends Service {
    private MediaPlayer mediaPlayer ;
    private List<Song> songsList;
    private boolean isData;
    private int Position;
    private MyBinder binder = new MyBinder();
    public SongPlayService() {
    }
    //自定义自己的binder
    public class MyBinder extends Binder {
        //播放音乐
        public void play() throws IOException {
            if(isData) {
                mediaPlayer.start();
            }
        }

        //检测是否设置数据
        public boolean isData() {
            return isData;
        }

        //检测是否在播放
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        //暂停音乐
        public void pause() {
            if (isData) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
        //获取资源
        public List<Song> getDatas()
        {
            return songsList;
        }
        //设置资源
        public void setData(int position) throws IOException {
            Position = position;
            try {
                mediaPlayer.setDataSource(songsList.get(position).getSongId());
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            mediaPlayer.prepare();
            isData = true;
        }
        public void setDatabase(List<Song> list)
        {
            songsList = list;
        }
        //下一首
        public void nextSong() throws IOException {
            if (Position + 1 <songsList.size()) {
                mediaPlayer.reset();
                isData = true;
                mediaPlayer.setDataSource(songsList.get(Position + 1).getSongId());
                mediaPlayer.prepare();
                Log.d("11123",songsList.get(Position).getName());
                Log.d("11123",songsList.get(Position + 1).getName());
                mediaPlayer.start();
                Position = Position+1;
            }
        }
        //上一首
        public void lastSong() throws IOException {
            if(Position-1>=0) {
                Position = Position - 1;
            }
            mediaPlayer.reset();
            isData = true;
            mediaPlayer.setDataSource(songsList.get(Position).getSongId());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        //重置mediaPlayer
        public void resetMediaPlayer()
        {
            mediaPlayer.reset();
        }
        //获取内容
        public Song getInfo()
        {
            return songsList.get(Position);
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        isData = false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    //参数为音频文件路径
    private Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap= BitmapFactory.decodeByteArray(picture,0,picture.length);
        return bitmap;
    }
}
