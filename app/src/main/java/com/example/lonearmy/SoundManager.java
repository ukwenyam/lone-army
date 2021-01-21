package com.example.lonearmy;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class SoundManager {

    private SoundPool soundPool;
    int shoot = -1;
    int jump = -1;
    int teleport = -1;
    int coin_pickup = -1;
    int gun_upgrade = -1;
    int player_burn = -1;
    int ricochet = -1;
    int hit_guard = -1;
    int explode = -1;
    int extra_life = -1;

    public void loadSound(Context context){
        soundPool = getNewSoundPool(10, AudioManager.STREAM_MUSIC,0);
        try{
            //Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            //create our fx
            descriptor = assetManager.openFd("shoot.ogg");
            shoot = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("jump.ogg");
            jump = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("teleport.ogg");
            teleport = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("coin_pickup.ogg");
            coin_pickup = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("gun_upgrade.ogg");
            gun_upgrade = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("player_burn.ogg");
            player_burn = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("ricochet.ogg");
            ricochet = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("hit_guard.ogg");
            hit_guard = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("explode.ogg");
            explode = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("extra_life.ogg");
            extra_life = soundPool.load(descriptor, 0);
        }catch(IOException e){
            //Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    private SoundPool getNewSoundPool(int maxStreams, int streamType, int srcQuality) {
        SoundPool.Builder sb = new SoundPool.Builder();
        AudioAttributes.Builder ab = new AudioAttributes.Builder();
        sb.setMaxStreams(maxStreams);
        ab.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        ab.setUsage(AudioAttributes.USAGE_MEDIA);
        sb.setAudioAttributes(ab.build());
        return sb.build();
    }

    public void playSound(String sound){
        switch (sound){
            case "shoot":
                soundPool.play(shoot, 1, 1, 0, 0, 1);
                break;
            case "jump":
                soundPool.play(jump, 1, 1, 0, 0, 1);
                break;
            case "teleport":
                soundPool.play(teleport, 1, 1, 0, 0, 1);
                break;
            case "coin_pickup":
                soundPool.play(coin_pickup, 1, 1, 0, 0, 1);
                break;
            case "gun_upgrade":
                soundPool.play(gun_upgrade, 1, 1, 0, 0, 1);
                break;
            case "player_burn":
                soundPool.play(player_burn, 1, 1, 0, 0, 1);
                break;
            case "ricochet":
                soundPool.play(ricochet, 1, 1, 0, 0, 1);
                break;
            case "hit_guard":
                soundPool.play(hit_guard, 1, 1, 0, 0, 1);
                break;
            case "explode":
                soundPool.play(explode, 1, 1, 0, 0, 1);
                break;
            case "extra_life":
                soundPool.play(extra_life, 1, 1, 0, 0, 1);
                break;
        }
    }
}// End SoundManager
