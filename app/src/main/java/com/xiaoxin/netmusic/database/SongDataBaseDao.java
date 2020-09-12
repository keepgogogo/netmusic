package com.xiaoxin.netmusic.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface SongDataBaseDao {

    @Insert
    void insert(Song... songs);

    @Query("SELECT * FROM SongList")
    List<Song> getAll();

    @Query("SELECT * FROM SongList WHERE songList =:songList")
    List<Song> getBySongList(String songList);

    @Query("SELECT * FROM SongList WHERE artist =:artist")
    List<Song> getByArtist(String artist);

    @Query("SELECT * FROM SongList WHERE name =:nameOfSong")
    List<Song> getByName(String nameOfSong);

    @Delete
    void delete(Song ... songs);

    @Update
    void update(Song ... songs);
}