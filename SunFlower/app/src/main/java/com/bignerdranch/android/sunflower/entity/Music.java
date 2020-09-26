package com.bignerdranch.android.sunflower.entity;

import android.graphics.Bitmap;

public class Music {

//     在这里所有的属性都是用public修饰的，所以在以后调用时直接调用就可以了
//     如果用private修饰是需要构建set和get方法

    //歌名
    public String title;
    //歌唱者
    public String artist;
    //专辑名
    public String album;
    public  int length;
    //专辑图片
    public Bitmap albumBip;
    public String path;
    public boolean isPlaying;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Bitmap getAlbumBip() {
        return albumBip;
    }

    public void setAlbumBip(Bitmap albumBip) {
        this.albumBip = albumBip;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
