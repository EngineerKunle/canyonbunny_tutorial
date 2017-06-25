package com.teamkunle.canyonbunny.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.teamkunle.canyonbunny.utils.GamePreferencesUtils;

/**
 * Created by EngineerKunle on 21/06/2017.
 */

public class AudioManager {
    public static final AudioManager instance = new AudioManager();

    private Music playingMusic;

    // singleton
    private AudioManager() {
    }

    public void play(Sound sound) {
        play(sound, 1f);
    }

    public void play(Sound sound, float volume) {
        play(sound, volume, 1f);
    }

    public void play(Sound sound, float volume, float pitch) {
        play(sound, volume, pitch, 1);
    }

    public void play(Sound sound, float volume, float pitch, float pan) {
        if(!GamePreferencesUtils.instance.sound) return;
        sound.play(GamePreferencesUtils.instance.volSound * volume, pitch, pan);
    }

    public void play(Music music){
        playingMusic = music;
        if (GamePreferencesUtils.instance.music) {
            music.setLooping(true);
            music.setVolume(GamePreferencesUtils.instance.volMusic);
            music.play();
        }
    }

    public void stopMusic() {
        if(playingMusic != null) playingMusic.stop();
    }

    public Music getPlayMusic() {
        return playingMusic;
    }

    public void onSettingsUpdated() {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferencesUtils.instance.volMusic);
        if (GamePreferencesUtils.instance.music) {
            if (!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}