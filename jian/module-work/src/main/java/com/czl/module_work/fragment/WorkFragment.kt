package com.czl.module_work.fragment

import android.graphics.Color
import android.graphics.Matrix
import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.LogUtils
import com.czl.base.base.BaseFragment
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.PercentBean
import com.czl.base.data.bean.WorkOrderBean
import com.czl.module_work.R
import com.czl.module_work.viewModel.WorkViewModel
import com.czl.module_work.BR
import com.czl.module_work.adapter.MyOrderAdapter
import com.czl.module_work.adapter.WorkTimeAdapter
import com.czl.module_work.databinding.WorkFragmentWorkBinding
import com.czl.module_work.viewModel.MyOrderDetailViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description

import com.github.mikephil.charting.formatter.IAxisValueFormatter

import com.github.mikephil.charting.data.BarData

import com.github.mikephil.charting.data.BarDataSet

import com.github.mikephil.charting.data.BarEntry

import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.random.Random
import com.github.mikephil.charting.components.YAxis
import java.util.*
import kotlin.collections.ArrayList
import com.github.mikephil.charting.utils.ViewPortHandler

import com.github.mikephil.charting.formatter.IValueFormatter

import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


/**
 * 工单
 */
@Route(path = AppConstants.Router.Work.F_WORK)
class WorkFragment : BaseFragment<WorkFragmentWorkBinding, WorkViewModel>() {

    private lateinit var mAdapter: WorkTimeAdapter
    override fun initContentView(): Int {
        return R.layout.work_fragment_work
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.tvTitle.set("工单中心")
        loadData()
        initAdapter()
    }

    private fun loadData() {
        viewModel.getUserRooms()
        viewModel.workOrderPercent()
        viewModel.workOrderTime()
    }

    private fun initAdapter() {
        mAdapter = WorkTimeAdapter()

        binding.ryCommon.apply {
            adapter = mAdapter
        }
    }

    override fun initViewObservable() {
        viewModel.uc.loadChatEvent.observe(this, Observer {
            it ?: return@Observer
            initChart(it)
        })

        viewModel.uc.loadOrderTimeEvent.observe(this, Observer {
            it ?: return@Observer
            mAdapter.addData(it)
        })
    }

