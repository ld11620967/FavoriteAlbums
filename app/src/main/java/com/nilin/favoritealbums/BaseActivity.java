package com.nilin.favoritealbums;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nilin on 2017/2/22.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected PlayService playService;
    private boolean isBound = false;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        settings = getSharedPreferences("setting", 0);
        int theme = settings.getInt("name", 0);
        if (savedInstanceState == null) {
            if (theme == 0) {
                setTheme(R.style.AppTheme_Base);
            } else if (theme == 1) {
                setTheme(R.style.Red);
            } else if (theme == 2) {
                setTheme(R.style.Orange);
            } else if (theme == 3) {
                setTheme(R.style.Yellow);
            } else if (theme == 4) {
                setTheme(R.style.Green);
            } else if (theme == 5) {
                setTheme(R.style.Cyan);
            } else if (theme == 6) {
                setTheme(R.style.Blue);
            } else if (theme == 7) {
                setTheme(R.style.Purple);
            } else
                setTheme(R.style.AppTheme_Base);
        } else {
            if (theme == 0) {
                setTheme(R.style.AppTheme_Base);
            } else if (theme == 1) {
                setTheme(R.style.Red);
            } else if (theme == 2) {
                setTheme(R.style.Orange);
            } else if (theme == 3) {
                setTheme(R.style.Yellow);
            } else if (theme == 4) {
                setTheme(R.style.Green);
            } else if (theme == 5) {
                setTheme(R.style.Cyan);
            } else if (theme == 6) {
                setTheme(R.style.Blue);
            } else if (theme == 7) {
                setTheme(R.style.Purple);
            } else
                setTheme(R.style.AppTheme_Base);
        }

        super.onCreate(savedInstanceState);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.PlayBinder playBinder = (PlayService.PlayBinder) service;
            playService = playBinder.getPlayService();
            playService.setMusicUpdatrListener(musicUpdatrListener);
            musicUpdatrListener.onChange();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playService = null;
            isBound = false;
        }
    };

    //实现MusicUpdatrListener的相关方法,把PlayService.MusicUpdatrListener的具体内容,
    // 延迟到子类来具体实现(把具体的操作步骤在子类中实现)
    private PlayService.MusicUpdatrListener musicUpdatrListener = new PlayService.MusicUpdatrListener() {
        @Override
        public void onPublish(int progress) {
            publish(progress);
        }

        //初始化，回调PlayAcitvity中的change(),用于PlayAcitvity中change()数据初始化。
        @Override
        public void onChange() {
            change();
        }
    };

    //抽象类(子类来具体实现,用于更新UI)
    public abstract void publish(int progress);

    public abstract void change();


    //绑定服务
    public void bindPlayService() {
        if (!isBound) {
            Intent intent = new Intent(this, PlayService.class);
            bindService(intent, conn, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    //解除服务
    public void unbindPlayService() {
        if (isBound) {
            unbindService(conn);
            isBound = false;
        }
    }
}

