package com.app.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.music.R;
import com.app.music.adapter.AdapterListSong;
import com.app.music.data.Constant;
import com.app.music.data.GlobalVariable;
import com.app.music.data.PlayerState;
import com.app.music.model.MusicSong;

import java.util.List;


public class FragmentTabSong extends Fragment {

    private View root;
    private GlobalVariable global;

    public FragmentTabSong() {
    }

    public static FragmentTabSong newInstance() {
        FragmentTabSong fragment = new FragmentTabSong();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tab_song, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        global = GlobalVariable.getInstance();

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        List<MusicSong> items = Constant.getMusicSong(getActivity());

        //set data and list adapter
        AdapterListSong mAdapter = new AdapterListSong(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        global.setMusicSong(mAdapter.getItem(0));

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListSong.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MusicSong obj, int position) {
                global.setMusicSong(obj);
                global.setPlayerState(PlayerState.RESTART);
            }
        });

        mAdapter.setOnMoreButtonClickListener(new AdapterListSong.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View view, MusicSong obj, MenuItem item) {
                Toast.makeText(getActivity(), obj.title + " (" + item.getTitle() + ") clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Toast.makeText(getActivity(), item.getTitle() + " Song Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}