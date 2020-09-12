package com.xiaoxin.netmusic;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayingBackService extends Service {

    public IBinder binder=new MyBinder();




    public void initMediaPlayer()
    {

    }


    public MusicPlayingBackService() {
        initMediaPlayer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
        // TODO: Return the communication channel to the service.
    }

    public class MyBinder extends Binder
    {
        public MusicPlayingBackService getService()
        {
            return MusicPlayingBackService.this;
        }
    }
}
