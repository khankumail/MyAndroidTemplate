package com.app.sample.mailbox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.sample.mailbox.ActivityComposeMail;
import com.app.sample.mailbox.ActivityMailDetails;
import com.app.sample.mailbox.ActivityMain;
import com.app.sample.mailbox.R;
import com.app.sample.mailbox.adapter.MailListAdapter;
import com.app.sample.mailbox.data.GlobalVariable;
import com.app.sample.mailbox.model.Mail;
import com.app.sample.mailbox.widget.DividerItemDecoration;

public class TrashFragment extends Fragment {

    private RecyclerView recyclerView;
    private MailListAdapter mAdapter;
    private ProgressBar progressBar;
    private GlobalVariable global;
    private View view;
    private SearchView searchView;
    private LinearLayout lyt_not_found;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trash, null);

        global = (GlobalVariable) getActivity().getApplication();

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        lyt_not_found = (LinearLayout) view.findViewById(R.id.lyt_not_found);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        mAdapter = new MailListAdapter(MailListAdapter.TRASH_MODE, getActivity(), global.getTrash());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MailListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Mail obj, int position) {
                ActivityMailDetails.navigate((ActivityMain) getActivity(), view.findViewById(R.id.lyt_parent), obj, MailListAdapter.TRASH_MODE, position);
            }
        });
        if (mAdapter.getItemCount() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        mAdapter.setItems(global.getTrash());
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_inbox, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint("Search mail...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                try {
                    mAdapter.getFilter().filter(s);
                } catch (Exception e) {
                }
                return true;
            }
        });
        // Detect SearchView icon clicks
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemsVisibility(menu, searchItem, false);
            }
        });

        // Detect SearchView close
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, searchItem, true);
                return false;
            }
        });
        searchView.onActionViewCollapsed();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i = 0; i < menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_mail) {
            Intent i = new Intent(getActivity().getApplicationContext(), ActivityComposeMail.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
