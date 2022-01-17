package com.czl.base.event

import com.czl.base.data.bean.MembersBean
import com.czl.base.data.bean.TodoBean
import com.jeremyliao.liveeventbus.core.LiveEvent

/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description 页面通信事件
 */
data class WorkOrderDetailRefreshEvent(val msg: String) : LiveEvent
data class WorkOrderRefreshEvent(val msg: String) : LiveEvent
data class AuditRefreshEvent(val msg: String) : LiveEvent
data class DispatchRefreshEvent(val msg: String) : LiveEvent
data class MembersSelectEvent(val members: List<MembersBean>) : LiveEvent
data class MainEvent(val msg: String?):LiveEvent
data class TokenExpiredEvent(val msg: String?)
data class RegisterSuccessEvent(val account: String?,val pwd:String?):LiveEvent
data class SearchHistoryEvent(val code:Int):LiveEvent
data class LogoutEvent(val code:Int):LiveEvent
data class SetPwdEvent(val code:Int):LiveEvent
data class PayResultEvent(val payType:Int):LiveEvent
data class LoginSuccessEvent(val code:Int):LiveEvent
data class RefreshUserFmEvent(val code:Int):LiveEvent
data class RefreshWebListEvent(val code:Int):LiveEvent
data class RefreshCollectStateEvent(val originId:Int):LiveEvent
data class SwitchReadHistoryEvent(val checked: Boolean):LiveEvent
data class TodoListRefreshEvent(val code: Int, val todoInfo: TodoBean.Data):LiveEvent
data class ModifyUserInfoEvent(val msg: String?): LiveEvent
data class AddCarCompleteEvent(val msg: String?): LiveEvent
data class WxPayCompleteEvent(val msg: String?): LiveEvent
data class RefreshHisCompleteEvent(val msg: String?): LiveEvent
