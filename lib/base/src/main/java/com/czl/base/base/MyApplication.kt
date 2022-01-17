package com.czl.base.base

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.CommonCallback
import com.alibaba.sdk.android.push.huawei.HuaWeiRegister
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.alibaba.sdk.android.push.register.MiPushRegister
import com.alibaba.sdk.android.push.register.OppoRegister
import com.alibaba.sdk.android.push.register.VivoRegister
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.Glide
import com.czl.base.BuildConfig
import com.czl.base.R
import com.czl.base.config.AppConstants
import com.czl.base.di.allModule
import com.czl.base.util.SpHelper
import com.czl.base.util.ToastHelper
import com.lxj.xpopup.XPopup
import com.michoi.calling.TalkHelper
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import es.dmoral.toasty.Toasty
import io.reactivex.plugins.RxJavaPlugins
import me.jessyan.autosize.AutoSizeConfig
import me.yokeyword.fragmentation.Fragmentation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.litepal.LitePal
import org.koin.core.logger.Level


/**
 * @author Alwyn
 * @Date 2020/7/20
 * @Description
 */
open class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        setApplication(this)
        LitePal.initialize(this)
        MMKV.initialize(this)
        // 初始化Fragmentation
        Fragmentation.builder()
            .stackViewMode(Fragmentation.NONE)
            .debug(BuildConfig.DEBUG)
            .install()
        // 屏幕适配
        AutoSizeConfig.getInstance().setCustomFragment(true).setBaseOnWidth(false)
            .setExcludeFontScale(true).designHeightInDp = 720
        //是否开启日志打印
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG).setConsoleSwitch(BuildConfig.DEBUG)
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(allModule)
        }
        RxJavaPlugins.setErrorHandler {
            ToastHelper.showErrorToast("系统错误")
            it.printStackTrace()
        }
        // 设置吐司不以队列循环展示
        Toasty.Config.getInstance().allowQueue(false).apply()
        XPopup.setPrimaryColor(ContextCompat.getColor(this, R.color.md_theme_red))
        // 切换情景模式
        initNightMode()

        initCloudChannel(this)

        TalkHelper.init(this, false, "d3bdce63d0de4d3884b7b1248381f0b7")
    }

    /**
     * 初始化云推送通道
     * @param applicationContext
     */
    private fun initCloudChannel(applicationContext: Context) {
        createNotificationChannel()
        PushServiceFactory.init(applicationContext)
        val pushService = PushServiceFactory.getCloudPushService()
        pushService.setDebug(true)
        pushService.setLogLevel(CloudPushService.LOG_DEBUG)
        pushService.register(applicationContext, object : CommonCallback {
            override fun onSuccess(response: String) {
                Log.d(TAG, "init cloudchannel success")
            }

            override fun onFailed(errorCode: String, errorMessage: String) {
                Log.d(
                    TAG,
                    "init cloudchannel failed -- errorcode:$errorCode -- errorMessage:$errorMessage"
                )
            }
        })
//        注册华为辅助通道
        HuaWeiRegister.register(applicationContext as Application?)
//        注册小米辅助通道
        MiPushRegister.register(applicationContext, "2882303761520061963", "5682006155963")
//        注册vivo辅助通道
        VivoRegister.register(applicationContext);
//        注册oppo辅助通道
        OppoRegister.register(
            applicationContext,
            "1919a1f830b64b33951b44a79ba16c08",
            "9c1be0e378074bfc9385e80a565f8db3"
        )
    }

    private fun initChannel() {
        createNotificationChannel()
        PushServiceFactory.init(applicationContext)
        val pushService = PushServiceFactory.getCloudPushService()
        pushService.bindAccount("18607100855", object : CommonCallback {
            override fun onSuccess(response: String) {
                Log.d(TAG, "init cloudchannel success")
            }

            override fun onFailed(errorCode: String, errorMessage: String) {
                Log.d(
                    TAG,
                    "init cloudchannel failed -- errorcode:$errorCode -- errorMessage:$errorMessage"
                )
            }
        })
    }

    private fun initNightMode() {
        if (SpHelper.decodeBoolean(AppConstants.SpKey.SYS_UI_MODE))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        else
            AppCompatDelegate.setDefaultNightMode(
                if (SpHelper.decodeBoolean(AppConstants.SpKey.USER_UI_MODE)) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            // 通知渠道的id。
            val id = "1"
            // 用户可以看到的通知渠道的名字。
            val name: CharSequence = "视频通话"
            // 用户可以看到的通知渠道的描述。
            val description = "仅用于通知您有门禁消息"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(id, name, importance)
            // 配置通知渠道的属性。
            mChannel.description = description
            // 设置通知出现时的闪灯（如果Android设备支持的话）。
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            // 设置通知出现时的震动（如果Android设备支持的话）。
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            // 最后在notificationmanager中创建该通知渠道。
            mNotificationManager.createNotificationChannel(mChannel)
        }
    }

    companion object {
        init {
            ClassicsFooter.REFRESH_FOOTER_FINISH = ""
            SmartRefreshLayout.setDefaultRefreshInitializer { _, layout ->
                layout.apply {
                    setEnableOverScrollDrag(true)
                    setEnableScrollContentWhenLoaded(false)
                    setEnableAutoLoadMore(true)
                    setEnableOverScrollBounce(true)
                    setFooterHeight(60f)
                }
            }
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.apply {
                    setPrimaryColorsId(R.color.md_theme_red, R.color.white)
                }
                MaterialHeader(context).setColorSchemeColors(
                    ContextCompat.getColor(
                        context,
                        R.color.md_theme_red
                    )
                )
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                ClassicsFooter(context).setFinishDuration(0)
            }
        }
    }

    private fun setApplication(application: Application) {
        //初始化工具类
        Utils.init(application)
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacks {
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                AppManager.instance.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
            override fun onActivityDestroyed(activity: Activity) {
                AppManager.instance.removeActivity(activity)
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        ARouter.getInstance().destroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        // mPaaS 初始化回调设置
//        QuinoxlessFramework.setup(this, object : IInitCallback() {
//            fun onPostInit() {
//                // 在这里开始使用 mPaaS 功能
//            }
//        })
    }
}