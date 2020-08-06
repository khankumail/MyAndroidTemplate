package com.app.music.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.app.music.adapter.AdapterListArtist;
import com.app.music.data.Constant;
import com.app.music.data.Tools;
import com.app.music.model.Artist;

import java.util.List;


public class FragmentTabArtist extends Fragment {

    private View root;

    public FragmentTabArtist() {
    }

    public static FragmentTabArtist newInstance() {
        FragmentTabArtist fragment = new FragmentTabArtist();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tab_artist, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        List<Artist> items = Constant.getArtist(getActivity());

        //set data and list adapter
        AdapterListArtist mAdapter = new AdapterListArtist(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListArtist.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Artist obj, int pos) {
                Toast.makeText(getActivity(), obj.title + " Clicked", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), item.getTitle() + " Artist Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}