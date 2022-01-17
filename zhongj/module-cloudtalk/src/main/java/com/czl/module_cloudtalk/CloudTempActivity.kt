package com.czl.module_cloudtalk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.split
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.sdk.android.push.popup.PopupNotifyClick
import com.alibaba.sdk.android.push.popup.PopupNotifyClickListener
import com.czl.base.config.AppConstants
import org.json.JSONException
import org.json.JSONObject

class CloudTempActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        super.onCreate(savedInstanceState)

        PopupNotifyClick { s, s1, map ->
             dealParamers(s1);
//            ARouter.getInstance()
//                .build(AppConstants.Router.Talk.A_CLOUD_TALK)
////                .withBundle("bundle", bundle)
//                //.withAction("com.sjzn.chd")
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                .navigation()
//            finish()
            Log.e("cloudTalkActivity", "titile: $s content: $s1")
        }.onCreate(this, intent)
    }
    private fun dealParamers(content: String) {
//        val str2 = String(Base64.decode(content.toByteArray(), Base64.DEFAULT))
        try {
            val objectMsg = JSONObject(content)
            val msg_id = objectMsg.getString("msg_id")
            val channel_profile = objectMsg.getString("channel_profile")
            val chatRoom = objectMsg.getString("chat_room")
            val from_addr = objectMsg.getString("from_addr")
            val type = objectMsg.getInt("chat_type")
            val bundle = Bundle()
            val time = System.currentTimeMillis().toString()
            bundle.putString("msg_id", msg_id)
            bundle.putString("chat_room", chatRoom)
            bundle.putString("from_addr", from_addr)
            bundle.putString("push_time", time)
            bundle.putInt("type", type)
            bundle.putString("channel_profile", channel_profile)
            /*---------------------------------------*/
            //TODO 自定义参数
            bundle.putString("title", "麦驰安防")
            /*---------------------------------------*/
            ARouter.getInstance()
                .build(AppConstants.Router.Talk.A_CLOUD_TALK)
                .withBundle("bundle", bundle)
                //.withAction("com.sjzn.chd")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }finally {
            finish()
        }
    }
}