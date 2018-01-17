package com.example.fengq.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.fengq.myapplication.tools.UIHelper;

/**
 * Created by fengq on 2017/5/10.
 */

public class LongPressTextView extends TextView implements View.OnTouchListener {

    private int MeasureWidth = 0;
    private float XPoint = 0;
    private float YPoint = 0;
    private float radius = 0;
    private int Alpha = 255;

    private Context context;
    private boolean isFirst = true;
    private Paint BGPaint;
    private Handler downHandler = new Handler();

    public LongPressTextView(Context context) {
        this(context, null);
    }

    public LongPressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        BGPaint = new Paint();
        BGPaint.setColor(Color.parseColor("#4fa0ee"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MeasureWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BGPaint.setAlpha(Alpha);
        canvas.drawCircle(XPoint, YPoint, radius, BGPaint);
    }

    private Runnable downRunnable = new Runnable() {
        @Override
        public void run() {
            radius += MeasureWidth / 100;
            if (Math.abs(MeasureWidth - XPoint) > MeasureWidth / 2) {
                if (radius > Math.abs(MeasureWidth - XPoint)) {
                    if (isFirst) {
                        isFirst = false;
                        UIHelper.ToastMessage(context, "left---radius>XPoint");
                    }
                }
            } else {
                if (radius > XPoint) {
                    if (isFirst) {
                        isFirst = false;
                        UIHelper.ToastMessage(context, "right--radius>XPoint");
                    }
                }
            }
            Alpha -= 2;
            postInvalidate();
            downHandler.postDelayed(this, 2);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("LongPress", "action_down");
                XPoint = event.getX();
                YPoint = event.getY();
                Log.e("LongPress", "MeasureWidth=" + MeasureWidth + ",XPoint=" + XPoint + ",YPoint=" + YPoint);
                downHandler.postDelayed(downRunnable, 0);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("LongPress", "action_up");
                if (downRunnable != null) {
                    downHandler.removeCallbacks(downRunnable);
                }
                radius = 0;
                Alpha = 255;
                isFirst = true;
                postInvalidate();
                break;
        }
        return true;
    }
}
