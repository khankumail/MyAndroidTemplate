package com.app.music;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.music.data.GlobalVariable;
import com.app.music.data.OnPlayerStateChange;
import com.app.music.data.PlayerState;
import com.app.music.data.Tools;
import com.app.music.model.MusicSong;
import com.app.music.widget.CircleTransform;
import com.squareup.picasso.Picasso;

public class ActivityPlayerDetail extends AppCompatActivity {

    public View parent_view;

    private GlobalVariable global;
    private ImageView image_album;
    private View lyt_disc;
    private FloatingActionButton bt_play;
    private AppCompatSeekBar seek_song_progressbar;
    private TextView tv_song_current_duration, tv_song_total_duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);
        parent_view = findViewById(R.id.parent_view);
        global = (GlobalVariable) getApplication();

        initToolbar();
        initComponent();
        actionHandle();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.systemBarLollipopDark(this);
    }

    private void initComponent() {
        seek_song_progressbar = (AppCompatSeekBar) findViewById(R.id.seek_song_progressbar);
        tv_song_current_duration = (TextView) findViewById(R.id.tv_song_current_duration);
        tv_song_total_duration = (TextView) findViewById(R.id.tv_song_total_duration);
        // set Progress bar values
        seek_song_progressbar.setProgress(0);
        seek_song_progressbar.setMax(Tools.MAX_PROGRESS);

        lyt_disc = (View) findViewById(R.id.lyt_disc);
        image_album = (ImageView) findViewById(R.id.image_album);
        bt_play = (FloatingActionButton) findViewById(R.id.bt_play);
        changeMusicInfo(global.getMusicSong());
        rotateImageAlbum();
        if (global.isPlaying()) {
            bt_play.setImageResource(R.drawable.ic_pause);
        } else {
            bt_play.setImageResource(R.drawable.ic_play);
        }
        updateTimerAndSeekbar();
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

        // Listeners
        seek_song_progressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int totalDuration = global.getMediaPlayer().getDuration();
                int currentPosition = Tools.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                global.getMediaPlayer().seekTo(currentPosition);
            }
        });
    }

    private void rotateImageAlbum() {
        if (!global.isPlaying()) return;
        lyt_disc.animate().setDuration(500)
                .setInterpolator(new LinearInterpolator())
                .rotation(lyt_disc.getRotation() + 5f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateImageAlbum();
                super.onAnimationEnd(animation);
            }
        });
        updateTimerAndSeekbar();
    }

    private void changeMusicInfo(MusicSong musicSong) {
        if (musicSong == null) return;
        Picasso.with(this).load(musicSong.image).transform(new CircleTransform()).into(image_album);
        ((TextView) findViewById(R.id.music_title)).setText(musicSong.title);
        ((TextView) findViewById(R.id.music_album)).setText(musicSong.album);
    }

    public void controlClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_repeat: {
                toggleButtonColor((ImageView) v);
                Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.bt_shuffle: {
                toggleButtonColor((ImageView) v);
                Toast.makeText(this, "Shuffle", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.bt_prev: {
                Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.bt_next: {
                Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    private boolean toggleButtonColor(ImageView bt) {
        String selected = (String) bt.getTag(bt.getId());
        if (selected != null) { // selected
            bt.setColorFilter(getResources().getColor(R.color.grey_hard), PorterDuff.Mode.SRC_ATOP);
            bt.setTag(bt.getId(), null);
            return false;
        } else {
            bt.setTag(bt.getId(), "selected");
            bt.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_player_detail, menu);
        Tools.changeMenuIconColor(menu, Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if(id == R.id.action_playlist){
            Toast.makeText(this, "Playlist Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    // stop player when destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateTimerAndSeekbar() {
        long totalDuration = global.getMediaPlayer().getDuration();
        long currentDuration = global.getMediaPlayer().getCurrentPosition();

        // Displaying Total Duration time
        tv_song_total_duration.setText(Tools.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        tv_song_current_duration.setText(Tools.milliSecondsToTimer(currentDuration));

        // Updating progress bar
        int progress = (int) (Tools.getProgressSeekBar(currentDuration, totalDuration));
        seek_song_progressbar.setProgress(progress);
    }
}