    private fun initChart(list: List<PercentBean>) {

        binding.barchart1.getLegend()
            .setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
        binding.barchart1.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        val barEntryList: MutableList<BarEntry> = ArrayList()
        val barEntryList2: MutableList<BarEntry> = ArrayList()
        val barEntryList3: MutableList<BarEntry> = ArrayList()
        val barEntryList4: MutableList<BarEntry> = ArrayList()
        list.forEachIndexed { index, it ->
//            barEntryList.add(BarEntry(index.toFloat(), it.baoyang.toFloat()))
//            barEntryList2.add(BarEntry(index.toFloat(), it.xunjian.toFloat()))
//            barEntryList3.add(BarEntry(index.toFloat(), it.baojing.toFloat()))
//            barEntryList4.add(BarEntry(index.toFloat(), it.weixiu.toFloat()))
//
            barEntryList.add(BarEntry(index.toFloat(), (15..30).random().toFloat()))
            barEntryList2.add(BarEntry(index.toFloat(), (15..30).random().toFloat()))
            barEntryList3.add(BarEntry(index.toFloat(), (15..30).random().toFloat()))
            barEntryList4.add(BarEntry(index.toFloat(), (15..30).random().toFloat()))
        }

        val barDataSet = BarDataSet(barEntryList, "保养工单")
        val barDataSet2 = BarDataSet(barEntryList2, "巡检工单")
        val barDataSet3 = BarDataSet(barEntryList3, "报警工单")
        val barDataSet4 = BarDataSet(barEntryList4, "维修工单")

        barDataSet.color = Color.parseColor("#70CA9E")
        barDataSet2.color = Color.parseColor("#6BB1FF")
        barDataSet3.color = Color.parseColor("#F29968")
        barDataSet4.color = Color.parseColor("#FF3B30")
        barDataSet.setDrawValues(false)
        barDataSet2.setDrawValues(false)
        barDataSet3.setDrawValues(false)
        barDataSet4.setDrawValues(false)


        val barData = BarData(
            barDataSet, barDataSet2, barDataSet3, barDataSet4
        )
        binding.barchart1.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return list.get(Math.abs(value).toInt() % list.size).label;
            }
        }
        val ratio = list.size.toFloat() / 10f
        binding.barchart1.zoom(ratio, 1f, 0f, 0f) //显示的时候是按照多大的比率缩放显示  1f表示不放大缩小


        binding.barchart1.axisLeft.setYOffset(0f) //这样会向下偏移 50%

        binding.barchart1.axisLeft.setLabelCount(8, false) //设置间距

        binding.barchart1.getAxisLeft().setValueFormatter(object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return value.toInt().toString()
            }
        })

        binding.barchart1.xAxis.textSize = 8f
        binding.barchart1.xAxis.textColor = Color.BLACK
        binding.barchart1.xAxis.axisMinimum = 0f
        if (list.size < 12) {
            binding.barchart1.xAxis.setLabelCount(8, true)
        } else {
            binding.barchart1.xAxis.setLabelCount(13, true)
        }
        binding.barchart1.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barchart1.xAxis.setCenterAxisLabels(true)
        binding.barchart1.xAxis.axisLineColor = Color.parseColor("#E5E5E5")
        binding.barchart1.axisLeft.axisLineColor = Color.parseColor("#3ECC98")
        binding.barchart1.xAxis.setDrawAxisLine(false) //横线
        binding.barchart1.xAxis.setDrawGridLines(false) //垂线
        binding.barchart1.axisRight.setDrawAxisLine(false)
        binding.barchart1.axisRight.isEnabled = false
        binding.barchart1.setScaleEnabled(false);//设置是否可以缩放
        binding.barchart1.setTouchEnabled(true);//设置是否可以触摸
        binding.barchart1.isDragEnabled = true
        val description = Description()
        description.isEnabled = false
        binding.barchart1.description = description


        /*  float groupSpace = 0.12f; //柱状图组之间的间距
          float barSpace =(float)((1 - 0.12) / yListValue.size() / 10); // x4 DataSet
          float barWidth =(float)((1 - 0.12) / yListValue.size() / 10 * 9); // x4 DataSet*/

        val groupSpace = 0.12f
        val barSpace = 0f
        val barWidth = (1 - 0.12).toFloat() / 4f / 10f * 9f + (1 - 0.12).toFloat() / 4f / 10f

        barData.barWidth = barWidth
        binding.barchart1.data = barData //先设置数据
