package com.nilin.favoritealbums;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayService extends Service {

    private static final String TAG = "test";

    public MediaPlayer mp;

    private MusicUpdatrListener musicUpdatrListener;

    //创建一个单实例的线程,用于更新音乐信息
    private ExecutorService es = Executors.newSingleThreadExecutor();

    public PlayService() {

    }

    class PlayBinder extends Binder {
        public PlayService getPlayService() {
            return PlayService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return new PlayBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        es.execute(updateSteatusRunnable);//更新进度值
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //回收线程
        if (es != null && !es.isShutdown()) {//当进度值等于空,并且,进度值没有关闭
            es.shutdown();
            es = null;
        }
    }

    //默认开始播放的方法
    public void start() {
        if (mp != null && !mp.isPlaying()) {//判断当前歌曲不等于空,并且没有在播放的状态
            mp.start();
        }
    }

    public void pause() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }

    public void stop() {
        mp.stop();
        try {
            mp.prepare();
            seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取当前是否为播放状态,提供给MyMusicListFragment的播放暂停按钮点击事件判断状态时调用
    public boolean isPlaying() {
        if (mp != null) {
            return mp.isPlaying();
        }
        return false;
    }

    //获取当前的进度值
    public int getCurrentProgress(){
        if(mp!=null && mp.isPlaying()){//mp,并且,为播放状态
            return mp.getCurrentPosition();
        }
        return 0;
    }

    //getDuration 获取文件的持续时间
    public int getDuration(){
        return mp.getDuration();
    }

    //seekTo 寻找指定的时间位置
    public void seekTo(int msec){
        mp.seekTo(msec);
    }

    //利用Runnable来实现多线程
    /**
     * Runnable
     * Java中实现多线程有两种途径:继承Thread类或者实现Runnable接口.
     * Runnable接口非常简单,就定义了一个方法run(),继承Runnable并实现这个
     * 方法就可以实现多线程了,但是这个run()方法不能自己调用,必须由系统来调用,否则就和别的方法没有什么区别了.
     * 好处:数据共享
     */
    Runnable updateSteatusRunnable = new Runnable() {//更新状态
        @Override
        public void run() {
            //不断更新进度值
            while (true) {
                //音乐更新监听不为空,并且,媒体播放不为空,并且媒体播放为播放状态
                if (musicUpdatrListener != null && mp != null && mp.isPlaying()) {
                    musicUpdatrListener.onPublish(getCurrentProgress());//获取当前的进度值
                }
                try {
                    Thread.sleep(500);//500毫秒更新一次
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //更新状态的接口(PlayService的内部接口),并在BaseActivity中实现
    public interface MusicUpdatrListener {//音乐更新监听器
        void onPublish(int progress);//发表进度事件(更新进度条)
        void onChange();

        //声明MusicUpdatrListener后,添加set方法
    }

    //set方法
    public void setMusicUpdatrListener(MusicUpdatrListener musicUpdatrListener) {
        this.musicUpdatrListener = musicUpdatrListener;
    }


}
