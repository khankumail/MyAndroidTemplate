package com.app.sample.mailbox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.sample.mailbox.ActivityComposeMail;
import com.app.sample.mailbox.ActivityMailDetails;
import com.app.sample.mailbox.R;
import com.app.sample.mailbox.adapter.PeoplesListAdapter;
import com.app.sample.mailbox.data.Constant;
import com.app.sample.mailbox.model.People;
import com.app.sample.mailbox.widget.CircleTransform;
import com.app.sample.mailbox.widget.DividerItemDecoration;
import com.squareup.picasso.Picasso;

public class PeoplesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PeoplesListAdapter mAdapter;
    private ProgressBar progressBar;
    View view;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_peoples, container, false);

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
		
        // specify an adapter (see also next example)
        mAdapter = new PeoplesListAdapter(getActivity(), Constant.getPeopleData(getActivity()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new PeoplesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, People people) {
                dialogPeopoleDetails(people);
            }
        });
        return view;
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
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_mail) {
            Intent i = new Intent(getActivity().getApplicationContext(), ActivityComposeMail.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    private void dialogPeopoleDetails(final People people) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_people_detail);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView)dialog.findViewById(R.id.name)).setText(people.getName());
        ((TextView)dialog.findViewById(R.id.address)).setText(people.getAddress());
        ImageView image = (ImageView)dialog.findViewById(R.id.image);
        Picasso.with(getActivity()).load(people.getPhoto())
                .resize(200, 200)
                .transform(new CircleTransform())
                .into(image);
        ((ImageView)dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_send_mail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ActivityComposeMail.class);
                i.putExtra(ActivityComposeMail.EXTRA_OBJCT, people);
                startActivity(i);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mAdapter.notifyDataSetChanged();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
