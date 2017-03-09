package com.bear.customerview.overscroll;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.HorizontalScrollView;

import com.bear.customerview.overscroll.adapter.HorizontalScrollViewOverScrollDecorAdapter;
import com.bear.customerview.overscroll.adapter.RecyclerViewOverScrollDecorAdapter;


/**
 * @author amit
 */
public class OverScrollDecoratorHelper {

    public static final int ORIENTATION_VERTICAL = 0;
    public static final int ORIENTATION_HORIZONTAL = 1;

    /**
     * Set up the over-scroll effect over a specified {@link RecyclerView} view.
     * <br/>Only recycler-views using <b>native</b> Android layout managers (i.e. {@link LinearLayoutManager},
     * {@link GridLayoutManager} and {@link StaggeredGridLayoutManager}) are currently supported
     * by this convenience method.
     *
     *
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */


    public static IOverScrollDecor setUpOverScroll(HorizontalScrollView scrollView) {
        return new HorizontalOverScrollBounceEffectDecorator(new HorizontalScrollViewOverScrollDecorAdapter(scrollView),HorizontalOverScrollBounceEffectDecorator.DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD,HorizontalOverScrollBounceEffectDecorator.DEFAULT_TOUCH_DRAG_MOVE_RATIO_BCK,5f);
    }

    public static IOverScrollDecor setUpOverScroll(RecyclerView recyclerView, int orientation) {
        switch (orientation) {
            case ORIENTATION_HORIZONTAL:
                return new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
            case ORIENTATION_VERTICAL:
                return new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(recyclerView));
            default:
                throw new IllegalArgumentException("orientation");
        }
    }

    /**
     * Set up the over-scroll over a generic view, assumed to always be over-scroll ready (e.g.
     * a plain text field, image view).
     *
     * @param view The view.
     * @param orientation One of {@link #ORIENTATION_HORIZONTAL} or {@link #ORIENTATION_VERTICAL}.
     *
     * @return The over-scroll effect 'decorator', enabling further effect configuration.
     */
//    public static IOverScrollDecor setUpStaticOverScroll(View view, int orientation) {
//        switch (orientation) {
//            case ORIENTATION_HORIZONTAL:
//                return new HorizontalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));
//
//            case ORIENTATION_VERTICAL:
//                return new VerticalOverScrollBounceEffectDecorator(new StaticOverScrollDecorAdapter(view));
//
//            default:
//                throw new IllegalArgumentException("orientation");
//        }
//    }
}
