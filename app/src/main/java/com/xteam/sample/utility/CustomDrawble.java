package com.xteam.sample.utility;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * Created by Amit on 10/24/15.
 */
public class CustomDrawble extends ReplacementSpan {

    private int mBackgroundColor;
    private int mForegroundColor;

    public CustomDrawble(int backgroundColor, int foregroundColor) {
        this.mBackgroundColor = backgroundColor;
        this.mForegroundColor = foregroundColor;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(measureText(paint, text, start, end));

    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        float padding = 4f;

        RectF rect = new RectF(x - 3, top + 3, x + measureText(paint, text, start, end)+3, bottom + 10);
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(rect, 5,5,paint);
        paint.setColor(mForegroundColor);
        canvas.drawText(text, start, end, x, y, paint);

    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}
