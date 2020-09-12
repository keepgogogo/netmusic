package com.xiaoxin.netmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int SONG_LIST_FRAGMENT=0;
    public static final int UNDER_PLAYING_FRAGMENT=1;
    public static final int LYRIC_FRAGMENT=2;

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentsContainer;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager=getSupportFragmentManager();
        sharedPreferences=getSharedPreferences("mainactivity",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.apply();

        fragmentsContainer=new ArrayList<>();
        fragmentsContainer.add(new SongListFragment());
        fragmentsContainer.add(new UnderPlayingFragment());
        fragmentsContainer.add(new LyricsFragment());
        initNavigation();
        setFragment(fragmentsContainer.get(SONG_LIST_FRAGMENT));
    }

    //对导航栏进行初始化，设置点击事件
    public void initNavigation() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.SongList:
                        setFragment(fragmentsContainer.get(SONG_LIST_FRAGMENT));
                        break;
                    case R.id.underPlaying:
                        setFragment(fragmentsContainer.get(UNDER_PLAYING_FRAGMENT));
                        break;
                    case R.id.lyrics:
                        setFragment(fragmentsContainer.get(LYRIC_FRAGMENT));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_in_main_activity,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public Fragment getFragmentInContainer(int index){return fragmentsContainer.get(index);}

    //set the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.AddNewSongList:
                Intent intent=new Intent(MainActivity.this,PlanAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    public SharedPreferences getSharedPreferences(){return sharedPreferences;}

    public SharedPreferences.Editor getEditor(){return editor;}

}