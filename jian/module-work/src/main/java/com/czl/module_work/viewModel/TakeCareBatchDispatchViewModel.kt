package com.czl.module_work.viewModel

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.ToastUtils
import com.czl.base.base.BaseBean
import com.czl.base.base.BaseViewModel
import com.czl.base.base.MyApplication
import com.czl.base.binding.command.BindingAction
import com.czl.base.binding.command.BindingCommand
import com.czl.base.config.AppConstants
import com.czl.base.data.DataRepository
import com.czl.base.data.bean.MembersBean
import com.czl.base.event.LiveBusCenter
import com.czl.base.extension.ApiSubscriberHelper
import com.czl.base.util.RxThreadHelper


/**
 * 创建日期：2022/1/5  18:39
 * 类说明:
 * @author：86152
 */
class TakeCareBatchDispatchViewModel(application: MyApplication, model: DataRepository) :
    BaseViewModel<DataRepository>(application, model) {
    val orderType = ObservableField(ORDER_TYPE_TAKE_CARE)

    val batchType = ObservableField(ORDER_BATCH_TYPE_DISPATCH)

    val btnText = ObservableField("确定派单")

    val isAgree = ObservableField(false)

    val isReject = ObservableField(false)

    val remarkText = ObservableField<String>()

    var orderIds = mutableListOf<String>()

    var selectMember: MembersBean? = null

    var isTakeCare = true

    val btnAgreeClick: BindingCommand<Void> = BindingCommand(BindingAction {
        isAgree.set(true)
        isReject.set(false)
    })

    val btnRejectClick: BindingCommand<Void> = BindingCommand(BindingAction {
        isAgree.set(false)
        isReject.set(true)
    })


    val btnSelectMembersClick: BindingCommand<Void> = BindingCommand(BindingAction {
        startContainerActivity(AppConstants.Router.Work.F_WORK_MEMBERS_SELECT)
    })

    val btnBatchDispatchClick: BindingCommand<Void> = BindingCommand(BindingAction {
        if (batchType.get() == ORDER_BATCH_TYPE_AUDIT) {
            audit()
            return@BindingAction
        }
        dispatch()
    })

    fun setOrders(orderIds: List<String>) {
        this.orderIds.addAll(orderIds)
    }

    fun setIsTakeCare(isTakeCare: Boolean) {
        this.isTakeCare = isTakeCare
    }

    fun audit() {
        val remark = remarkText.get() ?: ""
        if (isAgree.get() == false && isReject.get() == false) {
            ToastUtils.showShort("请进行审核!")
            return
        }
        if (isTakeCare) {
            model.inspectTaskCheck(remark, orderIds, isAgree.get() ?: false)
                .compose(RxThreadHelper.rxSchedulerHelper(this))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                    override fun onResult(t: BaseBean<Any>) {
                        ToastUtils.showShort("审核完成")
                        LiveBusCenter.postAuditRefreshEvent("")
                        uC.finishEvent.call();
                    }

                    override fun onFailed(msg: String?) {

                    }
                })
        } else {
            model.checkTaskCheck(remark, orderIds, isAgree.get() ?: false)
                .compose(RxThreadHelper.rxSchedulerHelper(this))
                .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                    override fun onResult(t: BaseBean<Any>) {
                        ToastUtils.showShort("审核完成")
                        LiveBusCenter.postAuditRefreshEvent("")
                        uC.finishEvent.call();
                    }

                    override fun onFailed(msg: String?) {

                    }
                })
        }

    }

    fun dispatch() {
        if (selectMember == null) {
            ToastUtils.showShort("请选择执行人!")
            return
        }
        val remark = remarkText.get() ?: ""
        val userId = selectMember!!.userId
        val username = selectMember!!.userName
        model.assignBatch(remark, orderIds, userId, username)
            .compose(RxThreadHelper.rxSchedulerHelper(this))
            .subscribe(object : ApiSubscriberHelper<BaseBean<Any>>() {
                override fun onResult(t: BaseBean<Any>) {
                    ToastUtils.showShort("派单成功!")
                    LiveBusCenter.postDispatchRefreshEvent("")
                    finish()
                }

                override fun onFailed(msg: String?) {

                }
            })
    }

    fun setMembers(member: MembersBean) {
        selectMember = member
    }

    companion object {
        /**
         * 保养订单
         */
        const val ORDER_TYPE_TAKE_CARE = 1001;

        /**
         * 巡检订单
         */
        const val ORDER_TYPE_PATROL = 1002;

        /**
         * 批量派单
         */
        const val ORDER_BATCH_TYPE_DISPATCH = 101;

        /**
         * 批量审核
         */
        const val ORDER_BATCH_TYPE_AUDIT = 102;

    }
}