package com.czl.base.data.source

import com.czl.base.data.bean.AreaIdBean
import com.czl.base.data.bean.LoginUser
import com.czl.base.data.bean.UserInfo
import com.czl.base.data.db.SearchHistoryEntity
import com.czl.base.data.db.WebHistoryEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.Serializable

/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
interface LocalDataSource {
    fun getLocalData(): String
    fun getLoginName(): String?
    fun getLoginPhone(): String?
    fun getLoginToken(): String?
    fun getLoginPwd(): String?
    fun saveUserData(userBean: LoginUser)
    fun saveLoginPwd(pwd: String)
    fun saveIsRememberPwd(isRemember:Boolean)
    fun saveRoomIdAndCode(idAndCode: String)
    fun isRememberPwd():Boolean
    fun getRoomIdAndCode(): String?
    fun saveUserInfoData(userInfo: UserInfo)
    fun getUserData(): LoginUser?
    fun getLocalUserInfo(): UserInfo?
    fun getUserId(): Int
    fun clearLoginState()
    fun clearAllData()
    fun saveUserSearchHistory(keyword: String): Flowable<Boolean>
    fun getSearchHistoryByUid(): Flowable<List<SearchHistoryEntity>>
    fun deleteSearchHistory(history: String): Disposable
    fun deleteAllSearchHistory(): Observable<Int>
    fun saveUserBrowseHistory(title: String, link: String)
    fun getUserBrowseHistoryByUid(): Flowable<List<WebHistoryEntity>>
    fun deleteBrowseHistory(title: String, link: String): Observable<Int>
    fun deleteAllWebHistory(): Observable<Int>
    fun saveFollowSysModeFlag(isFollow: Boolean = true)
    fun getFollowSysUiModeFlag(): Boolean
    fun saveUiMode(nightModeFlag: Boolean = false)
    fun getUiMode(): Boolean
    fun saveReadHistoryState(visible: Boolean)
    fun getReadHistoryState(): Boolean
    fun savePrivacyState(visible: Boolean)
    fun getPrivacyState(): Boolean
    fun getAreaName(): String
    fun saveAreaName(areaName: String)
    fun getAreaId(): String
    fun saveAreaId(areaId: String)
    fun getAreaList(): List<AreaIdBean>
    fun getLoginOutFlag(): Int
    fun saveLoginOutFlag(loginOutFlag: Int)
    fun saveLoginToken(token: String)

    fun <T : Serializable> saveCacheListData(list: List<T>)
    fun <T : Serializable> getCacheListData(key: String): List<T>?

}