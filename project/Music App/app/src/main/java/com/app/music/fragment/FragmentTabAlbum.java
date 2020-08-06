package com.app.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.music.ActivityAlbumDetail;
import com.app.music.ActivityMain;
import com.app.music.R;
import com.app.music.adapter.AdapterGridAlbum;
import com.app.music.data.Constant;
import com.app.music.data.Tools;
import com.app.music.model.MusicAlbum;
import com.app.music.widget.SpacingItemDecoration;

import java.util.List;


public class FragmentTabAlbum extends Fragment {

    private View root;

    private AdapterGridAlbum mAdapter;

    public FragmentTabAlbum() {
    }

    public static FragmentTabAlbum newInstance() {
        FragmentTabAlbum fragment = new FragmentTabAlbum();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_tab_album, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 4), true));
        recyclerView.setHasFixedSize(true);

        List<MusicAlbum> items = Constant.getMusicAlbum(getActivity());
        //set data and list adapter
        mAdapter = new AdapterGridAlbum(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterGridAlbum.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MusicAlbum obj, int position) {
                ActivityAlbumDetail.navigate(((ActivityMain) getActivity()), view, obj);
            }
        });


        return root;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
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
                Toast.makeText(getActivity(), item.getTitle() + " Album Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}