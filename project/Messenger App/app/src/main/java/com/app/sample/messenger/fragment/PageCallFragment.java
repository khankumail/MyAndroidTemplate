package com.app.sample.messenger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sample.messenger.R;
import com.app.sample.messenger.adapter.CallListAdapter;
import com.app.sample.messenger.data.Constant;
import com.app.sample.messenger.model.Friend;

import java.util.ArrayList;
import java.util.List;

public class PageCallFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private List<Friend> items = new ArrayList<>();
    private CallListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_call, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        items = Constant.getFriendsData(getActivity());

        // specify an adapter (see also next example)
        mAdapter = new CallListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

}
