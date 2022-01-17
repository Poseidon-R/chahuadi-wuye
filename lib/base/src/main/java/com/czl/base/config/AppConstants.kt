package com.czl.base.config

/**
 * @author Alwyn
 * @Date 2020/10/22
 * @Description 常量管理类
 */
interface AppConstants {

    object SpKey {
        const val LOGIN_NAME: String = "login_name"
        const val LOGIN_PHONE: String = "login_phone"
        const val USER_ID: String = "user_id"
        const val LOGIN_TOKEN: String = "app_login_token"
        const val LOGIN_PWD: String = "app_login_pwd"
        const val LOGIN_IS_REMEMBER_PWD: String = "app_login_is_remember_pwd"
        const val ROOMID_AND_CODE: String = "roomid_and_code"
        const val USER_JSON_DATA: String = "user_json_data"
        const val USER_INFO_JSON_DATA: String = "user_info_json_data"
        const val SYS_UI_MODE: String = "sys_ui_mode"
        const val USER_UI_MODE: String = "user_ui_mode"
        const val READ_HISTORY_STATE: String = "read_history_state"

        //判断首次安卓弹出隐私协议
        const val PRIVACY_STATE: String = "read_privacy_state"
        const val AREA_NAME_KEY: String = "area_name_key"
        const val AREA_ID_KEY: String = "area_id_key"
        const val AREA_LIST_KEY: String = "area_list_key"
        const val LOGIN_OUT_FLAG: String = "login_out_flag"
    }

    object CacheKey {
        // 缓存有效期时长1天 数据刷新会重新刷新时长
        const val CACHE_SAVE_TIME_SECONDS = 86400
        const val CACHE_HOME_BANNER = "cache_home_banner"
        const val CACHE_HOME_ARTICLE = "cache_home_article"
        const val CACHE_HOME_KEYWORD = "cache_home_keyword"
        const val CACHE_SQUARE_LIST = "cache_square_list"
        const val CACHE_PROJECT_SORT = "cache_project_sort"
        const val CACHE_PROJECT_CONTENT = "cache_project_content"
        const val END_DATE_SECONDS = 31 * 24 * 60 * 60 * 1000L//31天
        const val END_DATE_DAY = 31//31天
    }

    /**
     * value规则： /(module后缀)/(所在类名)
     * 路由 A_ : Activity
     *     F_ : Fragment
     */
    interface Router {
        object Web {
            const val F_WEB = "/web/WebFragment"
        }

        object Main {
            const val A_TEST = "/main/TestActivity"
            const val A_MAIN = "/main/MainActivity"
            const val F_HOME = "/main/HomeFragment"
            const val F_QR_SCAN = "/main/QRScanFragment"
        }

        object Login {
            const val F_LOGIN = "/login/LoginFragment"

            const val F_REGISTER = "/login/RegisterFragment"
            const val F_SET_PWD = "/login/SetPwdFragment"
            const val F_FORGETPW = "/login/ForgetPwFragment"
            const val F_VERIFYPHONE = "/login/VerifyPhoneFragment"
            const val F_VERIFYPASSWORD = "/login/VerifyPassWordFragment"
        }

        object Car {
            const val F_MYCAR = "/car/MyCarFragment"
            const val F_ADDCAR = "/car/AddCarFragment"
        }

        object Room {
            const val F_MYROOM = "/room/MyRoomFragment"
        }

        object Service {
            const val F_SERVICE = "/service/ServiceFragment"
        }

        object Work {

            const val F_WORK = "/work/WorkFragment"
            const val F_WORK_MY_ORDER = "/work/MyOrderFragment"
            const val F_WORK_MY_ORDER_DETAIL = "/work/MyOrderDetailFragment"
            const val F_WORK_TAKE_CARE_ORDER_DETAIL = "/work/TakeCareOrderDetailFragment"
            const val F_WORK_DEVICE_USER_APPOINTMENT = "/work/UserAppointmentFragment"
            const val F_WORK_DEVICE_APPOINTMENT_RECORD = "/work/AppointmentRecordFragment"
            const val F_WORK_DEVICE_PATROL = "/work/DevicePatrolFragment"
            const val F_WORK_DEVICE_PATROL_ORDER = "/work/PatrolOrderFragment"
            const val F_WORK_DEVICE_TAKE_CARE = "/work/DeviceTakeCareFragment"
            const val F_WORK_TAKE_CARE_ORDER = "/work/TakeCareOrderFragment"
            const val F_WORK_TAKE_CARE_RECORD = "/work/TakeCareRecordFragment"
            const val F_WORK_TAKE_CARE_DISPATCH = "/work/TakeCareDispatchFragment"
            const val F_WORK_PATROL_DISPATCH = "/work/PatrolDispatchFragment"
            const val F_WORK_TAKE_CARE_BATCH_DISPATCH = "/work/TakeCareBatchDispatchFragment"
            const val F_WORK_TAKE_CARE_DISPATCH_DETAIL = "/work/TakeCareDispatchDetailFragment"
            const val F_WORK_PATROL_DISPATCH_DETAIL = "/work/PatrolDispatchDetailFragment"
            const val F_WORK_TAKE_CARE_AUDIT = "/work/TakeCareAuditFragment"
            const val F_WORK_PATROL_AUDIT = "/work/PatrolAuditFragment"
            const val F_WORK_PATROL_AUDIT_DETAIL = "/work/PatrolAuditDetailFragment"
            const val F_WORK_TAKE_CARE_AUDIT_DETAIL = "/work/TakeCareAuditDetailFragment"
            const val F_WORK_MEMBERS_SELECT = "/work/MembersSelectFragment"
            const val F_WORK_UPLOAD_CERTIFICATE = "/work/UploadCertificateFragment"
            const val F_WORK_RECORD_CERTIFICATE = "/work/RecordCertificateFragment"
            const val F_WORK_DEVICE_INSPECTION = "/work/DeviceInspectionFragment"
            const val F_WORK_INSPECTION_ORDER = "/work/InspectionOrderFragment"
            const val F_WORK_INSPECTION_ORDER_DETAIL = "/work/InspectionOrderDetailFragment"
        }

