package com.hhq.mediaplayerdemo;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/1.
 * @author hhq
 */

public class MediaPlayerUtil {

    private static final int LOCAL = 1;
    private static final int NETWORK = 2;
    private static  MediaPlayerUtil mediaPlayerUtil;
    private MediaPlayer mediaPlayer;
    private String path;
    private int i;
    private Context context;

    //是否是暂停状态
    private boolean isPause=false;
    //播放到当前位置
    private int currentPosition;

    public static MediaPlayerUtil getInstance(){
        return mediaPlayerUtil == null ? mediaPlayerUtil = new MediaPlayerUtil() : mediaPlayerUtil;
    }

    private MediaPlayerUtil(){
        mediaPlayer = new MediaPlayer();
    }


    public MediaPlayer getMediaPlayer(){
        if(mediaPlayer != null) {
            return mediaPlayer;
        }
        return null;
    }

    /**
    * 初始化mediaplayer
     * @param path 音频播放地址
    */
    public void initMediaPlayer(Context context,String path, int i){
        try {
            this.path = path;
            this.i = i;
            this.context = context;
            mediaPlayer.reset();

            if(LOCAL == i) {
                mediaPlayer.setDataSource(path);
            }else if(NETWORK == i){
                mediaPlayer.setDataSource(context, Uri.parse(path));
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    isPause = false;
                    mediaPlayer.stop();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void playOrstop(){

        if(!mediaPlayer.isPlaying()&&!isPause) {
            mediaPlayer.prepareAsync();
        }else {
            if(isPause) {
                //如果是处于暂停状态那么继续播放并修改暂停状态
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.start();
                isPause = false;
            }else {
                mediaPlayer.reset();
                initMediaPlayer(context,path,i);
                isPause = false;
            }
        }
    }


    public void onPause(){
        if(mediaPlayer.isPlaying()) {
            //当音频处于播放状态时暂停播放修改暂停状态同时获取播放位置便于继续播放时使用
            mediaPlayer.pause();
            isPause = true;
            currentPosition = mediaPlayer.getCurrentPosition();
        }
    }

    public boolean isPlaying(){
        if(mediaPlayer.isPlaying()) {
            return true;
        }else {
            return false;
        }
    }

    public void release(){
        if(mediaPlayer!=null) {
            mediaPlayer.release();
        }
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public int getTotalTime(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public void reset(){
        if(mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    public void start(){
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

}
