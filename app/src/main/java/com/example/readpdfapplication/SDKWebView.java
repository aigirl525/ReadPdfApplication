package com.example.readpdfapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebView;

public class SDKWebView extends WebView {

    private boolean isDraw = false;
    private Paint paint;
    private RectF rectF;

    public SDKWebView(Context context) {
        super(context);
        init();
    }

    public SDKWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SDKWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10.0f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isDraw) {
            canvas.drawRoundRect(rectF, 48, 48, paint);
        }
    }

    public void drawOvalByAttr(float x, float y, float rx, float ry) {
        isDraw = true;
        rectF = new RectF(x - rx, y - ry, x + rx, y + ry);
        invalidate();
    }

    public void drawOvalByPos(float left, float top, float right, float bottom) {
        isDraw = true;
        rectF = new RectF(left, top, right, bottom);
        invalidate();
    }

    public void clear() {
        isDraw = false;
        invalidate();
    }

    public int getContentWidth() {
        return super.computeHorizontalScrollRange();
    }

    public int getContentHeight() {
        return super.computeVerticalScrollRange();
    }
}
