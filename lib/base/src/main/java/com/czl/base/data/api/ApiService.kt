package com.czl.base.data.api

import com.czl.base.base.BaseBean
import com.czl.base.data.bean.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author Alwyn
 * @Date 2020/7/22
 * @Description
 */
interface ApiService {

    /**
     * 登录
     */
//    @Headers("Domain-Name: douban") // Add the Domain-Name header
//    @POST("system/app/user/login/pwd")
    @POST("auth/appLogin")
    fun loginByPwd(
        @Body info: RequestBody
    ): Observable<BaseBean<LoginUser>>

    /**
     * 验证码登录
     */
    @POST("system/app/user/login/vc")
    fun loginByPhoneCode(
        @Body info: RequestBody
    ): Observable<BaseBean<LoginUser>>

    /**
     * 删除个人车辆
     */
    @POST("parking/app/parking/vehicle/delete")
    fun deleteCarList(
        @Body info: RequestBody
    ): Observable<BaseBean<Any?>>

    /**
     * 我的查询历史车辆列表
     */
    @POST("parking/app/parking/vehicle/myQueryHistory")
    fun getMyQueryHistory(): Observable<BaseBean<List<CarItem>>>

    /**
     * 获取用户详细信息
     */
    @GET("system/user/profile")
    fun getUserInfoNet(): Observable<BaseBean<UserInfo>>

    /**
     * 获取热线
     */
    @GET("system/app/user/hotLine")
    fun getHotLine(): Observable<BaseBean<String>>

    /**
     * 获取注销须知
     */
    @GET("system/app/user/logout/notice")
    fun getDeleteNotice(): Observable<BaseBean<String>>

    /**
     * 退出登录
     */
    @DELETE("system/app/user/logout")
    fun logout(): Observable<BaseBean<Any?>>

