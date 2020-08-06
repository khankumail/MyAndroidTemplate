package com.app.music.data;

import android.content.Context;
import android.content.res.TypedArray;

import com.app.music.R;
import com.app.music.model.Artist;
import com.app.music.model.MusicAlbum;
import com.app.music.model.MusicSong;
import com.app.music.model.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Constant {

    private static Random r = new Random();

    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }

    /**
     * Generate dummy data music song
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<MusicSong> getMusicSong(Context ctx) {
        List<MusicSong> items = new ArrayList<>();
        String song_name[] = ctx.getResources().getStringArray(R.array.song_name);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.album_cover);
        String album_name[] = ctx.getResources().getStringArray(R.array.album_name);
        int album_idx = 0;
        for (int i = 0; i < song_name.length; i++) {
            MusicSong obj = new MusicSong();
            obj.title = song_name[i];
            obj.album = album_name[album_idx];
            obj.album_id = album_idx;
            obj.image = drw_arr.getResourceId(album_idx, -1);
            if ((i + 1) % 2 == 0) {
                album_idx++;
            }
            items.add(obj);
        }
        return items;
    }

    public static List<MusicSong> getMusicSongByAlbumId(Context ctx, int id) {
        List<MusicSong> items = getMusicSong(ctx);
        List<MusicSong> filtered_items = new ArrayList<>();
        for (MusicSong m : items) {
            if (m.album_id == id) filtered_items.add(m);
        }
        return filtered_items;
    }

    /**
     * Generate dummy data music album
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<MusicAlbum> getMusicAlbum(Context ctx) {
        List<MusicAlbum> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.album_cover);
        String album_name[] = ctx.getResources().getStringArray(R.array.album_name);
        for (int i = 0; i < drw_arr.length(); i++) {
            MusicAlbum obj = new MusicAlbum();
            obj.id = i;
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = album_name[i];
            obj.brief = "2 Songs";
            obj.color = Tools.getMaterialColor(ctx, i);
            items.add(obj);
        }
        return items;
    }

    /**
     * Generate dummy data artist
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Artist> getArtist(Context ctx) {
        List<Artist> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.artist_photo);
        String name[] = ctx.getResources().getStringArray(R.array.artist_name);
        String brief[] = ctx.getResources().getStringArray(R.array.artist_brief);
        for (int i = 0; i < drw_arr.length(); i++) {
            Artist obj = new Artist();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.title = name[i];
            obj.brief = brief[i];
            items.add(obj);
        }
        return items;
    }

    /**
     * Generate dummy data playlist
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<Playlist> getPlaylist(Context ctx) {
        List<Playlist> items = new ArrayList<>();
        String name[] = ctx.getResources().getStringArray(R.array.playlist_name);
        for (int i = 0; i < name.length; i++) {
            Playlist obj = new Playlist();
            obj.title = name[i];
            obj.id = obj.title.hashCode();
            items.add(obj);
        }
        return items;
    }

}
