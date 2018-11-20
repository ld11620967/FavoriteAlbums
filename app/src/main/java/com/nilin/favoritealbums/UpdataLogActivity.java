package com.nilin.favoritealbums;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class UpdataLogActivity extends BaseActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_log);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("更新日志");
        setSupportActionBar(toolbar);
        //返回按钮
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.update_log_text);
        String lrc = getFromAssets("updata_log.txt");
        textView.setText(lrc);
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

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder Result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                Result.append(line).append("\r\n");
            }
            return Result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
