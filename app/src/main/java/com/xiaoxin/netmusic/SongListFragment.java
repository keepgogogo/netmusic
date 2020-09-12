package com.xiaoxin.netmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xiaoxin.netmusic.viewpager.SongListFragmentAdapter;

public class SongListFragment extends Fragment {

    private ViewPager2 viewPager2;
    private SongListFragmentAdapter songListFragmentAdapter;
    private TabLayout tabLayout;
    private final String[] tabLabels= {"我的歌单","歌曲列表"};

    @Override
    public void onViewCreated(@NonNull View view,@NonNull Bundle savedInstanceState)
    {
        songListFragmentAdapter=new SongListFragmentAdapter(this);
        viewPager2=(ViewPager2)view.findViewById(R.id.ViewPagerInSongListFragment);
        viewPager2.setAdapter(songListFragmentAdapter);

        tabLayout=(TabLayout)view.findViewById(R.id.TabLayoutInSongListFragment);
        new TabLayoutMediator(tabLayout,viewPager2,(new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabLabels[position]);
            }
        })).attach();
    }









    public SongListFragment() {
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
        return inflater.inflate(R.layout.fragment_song_list, container, false);
    }

}
