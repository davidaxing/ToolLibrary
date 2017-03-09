package com.bear.customerview.listviewanimation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nineoldandroids.animation.Animator;

/**
 * 创建人：quzongyang
 * <p/>
 * 创建时间：2016/4/21 0021 19:31
 * <p/>
 * 类描述：
 * <p/>
 * 版本：V1.0
 */

/**
 * An implementation of AnimationAdapter which applies a single Animator to
 * views.
 */
public abstract class SingleAnimationAdapter extends AnimationAdapter {

    public SingleAnimationAdapter(BaseAdapter baseAdapter) {
        super(baseAdapter);
    }

    @Override
    public Animator[] getAnimators(ViewGroup parent, View view) {
        Animator animator = getAnimator(parent, view);
        return new Animator[] { animator };
    }

    /**
     * Get the Animator to apply to the view.
     *
     * @param parent
     *            the ViewGroup which is the parent of the view.
     * @param view
     *            the view that will be animated, as retrieved by getView().
     */
    protected abstract Animator getAnimator(ViewGroup parent, View view);

}
