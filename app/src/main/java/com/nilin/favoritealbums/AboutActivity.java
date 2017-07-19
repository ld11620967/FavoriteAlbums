package com.nilin.favoritealbums;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by lgp on 2015/5/25.
 */
public class AboutActivity extends BaseActivity {

    Button home_btn;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        setSupportActionBar(toolbar);
        //返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        home_btn = (Button) findViewById(R.id.project_home_btn);
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Uri uri = Uri.parse("https://github.com/ld11620967/FavoriteAlbums");
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
    }

    @Override
    public void publish(int progress) {

    }

    @Override
    public void change() {

    }

    //返回按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
