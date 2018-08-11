package com.lancewu.graceviewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

/**
 * Created by wrs on 2018/8/10.
 * ViewPager一屏显示多页插件。<br/>
 * 快速实现一屏多页功能，原理是使用ViewPager的padding+clipPadding=false来使用，所以外部不需要再调用修改padding属性
 * 的方法
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
     * 获取当前page比例
     *
     * @return page比例
     */
    public float getPageHeightWidthRatio() {
        return mPageHeightWidthRatio;
    }

    /**
     * 设置当前page比例
     *
     * @param pageHeightWidthRatio page比例，如果小于0会被当做0处理
     */
    public void setPageHeightWidthRatio(float pageHeightWidthRatio) {
        if (pageHeightWidthRatio < 0) {
            pageHeightWidthRatio = 0;
        }
        if (mPageHeightWidthRatio == pageHeightWidthRatio) {
            return;
        }
        mPageHeightWidthRatio = pageHeightWidthRatio;
        mViewPager.requestLayout();
    }

    /**
     * 获取page水平最小间距
     *
     * @return 水平最小间距
     */
    public int getPageHorizontalMinMargin() {
        return mPageHorizontalMinMargin;
    }

    /**
     * 设置page水平最小间距
     *
     * @param pageHorizontalMinMargin 水平最小间距
     */
    public void setPageHorizontalMinMargin(int pageHorizontalMinMargin) {
        if (mPageHorizontalMinMargin == pageHorizontalMinMargin) {
            return;
        }
        mPageHorizontalMinMargin = pageHorizontalMinMargin;
        mViewPager.requestLayout();
    }

    /**
     * 获取page垂直最小间距
     *
     * @return 垂直最小间距
     */
    public int getPageVerticalMinMargin() {
        return mPageVerticalMinMargin;
    }

    /**
     * 设置page垂直最小间距
     *
     * @param pageVerticalMinMargin 垂直最小间距
     */
    public void setPageVerticalMinMargin(int pageVerticalMinMargin) {
        if (mPageVerticalMinMargin == pageVerticalMinMargin) {
            return;
        }
        mPageVerticalMinMargin = pageVerticalMinMargin;
        mViewPager.requestLayout();
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

        /**
         * 设置Page比例，<=0则比例无效
         *
         * @param pageHeightWidthRatio 比例，height:width
         * @return Builder
         */
        public Builder pageHeightWidthRatio(float pageHeightWidthRatio) {
            mPageHeightWidthRatio = pageHeightWidthRatio;
            return this;
        }

        /**
         * page水平最小边距，指显示page与ViewPager间的最小间距（因为按比例设定，高度可能不足以容纳，此时就要缩小宽度，
         * 即增加间距）
         *
         * @param pageHorizontalMinMargin page水平最小边距
         * @return Builder
         */
        public Builder pageHorizontalMinMargin(int pageHorizontalMinMargin) {
            mPageHorizontalMinMargin = pageHorizontalMinMargin;
            return this;
        }

        /**
         * page垂直最小边距
         *
         * @param pageVerticalMinMargin page垂直最小边距
         * @return Builder
         */
        public Builder pageVerticalMinMargin(int pageVerticalMinMargin) {
            mPageVerticalMinMargin = pageVerticalMinMargin;
            return this;
        }

        /**
         * 构建实例
         *
         * @return GraceMultiPagePlugin
         */
        public GraceMultiPagePlugin build() {
            return new GraceMultiPagePlugin(mViewPager, mPageHeightWidthRatio, mPageHorizontalMinMargin, mPageVerticalMinMargin);
        }
    }
}
