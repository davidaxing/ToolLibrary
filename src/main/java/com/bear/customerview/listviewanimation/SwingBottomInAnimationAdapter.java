package com.bear.customerview.listviewanimation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 创建人：quzongyang
 * <p/>
 * 创建时间：2016/4/21 0021 19:32
 * <p/>
 * 类描述：
 * <p/>
 * 版本：V1.0
 */

/**
 * An implementation of the AnimationAdapter class which applies a
 * swing-in-from-bottom-animation to views.
 */
public class SwingBottomInAnimationAdapter extends SingleAnimationAdapter {

    private final long mAnimationDelayMillis;
    private final long mAnimationDurationMillis;
    private BaseAdapter baseAdapter;

    public SwingBottomInAnimationAdapter(BaseAdapter baseAdapter) {
        this(baseAdapter, DEFAULTANIMATIONDELAYMILLIS, DEFAULTANIMATIONDURATIONMILLIS);
        this.baseAdapter = baseAdapter;
    }

    public BaseAdapter getAdapter(){
        return this.baseAdapter;
    }

    public SwingBottomInAnimationAdapter(BaseAdapter baseAdapter, long animationDelayMillis) {
        this(baseAdapter, animationDelayMillis, DEFAULTANIMATIONDURATIONMILLIS);
    }

    public SwingBottomInAnimationAdapter(BaseAdapter baseAdapter, long animationDelayMillis, long animationDurationMillis) {
        super(baseAdapter);
        mAnimationDelayMillis = animationDelayMillis;
        mAnimationDurationMillis = animationDurationMillis;
    }

    @Override
    protected long getAnimationDelayMillis() {
        return mAnimationDelayMillis;
    }

    @Override
    protected long getAnimationDurationMillis() {
        return mAnimationDurationMillis;
    }

    @Override
    protected Animator getAnimator(ViewGroup parent, View view) {
        return ObjectAnimator.ofFloat(view, "translationY", 500, 0);
    }

}
