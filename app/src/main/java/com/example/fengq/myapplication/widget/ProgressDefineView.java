package com.example.fengq.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.tools.PhoneUtil;

/**
 * Created by fengq on 2017/7/4.
 */

public class ProgressDefineView extends View {

    private Context context;

    private String bottom_text;
    private int bottom_text_color;
    private int progress_color;
    private int progress_second_color;
    private String progress_unit;
    private float progress_size;


    //圆的半径
    private int radius;

    private String score;
    private float maxScore;

    //view的宽高
    private int mWidth;
    private int mHeight;

    public ProgressDefineView(Context context) {
        this(context, null);
    }

    public ProgressDefineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressDefineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BraceletElectricityProgressBar);
        bottom_text = array.getString(R.styleable.BraceletElectricityProgressBar_bottom_text);
        bottom_text_color = array.getColor(R.styleable.BraceletElectricityProgressBar_bottom_text_color, 0xffffff);
        progress_color = array.getColor(R.styleable.BraceletElectricityProgressBar_progress_color, 0xffffff);
        progress_second_color = array.getColor(R.styleable.BraceletElectricityProgressBar_progress_second_color, 0xffffff);
        progress_unit = array.getString(R.styleable.BraceletElectricityProgressBar_progress_unit);
        progress_size = array.getDimension(R.styleable.BraceletElectricityProgressBar_progress_size, getResources().getDimension(R.dimen.progress_size));
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
        setMeasuredDimension(mWidth, mHeight);//如果不设置这句话，这自己定义的宽高不会起作用
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArc(canvas);
        drawRectangle(canvas);
        drawDottedArc(canvas);
        drawText(canvas);
        drawScoreText(canvas);
    }

    public void drawRectangle(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//设置只描边
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(progress_size);//设置画笔宽度
        RectF rectF = new RectF(0, 0, mWidth, mHeight);//左上角和
        canvas.drawRect(rectF, paint);
    }

    public void drawArc(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);//设置只描边
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(progress_size);//设置画笔宽度
        RectF rectF = new RectF(0 + 30, 0 + 30, mWidth - 30, mHeight - 30);
        float progress;
        if (score != null) {
            if (score.contains(".")) {
                progress = Float.valueOf(score) / maxScore * 100f;
            } else {
                progress = Integer.valueOf(score) / maxScore * 100f;
            }
            canvas.drawArc(rectF, -90, 3.6f * progress, false, paint);
        }
        canvas.restore();
    }

    public void drawDottedArc(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(progress_size / 2.0f);
        paint.setStyle(Paint.Style.STROKE);//设置只描边
        paint.setColor(Color.WHITE);
        paint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        RectF rectF = new RectF(0 + 5, 0 + 5, mWidth - 5, mHeight - 5);
        canvas.drawArc(rectF, -90, 360, false, paint);
    }

    public void drawText(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);//x坐标设置在字符串的中心（没有设置则为字符的左边在屏幕上的位置）
        paint.setTextSize(progress_size * 3.0f);
        paint.setAntiAlias(true);//设置没有锯齿
        paint.setStyle(Paint.Style.STROKE);//设置只描边
        paint.setColor(Color.WHITE);
        canvas.drawText(bottom_text, mWidth / 2, mHeight / 4 * 3, paint);
        canvas.restore();
    }

    public void drawScoreText(Canvas canvas) {
        canvas.save();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);//x坐标设置在字符串的中心（没有设置则为字符的左边在屏幕上的位置）
        paint.setAntiAlias(true);//设置没有锯齿
        paint.setStyle(Paint.Style.STROKE);//设置只描边
        paint.setColor(Color.WHITE);

        if (score != null) {
            paint.setTextSize(progress_size * 8.0f);
            canvas.drawText(score, mWidth / 2, mHeight / 2, paint);//分数
            paint.setTextSize(progress_size * 4.0f);
            int length = score.getBytes().length;
            if (length == 1) {
                canvas.drawText(progress_unit, mWidth / 2 + length * progress_size * 3.5f, mHeight / 2, paint);//单位
            } else if (length == 2) {
                canvas.drawText(progress_unit, mWidth / 2 + length * progress_size * 3.0f, mHeight / 2, paint);//单位
            } else if (length == 3) {
                canvas.drawText(progress_unit, mWidth / 2 + length * progress_size * 2.8f, mHeight / 2, paint);//单位
            }
        }

        canvas.restore();
    }

    public void setScoreAndMaxScore(String score, float maxScore) {
        this.score = score;
        this.maxScore = maxScore;
        invalidate();

    }

}
