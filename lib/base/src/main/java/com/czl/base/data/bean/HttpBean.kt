package com.czl.base.data.bean

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.czl.base.BR
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * @author Alwyn
 * @Date 2020/10/15
 * @Description
 */
data class LoginUser(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: String,
    @SerializedName("area_id_list")
    val areaIdList: List<AreaIdBean>,
)

data class AreaIdBean(
    val areaId: String,
    val areaName: String,
)

data class UploadCertificateParams(
    val handleImage: String,
    val handleState: String,
    val inspectTaskDetailIdList: List<String>
)


data class UploadAlarmCertificateParams(
    var handleImage: String,
    var handleInfo: String,
    var orderId: String
)


data class AssignParams(
    val note: String,
    val orderId: String,
    val userId: String,
    val userName: String
)

data class FinishOrderParams(
    val orderId: String,
)

data class CheckTaskRegisterParams(
    val checkTaskDetailId: String,
    val handleState: String,
    var standardList: List<PatrolOrderDetailBean.StandardList>
)


data class AssignBatchParams(
    val note: String,
    val orderId: List<String>,
    val userId: String,
    val userName: String
)

data class AuditParams(
    val remark: String,
    val idList: List<String>,
    val isOk: Boolean
)

data class ReceivingOrderParams(
    val helpUserId: String,
    val helpUserName: String,
    val note: String,
    val orderId: String,
    val serviceTime: String,
    val userId: String,
    val userName: String
)

data class PageParams(
    val pageNum: String,
    val pageSize: String,
    val orderBy: String,
    val data: Data
) {
    data class Data(
        val areaId: String = "",
        val orderType: String = ""
    )
}

data class AreaInfo(
    val areaId: String,
    val areaName: String
) : Serializable

data class UserInfo(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("avatarUrl")
    val avatarUrl: String?,
    @SerializedName("nickName")
    val nickName: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("idCard")
    val idCard: String?,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("userId")
    val userId: String?,

    )

data class PayInfoBean(
    val data: Data,
    val code: Int,
    val msg: String
) {
    data class Data(
        val payinfo: String,
        var reqsn: String,
    )
}

data class RoomBean(
    val houseId: String,
    val houseCode: String
)

data class PayDetail(
    val chargingId: String,
    val payNo: String,
    val orderId: String,
    val plateNumber: String,
    val plateColor: String,
    val chargeTotal: String,
    val discountAmount: String,
    val charge: String,
    val remark: String,
    val paid: String,
    val sealName: String,
    val inRecordId: String,
    val inTime: String,
    val feesTime: String,
    val stopTime: String,
    val createTime: Date,
    val payTime: String,
) : Serializable

data class CarItem(
    val cardPrice: Float,
    val createTime: String,
    val remark: String,
    val vehicleBrand: String,
    val vehicleColor: String,
    val vehicleId: Int,
    val vehicleModel: String,
    val vehicleNo: String,
    val vehicleType: String,
    val vehicleUserId: Int,
    val houseId: String,
    val isCardExpired: Int,
    var status: Boolean,
    var cardFree: Boolean,
) : Serializable

data class ArticleBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("collect")
        val collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) {
        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}

data class ImgRspBean(
    val name: String,
    val url: String
)

data class HomeBannerBean(
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("id")
    val id: Int,
    @SerializedName("imagePath")
    val imagePath: String = "",
    @SerializedName("isVisible")
    val isVisible: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: Int,
    @SerializedName("url")
    val url: String = ""
) : Serializable

data class HomeArticleBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    var datas: ArrayList<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) : Serializable {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
//        @SerializedName("collect")
//        var collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable(), Serializable {
        @Bindable
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }

        @Bindable
        var topFlag: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.topFlag)
            }

        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        ) : Serializable
    }
}

data class ProjectBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) : Serializable {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
//        @SerializedName("collect")
//        var collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable(), Serializable {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }

        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        ) : Serializable
    }
}

data class SearchDataBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Tag>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable() {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }

        data class Tag(
            @SerializedName("name")
            val name: String,
            @SerializedName("url")
            val url: String
        )
    }
}

data class SearchHotKeyBean(
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("visible")
    val visible: Int
) : Serializable

