package com.bear.customerview.overscroll;

import android.view.View;

/**
 * @author amit
 */
public interface IOverScrollDecor {
    View getView();
    void setOverScrollStateListener(IOverScrollStateListener listener);
    void setOverScrollUpdateListener(IOverScrollUpdateListener listener);
}
