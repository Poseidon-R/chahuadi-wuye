package com.czl.module_login.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.PermissionUtils.permission
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.czl.base.base.AppManager
import com.czl.base.base.BaseActivity
import com.czl.base.config.AppConstants
import com.czl.base.route.RouteCenter
import com.czl.base.util.DialogHelper
import com.czl.module_login.BR
import com.czl.module_login.R
import com.czl.module_login.databinding.ActivitySplashBinding
import com.czl.module_login.viewmodel.SplashViewModel
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.michoi.calling.TalkHelper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import java.util.*
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    private var isCheckPrivacy: Boolean = false

    private var PERMISSION_CODES = 1001
    private var PERMISSION_ALERT_WINDOW=1002
    private val PERMISSION_NOTIFICATION=1003
    override fun initContentView(): Int {
        return R.layout.activity_splash
    }

    override fun initParam() {
        ImmersionBar.hideStatusBar(window)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        initSplashBg()
        showPrivacy()
    }

    private fun initSplashBg() {
        Glide.with(this).load(
            R.mipmap.splash_img
        )
            .override(ScreenUtils.getAppScreenWidth(), ScreenUtils.getAppScreenHeight())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .skipMemoryCache(true)
            .into(binding.ivSplash)
    }

    private fun showPrivacy() {
        isCheckPrivacy = dataRepository.getPrivacyState();
        if (!isCheckPrivacy) {
            val string = resources.getString(R.string.privacy_tips)
            val key1 = resources.getString(R.string.privacy_tips_key1)
            val key2 = resources.getString(R.string.privacy_tips_key2)
            val index1 = string.indexOf(key1)
            val index2 = string.indexOf(key2)

            //?????????????????????

            //?????????????????????
            val spannedString = SpannableString(string)
            //????????????????????????
            val colorSpan1 = ForegroundColorSpan(resources.getColor(R.color.colorBlue))
            spannedString.setSpan(
                colorSpan1,
                index1,
                index1 + key1.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            val colorSpan2 = ForegroundColorSpan(resources.getColor(R.color.colorBlue))
            spannedString.setSpan(
                colorSpan2,
                index2,
                index2 + key2.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            //????????????????????????
            val sizeSpan1 = AbsoluteSizeSpan(18, true)
            spannedString.setSpan(
                sizeSpan1,
                index1,
                index1 + key1.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            val sizeSpan2 = AbsoluteSizeSpan(18, true)
            spannedString.setSpan(
                sizeSpan2,
                index2,
                index2 + key2.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            //??????????????????
            val clickableSpan1: ClickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View) {
                    startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
                        putString(
                            AppConstants.BundleKey.WEB_MENU_KEY,
                            AppConstants.Constants.AGREEMENT_TEXT_URL
                        )
                    })
                }

                override fun updateDrawState(ds: TextPaint) {
                    //???????????????????????????
                    ds.isUnderlineText = false
                }
            }
            spannedString.setSpan(
                clickableSpan1,
                index1,
                index1 + key1.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            val clickableSpan2: ClickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View) {
                    startContainerActivity(AppConstants.Router.Web.F_WEB, Bundle().apply {
                        putString(
                            AppConstants.BundleKey.WEB_MENU_KEY,
                            AppConstants.Constants.PRIVATE_TEXT_URL
                        )
                    })
                }

                override fun updateDrawState(ds: TextPaint) {
                    //???????????????????????????
                    ds.isUnderlineText = false
                }
            }
            spannedString.setSpan(
                clickableSpan2,
                index2,
                index2 + key2.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            XPopup.Builder(this)
                .asConfirm(
                    "????????????",
                    spannedString,
                    "??????",
                    "??????",
                    {
                        dataRepository.savePrivacyState(true)
//                        requestPermission()
                        toLogin()
                    },
                    { AppManager.instance.appExit() },
                    false
                ).show()

        } else {
//            requestPermission()
            toLogin()
        }
    }

    private fun toLogin() {
        viewModel.addSubscribe(
            Flowable.timer(1500L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (viewModel.model.getLoginName().isNullOrBlank()) {
                        startContainerActivity(AppConstants.Router.Login.F_LOGIN)
                    } else {
                        RouteCenter.navigate(AppConstants.Router.Main.A_MAIN)
                    }
                    finish()
                })
    }

//    private fun requestPermission() {
//        val p: MutableList<String> = ArrayList()
//        for (permission in PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    permission
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                p.add(permission)
//            }
//        }
//        if (p.size > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.requestPermissions(PERMISSIONS, PERMISSION_CODES)
//        } else {
//            toGetAlertWindowOPermission()
//        }
//    }

//    private fun checkPermission() {
//        val p: MutableList<String> = ArrayList()
//        for (permission in PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    permission
//                ) !== PackageManager.PERMISSION_GRANTED
//            ) {
//                p.add(permission)
//            }
//        }
//        if (p.size > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //???????????????????????????????????????????????????????????????
//            showNormalToast("???????????????????????????????????????????????????????????????")
//        }
//        toGetAlertWindowOPermission()
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == PERMISSION_CODES) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                showNormalToast("???????????????????????????????????????????????????????????????")
//                AppManager.instance.appExit()
//            } else {
//                checkPermission();
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode==PERMISSION_ALERT_WINDOW){
//            toGetAlertWindowOPermission()
//        }else if (requestCode==PERMISSION_NOTIFICATION){
//            toGetNotificationPermission()
//        }
    }

    override fun useBaseLayout(): Boolean {
        return false
    }


    /**
     * ????????????????????????
     */
//    private fun toGetNotificationPermission(){
////        Log.e("getNotification", "from $src")
//        val notificationEnable=NotificationManagerCompat.from(this).areNotificationsEnabled()
//        if (notificationEnable){
//            toLogin()
//        }else{
//            DialogHelper.showNomarlDialog(this,"??????","?????????????????????",{
//                //?????????????????????????????????
//                val intent=Intent()
//                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//                    intent.apply {
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
//                        putExtra(Settings.EXTRA_APP_PACKAGE,packageName)
//                    }
//
//                }else
//                    intent.apply {
//                        action="android.settings.APP_NOTIFICATION_SETTINGS"
//                        putExtra("app_package",packageName)
//                        putExtra("app_uid",applicationInfo.uid)
//                    }
//                startActivityForResult(intent,PERMISSION_NOTIFICATION)
//            },{
//                //app??????
//                AppManager.instance.appExit()
//            })
//        }
//    }

//    /**
//     * ???????????????????????????
//     */
//    private fun toGetAlertWindowOPermission(){
//        if (checkAlertWindowPermission()){
//            //????????????
//            toGetNotificationPermission()
//        }else{
//            //????????????????????????????????????,1.???????????????2.????????????????????????
//            DialogHelper.showNomarlDialog(this,"??????","????????????????????????",{
//                requestAlertWindowPermission()
//            },{
//                AppManager.instance.appExit()
//            })
//        }
//    }
//
//    private fun checkAlertWindowPermission():Boolean{
//        if (Build.VERSION.SDK_INT>=23){
//            return Settings.canDrawOverlays(this)
//        }
//        return false
//    }
//    private fun requestAlertWindowPermission(){
//        val intent= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
//        } else {
//            TODO("VERSION.SDK_INT < M")
//        }
//        intent.data= Uri.parse("package:$packageName")
//        startActivityForResult(intent,PERMISSION_ALERT_WINDOW)
//    }
}