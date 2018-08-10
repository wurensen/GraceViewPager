package com.lancewu.graceviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wrs on 2018/8/9.<br/>
 * 自定义ViewPager，在ViewPager原有基础上，提供拓展功能和修复问题。<br/>
 * 拓展功能：
 * <ol>
 * <li>提供自定义属性，快速实现一屏多页的功能；</li>
 * <li>支持width、childWidth变化后，滚动位置修正；</li>
 * </ol>
 * 修复：
 * <ol>
 * <li>PagerTransformer在设置paddingLeft和paddingRight时返回的position偏移问题；/li>
 * <li>PagerTransformer在数据刷新时返回的position错误问题；</li>
 * <li>ViewPager动态修改width、paddingLeft、paddingRight后滚动位置偏差问题；</li>
 * <li>ViewPager动态修改pageMargin后滚动位置偏差问题；</li>
 * </ol>
 */
public class GraceViewPager extends ViewPager {

    // 处理尺寸变化
    private GraceViewPagerSupport.SizeChangeHandler mSizeChangeHandler;
    // Page比例
    private float mPageHeightWidthRatio;
    // 水平最小间距
    private int mPageHorizontalMinMargin;
    // 垂直最小间距
    private int mPageVerticalMinMargin;
    private GraceMultiPagePlugin mMultiPagePlugin;

    public GraceViewPager(@NonNull Context context) {
        this(context, null);
    }

    public GraceViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        mSizeChangeHandler = new GraceViewPagerSupport.SizeChangeHandler();
        mMultiPagePlugin = new GraceMultiPagePlugin.Builder(this)
                .pageHeightWidthRatio(mPageHeightWidthRatio)
                .pageHorizontalMinMargin(mPageHorizontalMinMargin)
                .pageVerticalMinMargin(mPageVerticalMinMargin)
                .build();
    }

    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GraceViewPager);
        mPageHeightWidthRatio = typedArray.getFloat(R.styleable.GraceViewPager_gvp_pageHeightWidthRatio, 0);
        if (mPageHeightWidthRatio < 0) {
            mPageHeightWidthRatio = 0;
        }
        mPageHorizontalMinMargin = typedArray.getDimensionPixelSize(R.styleable.GraceViewPager_gvp_pageHorizontalMinMargin, 0);
        mPageVerticalMinMargin = typedArray.getDimensionPixelSize(R.styleable.GraceViewPager_gvp_pageVerticalMinMargin, 0);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mMultiPagePlugin.determinePageSize(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = r - l;
        // 响应宽度变化以便修正滚动位置
        mSizeChangeHandler.onSizeChange(this, width);
    }

    /**
     * 源码有bug会导致滚动位置偏移，请调用{@link #setGracePageMargin(int)}
     *
     * @param marginPixels Distance between adjacent pages in pixels
     */
    @Deprecated
    @Override
    public void setPageMargin(int marginPixels) {
        super.setPageMargin(marginPixels);
    }

    /**
     * Set the margin between pages.（修正滚动位置）
     *
     * @param marginPixels Distance between adjacent pages in pixels
     * @see #getPageMargin()
     * @see #setPageMarginDrawable(Drawable)
     * @see #setPageMarginDrawable(int)
     */
    public void setGracePageMargin(int marginPixels) {
        GraceViewPagerSupport.setPageMargin(this, marginPixels);
    }

    /**
     * PageTransformer源码返回的position未考虑padding，以及刷新后position错误，导致动画有问题，请调用
     * {@link #setGracePageTransformer(boolean, GracePageTransformer)}
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     */
    @Deprecated
    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable PageTransformer transformer) {
        super.setPageTransformer(reverseDrawingOrder, transformer);
    }

    /**
     * PageTransformer源码返回的position未考虑padding，以及刷新后position错误，导致动画有问题，请调用
     * {@link #setGracePageTransformer(boolean, GracePageTransformer, int)}
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @param pageLayerType       View layer type that should be used for ViewPager pages. It should be
     *                            either {@link View#LAYER_TYPE_HARDWARE},
     *                            {@link View#LAYER_TYPE_SOFTWARE}, or
     *                            {@link View#LAYER_TYPE_NONE}.
     */
    @Deprecated
    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, @Nullable PageTransformer transformer, int pageLayerType) {
        super.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType);
    }

    /**
     * 修正了返回position偏移以及数据刷新时错误问题
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     */
    public void setGracePageTransformer(boolean reverseDrawingOrder, @Nullable GracePageTransformer transformer) {
        super.setPageTransformer(reverseDrawingOrder, transformer);
    }

    /**
     * 修正了返回position偏移以及数据刷新时错误问题
     *
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     *                            to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @param pageLayerType       View layer type that should be used for ViewPager pages. It should be
     *                            either {@link View#LAYER_TYPE_HARDWARE},
     *                            {@link View#LAYER_TYPE_SOFTWARE}, or
     *                            {@link View#LAYER_TYPE_NONE}.
     */
    public void setGracePageTransformer(boolean reverseDrawingOrder, @Nullable GracePageTransformer transformer, int pageLayerType) {
        super.setPageTransformer(reverseDrawingOrder, transformer, pageLayerType);
    }

    /**
     * adapter传入{@link GracePagerAdapter}实现；或者直接使用
     * {@link #setGraceAdapter(GracePagerAdapter)}以便能够按需更新数据；
     *
     * @param adapter Adapter to use
     */
    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * Set a PagerAdapter that will supply views for this pager as needed.
     * （{@link GracePagerAdapter}）
     *
     * @param adapter Adapter to use
     */
    public void setGraceAdapter(@NonNull GracePagerAdapter adapter) {
        setAdapter(adapter);
    }
}