data class ProjectSortBean(
    @SerializedName("children")
    val children: List<Any>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("visible")
    val visible: Int
) : Serializable

data class UserScoreBean(
    @SerializedName("coinCount")
    val coinCount: Int,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("username")
    val username: String
)

data class UserShareBean(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("shareArticles")
    val shareArticles: ShareArticles
) {
    data class CoinInfo(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )

    data class ShareArticles(
        @SerializedName("curPage")
        val curPage: Int,
        @SerializedName("datas")
        val datas: List<Data>,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("over")
        val over: Boolean,
        @SerializedName("pageCount")
        val pageCount: Int,
        @SerializedName("size")
        val size: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class Data(
            @SerializedName("apkLink")
            val apkLink: String,
            @SerializedName("audit")
            val audit: Int,
            @SerializedName("author")
            val author: String,
            @SerializedName("canEdit")
            val canEdit: Boolean,
            @SerializedName("chapterId")
            val chapterId: Int,
            @SerializedName("chapterName")
            val chapterName: String,
            @SerializedName("collect")
            val collect: Boolean,
            @SerializedName("courseId")
            val courseId: Int,
            @SerializedName("desc")
            val desc: String,
            @SerializedName("descMd")
            val descMd: String,
            @SerializedName("envelopePic")
            val envelopePic: String,
            @SerializedName("fresh")
            val fresh: Boolean,
            @SerializedName("id")
            val id: Int,
            @SerializedName("link")
            val link: String,
            @SerializedName("niceDate")
            val niceDate: String,
            @SerializedName("niceShareDate")
            val niceShareDate: String,
            @SerializedName("origin")
            val origin: String,
            @SerializedName("prefix")
            val prefix: String,
            @SerializedName("projectLink")
            val projectLink: String,
            @SerializedName("publishTime")
            val publishTime: Long,
            @SerializedName("realSuperChapterId")
            val realSuperChapterId: Int,
            @SerializedName("selfVisible")
            val selfVisible: Int,
            @SerializedName("shareDate")
            val shareDate: Long,
            @SerializedName("shareUser")
            val shareUser: String,
            @SerializedName("superChapterId")
            val superChapterId: Int,
            @SerializedName("superChapterName")
            val superChapterName: String,
            @SerializedName("tags")
            val tags: List<Any>,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int,
            @SerializedName("userId")
            val userId: Int,
            @SerializedName("visible")
            val visible: Int,
            @SerializedName("zan")
            val zan: Int
        )
    }
}

data class UserScoreDetailBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("date")
        val date: Long,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("reason")
        val reason: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("userName")
        val userName: String
    )
}

data class UserRankBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )
}

data class PayRecordListBean(
    val records: List<Record>,
    val total: Int,
    val size: Int,
    val current: Int,
    val pages: Int,
) : Serializable {
    data class Record(
        val orderNo: String,
        val bizType: String,
        val payResult: String,
        val amount: String,
        val payType: String,
        val payTime: Date,
        val carNo: String,
    ) : Serializable
}

data class RecordDetailBean(
    val amount: String,
    val bizType: String,
    val carNo: String,
    val orderNo: String,
    val payResult: String,
    val payTime: Date,
    val payType: String,
    val validPeriod: String,
    val payInfo: String,
) : Serializable

data class PayResultBean(
    val status: Int,
    val msg: String
) : Serializable

data class RepairBean(
    val createTime: Date,
    val description: String,
    val no: String,
    val status: String
)

data class RepairListBean(
    val models: List<RepairBean>,
    val pageIndex: Int,
    val pageSize: Int,
    val pages: Int,
    val sum: Int
) : Serializable


data class MonthCardListBean(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<Data>
) : Serializable {
    data class Data(
        val applyUserId: Int,
        val applyUserName: String,
        val applyUserPhone: String,
        val cardId: Int,
        val cardNo: String,
        val cardState: String,
        val cardType: String,
        val cardVehicleType: String,
        val createDateTime: String,
        val endDate: Date,
        val parkingSpaceId: Int,
        val parkingSpaceNo: String,
        val personId: String,
        val restDay: String,
        val startDate: Date,
        val vehicleId: Int,
        val vehicleNo: String,
        var freeCard: Boolean,
    ) : Serializable
}

