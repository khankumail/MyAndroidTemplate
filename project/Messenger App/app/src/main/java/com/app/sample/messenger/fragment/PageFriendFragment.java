package com.app.sample.messenger.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sample.messenger.ActivityChatDetails;
import com.app.sample.messenger.ActivityMain;
import com.app.sample.messenger.ActivityViewProfile;
import com.app.sample.messenger.R;
import com.app.sample.messenger.adapter.FriendsListAdapter;
import com.app.sample.messenger.data.Constant;
import com.app.sample.messenger.model.Friend;
import com.app.sample.messenger.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PageFriendFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;
    private List<Friend> items = new ArrayList<>();
    private FriendsListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.page_fragment_friend, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        items = Constant.getFriendsData(getActivity());

        // specify an adapter (see also next example)
        mAdapter = new FriendsListAdapter(getActivity(), items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int position) {
                ActivityChatDetails.navigate((ActivityMain) getActivity(), v.findViewById(R.id.lyt_parent), obj, null);
            }
        });

        mAdapter.setOnMoreButtonClickListener(new FriendsListAdapter.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View v, Friend obj, int actionId) {
                switch (actionId){
                    case R.id.action_profile:
                        Intent intent = new Intent(getActivity(), ActivityViewProfile.class);
                        intent.putExtra(ActivityChatDetails.KEY_FRIEND, obj);
                        startActivity(intent);
                    break;
                    case R.id.action_info:
                        dialogPeopoleDetails(obj);
                    break;
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void dialogPeopoleDetails(final Friend friend) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_contact_info);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView)dialog.findViewById(R.id.name)).setText(friend.getName());
        ((TextView)dialog.findViewById(R.id.address)).setText(Constant.getBoolean()?"Active Now" : "Inactive");
        ImageView image = (ImageView)dialog.findViewById(R.id.image);
        Picasso.with(getActivity()).load(friend.getPhoto())
                .resize(200, 200)
                .transform(new CircleTransform())
                .into(image);
        ((Button) dialog.findViewById(R.id.bt_send_message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityChatDetails.class);
                intent.putExtra(ActivityChatDetails.KEY_FRIEND, friend);
                startActivity(intent);
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
