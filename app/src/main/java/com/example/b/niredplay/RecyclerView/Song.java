package com.example.b.niredplay.RecyclerView;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;
    private String singer;
    private String songId;
    private String imageId;

    public Song() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public Song(String name, String singer, String songId) {
        this.name = name;
        this.singer = singer;
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public String getSongId() {
        return songId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
