package com.app.sample.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sample.messenger.adapter.PhotosListAdapter;
import com.app.sample.messenger.data.Constant;
import com.app.sample.messenger.data.Tools;
import com.app.sample.messenger.model.Friend;
import com.app.sample.messenger.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityViewProfile extends AppCompatActivity {
    public static String KEY_FRIEND = "com.app.sample.messenger.FRIEND";

    private ActionBar actionBar;
    private Friend friend;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        parent_view = findViewById(android.R.id.content);

        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);
        initToolbar();

        iniComponen();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("View Profile");
    }

    public void iniComponen() {
        ((TextView) findViewById(R.id.name)).setText(friend.getName());
        ImageView image = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(friend.getPhoto()).resize(300, 300).transform(new CircleTransform()).into(image);
        setPhotosGallery();
        ((Button) findViewById(R.id.bt_more)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(parent_view, "More Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setPhotosGallery() {
        List<Integer> new_images = Constant.getRandomPhotos(this);
        RecyclerView galleryRecycler = (RecyclerView) findViewById(R.id.galleryRecycler);
        galleryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        PhotosListAdapter adapter = new PhotosListAdapter(new_images);
        galleryRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int viewModel, int pos) {
                Snackbar.make(parent_view, "Photo Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handle click on action bar
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                Snackbar.make(parent_view, item.getTitle() + " Clicked ", Snackbar.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}
