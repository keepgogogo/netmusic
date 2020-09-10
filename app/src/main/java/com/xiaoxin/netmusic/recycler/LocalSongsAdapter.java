package com.xiaoxin.netmusic.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.xiaoxin.netmusic.R;
import com.xiaoxin.netmusic.Song;

import java.util.List;

public class LocalSongsAdapter
        extends RecyclerView.Adapter<LocalSongsAdapter.LocalSongsViewHolder>
        implements View.OnClickListener{

    private List<Song> mData;
    private LocalSongsViewHolder viewHolder;

    public void setMData(List<Song> mData){this.mData=mData;}

    public List<Song> getMData(){return mData;}

    @Override
    public int getItemCount(){return mData.size();}

    @Override
    public LocalSongsViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
    {
        View itemView=(View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_song_recycler_widget,parent,false);
        return new LocalSongsViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(LocalSongsViewHolder holder,int position)
    {
        String nameOfSong=mData.get(position).getName();
        String singerOfSong=mData.get(position).getArtist();
        holder.TextViewForNameOfSong.setText(nameOfSong==null?"":nameOfSong);
        holder.TextViewForNameOfSinger.setText(singerOfSong==null?"":singerOfSong);
        holder.CardView.setTag(position);
    }

    public class LocalSongsViewHolder extends RecyclerView.ViewHolder {
        public TextView TextViewForNameOfSong;
        public TextView TextViewForNameOfSinger;
        public CardView CardView;
        public LocalSongsViewHolder(@NonNull View itemView) {
            super(itemView);
            CardView=(CardView)itemView.findViewById(R.id.CardViewForRecyclerInLocalSongs);
            CardView.setOnClickListener(LocalSongsAdapter.this);
            TextViewForNameOfSinger=(TextView)itemView.findViewById(R.id.TextViewSingerNameInLocalSongRecyclerWidget);
            TextViewForNameOfSong=(TextView)itemView.findViewById(R.id.TextViewSongNameInLocalSongRecyclerWidget);
            viewHolder=LocalSongsViewHolder.this;
        }
    }

    /**
     * 点击事件监听
     */

    private LocalSongsRecyclerClickListener clickListener;

    public void setClickListener(LocalSongsRecyclerClickListener clickListener){this.clickListener=clickListener;}

    public interface LocalSongsRecyclerClickListener
    {
        void onClick(View view,ViewNameLocalSong viewName,int position);
    }

    @Override
    public void onClick(View view)
    {
        int position=(int)view.getTag();
        if(clickListener!=null)
        {
            switch (view.getId())
            {
                case R.id.CardViewForRecyclerInLocalSongs:
                    clickListener.onClick(view,ViewNameLocalSong.CHOOSE,position);
                    break;
                default:
                    break;
            }
        }
    }

    public enum ViewNameLocalSong
    {
        CHOOSE
    }

}
