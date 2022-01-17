package com.czl.base.data.source.impl

import com.czl.base.base.BaseBean
import com.czl.base.data.api.ApiService
import com.czl.base.data.bean.*
import com.czl.base.data.source.HttpDataSource
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
class HttpDataImpl(private val apiService: ApiService) : HttpDataSource {

    override fun loginByPwd(paramsJson: String): Observable<BaseBean<LoginUser>> {
        var body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramsJson)
        return apiService.loginByPwd(body)
    }

    override fun loginByPhoneCode(paramsJson: String): Observable<BaseBean<LoginUser>> {
        var body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), paramsJson)
        return apiService.loginByPhoneCode(body)
    }

    override fun logout(): Observable<BaseBean<Any?>> {
        return apiService.logout()
    }

    override fun getHotLine(): Observable<BaseBean<String>> {
        return apiService.getHotLine()
    }

    override fun getDeleteNotice(): Observable<BaseBean<String>> {
        return apiService.getDeleteNotice()
    }

    override fun getPhoneCode(phone: String): Observable<BaseBean<Any?>> {
        var body: RequestBody =
            toGsonParams(mapOf("phone" to phone, "templateCode" to "SMS_216373127"))
        return apiService.getPhoneCode(body)
    }

    override fun takeRepair(params: Map<String, String>): Observable<BaseBean<Any?>> {
        var body: RequestBody = toGsonParams(params)
        return apiService.takeRepair(body)
    }

    override fun getMonthPrice(vehicleNo: String, areaId: String): Observable<BaseBean<Float>> {
        return apiService.getMonthPrice(vehicleNo, areaId)
    }

    override fun parkCharging(vehicleNo: String): Observable<BaseBean<PayDetail>> {
        return apiService.parkCharging(vehicleNo)
    }

    override fun getUserRooms(phone: String, areaId: String): Observable<BaseBean<List<RoomBean>>> {
        return apiService.getUserRooms(phone, areaId)
    }

    override fun takeCareOrderReady(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>> {
        val pageParams =
            TakeCarePageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskReady(body)
    }

    override fun patrolOrderReady(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.checkTaskReady(body)
    }

    override fun takeCareOrderExec(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>> {
        val pageParams =
            TakeCarePageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskExec(body)
    }

    override fun patrolOrderExec(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.checkTaskExec(body)
    }


    override fun takeCareOrderFinish(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>> {
        val pageParams =
            TakeCarePageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskFinish(body)
    }

    override fun patrolOrderFinish(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.checkTaskFinish(body)
    }


    override fun takeCareOrderRecord(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareRecordBean>> {
        val pageParams =
            TakeCarePageParams(
                pageNum.toString(),
                pageSize.toString(),
                "",
                TakeCarePageParams.Data(deviceNo = filter)
            )
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskTaskPage(body)
    }

    override fun takeCareOrderDispatchReady(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskUnAssign(body)
    }

    override fun patrolOrderDispatchReady(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.patrolTaskUnAssign(body)
    }

    override fun patrolOrderDispatchFinish(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.patrolTaskAssign(body)
    }

    override fun takeCareOrderDispatchFinish(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskAssigned(body)
    }

    override fun takeCareOrderAuditUnConfirm(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskUnConfirm(body)
    }

    override fun takeCareOrderAuditConfirm(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", filter)
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.inspectTaskConfirm(body)
    }

    override fun checkTaskAuditUnConfirm(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.checkTaskUnConfirm(body)
    }

    override fun checkTaskAuditConfirm(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        val pageParams =
            TakeCareRecordPageParams(pageNum.toString(), pageSize.toString(), "", "")
        val body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.checkTaskConfirm(body)
    }


    override fun workOrderReady(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>> {
        var pageParams =
            PageParams(
                pageNum.toString(),
                pageSize.toString(),
                "",
                PageParams.Data(orderType = type)
            )
        var body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.workOrderReady(body)
    }

    override fun workOrderExec(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>> {
        var pageParams =
            PageParams(
                pageNum.toString(),
                pageSize.toString(),
                "",
                PageParams.Data(orderType = type)
            )
        var body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.workOrderExec(body)
    }

    override fun workOrderFinish(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>> {
        var pageParams =
            PageParams(
                pageNum.toString(),
                pageSize.toString(),
                "",
                PageParams.Data(orderType = type)
            )
        var body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.workOrderFinish(body)
    }

    override fun changeUserInfo(userInfo: UserInfo): Observable<BaseBean<Any?>> {
        var obj: String = Gson().toJson(userInfo)
        var body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj)
        return apiService.changeUserInfo(body)
    }

    override fun retrievePassword(
        phone: String,
        code: String,
        pwd: String
    ): Observable<BaseBean<Any?>> {
        var body: RequestBody =
            toGsonParams(mapOf("phone" to phone, "verifyCode" to code, "newPassword" to pwd))
        return apiService.retrievePassword(body)
    }

    override fun deleteCarList(
        vehicleNos: List<String>,
        areaId: String
    ): Observable<BaseBean<Any?>> {
        val obj: String = Gson().toJson(mapOf("vehicleNos" to vehicleNos, "areaId" to areaId))
        val body: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj)
        return apiService.deleteCarList(body)
    }

    override fun deleteQueryCar(vehicleNo: String, areaId: String): Observable<BaseBean<Any?>> {
        return apiService.deleteQueryCar(vehicleNo, areaId)
    }

    override fun getMyQueryHistory(): Observable<BaseBean<List<CarItem>>> {
        return apiService.getMyQueryHistory()
    }

    override fun getMyCarList(
        vehiclesWithNoPlan: Boolean,
        areaId: String
    ): Observable<BaseBean<List<CarItem>>> {
        return apiService.getMyCarList(vehiclesWithNoPlan, areaId)
    }

    override fun verifyPwdNet(
        newPassword: String,
        oldPassword: String
    ): Observable<BaseBean<Any?>> {
        var body: RequestBody = toGsonParams(
            mapOf(
                "newPassword" to newPassword,
                "oldPassword" to oldPassword
            )
        )

        return apiService.verifyPwdNet(body)
    }

    override fun initPassword(newPassword: String): Observable<BaseBean<Any?>> {
        var body: RequestBody = toGsonParams(
            mapOf(
                "password" to newPassword
            )
        )
        return apiService.initPassword(body)
    }

    private fun toGsonParams(params: Map<String, String>): RequestBody {
        var gson = Gson()
        var obj: String = gson.toJson(
            params
        )
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obj)
    }

    override fun deleteUserAccount(): Observable<BaseBean<Any?>> {
        return apiService.deleteUserAccount()
    }

    override fun addPersonCar(params: Map<String, String>): Observable<BaseBean<Any?>> {
        var body: RequestBody = toGsonParams(params)
        return apiService.addPersonCar(body)
    }

    override fun register(
        username: String,
        password: String,
        repassword: String
    ): Observable<BaseBean<Any?>> {
        return apiService.register(username, password, repassword)
    }

    override fun uploadHeadImg(imgSrc: String): Observable<BaseBean<ImgRspBean>> {
        var file = File(imgSrc)
        // 创建 RequestBody，用于封装构建RequestBody
//        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        var body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return apiService.uploadHeadImg(body)
    }

    override fun getRecordDetail(orderNo: String): Observable<BaseBean<RecordDetailBean>> {
        return apiService.getRecordDetail(orderNo)
    }

    override fun queryPayResult(
        orderNo: String,
        areaId: String
    ): Observable<BaseBean<PayResultBean>> {
        return apiService.queryPayResult(orderNo, areaId)
    }

    override fun getMonthCardList(
        pageNum: Int,
        pageSize: Int,
        areaId: String
    ): Observable<BaseBean<MonthCardListBean>> {
        var pageParams =
            PageParams(pageNum.toString(), pageSize.toString(), "", PageParams.Data(areaId))
        var body: RequestBody =
            RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                Gson().toJson(pageParams)
            )
        return apiService.getMonthCardList(body)
    }

    override fun getRepairList(
        params: Map<String, String>
    ): Observable<BaseBean<RepairListBean>> {
        var body: RequestBody = toGsonParams(params)
        return apiService.getRepairList(body)
    }

    override fun getPayRecordList(
        params: Map<String, String>
    ): Observable<BaseBean<PayRecordListBean>> {
        var body: RequestBody = toGsonParams(params)
        return apiService.getPayRecordList(body)
    }

    override fun placeAnOrder(params: Map<String, String>): Observable<BaseBean<PayInfoBean>> {
        var body: RequestBody = toGsonParams(params)
        return apiService.placeAnOrder(body)
    }

    override fun getVersionName(): Observable<BaseBean<Any?>> {
        return apiService.getVersionName("0")
    }

    override fun getUserInfoNet(): Observable<BaseBean<UserInfo>> {
        return apiService.getUserInfoNet()
    }


    override fun listUser(): Observable<BaseBean<List<MembersBean>>> {
        return apiService.listUser()
    }

    override fun getFullOrder(orderId: String): Observable<BaseBean<WorkOrderDetailBean>> {
        return apiService.getFullOrder(orderId)
    }

    override fun getTakeCareFullOrder(orderId: String): Observable<BaseBean<WorkOrderDetailBean>> {
        return apiService.getTakeCareFullOrder(orderId)
    }

    override fun getPatrolFullOrder(orderId: String): Observable<BaseBean<PatrolOrderDetailBean>> {
        return apiService.getPatrolFullOrder(orderId)
    }

    override fun getAlarmFullOrder(orderId: String): Observable<BaseBean<AlarmOrderDetailBean>> {
        return apiService.getAlarmFullOrder(orderId)
    }
    override fun assign(
        note: String,
        orderId: String,
        userId: String,
        username: String
    ): Observable<BaseBean<Any>> {
        val pageParams = AssignParams(note, orderId, userId, username)
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(pageParams)
        )
        return apiService.assign(body)
    }


    override fun assignBatch(
        note: String,
        orderId: List<String>,
        userId: String,
        username: String
    ): Observable<BaseBean<Any>> {
        val pageParams = AssignBatchParams(note, orderId, userId, username)
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(pageParams)
        )
        return apiService.assignBatch(body)
    }

    override fun workOrderToday(): Observable<BaseBean<Map<String, Int>>> {
        return apiService.workOrderToday()
    }

    override fun inspectTaskCheck(
        note: String,
        orderId: List<String>,
        isOk: Boolean
    ): Observable<BaseBean<Any>> {
        val pageParams = AuditParams(note, orderId, isOk)
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(pageParams)
        )
        return apiService.inspectTaskCheck(body)
    }


    override fun checkTaskCheck(
        note: String,
        orderId: List<String>,
        isOk: Boolean
    ): Observable<BaseBean<Any>> {
        val pageParams = AuditParams(note, orderId, isOk)
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(pageParams)
        )
        return apiService.checkTaskCheck(body)
    }

    override fun workOrderAccept(
        userId: String,
        userName: String,
        member: MembersBean?,
        note: String,
        orderId: String,
        serviceTime: String
    ): Observable<BaseBean<Any>> {
        val params = ReceivingOrderParams(
            member?.userId ?: "",
            member?.userName ?: "",
            note,
            orderId, serviceTime,
            userId, userName
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.workOrderAccept(body)
    }

    override fun workOrderTransfer(
        userId: String,
        userName: String,
        note: String,
        orderId: String,
    ): Observable<BaseBean<Any>> {
        val params = AssignParams(
            note, orderId, userId, userName
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.workOrderTransfer(body)
    }

    override fun inspectTaskRegister(
        handleImage: String,
        handleState: String,
        inspectTaskDetailIdList: List<String>,
    ): Observable<BaseBean<Any>> {
        val params = UploadCertificateParams(
            handleImage, handleState, inspectTaskDetailIdList
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.inspectTaskRegister(body)
    }


    override fun alarmTaskRegister(
        handleImage: String,
        handleInfo: String,
        orderId: String
    ): Observable<BaseBean<Any>> {
        val params = UploadAlarmCertificateParams(
            handleImage, handleInfo, orderId
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.alarmTaskRegister(body)
    }
    override fun inspectTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>> {
        val params = FinishOrderParams(
            orderId
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.inspectTaskFinishOrder(body)
    }
    override fun checkTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>> {
        val params = FinishOrderParams(
            orderId
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.checkTaskFinishOrder(body)
    }
    override fun alarmTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>> {
        val params = FinishOrderParams(
            orderId
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.alarmTaskFinishOrder(body)
    }

    override fun checkTaskRegister(
        checkTaskDetailId: String,
        handleState: String,
        standardList: List<PatrolOrderDetailBean.StandardList>
    ): Observable<BaseBean<Any>> {
        val params = CheckTaskRegisterParams(
            checkTaskDetailId,
            handleState,
            standardList
        );
        val body: RequestBody = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            Gson().toJson(params)
        )
        return apiService.checkTaskRegister(body)
    }


    override fun uploadImg(imgSrc: String): Observable<BaseBean<ImgRspBean>> {
        var file = File(imgSrc)
        // 创建 RequestBody，用于封装构建RequestBody
//        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        var body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return apiService.uploadImg(body)
    }

    override fun workOrderPercent(type: Int): Observable<BaseBean<List<PercentBean>>> {
        return apiService.workOrderPercent(type)
    }

    override fun workOrderTime(): Observable<BaseBean<List<WorkOrderTimeBean>>> {
        return apiService.workOrderTime()
    }
}