package com.app.music.data;

public interface OnPlayerStateChange {

    void onStart();

    void onPause();

    void onRestart();

    void onComplete();

}
