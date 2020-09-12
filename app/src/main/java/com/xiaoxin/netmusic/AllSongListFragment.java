package com.xiaoxin.netmusic;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.xiaoxin.netmusic.database.Song;
import com.xiaoxin.netmusic.database.SongDataBase;
import com.xiaoxin.netmusic.database.SongDataBaseDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AllSongListFragment extends Fragment {

    private ExpandableListView expandableListView;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MainActivity mainActivity;

    private SongDataBase database;
    private SongDataBaseDao dataBaseDao;
    private List<List<Song>> allSongsDividedByList;
    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        mainActivity=(MainActivity)getActivity();

        expandableListView=(ExpandableListView)view.findViewById(R.id.ExpandableListViewInAllSongListFragment);

        editor=mainActivity.getEditor();
        sharedPreferences=mainActivity.getSharedPreferences();

        allSongsDividedByList=new ArrayList<>();
        database=SongDataBase.getDatabase(getActivity());
        dataBaseDao=database.SongDataBaseDao();

        //读取所有歌曲信息
        loadAllSongs();


    }

    @SuppressLint("CheckResult")
    public void loadAllSongs()
    {
        Observable.create(new ObservableOnSubscribe<List<List<Song>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<List<Song>>> emitter)throws Exception
            {
                List<List<Song>> listsOfSongList=new ArrayList<>();
                int i=sharedPreferences.getInt("numberOfSongList",-1);
                for(int x=0;x<i&&i!=-1;x++)
                {
                    listsOfSongList.add(dataBaseDao
                            .getBySongList(sharedPreferences.getString("songlist"+x,"")));
                }
                emitter.onNext(listsOfSongList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<List<Song>>>() {
                    @Override
                    public void accept(List<List<Song>> lists) throws Exception {
                        allSongsDividedByList=lists;
                    }
                })
    }









    public AllSongListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_song_list, container, false);
    }
}
