package com.hhq.mediaplayerdemo;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/2.
 * @author  hhq
 */

public class MediaRecorderUtil {

    private static MediaRecorderUtil mediaRecorderUtil;
    private MediaRecorder mediaRecorder;

    public static MediaRecorderUtil getInstance(){
        return mediaRecorderUtil == null ? mediaRecorderUtil = new MediaRecorderUtil() : mediaRecorderUtil;
    }

    private MediaRecorderUtil(){
        mediaRecorder = new MediaRecorder();
    }


    public void initMediaRecorder(String path){
        try {
            mediaRecorder.reset();
            // 设置录音的声音来源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置声音编码的格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(path);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getVoice(){
        if(mediaRecorder!=null) {
            return mediaRecorder.getMaxAmplitude();
        }
        return 0;
    }

    public void stop(){
        // 停止录音
        mediaRecorder.stop();
    }


    public void release(){
        // 释放资源
        mediaRecorder.release();
    }
}
