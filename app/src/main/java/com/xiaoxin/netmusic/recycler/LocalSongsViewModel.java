package com.xiaoxin.netmusic.recycler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xiaoxin.netmusic.Song;

import java.util.List;

public class LocalSongsViewModel extends ViewModel {

    private MutableLiveData<List<Song>> currentData;

    public MutableLiveData<List<Song>> getCurrentData() {

        if(currentData==null)
        {
            currentData=new MutableLiveData<>();
        }

        return currentData;
    }
}
