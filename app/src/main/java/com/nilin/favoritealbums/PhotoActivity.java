package com.nilin.favoritealbums;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.ImageView;
import android.app.*;
import android.os.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


public class PhotoActivity extends Activity {


    private int[] images;   //图片ID数组

    private PointF startPoint = new PointF();

    @SuppressLint("ClickableViewAccessibility")
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);

        ViewPager viewPager = findViewById(R.id.viewPager);

        images = new int[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4,
                R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9,
                R.drawable.p10, R.drawable.p11, R.drawable.p12, R.drawable.p13, R.drawable.p14,
                R.drawable.p15, R.drawable.p16, R.drawable.p17, R.drawable.p18, R.drawable.p19,
                R.drawable.p20, R.drawable.p21, R.drawable.p22, R.drawable.p23, R.drawable.p24,
                R.drawable.p25, R.drawable.p26, R.drawable.p27, R.drawable.p28, R.drawable.p29,
                R.drawable.p30, R.drawable.p31, R.drawable.p32, R.drawable.p33, R.drawable.p34,
                R.drawable.p35, R.drawable.p36, R.drawable.p37, R.drawable.p38};

        //-----初始化PagerAdapter------

        PagerAdapter adapter = new PagerAdapter() {

            public int getCount() {
                return images.length;
            }

            public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
                return arg0 == arg1;
            }

            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                View v = (View) object;
                ImageView iv = v.findViewById(R.id.viewPager);
                releaseImageViewResourse(iv);
                container.removeView(v);
            }

            private void releaseImageViewResourse(ImageView iv) {
                if (iv == null)
                    return;
                Drawable drawable = iv.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                }
                //希望做一次垃圾回收
                System.gc();
            }

            //设置ViewPager指定位置要显示的view
            @NonNull
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView im = new ImageView(PhotoActivity.this);
                im.setImageResource(images[position]);
                container.addView(im);
                return im;
            }
        };

        viewPager.setAdapter(adapter);
        Intent intent = getIntent();
        int start = intent.getIntExtra("position", 0);
        viewPager.setCurrentItem(start);    //设置ViePager起始页



        viewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPoint.set(event.getX(), event.getY());
                        break;

//                    case MotionEvent.ACTION_MOVE:
//                        break;

                    case MotionEvent.ACTION_UP:
                        if (Math.abs((startPoint.x - event.getX())) <= 1 && Math.abs((startPoint.y - event.getY())) <= 1)
                            finish();
                        break;
                }
                return false;
            }
        });

    }

}

