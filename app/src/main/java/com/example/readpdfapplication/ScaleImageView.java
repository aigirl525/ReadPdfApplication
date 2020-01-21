package com.example.readpdfapplication;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatImageView;

public class ScaleImageView extends AppCompatImageView
        implements ScaleGestureDetector.OnScaleGestureListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final int MAX_SCALE_TIME = 4;

    private ScaleGestureDetector mScaleGestureDetector;
    // 缩放工具类
    private Matrix mMatrix;
    private boolean              mFirstLayout;
    private float                mBaseScale;


    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mFirstLayout = true;
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //移除OnGlobalLayoutListener
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScaleGestureDetector.onTouchEvent(event);
    }

    /**
     * 缩放进行中
     */
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (null == getDrawable() || mMatrix == null) {
            return true;
        }
        // 获取缩放因子
        float scaleFactor = detector.getScaleFactor();
        float scale = getScale();
        // 控件图片的缩放范围
        if ((scale < mBaseScale * MAX_SCALE_TIME && scaleFactor > 1.0f) || (scale > mBaseScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mBaseScale) {
                scaleFactor = mBaseScale / scale;
            }
            if (scale * scaleFactor > mBaseScale * MAX_SCALE_TIME) {
                scaleFactor = mBaseScale * MAX_SCALE_TIME / scale;
            }
            // 以屏幕中央位置进行缩放
            mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            Log.e("12345scale",scale+"");
            Log.e("12345mBaseScale",mBaseScale+"");
            Log.e("12345scaleFactor",scaleFactor+"");
            borderAndCenterCheck();
            setImageMatrix(mMatrix);
        }
        return false;
    }

    /**
     * 缩放开始
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    /**
     * 缩放结束
     */
    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    @Override
    public void onGlobalLayout() {
        if (mFirstLayout) {
            mFirstLayout = false;
            // 获取控件的宽度和高度
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            // 获取到ImageView对应图片的宽度和高度
            Drawable drawable = getDrawable();
            if (null == drawable) {
                return;
            }
            // 图片固有宽度
            int drawableWidth = drawable.getIntrinsicWidth();
            // 图片固有高度
            int drawableHeight = drawable.getIntrinsicHeight();
            // 接下来对图片做初始的缩放处理，保证图片能看全
            if (drawableWidth >= viewWidth && drawableHeight >= viewHeight) {
                // 图片宽度和高度都大于控件(缩小)
                mBaseScale = Math.min(viewWidth * 1.0f / drawableWidth, viewHeight * 1.0f / drawableHeight);
            } else if (drawableWidth > viewWidth && drawableHeight < viewHeight) {
                // 图片宽度大于控件,高度小于控件(缩小)
                mBaseScale = viewWidth * 1.0f / drawableWidth;
            } else if (drawableWidth < viewWidth && drawableHeight > viewHeight) {
                // 图片宽度小于控件,高度大于控件(缩小)
                mBaseScale = viewHeight * 1.0f / drawableHeight;
            } else {
                // 图片宽度小于控件,高度小于控件(放大)
                mBaseScale = Math.min(viewWidth * 1.0f / drawableWidth, viewHeight * 1.0f / drawableHeight);
            }
            // 将图片移动到手机屏幕的中间位置
            float distanceX = viewWidth / 2 - drawableWidth / 2;
            float distanceY = viewHeight / 2 - drawableHeight / 2;
            mMatrix.postTranslate(distanceX, distanceY);
            mMatrix.postScale(mBaseScale, mBaseScale, viewWidth / 2, viewHeight / 2);
            setImageMatrix(mMatrix);
        }
    }

    private float getScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /**
     * 获得图片放大缩小以后的宽和高
     */
    private RectF getMatrixRectF() {
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mMatrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 图片在缩放时进行边界控制
     */
    private void borderAndCenterCheck() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        // 缩放时进行边界检测，防止出现白边
        if (rect.width() >= viewWidth) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
            }
        }
        if (rect.height() >= viewHeight) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < viewHeight) {
                deltaY = viewHeight - rect.bottom;
            }
        }
        // 如果宽度或者高度小于控件的宽或者高；则让其居中
        if (rect.width() < viewWidth) {
            deltaX = viewWidth / 2f - rect.right + rect.width() / 2f;

        }
        if (rect.height() < viewHeight) {
            deltaY = viewHeight / 2f - rect.bottom + rect.height() / 2f;
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }



}
