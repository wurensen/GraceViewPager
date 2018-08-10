package com.lancewu.graceviewpager.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lancewu.graceviewpager.GracePageTransformer;
import com.lancewu.graceviewpager.GracePagerAdapter;
import com.lancewu.graceviewpager.GraceViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GraceViewPagerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GraceViewPager";

    private GraceViewPager mViewPager;
    private List<String> mData = new ArrayList<>();
    private Adapter mAdapter;
    private View mPlaceholderView;

    public static void start(Activity act) {
        Intent starter = new Intent(act, GraceViewPagerActivity.class);
        act.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grace_view_pager);

        buildTestData();

        mViewPager = findViewById(R.id.vp);
        mAdapter = new Adapter(mData);
        mViewPager.setGraceAdapter(mAdapter);
        mViewPager.setGracePageTransformer(false, new Transformer(mAdapter));
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

        mPlaceholderView = findViewById(R.id.placeholder);
        findViewById(R.id.reverse_btn).setOnClickListener(this);
        findViewById(R.id.add_btn).setOnClickListener(this);
        findViewById(R.id.delete_btn).setOnClickListener(this);
        findViewById(R.id.change_padding_btn).setOnClickListener(this);
        findViewById(R.id.change_margin_btn).setOnClickListener(this);
        findViewById(R.id.locate_btn).setOnClickListener(this);
        findViewById(R.id.smooth_btn).setOnClickListener(this);
    }

    private void buildTestData() {
        for (int i = 0; i < 10; i++) {
            mData.add("item:" + i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reverse_btn:
                Collections.reverse(mData);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.add_btn:
                mData.add(mViewPager.getCurrentItem(), "add item:" + mData.size());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.delete_btn:
                if (mData.size() > 0) {
                    mData.remove(mViewPager.getCurrentItem());
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.change_padding_btn:
                boolean visible = mPlaceholderView.getVisibility() == View.VISIBLE;
                mPlaceholderView.setVisibility(visible ? View.GONE : View.VISIBLE);
                break;
            case R.id.change_margin_btn:
                int pageMargin = mViewPager.getPageMargin();
                if (pageMargin == 0) {
                    pageMargin = dip2px(10);
                } else {
                    pageMargin = 0;
                }
//                mViewPager.setPageMargin(pageMargin);
                mViewPager.setGracePageMargin(pageMargin);
                break;
            case R.id.locate_btn:
            case R.id.smooth_btn:
                int nextInt = new Random().nextInt(mData.size());
                mViewPager.setCurrentItem(nextInt, v.getId() == R.id.smooth_btn);
                break;
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private class Transformer extends GracePageTransformer {

        private static final float SCALE = 0.9f;

        Transformer(@NonNull GracePagerAdapter pagerAdapter) {
            super(pagerAdapter);
        }

        @Override
        public void transformPageWithCorrectPosition(@NonNull View page, float position) {
            if (position >= -1 && position <= 1) {
                // [-1,1]，中间以及相邻的页面，一般相邻的才会用于计算动画
                float scale = SCALE + (1 - SCALE) * (1 - Math.abs(position));
                page.setScaleX(scale);
                page.setScaleY(scale);
            } else {
                // [-Infinity,-1)、(1,+Infinity]，超出相邻的范围
                page.setScaleX(SCALE);
                page.setScaleY(SCALE);
            }
        }

    }

    private class Adapter extends GracePagerAdapter<String> {

        Adapter(@NonNull List<String> items) {
            super(items);
        }

        @NonNull
        @Override
        protected View instantiateItemView(@NonNull ViewGroup container, String item, int position) {
            return getLayoutInflater().inflate(R.layout.page_item, container, false);
        }

        @Override
        protected void bindItemView(@NonNull View itemView, String item, int position, boolean first) {
            TextView tv = itemView.findViewById(R.id.tv);
            tv.setText(item);
        }

    }

}
