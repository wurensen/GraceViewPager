# GraceViewPager

## 背景

`ViewPager`在实际项目使用过程中，出现数据刷新使用姿势不正确、动画偏移、动态修改`width、paddingLeft、paddingRight、pageMargin`导致当前page定位异常。于是通过源码分析，解决这些问题，并对使用场景进行了封装。

可以通过下面的文章了解`ViewPager`为何会出现这些问题以及对应的解决方案：

1. [ViewPager源码分析](https://blog.csdn.net/wurensen/article/details/81390641)
2. [解决ViewPager动画异常](https://blog.csdn.net/wurensen/article/details/81544776)

## 简介



## 主要功能

1.支持`ViewPager`按需添加、删除视图，以及局部刷新；
2.修复多场景下`ViewPager.PageTransformer`返回的`position`错误，让开发者专注于动画实现；
3.修复`ViewPager`的`width、paddingLeft、paddingRight、pageMargin`动态改变导致当前page定位异常的问题；
4.直接使用自定义`GraceViewPager`，可快速实现一屏显示多Page的功能。

## 使用方式