//然后再设置分组柱状图
        binding.barchart1.axisLeft.setAxisMinimum(0f)
        binding.barchart1.getXAxis().setAxisMaximum(
            0 + binding.barchart1.getBarData().getGroupWidth(groupSpace, barSpace) * list.size
        );
        binding.barchart1.groupBars(0f, groupSpace, barSpace)
        binding.barchart1.setDrawValueAboveBar(false);//这里设置为true每一个直方图的值就会显示在直方图的顶部
        binding.barchart1.description.isEnabled = false;//设置描述不显示
        binding.barchart1.invalidate()
        val mMatrix = Matrix();
        mMatrix.postScale(list.size.toFloat() / 12f, 1f);
        binding.barchart1.viewPortHandler.refresh(mMatrix, binding.barchart1, false);
        binding.barchart1.animateY(1000);
    }

    /**
     * @param barChart  控件
     * @param barData   数据
     * @param isSlither 用来控制是否可以滑动
     */
    public fun showBarChart(barData: BarData, isSlither: Boolean) {
        //绘制高亮箭头
//        binding.barchart1.setDrawHighlightArrow(false);
        //设置值显示在柱状图的上边或者下边
        binding.barchart1.setDrawValueAboveBar(true);
        //设置绘制网格背景
        binding.barchart1.setDrawGridBackground(true);
        //设置双击缩放功能
        binding.barchart1.setDoubleTapToZoomEnabled(false);
        //设置规模Y启用
        binding.barchart1.setScaleYEnabled(false);
        //设置规模X启用
        binding.barchart1.setScaleXEnabled(false);
        //启用设置阻力
        binding.barchart1.setScaleEnabled(true);
        //设置每个拖动启用的高亮显示
        binding.barchart1.setHighlightPerDragEnabled(false);
        // 设置硬件加速功能
        binding.barchart1.setHardwareAccelerationEnabled(true);
        // 设置绘制标记视图
        binding.barchart1.setDrawMarkerViews(true);
        // 设置启用日志
        binding.barchart1.setLogEnabled(true);
        // 设置突出功能
//        binding.barchart1.setHighlightEnabled(true);
        // 设置拖动减速功能
        binding.barchart1.setDragDecelerationEnabled(true);
        // 数据描述
//        binding.barchart1.setDescription("");
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
//        binding.barchart1.setNoDataTextDescription("没有数据了");
        binding.barchart1.setNoDataText("O__O …");
        // 是否显示表格颜色
        binding.barchart1.setDrawGridBackground(false);


        /**
         * 下面这几个属性你们可以试试 挺有意思的
         * */
        // 设置是否可以触摸
        binding.barchart1.setTouchEnabled(isSlither);
        // 是否可以拖拽
        binding.barchart1.setDragEnabled(isSlither);//放大可拖拽
        // 是否可以缩放
        binding.barchart1.setScaleEnabled(false);
        // 集双指缩放
        binding.barchart1.setPinchZoom(false);


        // 设置背景
        binding.barchart1.setBackgroundColor(Color.parseColor("#01000000"));
        // 如果打开，背景矩形将出现在已经画好的绘图区域的后边。
        binding.barchart1.setDrawGridBackground(false);
        // 集拉杆阴影
        binding.barchart1.setDrawBarShadow(false);
        // 图例
        binding.barchart1.getLegend().setEnabled(false);
        // 设置数据
        binding.barchart1.setData(barData);
        // 隐藏右边的坐标轴 (就是右边的0 - 100 - 200 - 300 ... 和图表中横线)
        binding.barchart1.getAxisRight().setEnabled(false);
        // 隐藏左边的左边轴 (同上)
        // 网格背景颜色
        binding.barchart1.setGridBackgroundColor(Color.parseColor("#00000000"));
        // 是否显示表格颜色
        binding.barchart1.setDrawGridBackground(false);
        // 设置边框颜色
        binding.barchart1.setBorderColor(Color.parseColor("#00000000"));
        // 说明颜色
//        binding.barchart1.setDescriptionColor(Color.parseColor("#00000000"));
        // 拉杆阴影
        binding.barchart1.setDrawBarShadow(false);
        // 打开或关闭绘制的图表边框。（环绕图表的线）
        binding.barchart1.setDrawBorders(false);
        val mLegend = binding.barchart1.getLegend(); // 设置比例图标示
        // 设置窗体样式
//        binding.barchart1.setForm(Legend.LegendForm.CIRCLE);
        // 字体
//        binding.barchart1.setFormSize(4f);
        // 字体颜色
//        binding.barchart1.setTextColor(Color.parseColor("#00000000"));
        val xAxis = binding.barchart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
//        xAxis.setSpaceBetweenLabels(2);
//        xAxis.setTextColor(0x000000); // 设置轴标签的颜色。
//        xAxis.setTextSize(18); // 设置轴标签的文字大小。
//        xAxis.setTypeface( ) ;// 设置轴标签的 Typeface。
//        xAxis.setGridColor( int color); /// 设置该轴的网格线颜色。
//        xAxis.setGridLineWidth( float width);// 设置该轴网格线的宽度。
//        xAxis.setAxisLineColor( int color); // 设置轴线的轴的颜色。
//        xAxis.setAxisLineWidth( float width);// 设置该轴轴行的宽度。
//        barChart.animateY(1000); // 立即执行的动画,Y轴

        if (isSlither) {
            //当为true时,放大图
            // 为了使 柱状图成为可滑动的,将水平方向 放大 2.5倍

        } else {
            //当为false时 不对图进行操作
            binding.barchart1.animateY(1000);
        }
    }
}