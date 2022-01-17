package com.czl.module_work.view

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.czl.base.callback.TimePickerListener
import com.czl.base.config.AppConstants
import com.czl.base.data.bean.MembersBean
import com.czl.base.data.bean.PatrolOrderDetailBean
import com.czl.base.mvvm.ui.ContainerFmActivity
import com.czl.base.widget.TimePickerPopup
import com.czl.module_work.R
import com.czl.module_work.adapter.MembersSelectAdapter
import com.czl.module_work.adapter.PatrolTaskContentAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import java.util.*


/**
 * 创建日期：2022/1/15  15:08
 * 类说明:
 * @author：86152
 */
class PatrolCertificatePop(context: Context) : BottomPopupView(context) {

    private var mAdapter: PatrolTaskContentAdapter? = null

    private var deviceCodeView: TextView? = null

    private var deviceNameView: TextView? = null

    private var deviceLocationView: TextView? = null

    private var taskContentView: TextView? = null

    private var deviceStatusNormalView: View? = null

    private var deviceStatusExceptionView: View? = null

    private var taskContentStatus1: View? = null

    private var taskContentStatus2: View? = null

    private var taskContentStatus3: View? = null

    private var contentListView: ShimmerRecyclerView? = null

    private var mListener: PatrolSaveListener? = null

    override fun getImplLayoutId(): Int = R.layout.work_dialog_patrol_certificate

    private var task: PatrolOrderDetailBean.TaskList? = null

    fun setTask(task: PatrolOrderDetailBean.TaskList) {
        this.task = task
        deviceCodeView?.setText("设备编码:" + task.proDeviceInfo.deviceNo)
        deviceNameView?.setText("设备名称:" + task.proDeviceInfo.deviceName)
        deviceLocationView?.setText("设备位置:" + task.proDeviceInfo.location)
        if (task.taskDetail.handleState == "1") {
//            未检
        }
        if (task.taskDetail.handleState == "2") {
//            正常
            deviceStatusNormalView?.isSelected = true
        }
        if (task.taskDetail.handleState == "3") {
//            异常
            deviceStatusNormalView?.isSelected = true
        }
        mAdapter?.setNewInstance(task.standardList as MutableList<PatrolOrderDetailBean.StandardList>)
    }

    fun setPatrolSaveListener(listener: PatrolSaveListener) {
        this.mListener = listener
    }

    override fun initPopupContent() {
        super.initPopupContent()
        mAdapter = PatrolTaskContentAdapter()
        deviceCodeView = findViewById(R.id.tv_device_code)
        deviceNameView = findViewById(R.id.tv_device_name)
        deviceLocationView = findViewById(R.id.tv_device_location)
        taskContentView = findViewById(R.id.tv_task_content)
        deviceStatusNormalView = findViewById(R.id.ll_normal)
        deviceStatusExceptionView = findViewById(R.id.ll_exception)
        contentListView = findViewById(R.id.ry_common)
//        taskContentStatus1 = findViewById(R.id.ll_patrol_1)
//        taskContentStatus2 = findViewById(R.id.ll_patrol_2)
//        taskContentStatus3 = findViewById(R.id.ll_patrol_3)

//        taskContentStatus1?.setOnClickListener {
//            taskContentStatus1?.isSelected = true
//            taskContentStatus2?.isSelected = false
//            taskContentStatus3?.isSelected = false
//        }
//        taskContentStatus2?.setOnClickListener {
//            taskContentStatus1?.isSelected = false
//            taskContentStatus2?.isSelected = true
//            taskContentStatus3?.isSelected = false
//        }
//        taskContentStatus3?.setOnClickListener {
//            taskContentStatus1?.isSelected = false
//            taskContentStatus2?.isSelected = false
//            taskContentStatus3?.isSelected = true
//        }
        contentListView?.apply {
            adapter = mAdapter
        }
        deviceStatusNormalView?.setOnClickListener {
            deviceStatusNormalView?.isSelected = true
            deviceStatusExceptionView?.isSelected = false
        }
        deviceStatusExceptionView?.setOnClickListener {
            deviceStatusNormalView?.isSelected = false
            deviceStatusExceptionView?.isSelected = true
        }
        findViewById<View>(R.id.tv_cancel).setOnClickListener { dismiss() }

        deviceCodeView?.setText("设备编码:" + task?.proDeviceInfo?.deviceNo)
        deviceNameView?.setText("设备名称:" + task?.proDeviceInfo?.deviceName)
        deviceLocationView?.setText("设备位置:" + task?.proDeviceInfo?.location)
        if (task?.taskDetail?.handleState == "1") {
            //未检
        }
        if (task?.taskDetail?.handleState == "2") {
            //正常
            deviceStatusNormalView?.isSelected = true
        }
        if (task?.taskDetail?.handleState == "3") {
            //异常
            deviceStatusNormalView?.isSelected = true
        }
        mAdapter?.setNewInstance(task?.standardList as MutableList<PatrolOrderDetailBean.StandardList>)
        findViewById<View>(R.id.tv_confirm).setOnClickListener {
            val status = if (deviceStatusNormalView?.isSelected == true) {
                "2"
            } else if (deviceStatusExceptionView?.isSelected == true) {
                "3"
            } else {
                "1"
            }
            mAdapter
            mListener?.save(task?.taskDetail?.checkTaskDetailId?:"",status, mAdapter!!.data)
            dismiss()
        }

    }

    interface PatrolSaveListener {

        fun save(id:String,status: String, task: List<PatrolOrderDetailBean.StandardList>)

    }
}
