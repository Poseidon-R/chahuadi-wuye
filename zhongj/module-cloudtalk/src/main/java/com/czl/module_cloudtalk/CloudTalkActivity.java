package com.czl.module_cloudtalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.alibaba.sdk.android.push.popup.PopupNotifyClick;
import com.alibaba.sdk.android.push.popup.PopupNotifyClickListener;
import com.czl.base.config.AppConstants;
import com.czl.module_cloudtalk.CloudTalkViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Author:xch
 * Date:2021/9/17
 * Do:
 */
@Route(path = AppConstants.Router.Talk.A_CLOUD_TALK)
public class CloudTalkActivity extends Activity {

    private CloudTalkViewUtil util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        Log.e("cloudTalkActivity","onCreate");
        setContentView(R.layout.activity_cloud_talk);
        RelativeLayout rlMedia = findViewById(R.id.rl_media);
        util = new CloudTalkViewUtil(this, rlMedia);
//        new PopupNotifyClick(new PopupNotifyClickListener() {
//            @Override
//            public void onSysNoticeOpened(String s, String s1, Map<String, String> map) {
////                dealParamers(s1);
//                Log.e("cloudTalkActivity","titile: "+s+" content: "+s1);
//            }
//        }).onCreate(this,getIntent());
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if (null!=bundle){
            util.onCreate(bundle);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
    //    private void dealParamers(String content){
//        String str2 = new String(Base64.decode(content.getBytes(), Base64.DEFAULT));
//        try {
//            JSONObject objectMsg = new JSONObject(content);
//            String msg_id = objectMsg.getString("msg_id");
//            String channel_profile = objectMsg.getString("channel_profile");
//            String chatRoom = objectMsg.getString("chat_room");
//            String from_addr = objectMsg.getString("from_addr");
//            int type = objectMsg.getInt("chat_type");
//            Bundle bundle = new Bundle();
//            String time = String.valueOf(System.currentTimeMillis());
//            bundle.putString("msg_id", msg_id);
//            bundle.putString("chat_room", chatRoom);
//            bundle.putString("from_addr", from_addr);
//            bundle.putString("push_time", time);
//            bundle.putInt("type", type);
//            bundle.putString("channel_profile", channel_profile);
//
//            /*---------------------------------------*/
//            //TODO 自定义参数
//            bundle.putString("title", "麦驰安防");
//            util.onCreate(bundle);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean onVolumeDown() {
        if (util == null) return false;
        return util.onVolumeDown();
    }

    public boolean onVolumeUp() {
        if (util == null) return false;
        return util.onVolumeUp();
    }

    @Override
    public void onBackPressed() {
        if (util != null) {
            util.onBackPressed();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        util.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        util.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        util.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        util.onRequestPermissionsResult(requestCode, grantResults);
    }
}