data class SquareListBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) : Serializable {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Any>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable(), Serializable {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }
    }
}

data class SystemTreeBean(
    @SerializedName("children")
    val children: List<Children>,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("order")
    val order: Int,
    @SerializedName("parentChapterId")
    val parentChapterId: Int,
    @SerializedName("userControlSetTop")
    val userControlSetTop: Boolean,
    @SerializedName("visible")
    val visible: Int
) {
    data class Children(
        @SerializedName("children")
        val children: List<Any>,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("order")
        val order: Int,
        @SerializedName("parentChapterId")
        val parentChapterId: Int,
        @SerializedName("userControlSetTop")
        val userControlSetTop: Boolean,
        @SerializedName("visible")
        val visible: Int,
        @Transient
        var group: String
    )
}

data class NavigationBean(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("cid")
    val cid: Int,
    @SerializedName("name")
    val name: String
) {
    data class Article(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("collect")
        val collect: Boolean,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Any,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Any>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    )
}

data class SystemDetailBean(
    @SerializedName("curPage")
    val curPage: Int,
    @SerializedName("datas")
    val datas: List<Data>,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("over")
    val over: Boolean,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("size")
    val size: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Data(
        @SerializedName("apkLink")
        val apkLink: String,
        @SerializedName("audit")
        val audit: Int,
        @SerializedName("author")
        val author: String,
        @SerializedName("canEdit")
        val canEdit: Boolean,
        @SerializedName("chapterId")
        val chapterId: Int,
        @SerializedName("chapterName")
        val chapterName: String,
        @SerializedName("courseId")
        val courseId: Int,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("descMd")
        val descMd: String,
        @SerializedName("envelopePic")
        val envelopePic: String,
        @SerializedName("fresh")
        val fresh: Boolean,
        @SerializedName("id")
        val id: Int,
        @SerializedName("link")
        val link: String,
        @SerializedName("niceDate")
        val niceDate: String,
        @SerializedName("niceShareDate")
        val niceShareDate: String,
        @SerializedName("origin")
        val origin: String,
        @SerializedName("prefix")
        val prefix: String,
        @SerializedName("projectLink")
        val projectLink: String,
        @SerializedName("publishTime")
        val publishTime: Long,
        @SerializedName("realSuperChapterId")
        val realSuperChapterId: Int,
        @SerializedName("selfVisible")
        val selfVisible: Int,
        @SerializedName("shareDate")
        val shareDate: Long,
        @SerializedName("shareUser")
        val shareUser: String,
        @SerializedName("superChapterId")
        val superChapterId: Int,
        @SerializedName("superChapterName")
        val superChapterName: String,
        @SerializedName("tags")
        val tags: List<Any>,
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("visible")
        val visible: Int,
        @SerializedName("zan")
        val zan: Int
    ) : BaseObservable() {
        @Bindable
        @SerializedName("collect")
        var collect: Boolean = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.collect)
            }
    }
}

