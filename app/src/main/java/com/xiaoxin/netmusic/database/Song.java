package com.xiaoxin.netmusic.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SongList")
public class Song {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //歌曲文件路径
    @ColumnInfo
    private String path;

    //歌曲名
    @ColumnInfo
    private String name;

    //歌曲所属专辑
    @ColumnInfo
    private String album;

    //歌曲艺术家
    @ColumnInfo
    private String artist;

    //歌曲大小
    @ColumnInfo
    private long size;

    //歌曲时长
    @ColumnInfo
    private int duration;

    //歌曲所属歌单
    @ColumnInfo
    private String songList;

    //专辑封面id
    @ColumnInfo
    private long albumId;

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getAlbumId() {
        return albumId;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSongList(String songList) {
        this.songList = songList;
    }

    public String getSongList() {
        return songList;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public long getSize() {
        return size;
    }

    public int getDuration() {
        return duration;
    }

    public int getId() {
        return id;
    }
}
