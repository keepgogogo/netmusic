package com.xiaoxin.netmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.xiaoxin.netmusic.recycler.LocalSongsAdapter;
import com.xiaoxin.netmusic.recycler.LocalSongsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LocalSongActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="LocalSongActivity";

    private EditText editText;
    private ProgressBar progressBar;
    private Button buttonForConfirmEditText;

    private FileLoader fileLoader;
    private RecyclerView recyclerView;
    private LocalSongsAdapter adapter;
    private LocalSongsViewModel viewModel;
    private RecyclerView.LayoutManager layoutManager;
    private List<Song> songsOfLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_song);

        buttonForConfirmEditText=(Button)findViewById(R.id.ButtonForSearchInLocalSongActivity);
        buttonForConfirmEditText.setOnClickListener(this);
        editText=(EditText)findViewById(R.id.EditTextInLocalSongActivity);
        progressBar=(ProgressBar)findViewById(R.id.ProgressBarInLocalSongs);
        progressBar.setVisibility(View.VISIBLE);


        recyclerView=(RecyclerView)findViewById(R.id.RecyclerViewLocalSongs);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        viewModel= ViewModelProviders.of(this).get(LocalSongsViewModel.class);
        adapter=new LocalSongsAdapter();

        final Observer<List<Song>> ListOfSongsObserver= new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                adapter.setMData(songs);
                recyclerView.setAdapter(adapter);
            }
        };

        viewModel.getCurrentData().observe(this,ListOfSongsObserver);

        adapter.setClickListener(new LocalSongsAdapter.LocalSongsRecyclerClickListener() {
            @Override
            public void onClick(View view, LocalSongsAdapter.ViewNameLocalSong viewName, int position) {
                switch (viewName)
                {
                    case CHOOSE:
                        //TODO
                        break;
                    default:
                        break;
                }
            }
        });

        //该方法调用rxjava读取手机中目前已存在的歌曲信息
        loadSongs();
        //该方法中启动对editText中内容变化的监听
        editTextListenStart();



    }

    /**
     * 该方法使用rxjava2读取手机中目前已存在的歌曲信息，并更新adapter中的数据以更新列表
     */
    @SuppressLint("CheckResult")
    public void loadSongs()
    {
        fileLoader=new FileLoader();
        fileLoader.setContext(this)
                .setRequestUri(FileLoader.REQUEST_AUDIO_EXTERNAL_CONTENT_URI)
                .setSortOrder(FileLoader.AUDIO_DEFAULT_SORT_ORDER)
                .getStorageAccess(LocalSongActivity.this);

        Observable.create(new ObservableOnSubscribe<List<Song>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Song>> emitter)throws Exception
            {
                List<Song> song=new ArrayList<>();
                fileLoader.startQuery();
                Cursor cursor=fileLoader.getCursor();
                while(cursor.moveToNext())
                {
                    Song temp=new Song();
                    temp.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                    if(!(new File(temp.getPath()).exists()))
                    {
                        continue;
                    }
                    temp.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    temp.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    temp.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    temp.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    song.add(temp);
                }
                emitter.onNext(song);
            }
        }).map(new Function<List<Song>, List<Song>>()
        {
            @Override
            public List<Song> apply(@NonNull List<Song> songs) throws Exception
            {
                Log.d(TAG, "apply: work");
                return songs;
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<Song>>() {
                    @Override
                    public void accept(List<Song> songs) throws Exception {
                        viewModel.getCurrentData().setValue(songs);
                        songsOfLocal=songs;
                        progressBar.setVisibility(View.GONE);
                        progressBar.setMaxWidth(0);
                    }
                });
    }

    /**
     * 该方法中启动对editText中内容变化的监听
     */
    public void editTextListenStart()
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String m=s.toString();

            }

            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                final String m=s.toString();
                if(m.equals(""))
                {
                    viewModel.getCurrentData().setValue(songsOfLocal);
                }
                else
                {
                    Observable.create(new ObservableOnSubscribe<List<Song>>() {
                        @Override
                        public void subscribe(ObservableEmitter<List<Song>> emitter) throws Exception {
                            List<Song> temp=new ArrayList<>();
                            for(Song song : songsOfLocal)
                            {
                                if(song.getName().contains(m))
                                {
                                    temp.add(song);
                                }
                            }
                            emitter.onNext(temp);
                        }
                    }).map(new Function<List<Song>, List<Song>>() {
                        @Override
                        public List<Song> apply(@NonNull List<Song> songs)
                        {
                            return songs;
                        }

                    }).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(new Consumer<List<Song>>() {
                                @Override
                                public void accept(List<Song> songs) throws Exception {
                                    viewModel.getCurrentData().setValue(songs);
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * button点击事件
     */
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ButtonForSearchInLocalSongActivity:
                View viewFocus = this.getCurrentFocus();
                if (viewFocus != null) {
                    InputMethodManager imManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imManager.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }


}