data class ShareUserDetailBean(
    @SerializedName("coinInfo")
    val coinInfo: CoinInfo,
    @SerializedName("shareArticles")
    val shareArticles: ShareArticles
) {
    data class CoinInfo(
        @SerializedName("coinCount")
        val coinCount: Int,
        @SerializedName("level")
        val level: Int,
        @SerializedName("rank")
        val rank: String,
        @SerializedName("userId")
        val userId: Int,
        @SerializedName("username")
        val username: String
    )

    data class ShareArticles(
        @SerializedName("curPage")
        val curPage: Int,
        @SerializedName("datas")
        val datas: List<Data>,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("over")
        val over: Boolean,
        @SerializedName("pageCount")
        val pageCount: Int,
        @SerializedName("size")
        val size: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class Data(
            @SerializedName("apkLink")
            val apkLink: String,
            @SerializedName("audit")
            val audit: Int,
            @SerializedName("author")
            val author: String,
            @SerializedName("canEdit")
            val canEdit: Boolean,
            @SerializedName("chapterId")
            val chapterId: Int,
            @SerializedName("chapterName")
            val chapterName: String,
            @SerializedName("courseId")
            val courseId: Int,
            @SerializedName("desc")
            val desc: String,
            @SerializedName("descMd")
            val descMd: String,
            @SerializedName("envelopePic")
            val envelopePic: String,
            @SerializedName("fresh")
            val fresh: Boolean,
            @SerializedName("id")
            val id: Int,
            @SerializedName("link")
            val link: String,
            @SerializedName("niceDate")
            val niceDate: String,
            @SerializedName("niceShareDate")
            val niceShareDate: String,
            @SerializedName("origin")
            val origin: String,
            @SerializedName("prefix")
            val prefix: String,
            @SerializedName("projectLink")
            val projectLink: String,
            @SerializedName("publishTime")
            val publishTime: Long,
            @SerializedName("realSuperChapterId")
            val realSuperChapterId: Int,
            @SerializedName("selfVisible")
            val selfVisible: Int,
            @SerializedName("shareDate")
            val shareDate: Long,
            @SerializedName("shareUser")
            val shareUser: String,
            @SerializedName("superChapterId")
            val superChapterId: Int,
            @SerializedName("superChapterName")
            val superChapterName: String,
            @SerializedName("tags")
            val tags: List<Any>,
            @SerializedName("title")
            val title: String,
            @SerializedName("type")
            val type: Int,
            @SerializedName("userId")
            val userId: Int,
            @SerializedName("visible")
            val visible: Int,
            @SerializedName("zan")
            val zan: Int
        ) : BaseObservable() {
            @Bindable
            @SerializedName("collect")
            var collect: Boolean = false
                set(value) {
                    field = value
                    notifyPropertyChanged(BR.collect)
                }
        }
    }
}

class TodoBean() : Parcelable {
    var curPage = 0
    var offset = 0

    @SerializedName("over")
    var isOver = false
    var pageCount = 0
    var size = 0
    var total = 0
    var datas: List<Data> = arrayListOf()

    constructor(parcel: Parcel) : this() {
        curPage = parcel.readInt()
        offset = parcel.readInt()
        isOver = parcel.readByte() != 0.toByte()
        pageCount = parcel.readInt()
        size = parcel.readInt()
        total = parcel.readInt()
        datas = parcel.createTypedArrayList(Data.CREATOR)!!
    }

    class Data() : BaseObservable(), Parcelable {
        var completeDate: Long? = null
        var completeDateStr: String? = null
        var content: String? = null
        var date: Long = 0
        var dateStr: String? = null
        var id = 0
        var priority = 0
        var title: String? = null
        var type = 0
        var userId = 0

        @Bindable
        var dateExpired = false
            set(value) {
                field = value
                notifyPropertyChanged(BR.dateExpired)
            }

        @Bindable
        var status = 0
            set(status) {
                field = status
                notifyPropertyChanged(BR.status)
            }

        constructor(parcel: Parcel) : this() {
            completeDate = parcel.readLong()
            completeDateStr = parcel.readString()
            content = parcel.readString()
            date = parcel.readLong()
            dateStr = parcel.readString()
            id = parcel.readInt()
            priority = parcel.readInt()
            title = parcel.readString()
            type = parcel.readInt()
            userId = parcel.readInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                dateExpired = parcel.readBoolean()
            } else {
                dateExpired = parcel.readInt() == 1
            }
            status = parcel.readInt()
        }


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeLong(completeDate ?: 0)
            parcel.writeString(completeDateStr)
            parcel.writeString(content)
            parcel.writeLong(date)
            parcel.writeString(dateStr)
            parcel.writeInt(id)
            parcel.writeInt(priority)
            parcel.writeString(title)
            parcel.writeInt(type)
            parcel.writeInt(userId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                parcel.writeBoolean(dateExpired)
            } else {
                parcel.writeInt(if (dateExpired) 1 else 0)
            }
            parcel.writeInt(status)
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Data

            if (completeDate != other.completeDate) return false
            if (completeDateStr != other.completeDateStr) return false
            if (content != other.content) return false
            if (date != other.date) return false
            if (dateStr != other.dateStr) return false
            if (id != other.id) return false
            if (priority != other.priority) return false
            if (title != other.title) return false
            if (type != other.type) return false
            if (userId != other.userId) return false
            if (dateExpired != other.dateExpired) return false
            if (status != other.status) return false

