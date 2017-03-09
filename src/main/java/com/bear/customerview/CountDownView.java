package com.bear.customerview;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.TextureView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/*
 *
 *
 * 版 权 :@Copyright 北京优多鲜道科技有限公司版权所有
 *
 * 作 者 :desperado
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/6/16  17:08
 *
 * 描 述 :倒计时控件
 *
 * 修订日期 :
 */

public class CountDownView extends TextureView implements TextureView.SurfaceTextureListener {

    private static final String LESS_THAN_TEN_FORMAT = "%02d";
    private static final String COLON = ":";

    private static final int DEFAULT_COLOR_BACKGROUND = 0xff000000;
    private static final int DEFAULT_COLOR_COLON = 0xff000000;
    private static final int DEFAULT_COLOR_TIME = 0xffffffff;
    private DisplayMetrics mMetrics;

    private static final int COUNT_DOWN_INTERVAL = 1000;

    private static final long ONE_HOUR = 1000 * 60 * 60L;
    private static final long ONE_MINUTE = 1000 * 60L;
    private static final long ONE_SECOND = 1000L;

    private volatile long mMillisInFuture = 0L;

    /**************
     * Default dp *
     **************/
    private static final float DEFAULT_BACKGROUND_PAINT_WIDTH = 0.01f;
    private static final float DEFAULT_COLON_PAINT_WIDTH = 0.66f;
    private static final float DEFAULT_TIME_PAINT_WIDTH = 0.77f;
    private static final float DEFAULT_ROUND_RECT_RADIUS = 2.66f;
    private static final float DEFAULT_RECT_WIDTH = 18.0f;
    private static final float DEFAULT_RECT_HEIGHT = 17.0f;
    private static final float DEFAULT_RECT_SPACING = 6.0f;
    private static final float DEFAULT_TIME_TEXT_SIZE = 13.0f;
    private static final float DEFAULT_COLON_TEXT_SIZE = 13.0f;

    // 66dp
    private static final float DEFAULT_VIEW_WIDTH = DEFAULT_RECT_WIDTH * 3 +
            DEFAULT_RECT_SPACING * 2;
    // 17dp
    private static final float DEFAULT_VIEW_HEIGHT = DEFAULT_RECT_HEIGHT;

    /**************
     * Default px *
     **************/
    private float rectWidth;
    private float rectHeight;
    private float rectSpacing;
    private float rectRadius;

    private float paddingLeft;
    private float paddingTop;
    private float paddingRight;
    private float paddingBottom;

    private float firstTranslateX;
    private float firstTranslateColonX;
    private float secondTranslateX;
    private float secondTranslateColonX;

    private int timeHour;
    private int timeMinute;
    private int timeSecond;

    private int viewWidth;
    private int viewHeight;
    private float defaultWrapContentWidth;
    private float defaultWrapContentHeight;

    private EasyThread mThread;

