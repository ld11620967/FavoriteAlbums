package com.nilin.favoritealbums;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private Photo[] photos = {new Photo(R.drawable.p1), new Photo(R.drawable.p2),
            new Photo(R.drawable.p3), new Photo(R.drawable.p4),
            new Photo(R.drawable.p5), new Photo(R.drawable.p6),
            new Photo(R.drawable.p7), new Photo(R.drawable.p8),
            new Photo(R.drawable.p9), new Photo(R.drawable.p10),
            new Photo(R.drawable.p11), new Photo(R.drawable.p12),
            new Photo(R.drawable.p13), new Photo(R.drawable.p14),
            new Photo(R.drawable.p15), new Photo(R.drawable.p16),
            new Photo(R.drawable.p17), new Photo(R.drawable.p18),
            new Photo(R.drawable.p19), new Photo(R.drawable.p20),
            new Photo(R.drawable.p21), new Photo(R.drawable.p22),
            new Photo(R.drawable.p23), new Photo(R.drawable.p24),
            new Photo(R.drawable.p25), new Photo(R.drawable.p26),
            new Photo(R.drawable.p27), new Photo(R.drawable.p28),
            new Photo(R.drawable.p29), new Photo(R.drawable.p30),
            new Photo(R.drawable.p31), new Photo(R.drawable.p32),
            new Photo(R.drawable.p33), new Photo(R.drawable.p34),
            new Photo(R.drawable.p35), new Photo(R.drawable.p36),
            new Photo(R.drawable.p37), new Photo(R.drawable.p38)};

    private List<Photo> photosList = new ArrayList<>();

    private PhotoAdapter adapter;

    private long mExitTime = 0;

    boolean isExit = false;

    private Toast mytoast;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PgyUpdateManager.register(MainActivity.this, getString(R.string.file_provider));
        initPhotos();
        //绑定服务
        bindPlayService();
        initView();
    }

    public void initView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoAdapter(photosList);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            SharedPreferences.Editor editor = settings.edit();

            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.updata:
//                        PgyUpdateManager.register(MainActivity.this, getString(R.string.file_provider));
                        DisplayToast("没有新版本时点击无效");
                        break;
                    case R.id.change_theme:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setIcon(R.drawable.ic_rainbow);
                        builder.setTitle("七色彩虹");
                        final String[] color = {"默认", "红色主题", "橙色主题", "黄色主题", "绿色主题",
                                "青色主题", "蓝色主题", "紫色主题"};
                        builder.setItems(color, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        editor.putInt("name", 0);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 1:
                                        editor.putInt("name", 1);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 2:
                                        editor.putInt("name", 2);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 3:
                                        editor.putInt("name", 3);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 4:
                                        editor.putInt("name", 4);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 5:
                                        editor.putInt("name", 5);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 6:
                                        editor.putInt("name", 6);
                                        editor.commit();
                                        recreate();
                                        break;
                                    case 7:
                                        editor.putInt("name", 7);
                                        editor.commit();
                                        recreate();
                                        break;
                                }
                            }
                        });
                        builder.show();
                        break;
                    case R.id.updata_log:
                        Intent intent_updatalog = new Intent(MainActivity.this, UpdataLogActivity.class);
                        startActivity(intent_updatalog);
                        break;
                    case R.id.about:
                        Intent intent_about = new Intent(MainActivity.this, AboutActivity.class);
                        startActivity(intent_about);
                        break;
                    default:
                }
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playService.mp.isPlaying()) {
                    playService.start();
                    DisplayToast("开启音乐");
                } else {
                    playService.pause();
                    DisplayToast("暂停音乐");
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
                return true;
            }
        });

    }

    @Override
    public void publish(int progress) {

    }

    @Override
    public void change() {

    }

    private void initPhotos() {
        for (int i = 0; i < photos.length; i++) {
            photosList.add(photos[i]);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                DisplayToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                mytoast.cancel();
                if (!isExit) {
                    playService.stop();
                    isExit = true;
                    finish();
                } else {
                    playService.stop();
                    isExit = false;
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void DisplayToast(String str) {
        if (mytoast == null) {
            mytoast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        } else {
            mytoast.setText(str);
        }
        mytoast.show();
    }

    //解除服务
    protected void onDestroy() {
        super.onDestroy();
        unbindPlayService();
    }

}