            return true
        }

        override fun hashCode(): Int {
            var result = completeDate?.hashCode() ?: 0
            result = 31 * result + (completeDateStr?.hashCode() ?: 0)
            result = 31 * result + (content?.hashCode() ?: 0)
            result = 31 * result + date.hashCode()
            result = 31 * result + (dateStr?.hashCode() ?: 0)
            result = 31 * result + id
            result = 31 * result + priority
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + type
            result = 31 * result + userId
            result = 31 * result + dateExpired.hashCode()
            result = 31 * result + status
            return result
        }

        override fun toString(): String {
            return "Data(completeDate=$completeDate, completeDateStr=$completeDateStr, content=$content, date=$date, dateStr=$dateStr, id=$id, priority=$priority, title=$title, type=$type, userId=$userId, dateExpired=$dateExpired, status=$status)"
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TodoBean

        if (curPage != other.curPage) return false
        if (offset != other.offset) return false
        if (isOver != other.isOver) return false
        if (pageCount != other.pageCount) return false
        if (size != other.size) return false
        if (total != other.total) return false
        if (datas != other.datas) return false

        return true
    }

    override fun hashCode(): Int {
        var result = curPage
        result = 31 * result + offset
        result = 31 * result + isOver.hashCode()
        result = 31 * result + pageCount
        result = 31 * result + size
        result = 31 * result + total
        result = 31 * result + (datas.hashCode())
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(curPage)
        parcel.writeInt(offset)
        parcel.writeByte(if (isOver) 1 else 0)
        parcel.writeInt(pageCount)
        parcel.writeInt(size)
        parcel.writeInt(total)
        parcel.writeTypedList(datas)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "TodoBean(curPage=$curPage, offset=$offset, isOver=$isOver, pageCount=$pageCount, size=$size, total=$total, datas=$datas)"
    }

    companion object CREATOR : Parcelable.Creator<TodoBean> {
        override fun createFromParcel(parcel: Parcel): TodoBean {
            return TodoBean(parcel)
        }

        override fun newArray(size: Int): Array<TodoBean?> {
            return arrayOfNulls(size)
        }
    }
}

data class GuestApplyParam(
    var guestType: Int = 2,
    var startDateTime: String = "",
    var endDateTime: String = "",
    var guestName: String = "",
    var guestMobile: String = "",
    var identity: String = "",
    var guestPurpose: String? = "",
    var guestFaceUrl: String? = "",
    var visitName: String = "",
    var visitMobile: String = "",
    var buildName: String? = "",
    var unitName: String? = "",
)

data class ApplyPageParams(
    val pageNum: String,
    val pageSize: String,
    val orderBy: String,
    val data: Data
) {
    data class Data(
        var state: String = ""
    )
}

data class ApplyBean(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<Data>
) : Serializable {
    data class Data(
        val applyId: String? = "",
        val appointRecordId: String? = "",
        val approveRemark: String? = "",
        val approveStatus: String? = "",
        val cardNo: String? = "",
        val createTime: String? = "",
        val delFlag: String? = "",
        val endTime: String? = "",
        val guestCompany: String? = "",
        val guestFaceUrl: String? = "",
        val guestMobile: String? = "",
        val guestName: String? = "",
        val guestNo: String? = "",
        val guestPurpose: String? = "",
        val guestType: String? = "",
        val orderId: String? = "",
        val personId: String? = "",
        val qrCode: String? = "",
        val reservationTime: String? = "",
        val resource: String? = "",
        val startTime: String? = "",
        val updateTime: String? = "",
        val visitCompanyName: String? = "",
        val visitMobile: String? = "",
        val visitName: String? = "",
        val visitTime: String? = "",
        val visitUserId: String? = ""
    )
}

data class MembersBean(
    var avatar: String,
    var delFlag: String,
    var deptId: String,
    var email: String,
    var nickName: String,
    var phonenumber: String,
    var remark: String,
    var sex: String,
    var status: String,
    var userId: String,
    var userName: String,
    var userType: String,
)

