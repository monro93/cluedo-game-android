package com.example.albert.cluedo;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.GridLayout;

/**
 * Classe que representa un tauler
 */
public class Tauler extends GridLayout {
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;

    private float mScaleFactor = 1.f;
    private float maxWidth = 0.0f;
    private float maxHeight = 0.0f;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mPosX;
    private float mPosY;
    private float width;
    private float height;

    /**
     * Constructor amb un paràmetre
     * @param context, representa un recurs
     */
    public Tauler(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    /**
     * Constructor amb dos paràmetre
     * @param context, representa un recurs
     * @param attrs, representa un atribut
     */
    public Tauler(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    /**
     * Constructor amb tres paràmetre
     * @param context, representa un recurs
     * @param attrs, representa un atribut
     * @param defStyleAttr, representa un estil
     */
    public Tauler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    /**
     * Mètode que agafa les mesures de tauler
     * @param widthMeasureSpec, representa el ample
     * @param heightMeasureSpec, representa el alt
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        getMeasuredHeight();
        getMeasuredWidth();
    }

    /**
     * Mètode que realitza un acció en tocar el tauler
     * @param ev, representa un esdeveniment
     * @return true
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        super.onTouchEvent(ev);
        final int action = ev.getAction();
        mScaleDetector.onTouchEvent(ev);
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;

                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                if (mPosX > 0.0f)
                    mPosX = 0.0f;
                else if (mPosX < maxWidth)
                    mPosX = maxWidth;

                if (mPosY > 0.0f)
                    mPosY = 0.0f;
                else if (mPosY < maxHeight)
                    mPosY = maxHeight;

                mLastTouchX = x;
                mLastTouchY = y;

                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    /**
     * Mètode que escala les posicions en fer zoom al canvas
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.restore();
    }

    /**
     * Mètode que dibuixa en fer zoom al canvas
     * @param canvas
     */
    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        if (mScaleFactor == 1.0f) {
            mPosX = 0.0f;
            mPosY = 0.0f;
        }
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);
        super.dispatchDraw(canvas);
        canvas.restore();
        invalidate();
    }

    /**
     * Mètode que escolta quan es fa el gest de zoom
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 3.0f));
            maxWidth = width - (width * mScaleFactor);
            maxHeight = height - (height * mScaleFactor);
            invalidate();
            return true;
        }
    }

}
