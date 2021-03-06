package com.xiaoxin.netmusic.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class SongDataBase extends RoomDatabase {
    public abstract SongDataBaseDao SongDataBaseDao();
    private static volatile SongDataBase INSTANCE;//volatile保证从内存读写，各线程中获取的database实例相同

    public static SongDataBase getDatabase(final Context context)
    {
        if(INSTANCE == null )
        {
            synchronized (SongDataBase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            SongDataBase.class,
                            "SongDataBase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
