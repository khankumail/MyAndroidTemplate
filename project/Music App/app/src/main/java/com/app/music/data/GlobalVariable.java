package com.app.music.data;

import android.app.Application;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.Toast;

import com.app.music.model.MusicSong;
import com.app.music.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Application {

    private static GlobalVariable mInstance;

    public static synchronized GlobalVariable getInstance() {
        return mInstance;
    }

    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    // current playing music song
    private MusicSong musicSong;

    private OnMusicSongChange onMusicSongChange;
    private OnPlayerStateChange onPlayerStateChange;

    private List<Playlist> playlists = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Media Player
        mp = new MediaPlayer();

        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor afd = getAssets().openFd("short_music.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
        } catch (Exception e) {
            Toast.makeText(this, "Cannot load audio file", Toast.LENGTH_SHORT).show();
        }

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onPlayerStateChange != null) onPlayerStateChange.onComplete();
            }
        });

        playlists = Constant.getPlaylist(this);
    }

    public MediaPlayer getMediaPlayer() {
        return mp;
    }

    public MusicSong getMusicSong() {
        return musicSong;
    }

    public boolean isPlaying() {
        try {
            return (mp != null && mp.isPlaying());
        } catch (Exception e) {
            return false;
        }
    }

    public void releasePlayer() {
        mp.release();
    }

    public void setPlayerState(PlayerState state) {
        try {
            if (state.equals(PlayerState.START)) {
                mp.start();
                if (onPlayerStateChange != null) onPlayerStateChange.onStart();
            } else if (state.equals(PlayerState.PAUSE)) {
                mp.pause();
                if (onPlayerStateChange != null) onPlayerStateChange.onPause();
            } else if (state.equals(PlayerState.RESTART)) {
                mp.seekTo(0);
                if (!mp.isPlaying()) {
                    setPlayerState(PlayerState.START);
                }
                if (onPlayerStateChange != null) onPlayerStateChange.onRestart();
            }
        } catch (Exception e) {

        }
    }

    public void setMusicSong(MusicSong musicSong) {
        this.musicSong = musicSong;
        if (onMusicSongChange != null) onMusicSongChange.onChange(this.musicSong);
    }

    public void setOnMusicSongChange(OnMusicSongChange onMusicSongChange) {
        this.onMusicSongChange = onMusicSongChange;
    }

    public void setOnPlayerStateChange(OnPlayerStateChange onPlayerStateChange) {
        this.onPlayerStateChange = onPlayerStateChange;
    }


    // setter getter playlist

    public List<Playlist> getPlaylist() {
        return playlists;
    }

    public Playlist addPlaylist(String name) {
        Playlist p = new Playlist();
        p.title = name;
        p.id = p.title.hashCode();
        this.playlists.add(p);
        return p;
    }

    public void removePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
    }

    public Playlist updatePlaylist(Playlist playlist) {
        for (Playlist p : this.playlists) {
            if (p.id == playlist.id) {
                p.title = playlist.title;
                p.id = playlist.title.hashCode();
                playlist = p;
                break;
            }
        }
        return playlist;
    }

    public void clearPlaylist() {
        this.playlists.clear();
    }

    public boolean isPlaylistExist(String name) {
        Playlist p = new Playlist();
        p.title = name;
        p.id = p.title.hashCode();
        return this.playlists.contains(p);
    }
}