        object Report {
            const val F_REPORT_SUBMIT = "/report/submit"
            const val F_REPAIR_REPORT = "/report/repairReportFragment"
            const val F_REPORT_LIST = "/report/reportListFragment"
            const val F_SERVICE_SCORE = "/report/ServiceScoreFragment"
        }

        object Order {
            const val F_ORDER_DETAIL = "/order/detail"
            const val F_ORDER_MANAGER = "/order/OrderManagerFragment"
            const val F_ORDER_LIST = "/order/OrderListFragment"
            const val F_TRANSFER_ORDER = "/order/TransferOrderFragment"
            const val F_RECEIVE_ORDER = "/order/ReceiveOrderFragment"
        }


        object User {
            const val F_USER_INFO = "/user/UserInfoFragment"
            const val F_FIRST = "/user/FirstFragment"
            const val F_USER = "/user/UserFragment"
            const val F_ABOUT = "/user/AboutFragment"
            const val F_USER_SCORE = "/user/UserScoreFragment"
            const val F_USER_RANK = "/user/UserRankFragment"
            const val F_USER_COLLECT = "/user/UserCollectFragment"
            const val F_USER_SHARE = "/user/UserShareFragment"
            const val F_USER_BROWSE = "/user/UserBrowseFragment"
            const val F_USER_SETTING = "/user/UserSettingFragment"
            const val F_USER_DETAIL = "/user/ShareUserDetailFragment"
            const val F_USER_TODO = "/user/UserTodoFragment"
            const val F_USER_TODO_INFO = "/user/UserTodoInfoFragment"
        }

        object Park {
            const val F_PARK_DETAIL = "/park/ParkDetailFragment"
            const val F_CAR_LIST = "/park/CarListFragment"
            const val F_BUY_CARD = "/park/BuyCardFragment"
            const val F_RENEW_CARD = "/park/RenewCardFragment"
            const val F_PAY_RECORD_DETAIL = "/park/PayRecordDetailFragment"
            const val F_PAY_RECORDS = "/park/PayRecordsFragment"
        }

        object Pay {
            const val F_PAY_DETAIL = "/pay/PayFragment"
            const val F_PAY_RESULT = "/pay/PayResultFragment"
        }

        object Talk {
            const val A_CLOUD_TALK = "/talk/CloudTalkActivity"
        }

        object Square {
            const val F_SQUARE = "/square/SquareFragment"
            const val F_NAV = "/square/NavigateFragment"
            const val F_SYSTEM = "/square/SystemTreeFragment"
            const val F_SYS_DETAIL = "/square/SystemDetailFragment"
            const val F_SYS_CONTENT = "/square/SysContentFragment"
        }

        object Project {
            const val F_PROJECT = "/project/ProjectFragment"
        }

        object Search {
            const val F_SEARCH = "/search/SearchFragment"
        }
    }

    object BundleKey {
        const val MAIN2FIRST = "main2first"
        const val WEB_URL = "web_url"
        const val MAIN_SEARCH_KEYWORD = "main_search_keyword"
        const val USER_SCORE = "user_score"
        const val USER_RANK = "user_rank"
        const val WEB_URL_COLLECT_FLAG = "web_url_collect_flag"
        const val WEB_URL_ID = "web_url_id"
        const val SYSTEM_DETAIL = "system_detail"
        const val SYSTEM_DETAIL_POSITION = "system_detail_position"
        const val WEB_MENU_KEY = "web_menu_key"
        const val USER_ID = "user_id"
        const val SEARCH_HOT_KEY_LIST = "search_hot_key_list"
        const val SYS_CONTENT_TITLE = "sys_content_title"
        const val TODO_INFO_DATA = "todo_info_data"
        const val MONTH_CARD_DATA = "month_card_data"
        const val ORDER_NO_KEY = "order_no_key"
        const val PAY_ORDER_KEY = "pay_order_key"
        const val VEHICLE_NO_KEY = "vehicle_no_key"
        const val PROPERTY_STATE = "property_state"

        const val WORK_ORDER_TYPE = "work_order_type"
        const val WORK_ORDER_STATUS = "work_order_status"
        const val WORK_ORDER_ID = "work_order_id"

        const val WORK_RECORD_IMG = "work_record_img"

        const val WORK_BATCH_ORDER_TYPE = "work_batch_order_type"
        const val WORK_BATCH_TYPE = "work_batch_type"
        const val WORK_BATCH_ORDER_IDS = "work_batch_order_ids"

        const val WORK_ORDER_TASK_IDS = "work_order_task_ids"

        const val WORK_ORDER_ALARM_ID = "work_order_alarm_id"

    }

    object Constants {
        const val REGEX_URL =
            "^((http|https):\\/\\/)(([A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*\$"
        const val PLAIN_TEXT_TYPE = 100
        const val IMAGE_TEXT_TYPE = 101
        const val PAY_SUCCESS_TYPE = 100
        const val PAY_FAIL_TYPE = 101

        //隐私地址
        const val PRIVATE_TEXT_URL = "https://dxzx.cscec-zn-bim.com/privatetext.html"

        //协议地址
        const val AGREEMENT_TEXT_URL = "https://dxzx.cscec-zn-bim.com/agreement.html"
    }
}