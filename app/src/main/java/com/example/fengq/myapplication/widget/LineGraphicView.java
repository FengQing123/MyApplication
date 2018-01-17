package com.example.fengq.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.fengq.myapplication.tools.PhoneUtil;

import java.util.ArrayList;

/**
 * Created by fengq on 2017/7/5.
 * 该类为自定义表格类
 */

public class LineGraphicView extends View {

    private int CIRCLE_SIZE = 10;

    private Context context;

    /**
     * View 宽高
     */
    private int mWidth;
    private int mHeight;
    /**
     * 画笔
     */
    private Paint mPaint;

    private int bHeight = 0;
    private int bWidth = 0;


    /**
     * Y轴最大值
     */
    private int maxValue;
    /**
     * Y轴间距值
     */
    private int averageValue;

    /**
     * Y轴间隔高度
     */
    private int spacingHeight;

    private int marginTop = 30;
    private int marginBottom = 80;


    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    /**
     * 纵坐标值
     */
    private ArrayList<Double> yRawData = new ArrayList<>();
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas = new ArrayList<>();

    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值


    public LineGraphicView(Context context) {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineGraphicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//消除锯齿


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
        setMeasuredDimension(mWidth, mHeight);

        if (bHeight == 0) {
            bHeight = mHeight - marginBottom;
        }
        bWidth = PhoneUtil.dip2px(context, 30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.BLACK);
        //画直线（横向）
        drawAllXLine(canvas);
        // 画直线（纵向）
//        drawAllYLine(canvas);
        // 点的操作设置
        mPoints = getPoints();
        //画虚线
        drawDottedLine(canvas, mPoints);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(PhoneUtil.dip2px(context, 2.5f));
        mPaint.setStyle(Paint.Style.STROKE);
        drawScrollLine(canvas);//画折线图
//        drawLine(canvas);//画曲线图

        int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
        Shader shader = new LinearGradient(0, 0, 100, 100, colors, null, Shader.TileMode.REPEAT);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(shader);


        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 10, mPaint);
        }
    }

    /**
     * 画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas) {
//        if (spacingHeight > 0) {
//            for (int i = 0; i <= spacingHeight; i++) {
//                // 坐标线
//                canvas.drawLine(bWidth, bHeight - (bHeight / spacingHeight) * i + marginTop, (mWidth - bWidth), bHeight - (bHeight / spacingHeight) * i + marginTop, mPaint);
//                //Y轴坐标值
//                drawYText(String.valueOf(averageValue * i), bWidth / 2, bHeight - (bHeight / spacingHeight) * i + marginTop, canvas);
//            }
//        }

        //单纯画X轴
        canvas.drawLine(bWidth, bHeight + marginTop, (mWidth - bWidth), bHeight + marginTop, mPaint);

    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas) {
        for (int i = 0; i < yRawData.size(); i++) {
            //坐标线
            canvas.drawLine(bWidth + (mWidth - bWidth) / yRawData.size() * i, marginTop, bWidth + (mWidth - bWidth) / yRawData.size() * i, bHeight + marginTop, mPaint);
            //X轴坐标值
            drawXText(xRawDatas.get(i), bWidth + (mWidth - bWidth) / yRawData.size() * i, bHeight + PhoneUtil.dip2px(context, 26), canvas);// X坐标
        }
    }

    /**
     * 画虚线
     *
     * @param canvas
     * @param points
     */
    private void drawDottedLine(Canvas canvas, Point[] points) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        Path path = new Path();
        for (int i = 0; i < yRawData.size(); i++) {
            path.moveTo(bWidth + (mWidth - bWidth) / yRawData.size() * i, points[i].y);
            path.lineTo(bWidth + (mWidth - bWidth) / yRawData.size() * i, bHeight + marginTop);
            PathEffect effects = new DashPathEffect(new float[]{15, 10}, 0);
            paint.setPathEffect(effects);
            canvas.drawPath(path, paint);
        }
    }

    private void drawYText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(PhoneUtil.dip2px(context, 12));
        p.setColor(Color.GRAY);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    private void drawXText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(PhoneUtil.dip2px(context, 12));
        p.setColor(Color.GRAY);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, x, y, p);
    }

    /**
     * 画曲线图
     *
     * @param canvas
     */
    public void drawScrollLine(Canvas canvas) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 画折线图
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    private Point[] getPoints() {
        for (int i = 0; i < yRawData.size(); i++) {
            xList.add(bWidth + (mWidth - bWidth) / yRawData.size() * i);
        }
        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++) {
            int ph = bHeight - (int) (bHeight * (yRawData.get(i) / maxValue));
            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }


    public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData, int maxValue, int averageValue) {
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        this.spacingHeight = maxValue / averageValue;
    }
}
