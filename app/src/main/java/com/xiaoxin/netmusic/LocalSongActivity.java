package com.xiaoxin.netmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.xiaoxin.netmusic.database.Song;
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
    private KeyListener keyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_song);

        setTitle("本地音乐");

        buttonForConfirmEditText=(Button)findViewById(R.id.ButtonForSearchInLocalSongActivity);
        buttonForConfirmEditText.setOnClickListener(this);
        editText=(EditText)findViewById(R.id.EditTextInLocalSongActivity);
        progressBar=(ProgressBar)findViewById(R.id.ProgressBarInLocalSongs);
        progressBar.setVisibility(View.VISIBLE);
        keyListener=editText.getKeyListener();
        editText.setKeyListener(null);
        editText.setHint("请等待歌曲检索完成");


        recyclerView=(RecyclerView)findViewById(R.id.RecyclerViewLocalSongs);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        viewModel= new ViewModelProvider(this).get(LocalSongsViewModel.class);
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
                        Song temp=adapter.getMData().get(position);

                        //TODO
                        break;
                    default:
                        break;
                }
            }
        });
        //获取存储权限
        //getStorageAccess();
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
                    temp.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                    temp.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    temp.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    temp.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    temp.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    temp.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
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
                        editText.setKeyListener(keyListener);
                        editText.setHint("请输入歌曲名称");
//                        progressBar.setMaxWidth(0);
                    }
                }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "accept: lost");
            }
        }).subscribe();
    }

    /**
     * 该方法中启动对editText中内容变化的监听
     */
    public void editTextListenStart()
    {
        editText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("CheckResult")
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count)
            {
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
                                if((song.getName()!=null)&&song.getName().contains(m))
                                {
                                    temp.add(song);
                                }
                            }
                            if (temp.size()==0)
                            {
                                emitter.onNext(new ArrayList<Song>());
                            }
                            else
                            {
                                emitter.onNext(temp);
                            }
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
                            }).subscribe();
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
                    assert imManager != null;
                    imManager.hideSoftInputFromWindow(viewFocus.getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }

//    public void getStorageAccess()
//    {
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.
//                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//
//        }
//        if(ContextCompat.checkSelfPermission(this, android.Manifest.
//                permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
//        {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
//        }
//    }


}