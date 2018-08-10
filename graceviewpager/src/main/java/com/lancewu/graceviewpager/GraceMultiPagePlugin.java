package com.lancewu.graceviewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

/**
 * Created by wrs on 2018/8/10.
 * ViewPager一屏显示多页插件。<br/>
 * 快速实现一屏多页功能，原理是使用
 */
public class GraceMultiPagePlugin {
    // VP
    private ViewPager mViewPager;
    // Page比例
    private float mPageHeightWidthRatio;
    // 水平最小间距
    private int mPageHorizontalMinMargin;
    // 垂直最小间距
    private int mPageVerticalMinMargin;

    private GraceMultiPagePlugin(ViewPager viewPager, float pageHeightWidthRatio,
                                 int pageHorizontalMinMargin, int pageVerticalMinMargin) {
        mViewPager = viewPager;
        mPageHeightWidthRatio = pageHeightWidthRatio;
        mPageHorizontalMinMargin = pageHorizontalMinMargin;
        mPageVerticalMinMargin = pageVerticalMinMargin;
    }

    /**
     * 确定Page的尺寸
     *
     * @param width  ViewPager宽度
     * @param height ViewPager高度
     */
    public void determinePageSize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
        int paddingHorizontal = mPageHorizontalMinMargin;
        int paddingVertical = mPageVerticalMinMargin;
        int availableWidth = width - 2 * paddingHorizontal;
        int availableHeight = height - 2 * paddingVertical;
        // 根据比例，计算合适的padding
        if (mPageHeightWidthRatio > 0 && availableHeight > 0 && availableWidth > 0) {
            float ratio = (float) availableHeight / availableWidth;
            // page比例大于剩余空间比例，水平空间充裕
            if (mPageHeightWidthRatio >= ratio) {
                int pageWidth = (int) (availableHeight / mPageHeightWidthRatio);
                paddingHorizontal += (availableWidth - pageWidth) * 0.5f;
            } else {
                int pageHeight = (int) (availableWidth * mPageHeightWidthRatio);
                paddingVertical += (availableHeight - pageHeight) * 0.5f;
            }
        }
        if (mViewPager.getPaddingLeft() == paddingHorizontal
                && mViewPager.getPaddingRight() == paddingHorizontal
                && mViewPager.getPaddingTop() == paddingVertical
                && mViewPager.getPaddingBottom() == paddingVertical) {
            return;
        }
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }

    /**
     * MultiPagePlugin实例Builder
     */
    public static class Builder {
        // VP
        private ViewPager mViewPager;
        // Page比例
        private float mPageHeightWidthRatio;
        // 水平最小间距
        private int mPageHorizontalMinMargin;
        // 垂直最小间距
        private int mPageVerticalMinMargin;

        public Builder(@NonNull ViewPager viewPager) {
            mViewPager = viewPager;
        }

        public Builder pageHeightWidthRatio(float pageHeightWidthRatio) {
            mPageHeightWidthRatio = pageHeightWidthRatio;
            return this;
        }

        public Builder pageHorizontalMinMargin(int pageHorizontalMinMargin) {
            mPageHorizontalMinMargin = pageHorizontalMinMargin;
            return this;
        }

        public Builder pageVerticalMinMargin(int pageVerticalMinMargin) {
            mPageVerticalMinMargin = pageVerticalMinMargin;
            return this;
        }

        public GraceMultiPagePlugin build() {
            return new GraceMultiPagePlugin(mViewPager, mPageHeightWidthRatio, mPageHorizontalMinMargin, mPageVerticalMinMargin);
        }
    }
}
