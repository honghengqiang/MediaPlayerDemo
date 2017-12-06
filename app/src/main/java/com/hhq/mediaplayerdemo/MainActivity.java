package com.hhq.mediaplayerdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.playOrStop)
    Button playOrStop;
    @BindView(R.id.pause)
    Button pause;
    @BindView(R.id.recorder)
    Button recorder;
    @BindView(R.id.stop)
    Button stop;
    @BindView(R.id.play)
    Button play;
    @BindView(R.id.crash)
    Button crash;

    private MediaPlayerUtil mediaPlayerUtil;
    private MediaRecorderUtil mediaRecorderUtil;
    private String path;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mediaPlayerUtil = MediaPlayerUtil.getInstance();
        mediaRecorderUtil = MediaRecorderUtil.getInstance();

        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        MainActivityPermissionsDispatcher.playWithCheck(MainActivity.this);
    }


    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
    void play() {
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Love_Me_For_Me.mp3";
        //mediaPlayerUtil.initMediaPlayer(this,path,1);
        //mediaPlayerUtil.initMediaPlayer(MainActivity.this, path + "/recorder.amr", 1);
        String path = "https://files.7tkt.com/question_voice_file/d25584415b80527c42d70f03162d1776_ryupppj9ew8r4qtzgnngi86ql4y4xtr5.mp3";
        mediaPlayerUtil.initMediaPlayer(this, path, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
    void showWhy(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("该功能需要内存读写权限，不开启APP将无法正常工作！")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();//再次执行权限请求
                    }
                }).show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
    void showDenied() {
        finish();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
    void showNotAsk() {
        new AlertDialog.Builder(this)
                .setMessage("该功能需要内存读写权限，不开启将无法正常工作！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerUtil.release();
        mediaRecorderUtil.release();
    }

    @OnClick({R.id.playOrStop, R.id.pause, R.id.recorder, R.id.stop, R.id.play,R.id.crash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playOrStop:
                mediaPlayerUtil.playOrstop();
                break;
            case R.id.pause:
                mediaPlayerUtil.onPause();
                break;
            case R.id.recorder:
                mediaRecorderUtil.initMediaRecorder(path + "/recorder.amr");
                break;
            case R.id.stop:
                mediaRecorderUtil.stop();
                break;
            case R.id.play:
                mediaPlayerUtil.playOrstop();
                break;
            case R.id.crash:
                Toast.makeText(MainActivity.this, list.size()+"s", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
