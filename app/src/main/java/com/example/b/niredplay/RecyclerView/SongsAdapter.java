package com.example.b.niredplay.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.b.niredplay.R;

import java.io.IOException;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
    private OnRecyclerViewClickListener listener;
    private List<Song> mSongList ;

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public SongsAdapter(List<Song> mSongList) {
        this.mSongList = mSongList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView songName;
        TextView singer;
        ImageView imageView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            singer = (TextView) itemView.findViewById(R.id.song_singer);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_layout,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song = mSongList.get(i);
        viewHolder.songName.setText(song.getName());
        viewHolder.singer.setText(song.getSinger());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("11123","有用");
                if(listener != null) {
                    try {
                        listener.onItemClickListener(v);
                        Log.d("kkl","111");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(listener != null) {
                    try {
                        listener.onItemLongClickListener(v);
                        Log.d("kkl","111");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }




        @Override
    public int getItemCount() {
        return mSongList.size();
    }
    private Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        return bitmap;
    }
}
