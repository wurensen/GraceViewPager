package com.lancewu.graceviewpager.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lancewu.graceviewpager.GracePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DataSetChangedActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GraceViewPager";

    private ViewPager mViewPager;
    private List<String> mData = new ArrayList<>();
    private PagerAdapter mAdapter;

    public static void start(Activity act) {
        Intent starter = new Intent(act, DataSetChangedActivity.class);
        act.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_set_changed);

        buildTestData();

        mViewPager = findViewById(R.id.vp);
//        mAdapter = new Adapter();
        mAdapter = new GraceAdapter(mData);
        mViewPager.setAdapter(mAdapter);
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

        findViewById(R.id.reset_btn).setOnClickListener(this);
        findViewById(R.id.swap_btn).setOnClickListener(this);
        findViewById(R.id.add_btn).setOnClickListener(this);
        findViewById(R.id.add_pre_btn).setOnClickListener(this);
        findViewById(R.id.delete_btn).setOnClickListener(this);
        findViewById(R.id.delete_pre_btn).setOnClickListener(this);
    }

    private void buildTestData() {
        mData.clear();
        for (int i = 0; i < 10; i++) {
            mData.add("item:" + i);
        }
    }

    @Override
    public void onClick(View v) {
        int curIndex = mViewPager.getCurrentItem();
        int preItemIndex = curIndex > 0 ? curIndex - 1 : curIndex;
        switch (v.getId()) {
            case R.id.reset_btn:
                buildTestData();
                break;
            case R.id.swap_btn:
                // 交换当前左右两个item
                int preIndex = curIndex - 1;
                int nextIndex = curIndex + 1;
                String pre = preIndex < 0 ? null : mData.get(preIndex);
                String next = nextIndex >= mData.size() ? null : mData.get(nextIndex);
                if (pre == null) {
                    mData.remove(nextIndex);
                    mData.add(0, next);
                } else if (next == null) {
                    mData.remove(preIndex);
                    mData.add(pre);
                } else {
                    mData.remove(preIndex);
                    mData.add(preIndex, next);
                    mData.remove(nextIndex);
                    mData.add(nextIndex, pre);
                }
                break;
            case R.id.add_btn:
                mData.add(curIndex, "item:" + mData.size());
                break;
            case R.id.add_pre_btn:
                mData.add(preItemIndex, "item:" + mData.size());
                break;
            case R.id.delete_btn:
                if (curIndex < mData.size()) {
                    mData.remove(curIndex);
                }
            case R.id.delete_pre_btn:
                mData.remove(preItemIndex);
                break;
        }
        mAdapter.notifyDataSetChanged();
    }

    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            ViewHolder viewHolder = (ViewHolder) object;
            return viewHolder.mItemView == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            String item = mData.get(position);
            Log.d(TAG, "instantiateItem() called with: position = [" + position + "],item=" + item);
            View itemView = getLayoutInflater().inflate(R.layout.page_item, container, false);
            container.addView(itemView);
            bindData(item, itemView);
            return new ViewHolder<>(itemView, item, position);
        }

        private void bindData(String item, View itemView) {
            TextView tv = itemView.findViewById(R.id.tv);
            tv.setText(item);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ViewHolder viewHolder = (ViewHolder) object;
            container.removeView(viewHolder.mItemView);
            Log.d(TAG, "destroyItem() called with: position = [" + position + "],item=" + viewHolder.mItem);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            ViewHolder<String> viewHolder = (ViewHolder<String>) object;
            String item = viewHolder.mItem;
            // 当前内存中页面数据是否还存在于刷新后的数据集合中
            int newIndex = mData.indexOf(item);
            int itemPosition = newIndex == -1 ? POSITION_NONE : newIndex;
            Log.d(TAG, "getItemPosition: item=" + item + ",itemPosition=" + itemPosition);
            int oldPosition = viewHolder.mPosition;
            // 数据索引发生改变
            if (itemPosition >= 0) {
                if (oldPosition != itemPosition) {
                    Log.d(TAG, "getItemPosition: item=" + item + " position changed:"
                            + oldPosition + "->" + itemPosition);
                    // 更新新的索引位置
                    viewHolder.mPosition = itemPosition;
                }
                // 重新绑定数据以便于刷新视图内容
                bindData(item, viewHolder.mItemView);
            }
            return itemPosition;
        }
    }

    private class ViewHolder<Item> {
        private View mItemView;
        private Item mItem;
        private int mPosition;

        ViewHolder(View itemView, Item item, int position) {
            mItemView = itemView;
            mItem = item;
            mPosition = position;
        }
    }

    // 继承封装的适配器
    private class GraceAdapter extends GracePagerAdapter<String> {

        GraceAdapter(@NonNull List<String> items) {
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
