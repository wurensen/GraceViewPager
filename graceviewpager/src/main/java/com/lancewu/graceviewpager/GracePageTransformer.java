package com.lancewu.graceviewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lancewu.graceviewpager.util.LogUtil;

/**
 * Created by wrs on 2018/8/7.
 * <br/>
 * ViewPager动画抽象类。主要负责修复ViewPager.PageTransformer使用中出现的问题：
 * <ul>
 * <li>使用'paddingLeft' + 'clipPadding=false'后（实现显示多页），position偏移问题</li>
 * <li>刷新过程中，position错误问题</li>
 * <li>调用跳转到某个未创建的item后，但view未被测量布局时，position错误问题</li>
 * </ul>
 * 使得使用者能专注于页面切换动画的具体实现，而无需关注position的矫正问题。
 */
public abstract class GracePageTransformer implements ViewPager.PageTransformer {

    // 拓展的PagerAdapter
    private GracePagerAdapter mPagerAdapter;

    public GracePageTransformer(@NonNull GracePagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        ViewPager viewPager;
        if (page.getParent() instanceof ViewPager) {
            viewPager = ((ViewPager) page.getParent());
        } else {
            return;
        }
        /*
         * 问题1：调用刷新后，如果数据有位置更新会立马调用该方法，此时还未调用layout方法，所以当前page的left位置不一定
         * 代表最终layout位置；
         * 解决方案：判断该方法执行时是否处于刷新中，如果处于刷新，根据page真正的数据位置得出对应的position
         * 问题2：page还未被测量布局时，position值错误
         * 解决方案：根据page真正的数据位置得出对应的position
         * 问题3：使用'paddingLeft' + 'clipPadding=false'后（实现显示多页），源码返回的position在计算时未考虑paddingLeft
         * 解决方案：加入paddingLeft，重新计算position
         */

        // 数据刷新、填充新page的时候，要判断page真正的位置才能得到正确的position
        boolean dataSetChanging = mPagerAdapter.isDataSetChanging();
        boolean requirePagePosition = dataSetChanging || viewPager.isLayoutRequested();
        if (requirePagePosition) {
            int currentItem = viewPager.getCurrentItem();
            int pageViewIndex = mPagerAdapter.getPageViewPosition(page);
            LogUtil.d("transformPage() isDataSetChanging: currentItem = ["
                    + currentItem + "], pageViewIndex = [" + pageViewIndex + "]");
            if (currentItem == pageViewIndex) {
                position = 0;
            } else {
                position = pageViewIndex - currentItem;
            }
        } else {
            position = getPositionConsiderPadding(viewPager, page);
        }
        LogUtil.d("transformPage() called with: page = [" + page + "], position = [" + position + "]");
        transformPageWithCorrectPosition(page, position);
    }

    private float getPositionConsiderPadding(ViewPager viewPager, View page) {
        // padding影响了position，自己生成position
        int clientWidth = viewPager.getMeasuredWidth() - viewPager.getPaddingLeft() - viewPager.getPaddingRight();
        return (float) (page.getLeft() - viewPager.getScrollX() - viewPager.getPaddingLeft()) / clientWidth;
    }

    /**
     * 对页面做动画
     *
     * @param page     页面
     * @param position 修正后的position
     * @see android.support.v4.view.ViewPager.PageTransformer#transformPage(View, float)
     */
    public abstract void transformPageWithCorrectPosition(@NonNull View page, float position);
}
