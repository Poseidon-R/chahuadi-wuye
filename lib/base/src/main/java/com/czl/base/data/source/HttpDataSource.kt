package com.czl.base.data.source

import com.czl.base.base.BaseBean
import com.czl.base.data.bean.*
import io.reactivex.Observable

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface HttpDataSource {
    fun loginByPwd(paramsJson: String): Observable<BaseBean<LoginUser>>
    fun loginByPhoneCode(paramsJson: String): Observable<BaseBean<LoginUser>>
    fun getPhoneCode(phone: String): Observable<BaseBean<Any?>>
    fun takeRepair(params: Map<String, String>): Observable<BaseBean<Any?>>
    fun getMonthPrice(vehicleNo: String, areaId: String): Observable<BaseBean<Float>>
    fun parkCharging(vehicleNo: String): Observable<BaseBean<PayDetail>>
    fun retrievePassword(phone: String, code: String, pwd: String): Observable<BaseBean<Any?>>
    fun getMyCarList(
        vehiclesWithNoPlan: Boolean,
        areaId: String
    ): Observable<BaseBean<List<CarItem>>>

    fun deleteCarList(vehicleNos: List<String>, areaId: String): Observable<BaseBean<Any?>>
    fun deleteQueryCar(vehicleNo: String, areaId: String): Observable<BaseBean<Any?>>
    fun getMyQueryHistory(): Observable<BaseBean<List<CarItem>>>
    fun logout(): Observable<BaseBean<Any?>>
    fun getHotLine(): Observable<BaseBean<String>>
    fun getDeleteNotice(): Observable<BaseBean<String>>
    fun verifyPwdNet(
        newPassword: String,
        oldPassword: String
    ): Observable<BaseBean<Any?>>

    fun initPassword(
        newPassword: String
    ): Observable<BaseBean<Any?>>

    fun deleteUserAccount(): Observable<BaseBean<Any?>>
    fun addPersonCar(params: Map<String, String>): Observable<BaseBean<Any?>>
    fun getUserRooms(phone: String, areaId: String): Observable<BaseBean<List<RoomBean>>>
    fun register(username: String, password: String, repassword: String): Observable<BaseBean<Any?>>
    fun getRecordDetail(orderNo: String): Observable<BaseBean<RecordDetailBean>>
    fun queryPayResult(orderNo: String, areaId: String): Observable<BaseBean<PayResultBean>>
    fun getMonthCardList(
        pageNum: Int,
        pageSize: Int,
        areaId: String
    ): Observable<BaseBean<MonthCardListBean>>

    fun getRepairList(
        params: Map<String, String>
    ): Observable<BaseBean<RepairListBean>>

    fun getPayRecordList(
        params: Map<String, String>
    ): Observable<BaseBean<PayRecordListBean>>

    fun placeAnOrder(
        params: Map<String, String>
    ): Observable<BaseBean<PayInfoBean>>

    fun getVersionName(): Observable<BaseBean<Any?>>
    fun getUserInfoNet(): Observable<BaseBean<UserInfo>>
    fun uploadHeadImg(imgSrc: String): Observable<BaseBean<ImgRspBean>>
    fun changeUserInfo(userInfo: UserInfo): Observable<BaseBean<Any?>>

    fun takeCareOrderReady(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>>

    fun patrolOrderReady(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun takeCareOrderExec(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>>

    fun patrolOrderExec(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun takeCareOrderFinish(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>>

    fun patrolOrderFinish(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun takeCareOrderRecord(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareRecordBean>>

    fun takeCareOrderDispatchReady(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>>

    fun patrolOrderDispatchReady(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun patrolOrderDispatchFinish(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun takeCareOrderDispatchFinish(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>>


    fun takeCareOrderAuditUnConfirm(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>>

    fun takeCareOrderAuditConfirm(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>>


    fun checkTaskAuditUnConfirm(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun checkTaskAuditConfirm(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>>

    fun workOrderReady(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>>

    fun workOrderExec(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>>

    fun workOrderFinish(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>>

    fun listUser(): Observable<BaseBean<List<MembersBean>>>

    fun getFullOrder(orderId: String): Observable<BaseBean<WorkOrderDetailBean>>
    fun workOrderToday(): Observable<BaseBean<Map<String, Int>>>
    fun getTakeCareFullOrder(orderId: String): Observable<BaseBean<WorkOrderDetailBean>>

    fun getPatrolFullOrder(orderId: String): Observable<BaseBean<PatrolOrderDetailBean>>

    fun getAlarmFullOrder(orderId: String): Observable<BaseBean<AlarmOrderDetailBean>>

    fun assign(
        note: String,
        orderId: String,
        userId: String,
        username: String
    ): Observable<BaseBean<Any>>

    fun assignBatch(
        note: String,
        orderId: List<String>,
        userId: String,
        username: String
    ): Observable<BaseBean<Any>>

    fun inspectTaskCheck(
        note: String,
        orderId: List<String>,
        isOk: Boolean
    ): Observable<BaseBean<Any>>

    fun checkTaskCheck(
        note: String,
        orderId: List<String>,
        isOk: Boolean
    ): Observable<BaseBean<Any>>

    fun workOrderAccept(
        userId: String,
        userName: String,
        member: MembersBean?,
        note: String,
        orderId: String,
        serviceTime: String
    ): Observable<BaseBean<Any>>

    fun workOrderTransfer(
        userId: String,
        userName: String,
        note: String,
        orderId: String,
    ): Observable<BaseBean<Any>>

    fun inspectTaskRegister(
        handleImage: String,
        handleState: String,
        inspectTaskDetailIdList: List<String>,
    ): Observable<BaseBean<Any>>

    fun alarmTaskRegister(
        handleImage: String,
        handleInfo: String,
        orderId: String
    ): Observable<BaseBean<Any>>


    fun inspectTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>>

    fun checkTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>>

    fun alarmTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>>

    fun checkTaskRegister(
        checkTaskDetailId: String,
        handleState: String,
        standardList: List<PatrolOrderDetailBean.StandardList>
    ): Observable<BaseBean<Any>>


    fun uploadImg(imgSrc: String): Observable<BaseBean<ImgRspBean>>

    fun workOrderPercent(type: Int):Observable<BaseBean<List<PercentBean>>>

    fun workOrderTime():Observable<BaseBean<List<WorkOrderTimeBean>>>
}