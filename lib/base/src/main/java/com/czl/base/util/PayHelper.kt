package com.czl.base.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.net.URLEncoder

object PayHelper {

    fun openAlipay(payInfo: String, context: Context) {
        var intent = Intent(Intent.ACTION_VIEW)
        if (SystemUtil.isAliPayInstalled(context)) {
            var uri: Uri = Uri.parse(
                "alipays://platformapi/startapp?saId=10000007&qrcode=" + URLEncoder.encode(
                    payInfo,
                    "UTF-8"
                )
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = uri
        } else {
            var contentUrl = Uri.parse(payInfo);
            intent.data = contentUrl;
        }
        context.startActivity(intent);
    }

    fun openWxPay(parStr: String, context: Context) {
        if (SystemUtil.isWeixinAvilible(context)) {
            val appId = "wx13a65502bfbc14ec" // 正式  填移动应用(App)的 AppId，非小程序的 AppID
            val api = WXAPIFactory.createWXAPI(context, appId)
            val req = WXLaunchMiniProgram.Req()
            req.userName = "gh_dc95989c60b4" // 填小程序原始id
            //ispayType : 1  代表是待支付
            req.path =
                "/pages/skipPayment/skipPayment?${parStr}"//拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
            req.miniprogramType =
                WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
            api.sendReq(req)
        } else {
            ToastHelper.showErrorToast("您还没有安装微信，请先安装微信客户端")
        }
    }
}