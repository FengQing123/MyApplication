package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.example.fengq.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * Created by fengq on 2017/6/2.
 */

public class MPAndroidChartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpandroidchart);

        //折线图
        LineChart mLineChart = (LineChart) findViewById(R.id.line_chart);
        setLineChart(mLineChart);
        loadLineChartData(mLineChart);
//        //柱形图
//        BarChart mBarChart = (BarChart) findViewById(R.id.bar_chart);
//        setBarChart(mBarChart);
//        loadBarChartData(mBarChart);
//        //饼形图
//        PieChart mPieChart = (PieChart) findViewById(R.id.pie_chart);
//        setPieChart(mPieChart);
//        loadPieChartData(mPieChart);
    }

    private void setLineChart(LineChart lineChart) {
        Description description = new Description();
        description.setText("describe");
        lineChart.setDescription(description);
        lineChart.setDrawGridBackground(false);//设置网格背景
        lineChart.setScaleEnabled(false);//设置缩放
        lineChart.setDoubleTapToZoomEnabled(false);//设置双击是否缩放

        //设置 X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置
//        xAxis.setTypeface(mTf);//设置字体
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        //获得左侧侧坐标轴
        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5);
//        leftAxis.setAxisLineWidth(1.5f);

        //设置右侧坐标轴
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);//右侧坐标轴线
        rightAxis.setDrawLabels(false);//右侧坐标轴数组Lable
//        rightAxis.setTypeface(mTf);
//        rightAxis.setLabelCount(5);
//        rightAxis.setDrawGridLines(false);
    }

    private void loadLineChartData(LineChart lineChart) {
        //所有线的List
        ArrayList<ILineDataSet> allLinesList = new ArrayList();

        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < 12; i++) {
            //Entry(yValue,xIndex);一个Entry表示一个点，第一个参数为y值，第二个为X轴List的角标
            entryList.add(new Entry((int) (Math.random() * 65) + 40, i));
        }

        //LineDataSet可以看做是一条线
        LineDataSet dataSet1 = new LineDataSet(entryList, "dataLine1");
        dataSet1.setLineWidth(2.5f);
        dataSet1.setCircleSize(4.5f);
        dataSet1.setHighLightColor(Color.RED);//设置点击某个点时，横竖两条线的颜色
        dataSet1.setDrawValues(false);//是否在点上绘制Value

        allLinesList.add(dataSet1);

        //LineData表示一个LineChart的所有数据(即一个LineChart中所有折线的数据)
        LineData mChartData = new LineData(allLinesList);

        // set data
        lineChart.setData(mChartData);
        lineChart.animateX(1500);//设置动画
    }
}