    /**
     * 获取手机验证码
     */
    @POST("system/app/user/verifyCode")
    fun getPhoneCode(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 修改用户信息
     */
    @POST("system/app/user/changeUserInfo")
    fun changeUserInfo(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 找回密码
     */
    @POST("system/app/user/retrievePassword")
    fun retrievePassword(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 修改密码
     */
    @POST("system/app/user/changePassword")
    fun verifyPwdNet(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 初始化密码
     */
    @POST("system/app/user/initPassword")
    fun initPassword(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 添加个人车辆
     */
    @POST("parking/app/parking/vehicle/add")
    fun addPersonCar(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 物业接单
     */
    @POST("property/workOrder/repair/take")
    fun takeRepair(@Body info: RequestBody): Observable<BaseBean<Any?>>

    /**
     * 获取月卡列表
     */
    @POST("parking/app/parking/parkingCard/myCardList")
    fun getMonthCardList(@Body info: RequestBody): Observable<BaseBean<MonthCardListBean>>

    /**
     * 缴费记录
     */
    @POST("payment/orders/parking/list")
    fun getPayRecordList(@Body info: RequestBody): Observable<BaseBean<PayRecordListBean>>

    /**
     * APP查询报修记录
     */
    @POST("property/workOrder/repair/list")
    fun getRepairList(@Body info: RequestBody): Observable<BaseBean<RepairListBean>>

    /**
     * 下订单
     */
    @POST("parking/parking/pay/placeAnOrder")
    fun placeAnOrder(@Body info: RequestBody): Observable<BaseBean<PayInfoBean>>

    /**
     * 注销用户账户
     */
    @DELETE("system/app/user/deleteUserAccount")
    fun deleteUserAccount(): Observable<BaseBean<Any?>>

    @Multipart
    @POST("file/upload")
    fun uploadHeadImg(@Part file: MultipartBody.Part): Observable<BaseBean<ImgRspBean>>

    /**
     * 注册
     */
    @POST("user/register")
    @FormUrlEncoded
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): Observable<BaseBean<Any?>>

    /**
     * 查询个人车辆
     */
    @POST("parking/app/parking/vehicle/myList")
    @FormUrlEncoded
    fun getMyCarList(
        @Field("vehiclesWithNoPlan") vehiclesWithNoPlan: Boolean,
        @Field("areaId") areaId: String
    ): Observable<BaseBean<List<CarItem>>>

    /**
     * 获取版本号
     */
    @GET("system/app/user/version")
    fun getVersionName(@Query("type") type: String): Observable<BaseBean<Any?>>

    /**
     * 删除停车查询记录
     */
    @GET("parking/parking/pay/deleteQueryRecord")
    fun deleteQueryCar(
        @Query("vehicleNo") vehicleNo: String,
        @Query("areaId") areaId: String
    ): Observable<BaseBean<Any?>>

    /**
     * 获取缴费详情
     */
    @GET("payment/orders/parking/detail/{orderNo}")
    fun getRecordDetail(@Path("orderNo") orderNo: String): Observable<BaseBean<RecordDetailBean>>

    /**
     * 查询订单支付结果
     */
    @POST("payment/allinpay/query")
    @FormUrlEncoded
    fun queryPayResult(
        @Field("orderNo") orderNo: String,
        @Field("areaId") areaId: String
    ): Observable<BaseBean<PayResultBean>>

    /**
     * 添加月卡时，计算月卡价格
     */
    @GET("parking/app/parking/parkingCard/feePerMonth")
    fun getMonthPrice(
        @Query("vehicleNo") vehicleNo: String,
        @Query("areaId") areaId: String
    ): Observable<BaseBean<Float>>

    /**
     * 请求计费
     */
    @GET("parking/parking/pay/parkCharging")
    fun parkCharging(
        @Query("vehicleNo") vehicleNo: String
    ): Observable<BaseBean<PayDetail>>

    /**
     * 根据业主ID查询业主关联房屋信息
     */
    @GET("system/owner/list/byOwnerPhone")
    fun getUserRooms(
        @Query("phone") phone: String,
        @Query("areaId") areaId: String,
    ): Observable<BaseBean<List<RoomBean>>>



    /**
     */
    @POST("property/inspectTask/ready")
    fun inspectTaskReady(@Body info: RequestBody): Observable<BaseBean<TakeCareBean>>

    /**
     */
    @POST("property/checkTask/ready")
    fun checkTaskReady(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>
    /**
     */
    @POST("property/inspectTask/exec")
    fun inspectTaskExec(@Body info: RequestBody): Observable<BaseBean<TakeCareBean>>

    /**
     */
    @POST("property/checkTask/exec")
    fun checkTaskExec(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>

    /**
     */
    @POST("property/inspectTask/finish")
    fun inspectTaskFinish(@Body info: RequestBody): Observable<BaseBean<TakeCareBean>>

    /**
     */
    @POST("property/checkTask/finish")
    fun checkTaskFinish(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>

    /**
     */
    @POST("property/inspectTask/taskPage")
    fun inspectTaskTaskPage(@Body info: RequestBody): Observable<BaseBean<TakeCareRecordBean>>

    /**
     */
    @POST("property/inspectTask/unAssign")
    fun inspectTaskUnAssign(@Body info: RequestBody): Observable<BaseBean<TakeCareDispatchBean>>

    /**
     */
    @POST("property/checkTask/unAssign")
    fun patrolTaskUnAssign(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>

    /**
     */
    @POST("property/checkTask/assigned")
    fun patrolTaskAssign(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>

    /**
     */
    @POST("property/inspectTask/unConfirm")
    fun inspectTaskUnConfirm(@Body info: RequestBody): Observable<BaseBean<TakeCareDispatchBean>>

    /**
     */
    @POST("property/inspectTask/confirm")
    fun inspectTaskConfirm(@Body info: RequestBody): Observable<BaseBean<TakeCareDispatchBean>>

    /**
     */
    @POST("property/checkTask/unConfirm")
    fun checkTaskUnConfirm(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>

    /**
     */
    @POST("property/checkTask/confirm")
    fun checkTaskConfirm(@Body info: RequestBody): Observable<BaseBean<PatrolBean>>

    /**
     */
    @POST("property/inspectTask/assigned")
    fun inspectTaskAssigned(@Body info: RequestBody): Observable<BaseBean<TakeCareDispatchBean>>

    /**
     */
    @POST("property/workOrder/ready")
    fun workOrderReady(@Body info: RequestBody): Observable<BaseBean<WorkOrderBean>>

    /**
     */
    @POST("property/workOrder/exec")
    fun workOrderExec(@Body info: RequestBody): Observable<BaseBean<WorkOrderBean>>

    /**
     */
    @POST("property/workOrder/finish")
    fun workOrderFinish(@Body info: RequestBody): Observable<BaseBean<WorkOrderBean>>

    /**
     */
    @GET("property/sys/common/listUser")
    fun listUser(): Observable<BaseBean<List<MembersBean>>>


    /**
     */
    @GET("property/common/proWorkOrder/getFullOrder/{orderId}")
    fun getFullOrder(@Path("orderId") orderId: String): Observable<BaseBean<WorkOrderDetailBean>>

    /**
     */
    @GET("property/inspectTask/getOrder/{orderId}")
    fun getTakeCareFullOrder(@Path("orderId") orderId: String): Observable<BaseBean<WorkOrderDetailBean>>


    /**
     */
    @GET("property/checkTask/getOrder/{orderId}")
    fun getPatrolFullOrder(@Path("orderId") orderId: String): Observable<BaseBean<PatrolOrderDetailBean>>

    /**
     */
    @POST("property/common/proWorkOrder/assign")
    fun assign(@Body info: RequestBody): Observable<BaseBean<Any>>

    /**
     */
    @POST("property/common/proWorkOrder/assignBatch")
    fun assignBatch(@Body info: RequestBody): Observable<BaseBean<Any>>

    /**
     */
    @POST("property/inspectTask/check")
    fun inspectTaskCheck(@Body info: RequestBody): Observable<BaseBean<Any>>

    /**
     */
    @POST("property/checkTask/check")
    fun checkTaskCheck(@Body info: RequestBody): Observable<BaseBean<Any>>
    /**
     * 获取月卡列表
     */
    @GET("property/workOrder/today")
    fun workOrderToday(): Observable<BaseBean<Map<String, Int>>>
    /**
     */
    @POST("property/common/proWorkOrder/accept")
    fun workOrderAccept(@Body info: RequestBody): Observable<BaseBean<Any>>

    @GET("property/workOrder/percent")
    fun workOrderPercent(@Query("type") type: Int):Observable<BaseBean<List<PercentBean>>>

    @GET("property/workOrder/time")
    fun workOrderTime():Observable<BaseBean<List<WorkOrderTimeBean>>>

    /**
     */
    @POST("property/common/proWorkOrder/transfer")
    fun workOrderTransfer(@Body info: RequestBody): Observable<BaseBean<Any>>

    /**
     */
    @POST("property/inspectTask/register")
    fun inspectTaskRegister(@Body info: RequestBody): Observable<BaseBean<Any>>
    /**
     */
    @POST("property/alarmRecord/register")
    fun alarmTaskRegister(@Body info: RequestBody): Observable<BaseBean<Any>>
    /**
     */
    @POST("property/inspectTask/finishOrder")
    fun inspectTaskFinishOrder(@Body info: RequestBody): Observable<BaseBean<Any>>
    /**
     */
    @POST("property/checkTask/finishOrder")
    fun checkTaskFinishOrder(@Body info: RequestBody): Observable<BaseBean<Any>>
    /**
     */
    @POST("property/alarmRecord/finishOrder")
    fun alarmTaskFinishOrder(@Body info: RequestBody): Observable<BaseBean<Any>>
    /**
     */
    @POST("property/checkTask/register")
    fun checkTaskRegister(@Body info: RequestBody): Observable<BaseBean<Any>>

    /**
     */
    @GET("property/alarmRecord/getOrder/{orderId}")
    fun getAlarmFullOrder(@Path("orderId") orderId: String): Observable<BaseBean<AlarmOrderDetailBean>>
    @Multipart
    @Headers("url_name:img")
    @POST("zhsq-api/file/upload")
    fun uploadImg(@Part file: MultipartBody.Part): Observable<BaseBean<ImgRspBean>>
}