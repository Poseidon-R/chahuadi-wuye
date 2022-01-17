package com.czl.base.data

import com.czl.base.base.BaseBean
import com.czl.base.base.BaseModel
import com.czl.base.data.bean.*
import com.czl.base.data.db.SearchHistoryEntity
import com.czl.base.data.db.WebHistoryEntity
import com.czl.base.data.source.HttpDataSource
import com.czl.base.data.source.LocalDataSource
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description 数据中心（本地+在线） 外部通过Koin依赖注入调用
 * 对于缓存或者在线数据的增删查改统一通过该数据仓库调用
 */
class DataRepository constructor(
    private val mLocalDataSource: LocalDataSource,
    private val mHttpDataSource: HttpDataSource
) : BaseModel(), LocalDataSource, HttpDataSource {

    //    companion object {
//        @Volatile
//        private var INSTANCE: DataRepository? = null
//        fun getInstance(
//            localDataSource: LocalDataSource,
//            httpDataSource: HttpDataSource
//        ): DataRepository? {
//            if (INSTANCE == null) {
//                synchronized(DataRepository::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = DataRepository(localDataSource, httpDataSource)
//                    }
//                }
//            }
//            return INSTANCE
//        }
//    }


    override fun logout(): Observable<BaseBean<Any?>> {
        return mHttpDataSource.logout()
    }

    override fun getHotLine(): Observable<BaseBean<String>> {
        return mHttpDataSource.getHotLine()
    }

    override fun getDeleteNotice(): Observable<BaseBean<String>> {
        return mHttpDataSource.getDeleteNotice()
    }

    override fun verifyPwdNet(
        newPassword: String,
        oldPassword: String
    ): Observable<BaseBean<Any?>> {
        return mHttpDataSource.verifyPwdNet(newPassword, oldPassword)
    }

    override fun initPassword(
        newPassword: String
    ): Observable<BaseBean<Any?>> {
        return mHttpDataSource.initPassword(newPassword)
    }

    override fun deleteUserAccount(): Observable<BaseBean<Any?>> {
        return mHttpDataSource.deleteUserAccount()
    }

    override fun addPersonCar(params: Map<String, String>): Observable<BaseBean<Any?>> {
        return mHttpDataSource.addPersonCar(params)
    }

    override fun getUserRooms(phone: String, areaId: String): Observable<BaseBean<List<RoomBean>>> {
        return mHttpDataSource.getUserRooms(phone, areaId)
    }


    override fun register(
        username: String,
        password: String,
        repassword: String
    ): Observable<BaseBean<Any?>> {
        return mHttpDataSource.register(username, password, repassword)
    }

    override fun getRecordDetail(orderNo: String): Observable<BaseBean<RecordDetailBean>> {
        return mHttpDataSource.getRecordDetail(orderNo)
    }

    override fun queryPayResult(
        orderNo: String,
        areaId: String
    ): Observable<BaseBean<PayResultBean>> {
        return mHttpDataSource.queryPayResult(orderNo, areaId)
    }

    override fun getMonthCardList(
        pageNum: Int,
        pageSize: Int,
        areaId: String
    ): Observable<BaseBean<MonthCardListBean>> {
        return mHttpDataSource.getMonthCardList(pageNum, pageSize, areaId)
    }

    override fun getRepairList(
        params: Map<String, String>
    ): Observable<BaseBean<RepairListBean>> {
        return mHttpDataSource.getRepairList(params)
    }

    override fun getPayRecordList(
        params: Map<String, String>
    ): Observable<BaseBean<PayRecordListBean>> {
        return mHttpDataSource.getPayRecordList(params)
    }

    override fun placeAnOrder(
        params: Map<String, String>
    ): Observable<BaseBean<PayInfoBean>> {
        return mHttpDataSource.placeAnOrder(params)
    }

    override fun getMyCarList(
        vehiclesWithNoPlan: Boolean,
        areaId: String
    ): Observable<BaseBean<List<CarItem>>> {
        return mHttpDataSource.getMyCarList(vehiclesWithNoPlan, areaId)
    }

    override fun deleteCarList(
        vehicleNos: List<String>,
        areaId: String
    ): Observable<BaseBean<Any?>> {
        return mHttpDataSource.deleteCarList(vehicleNos, areaId)
    }

    override fun deleteQueryCar(vehicleNo: String, areaId: String): Observable<BaseBean<Any?>> {
        return mHttpDataSource.deleteQueryCar(vehicleNo, areaId)
    }

    override fun getMyQueryHistory(): Observable<BaseBean<List<CarItem>>> {
        return mHttpDataSource.getMyQueryHistory()
    }

    override fun getVersionName(): Observable<BaseBean<Any?>> {
        return mHttpDataSource.getVersionName()
    }


    override fun getLocalData(): String {
        return mLocalDataSource.getLocalData()
    }

    override fun getLoginName(): String? {
        return mLocalDataSource.getLoginName()
    }

    override fun getLoginPhone(): String? {
        return mLocalDataSource.getLoginPhone()
    }

    override fun getLoginToken(): String? {
        return mLocalDataSource.getLoginToken()
    }

    override fun saveUserData(userBean: LoginUser) {
        mLocalDataSource.saveUserData(userBean)
    }

    override fun getLoginPwd(): String? {
        return mLocalDataSource.getLoginPwd()
    }

    override fun saveLoginPwd(pwd: String) {
        mLocalDataSource.saveLoginPwd(pwd)
    }

    override fun saveIsRememberPwd(isRemember: Boolean) {
        mLocalDataSource.saveIsRememberPwd(isRemember)
    }

    override fun saveRoomIdAndCode(idAndCode: String) {
        mLocalDataSource.saveRoomIdAndCode(idAndCode)
    }

    override fun isRememberPwd(): Boolean {
        return mLocalDataSource.isRememberPwd()
    }

    override fun getRoomIdAndCode(): String? {
        return mLocalDataSource.getRoomIdAndCode()
    }

    override fun saveUserInfoData(userInfo: UserInfo) {
        mLocalDataSource.saveUserInfoData(userInfo)
    }

    override fun getUserData(): LoginUser? {
        return mLocalDataSource.getUserData()
    }

    override fun getUserId(): Int {
        return mLocalDataSource.getUserId()
    }

    override fun clearLoginState() {
        mLocalDataSource.clearLoginState()
    }

    override fun clearAllData() {
        mLocalDataSource.clearAllData()
    }

    override fun saveUserSearchHistory(keyword: String): Flowable<Boolean> {
        return mLocalDataSource.saveUserSearchHistory(keyword)
    }

    override fun getSearchHistoryByUid(): Flowable<List<SearchHistoryEntity>> {
        return mLocalDataSource.getSearchHistoryByUid()
    }

    override fun deleteSearchHistory(history: String): Disposable {
        return mLocalDataSource.deleteSearchHistory(history)
    }

    override fun deleteAllSearchHistory(): Observable<Int> {
        return mLocalDataSource.deleteAllSearchHistory()
    }

    override fun saveUserBrowseHistory(title: String, link: String) {
        return mLocalDataSource.saveUserBrowseHistory(title, link)
    }

    override fun getUserBrowseHistoryByUid(): Flowable<List<WebHistoryEntity>> {
        return mLocalDataSource.getUserBrowseHistoryByUid()
    }

    override fun deleteBrowseHistory(title: String, link: String): Observable<Int> {
        return mLocalDataSource.deleteBrowseHistory(title, link)
    }

    override fun deleteAllWebHistory(): Observable<Int> {
        return mLocalDataSource.deleteAllWebHistory()
    }

    override fun saveFollowSysModeFlag(isFollow: Boolean) {
        return mLocalDataSource.saveFollowSysModeFlag(isFollow)
    }

    override fun getFollowSysUiModeFlag(): Boolean {
        return mLocalDataSource.getFollowSysUiModeFlag()
    }

    override fun saveUiMode(nightModeFlag: Boolean) {
        return mLocalDataSource.saveUiMode(nightModeFlag)
    }

    override fun getUiMode(): Boolean {
        return mLocalDataSource.getUiMode()
    }

    override fun saveReadHistoryState(visible: Boolean) {
        return mLocalDataSource.saveReadHistoryState(visible)
    }

    override fun getReadHistoryState(): Boolean {
        return mLocalDataSource.getReadHistoryState()
    }

    override fun savePrivacyState(visible: Boolean) {
        return mLocalDataSource.savePrivacyState(visible)
    }

    override fun getPrivacyState(): Boolean {
        return mLocalDataSource.getPrivacyState()
    }

    override fun getAreaName(): String {
        return mLocalDataSource.getAreaName()
    }

    override fun saveAreaName(areaName: String) {
        return mLocalDataSource.saveAreaName(areaName)
    }

    override fun getAreaId(): String {
        return mLocalDataSource.getAreaId()
    }

    override fun saveAreaId(areaId: String) {
        return mLocalDataSource.saveAreaId(areaId)
    }

    override fun getAreaList(): List<AreaIdBean> {
        return mLocalDataSource.getAreaList()
    }

    override fun getLoginOutFlag(): Int {
        return mLocalDataSource.getLoginOutFlag()
    }

    override fun saveLoginOutFlag(loginOutFlag: Int) {
        return mLocalDataSource.saveLoginOutFlag(loginOutFlag)
    }

    override fun takeRepair(params: Map<String, String>): Observable<BaseBean<Any?>> {
        return mHttpDataSource.takeRepair(params)
    }


    override fun saveLoginToken(token: String) {
        return mLocalDataSource.saveLoginToken(token)
    }

    override fun <T : Serializable> saveCacheListData(list: List<T>) {
        return mLocalDataSource.saveCacheListData(list)
    }

    override fun <T : Serializable> getCacheListData(key: String): List<T>? {
        return mLocalDataSource.getCacheListData(key)
    }

    override fun loginByPwd(paramsJson: String): Observable<BaseBean<LoginUser>> {
        return mHttpDataSource.loginByPwd(paramsJson)
    }

    override fun loginByPhoneCode(paramsJson: String): Observable<BaseBean<LoginUser>> {
        return mHttpDataSource.loginByPhoneCode(paramsJson)
    }

    override fun getPhoneCode(phone: String): Observable<BaseBean<Any?>> {
        return mHttpDataSource.getPhoneCode(phone)
    }

    override fun getMonthPrice(vehicleNo: String, areaId: String): Observable<BaseBean<Float>> {
        return mHttpDataSource.getMonthPrice(vehicleNo, areaId)
    }

    override fun parkCharging(vehicleNo: String): Observable<BaseBean<PayDetail>> {
        return mHttpDataSource.parkCharging(vehicleNo)
    }

    override fun retrievePassword(
        phone: String,
        code: String,
        pwd: String
    ): Observable<BaseBean<Any?>> {
        return mHttpDataSource.retrievePassword(phone, code, pwd)
    }

    override fun getUserInfoNet(): Observable<BaseBean<UserInfo>> {
        return mHttpDataSource.getUserInfoNet()
    }

    override fun getLocalUserInfo(): UserInfo? {
        return mLocalDataSource.getLocalUserInfo()
    }

    override fun uploadHeadImg(imgSrc: String): Observable<BaseBean<ImgRspBean>> {
        return mHttpDataSource.uploadHeadImg(imgSrc)
    }

    override fun changeUserInfo(userInfo: UserInfo): Observable<BaseBean<Any?>> {
        return mHttpDataSource.changeUserInfo(userInfo)
    }

    override fun takeCareOrderReady(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>> {
        return mHttpDataSource.takeCareOrderReady(pageNum, pageSize, filter)
    }

    override fun patrolOrderExec(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.patrolOrderExec(pageNum, pageSize)
    }

    override fun takeCareOrderExec(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>> {
        return mHttpDataSource.takeCareOrderExec(pageNum, pageSize, filter)
    }

    override fun takeCareOrderFinish(
        pageNum: Int,
        pageSize: Int,
        filter: TakeCarePageParams.Data
    ): Observable<BaseBean<TakeCareBean>> {
        return mHttpDataSource.takeCareOrderFinish(pageNum, pageSize, filter)
    }

    override fun patrolOrderFinish(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.patrolOrderFinish(pageNum, pageSize)
    }

    override fun takeCareOrderRecord(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareRecordBean>> {
        return mHttpDataSource.takeCareOrderRecord(pageNum, pageSize, filter)
    }

    override fun takeCareOrderDispatchReady(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        return mHttpDataSource.takeCareOrderDispatchReady(pageNum, pageSize, filter)
    }

    override fun patrolOrderDispatchReady(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.patrolOrderDispatchReady(pageNum, pageSize)
    }

    override fun patrolOrderDispatchFinish(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.patrolOrderDispatchFinish(pageNum, pageSize)
    }

    override fun takeCareOrderDispatchFinish(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        return mHttpDataSource.takeCareOrderDispatchFinish(pageNum, pageSize, filter)
    }

    override fun takeCareOrderAuditUnConfirm(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        return mHttpDataSource.takeCareOrderAuditUnConfirm(pageNum, pageSize, filter)
    }

    override fun takeCareOrderAuditConfirm(
        pageNum: Int,
        pageSize: Int,
        filter: String
    ): Observable<BaseBean<TakeCareDispatchBean>> {
        return mHttpDataSource.takeCareOrderAuditConfirm(pageNum, pageSize, filter)
    }

    override fun checkTaskAuditUnConfirm(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.checkTaskAuditUnConfirm(pageNum, pageSize)
    }

    override fun checkTaskAuditConfirm(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.checkTaskAuditConfirm(pageNum, pageSize)
    }

    override fun workOrderReady(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>> {
        return mHttpDataSource.workOrderReady(pageNum, pageSize, type)
    }

    override fun workOrderExec(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>> {
        return mHttpDataSource.workOrderExec(pageNum, pageSize, type)
    }

    override fun workOrderFinish(
        pageNum: Int,
        pageSize: Int,
        type: String
    ): Observable<BaseBean<WorkOrderBean>> {
        return mHttpDataSource.workOrderFinish(pageNum, pageSize, type)
    }

    override fun listUser(): Observable<BaseBean<List<MembersBean>>> {
        return mHttpDataSource.listUser()
    }

    override fun getFullOrder(orderId: String): Observable<BaseBean<WorkOrderDetailBean>> {
        return mHttpDataSource.getFullOrder(orderId)
    }

    override fun getTakeCareFullOrder(orderId: String): Observable<BaseBean<WorkOrderDetailBean>> {
        return mHttpDataSource.getTakeCareFullOrder(orderId)
    }

    override fun getPatrolFullOrder(orderId: String): Observable<BaseBean<PatrolOrderDetailBean>> {
        return mHttpDataSource.getPatrolFullOrder(orderId)
    }

    override fun getAlarmFullOrder(orderId: String): Observable<BaseBean<AlarmOrderDetailBean>> {
        return mHttpDataSource.getAlarmFullOrder(orderId)
    }

    override fun workOrderToday(): Observable<BaseBean<Map<String, Int>>> {
        return mHttpDataSource.workOrderToday()
    }


    override fun assign(
        note: String,
        orderId: String,
        userId: String,
        username: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.assign(note, orderId, userId, username)
    }

    override fun assignBatch(
        note: String,
        orderId: List<String>,
        userId: String,
        username: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.assignBatch(note, orderId, userId, username)
    }

    override fun inspectTaskCheck(
        note: String,
        orderId: List<String>,
        isOk: Boolean
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.inspectTaskCheck(note, orderId, isOk)
    }

    override fun checkTaskCheck(
        note: String,
        orderId: List<String>,
        isOk: Boolean
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.checkTaskCheck(note, orderId, isOk)
    }

    override fun patrolOrderReady(
        pageNum: Int,
        pageSize: Int,
    ): Observable<BaseBean<PatrolBean>> {
        return mHttpDataSource.patrolOrderReady(pageNum, pageSize)
    }

    override fun workOrderAccept(
        userId: String,
        userName: String,
        member: MembersBean?,
        note: String,
        orderId: String,
        serviceTime: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.workOrderAccept(userId, userName, member, note, orderId, serviceTime)
    }

    override fun workOrderPercent(type: Int): Observable<BaseBean<List<PercentBean>>> {
        return mHttpDataSource.workOrderPercent(type)
    }

    override fun workOrderTime(): Observable<BaseBean<List<WorkOrderTimeBean>>> {
        return mHttpDataSource.workOrderTime()
    }

    override fun workOrderTransfer(
        userId: String,
        userName: String,
        note: String,
        orderId: String,
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.workOrderTransfer(userId, userName, note, orderId)
    }

    override fun inspectTaskRegister(
        handleImage: String,
        handleState: String,
        inspectTaskDetailIdList: List<String>
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.inspectTaskRegister(
            handleImage,
            handleState,
            inspectTaskDetailIdList
        )
    }

    override fun alarmTaskRegister(
        handleImage: String,
        handleInfo: String,
        orderId: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.alarmTaskRegister(
            handleImage,
            handleInfo,
            orderId
        )
    }

    override fun inspectTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.inspectTaskFinishOrder(
            orderId
        )
    }

    override fun checkTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.checkTaskFinishOrder(
            orderId
        )
    }

    override fun alarmTaskFinishOrder(
        orderId: String
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.alarmTaskFinishOrder(
            orderId
        )
    }
    override fun checkTaskRegister(
        checkTaskDetailId: String,
        handleState: String,
        standardList: List<PatrolOrderDetailBean.StandardList>
    ): Observable<BaseBean<Any>> {
        return mHttpDataSource.checkTaskRegister(checkTaskDetailId, handleState, standardList)
    }

    override fun uploadImg(imgSrc: String): Observable<BaseBean<ImgRspBean>> {
        return mHttpDataSource.uploadImg(imgSrc)
    }
}