    private final Locale locale = Locale.getDefault();
    private final Calendar mCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));

    private Paint colonPaint;

    private Paint timePaint;
    private float timePaintBaseLine;

    private Paint backgroundPaint;
    private RectF backgroundRectF;

    private volatile long lastRecordTime = 0;
    private boolean runningState = false;

    private EasyCountDownListener mEasyCountDownListener;


    public CountDownView(Context context) {
        super(context);
        this.init(context, null);
    }


    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }


    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.mMetrics = this.getResources().getDisplayMetrics();
        this.defaultWrapContentWidth = this.dp2px(DEFAULT_VIEW_WIDTH);
        this.defaultWrapContentHeight = this.dp2px(DEFAULT_VIEW_HEIGHT);

        this.setSurfaceTextureListener(this);
        this.setOpaque(false);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        this.timeHour = a.getInteger(R.styleable.CountDownView_easyCountHour, 0);
        this.timeMinute = a.getInteger(R.styleable.CountDownView_easyCountMinute, 0);
        this.timeSecond = a.getInteger(R.styleable.CountDownView_easyCountSecond, 0);

        this.backgroundPaint = new Paint();
        this.backgroundPaint.setAntiAlias(true);
        this.backgroundPaint.setColor(
                a.getColor(R.styleable.CountDownView_easyCountBackgroundColor,
                        DEFAULT_COLOR_BACKGROUND));
        this.backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.backgroundPaint.setStrokeWidth(this.dp2px(DEFAULT_BACKGROUND_PAINT_WIDTH));
        this.backgroundPaint.setTextAlign(Paint.Align.CENTER);
        this.backgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        this.colonPaint = new Paint();
        this.colonPaint.setAntiAlias(true);
        this.colonPaint.setColor(
                a.getColor(R.styleable.CountDownView_easyCountColonColor,
                        DEFAULT_COLOR_COLON));
        this.colonPaint.setTextSize(
                a.getDimension(R.styleable.CountDownView_easyCountColonSize,
                        this.sp2px(DEFAULT_TIME_TEXT_SIZE)));
        this.colonPaint.setStrokeWidth(this.dp2px(DEFAULT_COLON_PAINT_WIDTH));
        this.colonPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.colonPaint.setTextAlign(Paint.Align.CENTER);
        this.colonPaint.setStrokeCap(Paint.Cap.ROUND);

        this.rectWidth = a.getDimension(R.styleable.CountDownView_easyCountRectWidth,
                this.dp2px(DEFAULT_RECT_WIDTH));
        this.rectHeight = a.getDimension(R.styleable.CountDownView_easyCountRectHeight,
                this.dp2px(DEFAULT_RECT_HEIGHT));
        this.rectSpacing = a.getDimension(R.styleable.CountDownView_easyCountRectSpacing,
                this.dp2px(DEFAULT_RECT_SPACING));
        this.refitBackgroundAttribute();

        this.timePaint = new Paint();
        this.timePaint.setAntiAlias(true);
        this.timePaint.setColor(a.getColor(R.styleable.CountDownView_easyCountTimeColor,
                DEFAULT_COLOR_TIME));
        this.timePaint.setTextSize(
                a.getDimension(R.styleable.CountDownView_easyCountTimeSize,
                        this.sp2px(DEFAULT_COLON_TEXT_SIZE)));
        this.timePaint.setStrokeWidth(this.dp2px(DEFAULT_TIME_PAINT_WIDTH));
        this.timePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.timePaint.setTextAlign(Paint.Align.CENTER);
        this.timePaint.setStrokeCap(Paint.Cap.ROUND);
        Paint.FontMetricsInt timePaintFontMetrics = this.timePaint.getFontMetricsInt();
        this.timePaintBaseLine = (this.backgroundRectF.bottom + this.backgroundRectF.top -
                timePaintFontMetrics.bottom - timePaintFontMetrics.top) / 2;

        this.rectRadius = a.getDimension(R.styleable.CountDownView_easyCountRectRadius,
                this.dp2px(DEFAULT_ROUND_RECT_RADIUS));
        a.recycle();

        this.updateTime();
    }


    private void updateTime() {
        this.mMillisInFuture = this.timeHour * ONE_HOUR + this.timeMinute * ONE_MINUTE +
                this.timeSecond * ONE_SECOND;
        this.setTime(this.mMillisInFuture);
    }


    private void refitBackgroundAttribute() {
        this.paddingLeft = this.getPaddingLeft();
        this.paddingTop = this.getPaddingTop();
        this.paddingRight = this.getPaddingRight();
        this.paddingBottom = this.getPaddingBottom();

        this.firstTranslateX = this.rectWidth + this.rectSpacing + paddingLeft;
        this.secondTranslateX = this.rectWidth * 2 + this.rectSpacing * 2 + paddingLeft;
        this.firstTranslateColonX = this.firstTranslateX - this.rectSpacing / 2;
        this.secondTranslateColonX = this.secondTranslateX - this.rectSpacing / 2;

        this.backgroundRectF = new RectF(0, 0, this.rectWidth, this.rectHeight);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        this.viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        float resultWidth;
        float resultHeight;

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                resultWidth = this.defaultWrapContentWidth;
                break;
            case MeasureSpec.EXACTLY:
            default:
                resultWidth = Math.max(this.viewWidth, this.defaultWrapContentWidth);
                break;
        }
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                resultHeight = this.defaultWrapContentHeight;
                break;
            case MeasureSpec.EXACTLY:
            default:
                resultHeight = Math.max(this.viewHeight, this.defaultWrapContentHeight);
                break;
        }
        resultWidth += (this.paddingLeft + this.paddingRight);
        resultHeight += (this.paddingTop + this.paddingBottom);
        this.setMeasuredDimension((int) resultWidth, (int) resultHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = w;
        this.viewHeight = h;
        this.refitBackgroundAttribute();
        this.invalidate();
    }


    public void setTimeHour(int timeHour) {
        this.timeHour = timeHour;
        this.updateTime();
    }


    public void setTimeMinute(int timeMinute) {
        this.timeMinute = timeMinute;
        this.updateTime();
    }


    public void setTimeSecond(int timeSecond) {
        this.timeSecond = timeSecond;
        this.updateTime();
    }


    public void setRectWidth(float rectWidthDp) {
        this.rectWidth = this.dp2px(rectWidthDp);
        this.refitBackgroundAttribute();
    }


    public void setRectHeight(float rectHeightDp) {
        this.rectHeight = this.dp2px(rectHeightDp);
        this.refitBackgroundAttribute();
    }


    public void setRectSpacing(float rectSpacingDp) {
        this.rectSpacing = this.dp2px(rectSpacingDp);
        this.refitBackgroundAttribute();
    }


    public void setEasyCountDownListener(EasyCountDownListener easyCountDownListener) {
        this.mEasyCountDownListener = easyCountDownListener;
    }


    public float getRectWidth() {
        return rectWidth;
    }


    public float getRectHeight() {
        return rectHeight;
    }


    public float getRectSpacing() {
        return rectSpacing;
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.start();
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.stop();
        return true;
    }


    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    public synchronized void start() {
        if (this.runningState) return;
        if (mMillisInFuture > 0) {
            this.mThread = new EasyThread();
            this.mThread.startThread();
            this.mThread.start();
            this.runningState = true;
            if (this.mEasyCountDownListener != null) {
                this.mEasyCountDownListener.onCountDownStart();
            }
        } else {
            this.drawZeroZeroZero();
            this.runningState = false;
        }
    }


    public synchronized void stop() {
        if (!this.runningState) return;
        if (this.mThread != null) {
            this.mThread.stopThread();
        }
        if (this.mEasyCountDownListener != null) {
            this.mEasyCountDownListener.onCountDownStop(this.mMillisInFuture);
        }
        this.runningState = false;
    }


    /**
     * Start count down by date
     *
     * @param date date
     */
    public void setTime(Date date) {
        this.mMillisInFuture = date.getTime();
    }


    /**
     * Start count down by timeMillis
     *
     * @param timeMillis timeMillis
     */
    public void setTime(long timeMillis) {
        this.mMillisInFuture = timeMillis;
        this.mCalendar.setTimeInMillis(this.mMillisInFuture);
    }


    private void drawZeroZeroZero() {
        Canvas canvas = null;
        try {
            synchronized (CountDownView.this) {
                canvas = CountDownView.this.lockCanvas();
                if (canvas == null) return;
                this.drawTimeAndBackground(canvas, String.format(locale, LESS_THAN_TEN_FORMAT, 0),
                        String.format(locale, LESS_THAN_TEN_FORMAT, 0),
                        String.format(locale, LESS_THAN_TEN_FORMAT, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class EasyThread extends Thread {

        public volatile boolean running = false;
        public volatile boolean completed = false;


        public EasyThread() {
            this.running = true;
        }


        public synchronized final void startThread() {
            this.completed = false;
            this.running = true;
        }


        public synchronized final void stopThread() {
            this.completed = true;
            this.running = false;
        }


        @Override
        public void run() {
            while (!this.completed) {
                while (this.running) {
                    Canvas canvas = null;
                    try {
                        synchronized (this) {
                            canvas = CountDownView.this.lockCanvas();
                            if (canvas == null) continue;
                            timeHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                            timeMinute = mCalendar.get(Calendar.MINUTE);
                            timeSecond = mCalendar.get(Calendar.SECOND);
                            drawTimeAndBackground(canvas,
                                    String.format(locale, LESS_THAN_TEN_FORMAT, timeHour),
                                    String.format(locale, LESS_THAN_TEN_FORMAT, timeMinute),
                                    String.format(locale, LESS_THAN_TEN_FORMAT, timeSecond));
                            // refresh time
                            mMillisInFuture -= 1000;
                            mCalendar.setTimeInMillis(mMillisInFuture);

                            if (mMillisInFuture < 0) {
                                this.completed = true;
                                this.running = false;
                            }
                            long pastTime = SystemClock.uptimeMillis() - lastRecordTime;
                            if (pastTime < COUNT_DOWN_INTERVAL) {
                                try {
                                    Thread.sleep(COUNT_DOWN_INTERVAL - pastTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            lastRecordTime = SystemClock.uptimeMillis();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    private void drawTimeAndBackground(Canvas canvas, String hour, String minute, String second) {
        canvas.save();
        canvas.translate(paddingLeft, paddingTop);
        canvas.drawRoundRect(backgroundRectF, rectRadius, rectRadius, backgroundPaint);
        canvas.drawText(hour, backgroundRectF.centerX(), timePaintBaseLine, timePaint);
        canvas.restore();

        canvas.save();
        canvas.translate(firstTranslateColonX, paddingTop);
        canvas.drawText(COLON, 0, timePaintBaseLine, colonPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(firstTranslateX, paddingTop);
        canvas.drawRoundRect(backgroundRectF, rectRadius, rectRadius, backgroundPaint);
        canvas.drawText(minute, backgroundRectF.centerX(), timePaintBaseLine, timePaint);
        canvas.restore();

        canvas.save();
        canvas.translate(secondTranslateColonX, paddingTop);
        canvas.drawText(COLON, 0, timePaintBaseLine, colonPaint);
        canvas.restore();

        canvas.save();
        canvas.translate(secondTranslateX, paddingTop);
        canvas.drawRoundRect(backgroundRectF, rectRadius, rectRadius, backgroundPaint);
        canvas.drawText(second, backgroundRectF.centerX(), timePaintBaseLine, timePaint);
        canvas.restore();
    }


    /**
     * Dp to px
     *
     * @param dp dp
     * @return px
     */
    private float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.mMetrics);
    }


    /**
     * Sp to px
     *
     * @param sp sp
     * @return sp
     */
    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.mMetrics);
    }


    public interface EasyCountDownListener {
        /**
         * When count down start
         */
        void onCountDownStart();

        /**
         * When count down stop
         *
         * @param millisInFuture millisInFuture
         */
        void onCountDownStop(long millisInFuture);
    }
}

