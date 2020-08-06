package com.app.music.model;

import java.io.Serializable;

public class Playlist implements Serializable {

    public int id;
    public String title;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Playlist)) {
            return false;
        }
        Playlist p = (Playlist) obj;
        if (p.id != id || !p.title.equals(title)) {
            return false;
        }
        return true;
    }
}
