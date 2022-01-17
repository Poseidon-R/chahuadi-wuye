package com.czl.base.push;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.czl.base.config.AppConstants;
import com.czl.base.util.TgSystem;
import com.michoi.calling.TkNetSocketOpt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        Log.i(TAG, "Im received msg:" + cPushMessage.getContent());
//        Toast.makeText(context, "title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent(), Toast.LENGTH_SHORT).show();
//        try {
//            JSONObject object = new JSONObject(cPushMessage.getContent());
//            int msg_type = object.getInt("msg_type");
//            Log.i(TAG, "Im received msg_type:" + msg_type);
//            if (msg_type == 0) {
//                JSONObject msg_data = object.getJSONObject("msg_data");
//                String action = msg_data.getString("action");
//                if (TextUtils.isEmpty(action)) return;
//                if (action.equals("call")) {
//                   String value = msg_data.getString("value");
        wakeUpScreen(context);
        if (!TgSystem.isRunningForeground(context)) {
            isRunningForegroundToApp(context, cPushMessage.getContent());
        } else {
            toTalk(cPushMessage.getContent());
        }
//                    toTalk(value);
//                }
//
//            }
//        } catch (JSONException e) {
//            Log.i(TAG, "Im received msg parse error");
//        }
//        test();

//        使用pendingintent开启activity
//        Intent intent = new Intent();
//        intent.setClassName("com.sjzn.chd","com.sjzn.module_cloudtalk.CloudTalkActivity");
//        PendingIntent.getActivity(context,101, intent,PendingIntent.FLAG_CANCEL_CURRENT).send();
    }


    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }

    private void toTalk(String value) {
        try {
            JSONObject objectMsg = new JSONObject(value);
            String msg_id = objectMsg.getString("msg_id");
            String channel_profile = objectMsg.getString("channel_profile");
            String chatRoom = objectMsg.getString("chat_room");
            String from_addr = objectMsg.getString("from_addr");
            int type = objectMsg.getInt("chat_type");
            receivedTalkMessage(msg_id, chatRoom, from_addr, type, channel_profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        receivedTalkMessage("", "", "", 0, "");

    }

    /**
     * 接收到云对讲消息后，调用此方法
     *
     * @param msg_id          消息ID
     * @param chat_room       房间号
     * @param from_addr       呼叫方地址
     * @param chat_type       呼叫类型 （0 ，手机端不开启视频  1 手机端开启视频）
     * @param channel_profile 对讲模式 0 通信模式和 1直播模式
     */

    private void receivedTalkMessage(final String msg_id, final String chat_room, final String from_addr, final int chat_type, final String channel_profile) {
        TkNetSocketOpt.ViperLogI(TAG, "receivedTalkMessage:" + from_addr);

        Bundle bundle = new Bundle();
        String time = String.valueOf(System.currentTimeMillis());
        bundle.putString("msg_id", msg_id);
        bundle.putString("chat_room", chat_room);
        bundle.putString("from_addr", from_addr);
//        bundle.putString("push_time", time);
        bundle.putString("chat_type", chat_type + "");
        bundle.putString("channel_profile", channel_profile);

        /*---------------------------------------*/
        //TODO 自定义参数
        bundle.putString("title", "麦驰安防");
        /*---------------------------------------*/
        ARouter.getInstance()
                .build(AppConstants.Router.Talk.A_CLOUD_TALK)
                .withBundle("bundle", bundle)
//                .withAction("com.sjzn.chd")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
        TkNetSocketOpt.ViperLogI(TAG, "receivedTalkMessage start activity");

        //使用类名开启activity
//        ComponentName componentName = new ComponentName("com.sjzn.chd", "com.sjzn.module_cloudtalk.CloudTalkActivity");
//        Intent intent = new Intent();
//        intent.setComponent(componentName);
//        startActivity(intent);
    }

    private void test() {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BundleKey.WEB_MENU_KEY,
                AppConstants.Constants.AGREEMENT_TEXT_URL);

        /*---------------------------------------*/
        //TODO 自定义参数
        bundle.putString("title", "麦驰安防");
        /*---------------------------------------*/
        ARouter.getInstance()
                .build(AppConstants.Router.Login.F_LOGIN)
                .withBundle("bundle", bundle)
//                .withAction("com.sjzn.chd")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
    }

    private void wakeUpScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

            wl.acquire(10 * 60 * 1000L /*10 minutes*/); // 点亮屏幕

            wl.release(); // 释放
        }
    }

    private void isRunningForegroundToApp(Context context, String value) {
        Log.d("CallReceiver", "isRunningForegroundToApp");
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //利用系统方法获取当前Task堆栈, 数目可按实际情况来规划，这里只是演示
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(20);

        Log.d("CallReceiver", "taskInfoList.size:" + taskInfoList.size());
        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            //遍历找到本应用的 task，并将它切换到前台

            Log.d("CallReceiver", "taskInfo  pid " + taskInfo.id);
            Log.d("CallReceiver", "taskInfo  processName " + taskInfo.topActivity.getPackageName());
            Log.d("CallReceiver", "taskInfo  getPackageName " + context.getPackageName());

            if (taskInfo.baseActivity.getPackageName().equals(context.getPackageName())) {

                Log.d("CallReceiver", "my  pid " + taskInfo.id);
                Log.d("CallReceiver", "my  processName " + taskInfo.topActivity.getPackageName());
                Log.d("CallReceiver", "my  getPackageName " + context.getPackageName());

                activityManager.moveTaskToFront(taskInfo.id, 0);

                toTalk(value);
//                Intent intent = new Intent(context, VideoCallActivity.class);
//                intent.putExtra("username", from).putExtra("isComingCall", true).
//                        putExtra("ex_message", ex_message);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                context.startActivity(intent);
                break;
            }
        }
    }

}
