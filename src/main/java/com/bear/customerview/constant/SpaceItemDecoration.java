package com.bear.customerview.constant;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/*
 *
 *
 * 版 权 :@Copyright 北京优多鲜道科技有限公司版权所有
 *
 * 作 者 :liuyuxing
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/7/13 下午19:32
 *
 * 描 述 :设置每个item间隔
 *
 * 修订日期 :
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildPosition(view) != 0)
            outRect.top = space;
    }
}
