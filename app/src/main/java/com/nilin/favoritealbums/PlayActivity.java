package com.nilin.favoritealbums;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nilin.favoritealbums.utils.MediaUtils;
import com.nilin.favoritealbums.view.DefaultLrcBuilder;
import com.nilin.favoritealbums.view.ILrcBuilder;
import com.nilin.favoritealbums.view.LrcRow;
import com.nilin.favoritealbums.view.LrcView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static com.nilin.favoritealbums.R.layout.activity_music;

public class PlayActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final int UPDATE_TIME = 0x10;    //更新播放事件的标记
    private static final int UPDATE_LRC = 0x20;     //更新播放事件的标记
    private static final float BITMAP_SCALE = 0.5f;     //背景图片缩放
    private static final float BLUR_RADIUS = 25f;       //背景图片高斯模糊程度
    View background;
    ImageButton imageButton_pause, imageButton_stop, imageButton_back, imageButton_start;
    TextView musicStatus, musicTime;
    SeekBar seekBar;
    private LrcView lrcView;    //歌词视图

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(activity_music);
        //歌词背景高斯模糊
        background = findViewById(R.id.music_background);
        Bitmap sampleImg = BitmapFactory.decodeResource(getResources(), R.drawable.p32);
        Bitmap gaussianBlurImg = Bblur(this, sampleImg);
        BitmapDrawable bg = new BitmapDrawable(gaussianBlurImg);
        background.setBackground(bg);

        seekBar = this.findViewById(R.id.MusicSeekBar);
        musicStatus = this.findViewById(R.id.MusicStatus);
        musicTime = this.findViewById(R.id.MusicTime);
        imageButton_start = findViewById(R.id.ib_play_start);
        imageButton_pause = findViewById(R.id.ib_play_pause);
        imageButton_stop = findViewById(R.id.ib_play_stop);
        imageButton_back = findViewById(R.id.iv_play_back);

        imageButton_start.setOnClickListener(this);
        imageButton_pause.setOnClickListener(this);
        imageButton_stop.setOnClickListener(this);
        imageButton_back.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        myHandler = new MyHandler(this);

        //显示歌词
        lrcView = findViewById(R.id.lrcView);
        String lrc = getFromAssets("lyric.lrc");
        ILrcBuilder builder = new DefaultLrcBuilder();
        List<LrcRow> rows = builder.getLrcRows(lrc);
        lrcView.setLrc(rows);
    }

    //Handler用于更新已经播放时间
    private static MyHandler myHandler;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            playService.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    static class MyHandler extends Handler {
        private PlayActivity playActivity;

        MyHandler(PlayActivity playActivity) {
            this.playActivity = playActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (playActivity != null) {
                switch (msg.what) {
                    case UPDATE_TIME://更新时间(已经播放时间)
                        playActivity.musicStatus.setText(MediaUtils.formatTime((int) msg.obj));
                        break;
                    case UPDATE_LRC:
                        playActivity.lrcView.seekLrcToTime((int) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void publish(int progress) {
        myHandler.obtainMessage(UPDATE_TIME, progress).sendToTarget();
        seekBar.setProgress(progress);
        myHandler.obtainMessage(UPDATE_LRC, progress).sendToTarget();
    }

    public void change() {
        musicTime.setText(MediaUtils.formatTime(playService.getDuration()));//设置结束时
        seekBar.setMax(playService.getDuration());//设置进度条最大值为MP3总时间
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_play_start:
                playService.start();
                break;
            case R.id.ib_play_pause:
                playService.pause();
                break;
            case R.id.ib_play_stop:
                playService.stop();
                seekBar.setProgress(0);
                musicStatus.setText("00:00");
                break;
            case R.id.iv_play_back:
                finish();
                break;
            default:
                break;
        }
    }

    //把播放服务的绑定和解绑放在onResume,onPause里,好处是,每次回到当前Activity就获取一次播放状态
    @Override
    protected void onResume() {
        super.onResume();
        bindPlayService();//绑定服务
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindPlayService();//解绑服务
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindPlayService();//解绑服务
    }

    /**
     * 歌词显示
     */
    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder Result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                Result.append(line).append("\r\n");
            }
            return Result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 歌词背景高斯模糊
     */
    public static Bitmap Bblur(Context context, Bitmap image) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(BLUR_RADIUS);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}