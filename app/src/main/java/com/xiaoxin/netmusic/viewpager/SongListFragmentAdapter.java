package com.xiaoxin.netmusic.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.xiaoxin.netmusic.AllSongListFragment;
import com.xiaoxin.netmusic.SongOfSongListFragment;

public class SongListFragmentAdapter extends FragmentStateAdapter {

    private AllSongListFragment allSongListFragment;
    private SongOfSongListFragment songOfSongListFragment;

    public SongListFragmentAdapter(Fragment fragment){
        super(fragment);
        allSongListFragment=new AllSongListFragment();
        songOfSongListFragment=new SongOfSongListFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        Fragment fragment=null;
        switch (position)
        {
            case 0:
                fragment=allSongListFragment;
                break;
            case 1:
                fragment=songOfSongListFragment;
                break;
            default:
                break;
        }
        assert fragment != null;
        return fragment;
    }

    @Override
    public int getItemCount(){return 2;}
}
