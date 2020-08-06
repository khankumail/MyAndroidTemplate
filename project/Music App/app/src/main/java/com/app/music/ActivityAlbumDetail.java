package com.app.music;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.music.adapter.AdapterListSong;
import com.app.music.data.Constant;
import com.app.music.data.GlobalVariable;
import com.app.music.data.OnMusicSongChange;
import com.app.music.data.OnPlayerStateChange;
import com.app.music.data.PlayerState;
import com.app.music.data.Tools;
import com.app.music.model.MusicAlbum;
import com.app.music.model.MusicSong;
import com.app.music.widget.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityAlbumDetail extends AppCompatActivity {

    public static final String EXTRA_OBJCT = "key.OBJCT";
    private MusicAlbum musicAlbum;

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, MusicAlbum obj) {
        Intent intent = new Intent(activity, ActivityAlbumDetail.class);
        intent.putExtra(EXTRA_OBJCT, obj);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, EXTRA_OBJCT);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private ImageView image_album;
    private ImageView bt_expand;
    private View lyt_disc;

    private ImageView bt_play;

    private GlobalVariable global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        global = (GlobalVariable) getApplication();

        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.image), EXTRA_OBJCT);

        // get extra object
        musicAlbum = (MusicAlbum) getIntent().getSerializableExtra(EXTRA_OBJCT);

        initToolbar();
        initComponent();
        actionHandle();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

        image_album = (ImageView) findViewById(R.id.image_album);
        bt_expand = (ImageView) findViewById(R.id.bt_expand);
        lyt_disc = (View) findViewById(R.id.lyt_disc);
        bt_play = (ImageView) findViewById(R.id.bt_play);
        Picasso.with(this).load(R.drawable.photo_album_1).transform(new CircleTransform()).into(image_album);

        // scrollable toolbar
        CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        CardView card_album_title = (CardView) findViewById(R.id.card_album_title);
        TextView album_name = (TextView) findViewById(R.id.album_name);
        collapsing_toolbar.setContentScrimColor(musicAlbum.color);
        card_album_title.setCardBackgroundColor(musicAlbum.color);
        album_name.setText(musicAlbum.name);

        // appbar
        AppBarLayout app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    Tools.systemBarLollipop(ActivityAlbumDetail.this, musicAlbum.color);
                } else if (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange()/2) {
                    // Expanded
                    Tools.systemBarTransparent(ActivityAlbumDetail.this);
                }
            }
        });

        ImageView ivImage = (ImageView) findViewById(R.id.image);
        ivImage.setImageResource(musicAlbum.image);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<MusicSong> items = Constant.getMusicSongByAlbumId(this, musicAlbum.id);

        //set data and list adapter
        AdapterListSong mAdapter = new AdapterListSong(this, items);
        recyclerView.setAdapter(mAdapter);

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
                Toast.makeText(getApplicationContext(), obj.title + " (" + item.getTitle() + ") clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionHandle() {
        // setup music component
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (global.isPlaying()) {
                    global.setPlayerState(PlayerState.PAUSE);
                } else {
                    global.setPlayerState(PlayerState.START);
                }
            }
        });

        bt_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityPlayerDetail.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        global.setOnPlayerStateChange(new OnPlayerStateChange() {
            @Override
            public void onStart() {
                bt_play.setImageResource(R.drawable.ic_pause);
                rotateImageAlbum();
            }

            @Override
            public void onPause() {
                bt_play.setImageResource(R.drawable.ic_play);
                rotateImageAlbum();
            }

            @Override
            public void onRestart() {

            }

            @Override
            public void onComplete() {
                rotateImageAlbum();
                bt_play.setImageResource(R.drawable.ic_play);
            }
        });

        global.setOnMusicSongChange(new OnMusicSongChange() {
            @Override
            public void onChange(MusicSong musicSong) {
                changeMusicInfo(musicSong);
            }
        });

        changeMusicInfo(global.getMusicSong());
        rotateImageAlbum();
        if (global.isPlaying()) {
            bt_play.setImageResource(R.drawable.ic_pause);
        } else {
            bt_play.setImageResource(R.drawable.ic_play);
        }
        super.onResume();
    }

    private void changeMusicInfo(MusicSong musicSong) {
        if (musicSong == null) return;
        Picasso.with(this).load(musicSong.image).transform(new CircleTransform()).into(image_album);
        ((TextView) findViewById(R.id.music_title)).setText(musicSong.title);
        ((TextView) findViewById(R.id.music_album)).setText(musicSong.album);
    }

    private void rotateImageAlbum() {
        if (!global.isPlaying()) return;
        lyt_disc.animate().setDuration(100)
                .setInterpolator(new LinearInterpolator())
                .rotation(lyt_disc.getRotation() + 5f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateImageAlbum();
                super.onAnimationEnd(animation);
            }
        });
    }
}