data class WorkOrderDetailBean(
    var workOrder: Order,
    var task: Task,
    var taskDetailList: List<TaskList>
) : Serializable {
    data class Order(
        var orderId: String,
        var orderNo: String,
        var orderTypeId: String,
        var orderTypeName: String,
        var orderDate: String,
        var orderName: String,
        var orderContent: String,
        var sendOrderType: Int,
        var orderState: Int,
        var isHasten: Boolean,
        var handleUserId: String,
        var handleUserName: String,
        var helpUserId: String,
        var helpUserName: String,
        var serviceTime: String,
        var finishTime: String,
        var createTime: String
    ) : Serializable

    data class Task(
        var inspectTaskId: String,
        var inspectPlanId: String,
        var taskNo: String,
        var startTime: String,
        var endTime: String,
        var taskState: String,
        var taskUserId: String,
        var taskUserName: String,
        var execUserId: String,
        var execUserName: String,
        var finishTime: String,
        var createTime: String,
    ) : Serializable

    data class TaskList(
        var inspectTaskId: String,
        var inspectTaskDetailId: String,
        var deviceId: String,
        var deviceName: String,
        var handleState: String,
        var handleImage: String,
        var handleUserId: String,
        var handleUserName: String,
        var handleTime: String,
        var handleDesc: String,
    ) : Serializable
}

data class AlarmOrderDetailBean(
    var workOrder: Order,
    var pointInfo: PointInfo,
    var alarmRecord: Alarm
) : Serializable {

    data class Alarm(
        var alarmDesc: String,
        var alarmId: String,
        var alarmImage: String,
        var alarmLevel: String,
        var alarmState: String,
        var alarmTime: String,
        var alarmTypeId: String,
        var alarmTypeName: String,
        var handleImage: String,
        var handleInfo: String,
        var pointId: String,
        var createTime: String
    ) : Serializable

    data class Order(
        var orderId: String,
        var orderNo: String,
        var orderTypeId: String,
        var orderTypeName: String,
        var orderDate: String,
        var orderName: String,
        var orderContent: String,
        var sendOrderType: Int,
        var orderState: Int,
        var isHasten: Boolean,
        var handleUserId: String,
        var handleUserName: String,
        var helpUserId: String,
        var helpUserName: String,
        var serviceTime: String,
        var finishTime: String,
        var createTime: String
    ) : Serializable

    data class PointInfo(
        var buildingId: String,
        var floorId: String,
        var ip: String,
        var location: String,
        var objectType: String,
        var pointName: String,
        var pointId: String,
        var pointTypeId: String,
        var roomId: String,
        var subId: String,
        var systemId: String,
        var createTime: String,
    ) : Serializable
}


data class PatrolOrderDetailBean(
    var workOrder: Order,
    var task: Task,
    var taskDetailList: List<TaskList>
) : Serializable {
    data class Order(
        var orderId: String,
        var orderNo: String,
        var orderTypeId: String,
        var orderTypeName: String,
        var orderDate: String,
        var orderName: String,
        var orderContent: String,
        var sendOrderType: Int,
        var orderState: Int,
        var isHasten: Boolean,
        var handleUserId: String,
        var handleUserName: String,
        var helpUserId: String,
        var helpUserName: String,
        var serviceTime: String,
        var finishTime: String,
        var createTime: String
    ) : Serializable

    data class Task(
        var checkPlanId: String,
        var checkTaskId: String,
        var taskNo: String,
        var startTime: String,
        var endTime: String,
        var taskState: String,
        var taskUserId: String,
        var taskUserName: String,
        var execUserId: String,
        var execUserName: String,
        var finishTime: String,
        var createTime: String,
    ) : Serializable

    data class TaskList(
        var proDeviceInfo: ProDeviceInfo,
        var standardList: List<StandardList>,
        var taskDetail: TaskDetail,
    ) : Serializable

    data class ProDeviceInfo(
        var buildingId: String,
        var buildingName: String,
        var buyDate: String,
        var checkPeriod: String,
        var contact: String,
        var createTime: String,
        var deviceBrand: String,
        var deviceId: String,
        var deviceModel: String,
        var deviceName: String,
        var deviceNo: String,
        var factoryNo: String,
        var floorName: String,
        var floorId: String,
        var location: String,
        var pointTypeName: String,
        var pointTypeId: String,
        var scrapDate: String,
        var supplier: String,
        var systemId: String,
        var systemName: String,
        var userId: String,
        var userName: String,
    )

    data class StandardList(
        var checkStandardDetailId: String,
        var checkStandardId: String,
        var checkTaskDetailId: String,
        var checkTaskDetailStandardId: String,
        var option1: String,
        var option2: String,
        var option3: String,
        var optionName: String,
        var remark: String,
        var selected: String,
    )

    data class TaskDetail(
        var checkTaskId: String,
        var checkTaskDetailId: String,
        var deviceId: String,
        var deviceName: String,
        var handleState: String,
        var handleUserId: String,
        var handleUserName: String,
        var handleTime: String,
        var pointTypeId: String,
    ) : Serializable
}

