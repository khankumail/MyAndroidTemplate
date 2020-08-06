package com.app.sample.messenger;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.sample.messenger.data.Tools;
import com.app.sample.messenger.model.Friend;
import com.app.sample.messenger.widget.CircleTransform;
import com.squareup.picasso.Picasso;

public class ActivityVideo extends AppCompatActivity {

    public static String KEY_FRIEND     = "com.app.sample.messenger.FRIEND";

    private Friend friend;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        parent_view = findViewById(android.R.id.content);

        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);

        initToolbar();

        // for system bar in lollipop
        Tools.systemBarLolipopDark(this);
    }

    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(friend.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }


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

    public void actionClick(View v){
        // Handle item selection
        switch (v.getId()) {
            case R.id.bt_end:
                onBackPressed();
                break;
            case R.id.bt_record:
                Snackbar.make(parent_view, "Record Clicked ", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

}
