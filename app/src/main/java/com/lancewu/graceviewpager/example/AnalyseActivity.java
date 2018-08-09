package com.lancewu.graceviewpager.example;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnalyseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GraceViewPager";

    private ViewPager mViewPager;
    private List<String> mData = new ArrayList<>();

    public static void start(Activity act) {
        Intent starter = new Intent(act, AnalyseActivity.class);
        act.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);

        buildTestData();

        mViewPager = findViewById(R.id.vp);
        mViewPager.setAdapter(new Adapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected() called with: position = [" + position + "]");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        findViewById(R.id.set_btn).setOnClickListener(this);
        findViewById(R.id.smooth_btn).setOnClickListener(this);
    }

    private void buildTestData() {
        for (int i = 0; i < 10; i++) {
            mData.add("item:" + i);
        }
    }

    @Override
    public void onClick(View v) {
        int nextInt = new Random().nextInt(mData.size());
        switch (v.getId()) {
            case R.id.set_btn:
                mViewPager.setCurrentItem(nextInt, false);
                break;
            case R.id.smooth_btn:
                mViewPager.setCurrentItem(nextInt, true);
                break;
        }
    }

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem() called with: position = [" + position + "]");
            View itemView = getLayoutInflater().inflate(R.layout.page_item, container, false);
            container.addView(itemView);
            bindData(position, itemView);
            return itemView;
        }

        private void bindData(int position, View itemView) {
            TextView tv = itemView.findViewById(R.id.tv);
            tv.setText(mData.get(position));
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            Log.d(TAG, "destroyItem() called with: position = [" + position + "]");
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }
    }
}