data class WorkOrderBean(
    val endRow: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val list: List<Data>
) : Serializable {
    data class Data(
        var createTime: String,
        var handleUserId: Int,
        var handleUserName: String,
        var isHasten: Boolean,
        val orderContent: String,
        val orderDate: String,
        val orderId: Int,
        val orderName: String,
        val orderNo: String,
        val orderState: Int,
        val orderTypeId: Int,
        val taskState: Int,
        val orderTypeName: String,
        val sendOrderType: String
    ) : Serializable
}

data class TakeCareBean(
    val pageNum: Int,
    val pageSize: Int,
    val size: Int,
    val total: Int,
    val list: List<TakeCareBean.Data>
) : Serializable {
    data class Data(
        var createTime: String,
        var handleUserId: Int,
        var handleUserName: String,
        var isHasten: Boolean,
        val orderContent: String,
        val orderDate: String,
        val orderId: Int,
        val orderName: String,
        val orderNo: String,
        val orderState: Int,
        val taskState: Int,
        val orderTypeId: Int,
        val orderTypeName: String,
        val sendOrderType: String
    ) : Serializable
}

data class PatrolBean(
    val pageNum: Int,
    val pageSize: Int,
    val size: Int,
    val total: Int,
    val list: List<Data>
) : Serializable {
    data class Data(
        var checkTaskId: String,
        var endTime: String,
        var exceptionNum: String,
        var execUserName: Boolean,
        val finishTime: String,
        val frequencyType: String,
        val num: String,
        val okNum: String,
        val orderId: String,
        val orderName: String,
        val orderNo: String,
        val orderState: String,
        val planName: String,
        val startTime: String,
        val taskNo: String,
        val taskState: String,
        val taskUserName: String,
        val undoNum: String

    ) : Serializable
}


data class TakeCareRecordBean(
    val pageNum: Int,
    val pageSize: Int,
    val size: Int,
    val total: Int,
    val list: List<TakeCareRecordBean.Data>
) : Serializable {
    data class Data(
        var createTime: String,
        var endTime: String,
        var execUserId: String,
        var execUserName: String,
        val orderName: String,
        val finishTime: String,
        val inspectPlanId: String,
        val inspectTaskId: String,
        val startTime: String,
        val taskNo: String,
        val orderId: String,
        val taskState: Int,
        val taskUserId: String,
        val taskUserName: String,
    ) : Serializable
}

data class TakeCareDispatchBean(
    val pageNum: Int,
    val pageSize: Int,
    val size: Int,
    val total: Int,
    val list: List<TakeCareDispatchBean.Data>
) : Serializable {
    data class Data(
        var frequencyType: String,
        var endTime: String,
        var execUserName: String,
        val orderId: String,
        val orderName: String,
        var orderNo: String,
        val orderState: Int,
        val planName: String,
        val finishTime: String,
        val inspectTaskId: String,
        val startTime: String,
        val taskNo: String,
        val taskState: Int,
        val taskUserName: String,
    ) : Serializable
}


data class TakeCarePageParams(
    val pageNum: String,
    val pageSize: String,
    val orderBy: String,
    val data: Data
) {
    data class Data(
        var deviceNo: String = "",
        var endTime: String = "",
        var handleUserName: String = "",
        var orderNo: String = "",
        var startTime: String = ""
    )
}

data class TakeCareRecordPageParams(
    val pageNum: String,
    val pageSize: String,
    val orderBy: String,
    val data: String
)

data class PercentBean(
    var baojing: Int,
    var baoyang: Int,
    var weixiu: Int,
    var xunjian: Int,
    var label: String
)

data class WorkOrderTimeBean(
    var label: String,
    @SerializedName("val")
    var value: String
)
