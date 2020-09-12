package com.xiaoxin.netmusic.expandablelist;

import com.xiaoxin.netmusic.database.Song;

import java.util.List;

public class SongListData {
    private String nameOfSongList;
    private List<Song> songOfThisSongList;

    public void setSongOfThisSongList(List<Song> songOfThisSongList) {
        this.songOfThisSongList = songOfThisSongList;
    }

    public void setNameOfSongList(String nameOfSongList) {
        this.nameOfSongList = nameOfSongList;
    }

    public List<Song> getSongOfThisSongList() {
        return songOfThisSongList;
    }

    public String getNameOfSongList() {
        return nameOfSongList;
    }
}
