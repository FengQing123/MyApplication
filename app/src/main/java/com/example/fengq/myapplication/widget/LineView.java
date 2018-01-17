package com.example.fengq.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.example.fengq.myapplication.tools.PhoneUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fengq on 2017/7/27.
 */

public class LineView extends View {
    private final static String X_KEY = "Xpos";
    private final static String Y_KEY = "Ypos";
    private int mWidth, mHeight;
    private Context context;

    private List<Map<String, Integer>> mListPoint = new ArrayList<Map<String, Integer>>();

    Paint mPaint = new Paint();

    public LineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            mWidth = PhoneUtil.dip2px(context, 300);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = PhoneUtil.dip2px(context, 500);
        }
        setMeasuredDimension(mWidth, mHeight);//如果不设置这句话，这自己定义的宽高不会起作用（不清楚）
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);

        for (int index = 0; index < mListPoint.size(); index++) {
            if (index > 0) {
                int x1 = mListPoint.get(index - 1).get(X_KEY);
                int y1 = mListPoint.get(index - 1).get(Y_KEY);
                int x2 = mListPoint.get(index).get(X_KEY);
                int y2 = mListPoint.get(index).get(Y_KEY);
                if (x1 > mWidth) {
                    x1 = 0;
                }
                canvas.drawLine(x1, y1, x2, y2, mPaint);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            }
        }
    }

    /**
     * @param curX which x position you want to draw.
     * @param curY which y position you want to draw.
     */
    public void setLinePoint(int curX, int curY) {
        Map<String, Integer> temp = new HashMap<String, Integer>();
        temp.put(X_KEY, curX);
        temp.put(Y_KEY, curY);
        mListPoint.add(temp);
        invalidate();
    }
}
