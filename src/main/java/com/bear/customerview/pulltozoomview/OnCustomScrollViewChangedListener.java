package com.bear.customerview.pulltozoomview;

import android.widget.AbsListView;

/**
 * Created by PC on 2016/7/13.
 */
public interface OnCustomScrollViewChangedListener {
    void onScreenStateChanged(int screenState);
    void onScrollChanged(int l, int t, int oldl, int oldt);
}
