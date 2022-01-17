package com.czl.module_cloudtalk;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.michoi.calling.CallBack;
import com.michoi.calling.ContextHandler;
import com.michoi.calling.SystemUtil;
import com.michoi.calling.TalkConstants;
import com.michoi.calling.TalkHelper;
import com.michoi.calling.TkNetSocketOpt;
import com.michoi.calling.ToastUtil;
import com.michoi.calling.UnlockAnimation;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CloudTalkViewUtil implements View.OnClickListener, ContextHandler.IContextHandler {


    private final RelativeLayout rootView;
    private final int screenWidth;
    private int containerH;
    private final Activity mContext;
    private String peerId;
    private final AudioManager audioManager;
    private static final String TAG = "cloud_talk_app";

    private int SDK_USER_UID;
    private String SDK_USER_TOKEN;


    private static final int MSG_INIT_TIME_OUT = 101;
    private static final int MSG_WAIT_ACCEPT_TIME_OUT = 102;
    private static final int MSG_TALKING_TIME_OUT = 103;
    private static final int MSG_UNLOCK_TIME_OUT = 104;
    private static final int MSG_ENTER_TIME_OUT = 105;

    private static final int MSG_SHOW_SURFACEVIEW = 201;
    private static final int MSG_HIDE_TOAST_MESSAGE = 202;


    private static final int MSG_WORK_THREAD_INI_FAILED = 301;


    /**
     * 进入房间等待时间
     */
    private final static int ENTER_ROOM_TIME = 10000;
    /**
     * 等待接听的等待时间
     */
    private final static int CALLING_COUNT_TIME = 60000;
    /**
     * 通话中的等待时间
     */
    private final static int TALKING_COUNT_TIME = 5 * 60 * 1000;
    /**
     * 开锁等待时间
     */
    private final static int UNLOCK_COUNT_TIME = 10000;

    /**
     * 进入房间等待回应时间
     */
    private final static int ENTER_COUNT_TIME = 5000;


    private Chronometer tickTime, tickTime1; // 定时器

    private Ringtone ringtone;

    private boolean unlock = false; // 是否按下开锁

    private TextView tvCloudStatus;
    private boolean active = false;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

    private static final int PERMISSION_CODES = 1001;
    private String roomId;
    private String msgId;
    private boolean inRoom;
    private FrameLayout container;
    private boolean userIn;
    private boolean hanging = false;
    private String user_mobile;
    private ImageView ivCall;
    private boolean firstShow;
    private int status = TalkConstants.STATUS_DEFAULT;
    private MBroadcast receiver;
    private boolean acceptClick = false;
    private boolean isLocalBad, isRemoteBad;
    private TextView tv_nick;
    private String lastStr;
    private final ArrayList<Integer> joined = new ArrayList<>();
    private ProgressDialog progress;
    private RelativeLayout parentView, ivShow;
    private View ll_action_1, ll_action_3, ll_top_container, iv_back;
    private boolean silenceExit = false;
    private View btn_answer, btn_hangup, btn_unlock, btn_answer_3, btn_hangup_3, btn_unlock_3;
    private String channel_profile;
    private TelephonyManager tm;
    private MyPhoneCallListener myPhoneCallListener;
    private boolean regested;
    private boolean accepting;
    private long clickTime;
    private long lastTime;
    private boolean changeType;
    private String tips;


    @Override
    public void handleMessage(Message msg) {
        TkNetSocketOpt.ViperLogI(TAG, "handleMessage msg:" + msg.what);
        String str = mContext.getResources().getString(R.string.cloud_talk_tip11);
        String str1 = mContext.getResources().getString(R.string.cloud_talk_tip12);
        if (!active) {
            TkNetSocketOpt.ViperLogI(TAG, "NOT active, return");
            return;
        }
        switch (msg.what) {
            case MSG_HIDE_TOAST_MESSAGE: { // 隐藏TOAST文本消息
                tvCloudStatus.setVisibility(View.INVISIBLE);
                tvCloudStatus.setText("");
                break;
            }

            case MSG_WAIT_ACCEPT_TIME_OUT:
            case MSG_TALKING_TIME_OUT: { // 通话超时
                hangUp();
                break;
            }
            case TalkConstants.TALK_CONNECTION_INTERRUPTED:
            case TalkConstants.TALK_CONNECTION_LOST:
                if (status < TalkConstants.STATUS_INROOM) return;
                sendAction("error");
                exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip4));
                break;
            case MSG_INIT_TIME_OUT://初始化获取云对讲账号超时或者进入房间超时
            case MSG_ENTER_TIME_OUT://进入房间等待回复OK超时
                if (userIn) return;
                sendAction("error");
                exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip5));
                break;
            case MSG_WORK_THREAD_INI_FAILED:
            case TalkConstants.TALK_ON_ERROR:
                if (status == TalkConstants.STATUS_FINISH) return;
                status = TalkConstants.STATUS_FINISH;
                sendAction("error");
                stopWorkThread(mContext.getResources().getString(R.string.cloud_talk_tip6));
                break;
//            case TalkConstants.TALK_INI_SUCCESS:
//                String ss = (String) msg.obj;
//                activationLicense(ss);
//                break;
            case TalkConstants.TALK_NETWORK_LOCAL_BAD:
                if (tv_nick != null && status < TalkConstants.STATUS_HANGING) {
                    String nowStr = tv_nick.getText().toString();
                    if (!str.equals(nowStr) && !str1.equals(nowStr)) {
                        lastStr = nowStr;
                    }
                    tv_nick.setText(str1);
                    isLocalBad = true;
                }
                break;
            case TalkConstants.TALK_NETWORK_REMOTE_BAD:
                if (tv_nick != null && status < TalkConstants.STATUS_HANGING) {
                    String nowStr = tv_nick.getText().toString();
                    if (!str.equals(nowStr) && !str1.equals(nowStr)) {
                        lastStr = nowStr;
                    }
                    tv_nick.setText(str);
                    isRemoteBad = true;
                }
                break;
            case TalkConstants.TALK_NETWORK_LOCAL_GOOD:
                if (isLocalBad && status < TalkConstants.STATUS_HANGING) {
                    String nowStr = tv_nick.getText().toString();
                    if (str.equals(nowStr) || str1.equals(nowStr)) {
                        tv_nick.setText(lastStr);
                    }
                    isLocalBad = false;
                }
                break;
            case TalkConstants.TALK_NETWORK_REMOTE_GOOD:
                if (isRemoteBad && status < TalkConstants.STATUS_HANGING) {
                    String nowStr = tv_nick.getText().toString();
                    if (str.equals(nowStr) || str1.equals(nowStr)) {
                        tv_nick.setText(lastStr);
                    }
                    isRemoteBad = false;
                }
                break;
            case TalkConstants.TALK_JOIN_SUCCESS:
                if (rootView == null) {
                    TalkHelper.HELPER.sendCallBusy();
                    finish();
                    return;
                }
                if (status >= TalkConstants.STATUS_INROOM) return;
                status = TalkConstants.STATUS_INROOM;
                enterRoomSuccess();
                break;
            case TalkConstants.TALK_USER_OFFLINE:
                Integer uidOut = (Integer) msg.obj;
                if (joined.contains(uidOut)) {
                    joined.remove(uidOut);
                    if (joined.size() == 0 && status < TalkConstants.STATUS_HANGING) {
                        exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip7));
                    }
                }
                TkNetSocketOpt.ViperLogI(TAG, uidOut + " 已退出房间" + joined.size() + " status:" + status);
                break;
            case TalkConstants.TALK_RECEIVER_MESSAGE_STOP:
                exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip8));
                break;
            case TalkConstants.TALK_USER_BUSY:
                if (acceptClick) return;
                TkNetSocketOpt.ViperLogI(TAG, "cloud talk 对方占线");
                exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip9));
                break;
            case TalkConstants.TALK_RECEIVER_MESSAGE_ACK_STOP:
                processHandler.removeCallbacks(runnable);
                exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip10));
                break;
            case TalkConstants.TALK_USER_JOINED:
                if (!userIn) {
                    userIn = true;
                    showView();
                }
                Integer uidEnter = (Integer) msg.obj;
                if (!joined.contains(uidEnter)) {
                    joined.add(uidEnter);
                }
                //TODO
                TkNetSocketOpt.ViperLogI(TAG, uidEnter + " 已进入房间");
                break;
            case TalkConstants.TALK_RECEIVER_MESSAGE_ACK_ENTER: {
                processHandler.removeMessages(MSG_ENTER_TIME_OUT);
                status = TalkConstants.STATUS_WAITING_USER;
                break;
            }
            case TalkConstants.TALK_RECEIVER_MESSAGE_ACK_ACCEPT: {
                if (acceptClick) {
                    acceptOk();
                } else {
                    exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip13));
                }
                break;
            }
            case TalkConstants.TALK_RECEIVER_MESSAGE_ACK_UNLOCK: {
                if (unlock) {
                    ToastUtil.show(mContext, mContext.getResources().getString(R.string.cloud_talk_tip3));
                }
                stopUnlockAnim();
                break;
            }
            case TalkConstants.TALK_VIDEO_SIZE_CHANGE:
                if (!firstShow) return;
                int[] size = (int[]) msg.obj;
                int width = size[0];
                int height = size[1];
                TkNetSocketOpt.ViperLogI(TAG, "video change width:" + width + "  height:" + height);
                RelativeLayout.LayoutParams lp;
                if (width > height) {
                    int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
                    int surfaceViewHight = screenWidth * size[1] / size[0];
                    lp = new RelativeLayout.LayoutParams(screenWidth, surfaceViewHight);
                } else {
                    int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
                    int screenHight = mContext.getResources().getDisplayMetrics().heightPixels * 2 / 3;
                    int surfaceViewWidth = screenHight * size[0] / size[1];
                    if (surfaceViewWidth > screenWidth) {
                        int surfaceViewHight = screenWidth * size[1] / size[0];
                        lp = new RelativeLayout.LayoutParams(screenWidth, surfaceViewHight);
                    } else
                        lp = new RelativeLayout.LayoutParams(surfaceViewWidth, screenHight);
                }
//                lp.setMargins(0,200,0,0);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                container.setLayoutParams(lp);
                break;
            case TalkConstants.TALK_VIDEO_STATE_CHANGE:
                int uid = (int) msg.obj;
                if (ivCall != null) {
                    ivCall.clearAnimation();
                    ivCall.setVisibility(View.GONE);
                }
                if (msg.arg1 == 1) {
                    firstShow = true;
                    TalkHelper.HELPER.setupRemoteVideo(mContext, container, uid);
                    updateVideoSize(mConfig);
                }
                break;
            case TalkConstants.TALK_FIRST_FRAME:
                firstShow = true;
                int[] size1 = (int[]) msg.obj;
                int videoWidth = size1[0];
                int videoHeight = size1[1];
                TkNetSocketOpt.ViperLogI(TAG, "TALK_FIRST_FRAME video width:" + videoWidth + "  height:" + videoHeight);
                updateVideoSize(mConfig);
                break;
        }
    }

    private void updateContainer() {
        if (container != null) {
            container.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, containerH);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            container.setLayoutParams(lp);
        }
        if (firstShow) {
            TalkHelper.HELPER.adjustVideo();
        }
    }

    private int mConfig = 2;

    public void updateVideoSize(int config) {
        mConfig = config;
        if (config == 1) {
            containerH = screenWidth * 240 / 320;
            ll_action_1.setVisibility(View.VISIBLE);
            ll_top_container.setVisibility(View.VISIBLE);
            ll_action_3.setVisibility(View.GONE);
            tickTime1.setVisibility(View.GONE);
            iv_back.setVisibility(View.GONE);
        } else {
            containerH = screenWidth;
            ll_action_1.setVisibility(View.GONE);
            ll_top_container.setVisibility(View.VISIBLE);
            ll_action_3.setVisibility(View.VISIBLE);
            tickTime1.setVisibility(View.GONE);
            iv_back.setVisibility(View.VISIBLE);
        }
        TkNetSocketOpt.ViperLogI(TAG, "update size:" + screenWidth + " | " + containerH);
        if (parentView != null) {
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, containerH);
//            parentView.setLayoutParams(lp);
        }
        updateContainer();
    }

    public CloudTalkViewUtil(Activity context, RelativeLayout rootView) {
        this.mContext = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        this.containerH = screenWidth * 240 / 320;
        this.rootView = rootView;
        if (rootView != null) {
            initView(rootView);
        }
    }

    public boolean onVolumeDown() {
        if (audioManager == null || ringtone == null) return false;
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
//        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        volume--;
        if (volume < 0) {
            volume = 0;
        }
        TkNetSocketOpt.ViperLogE(TAG, "onVolumeDown:" + volume);
        if (volume == 0) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
        }
        return true;
    }

    public boolean onVolumeUp() {
        if (audioManager == null || ringtone == null) return false;
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        volume++;
        if (volume > max) {
            volume = max;
        }
        TkNetSocketOpt.ViperLogE(TAG, "onVolumeUp:" + volume);
        try {
            audioManager.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
        } catch (Exception e) {
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, 0, AudioManager.FLAG_SHOW_UI);
            TkNetSocketOpt.ViperLogE(TAG, "setStreamVolume:" + e.getMessage());
            return false;
        }
        return true;
    }

    private void telephony() {
        tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            try {
                myPhoneCallListener = new MyPhoneCallListener();
                tm.listen(myPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);
            } catch (Exception e) {
                // 异常捕捉
            }
        }
    }

    public class MyPhoneCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:// 电话挂断
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK: //电话通话的状态
//                    TalkHelper.HELPER.sendCallBusy();
                    break;
                case TelephonyManager.CALL_STATE_RINGING: //电话响铃的状态
                    TalkHelper.HELPER.sendCallBusy();
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }


    public void onCreate(Bundle intent) {
        if (rootView != null) {
            status = TalkConstants.STATUS_INIT;
            silenceExit = false;
        } else {
            silenceExit = true;
        }
        roomId = intent.getString("chat_room");
        msgId = intent.getString("msg_id");
        String lastMsgId = TalkHelper.HELPER.getSpString("msg_id");
        TkNetSocketOpt.ViperLogI(TAG, "onCreate lastMsgId:" + lastMsgId + " roomId:" + roomId);
        if (TextUtils.equals(msgId, lastMsgId)) {
            finish();
            return;
        }
        channel_profile = intent.getString("channel_profile");
        TalkHelper.HELPER.setChannel_profile(channel_profile);
        user_mobile = intent.getString("user_mobile");
        TkNetSocketOpt.ViperLogI(TAG, "set CALLING_COUNT_TIME:" + CALLING_COUNT_TIME);
        if (TextUtils.isEmpty(roomId)) {
            Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.cloud_talk_tip2), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        TalkHelper.HELPER.setTalking(true);
        String from_addr = intent.getString("from_addr");
        tips = mContext.getString(R.string.cloud_talk_tip1, from_addr);
        String chat_type = intent.getString("chat_type");
        boolean supportVideo1 = "1".equals(chat_type);
        boolean supportVideo2 = !TextUtils.isEmpty(from_addr) && from_addr.contains("室内机");
        changeType = supportVideo1 || supportVideo2;
        TalkHelper.HELPER.setMobileSupportVideo(changeType);
        IntentFilter filter = new IntentFilter();
        filter.addAction(TalkConstants.ACTION_RECEIVER_MSG);
        receiver = new MBroadcast();
        mContext.registerReceiver(receiver, filter);
        telephony();
        onStart();
    }


    private void showView() {
        if (status >= TalkConstants.STATUS_HANGING) return;
        ivShow.setVisibility(View.VISIBLE);
        tv_nick.setText(tips);
//        if (changeType) {
//            btn_unlock.setVisibility(View.GONE);
//            btn_unlock_3.setVisibility(View.GONE);
//        } else {
            btn_unlock.setVisibility(View.VISIBLE);
            btn_unlock_3.setVisibility(View.VISIBLE);
//        }
        startPlayRing();
    }

    private class MBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mContext.isFinishing() || !mContext.isDestroyed()) {
                String msgContent = intent.getStringExtra("msg");
                if (!TextUtils.isEmpty(msgContent)) {
                    try {
                        JSONObject object = new JSONObject(msgContent);
                        int msgType = object.getInt("msg_type");
                        String receivedMsgId = object.getString("msg_id");
                        if (msgType == 0 && !TextUtils.isEmpty(receivedMsgId) && receivedMsgId.equals(msgId)) {
                            JSONObject content = object.getJSONObject("msg_data");
                            String action = content.getString("action");
                            peerId = intent.getStringExtra("peerId");
                            TalkHelper.HELPER.changeImPeerId(msgId, peerId);
                            if ("finish".equals(action)) {
                                if (status < TalkConstants.STATUS_HANGING) {
                                    exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip8));
                                }
                                return;
                            }
                            if (action.equals("stop")) {
                                sendAction("stopOk");
                            }
                            Message msg = new Message();
                            switch (action) {
                                case "stop":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_STOP;
                                    break;
                                case "stopOk":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_ACK_STOP;
                                    break;
                                case "enter":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_ENTER;
                                    break;
                                case "enterOk":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_ACK_ENTER;
                                    break;
                                case "unlockOk":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_ACK_UNLOCK;
                                    break;
                                case "accept":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_ACCEPT;
                                    break;
                                case "acceptOk":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_ACK_ACCEPT;
                                    break;
                                case "unlock":
                                    msg.what = TalkConstants.TALK_RECEIVER_MESSAGE_UNLOCK;
                                    break;
                            }
                            processHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        TkNetSocketOpt.ViperLogI(TAG, "im msg parse error");
                    }
                }
            }
        }
    }


    private void initView(RelativeLayout rootView) {

        parentView = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.cloud_talk_view, rootView, false);
        rootView.addView(parentView);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, containerH);
//        parentView.setLayoutParams(lp);

        ivCall = parentView.findViewById(R.id.iv_video_call);
        ivCall.setVisibility(View.VISIBLE);
        ivCall.setBackgroundResource(R.drawable.video_call_anim);
        AnimationDrawable anim = (AnimationDrawable) ivCall.getBackground();
        anim.start();

        ivShow = parentView.findViewById(R.id.iv_video_show);
        ivShow.setVisibility(View.GONE);

        tv_nick = parentView.findViewById(R.id.tv_nick);
        ll_action_1 = parentView.findViewById(R.id.ll_action_1);
        ll_action_3 = parentView.findViewById(R.id.ll_action_3);

        ll_top_container = parentView.findViewById(R.id.ll_top_container);
        iv_back = parentView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        btn_answer = parentView.findViewById(R.id.ll_answer_1);
        btn_answer.setOnClickListener(this);
        btn_hangup = parentView.findViewById(R.id.ll_hangup_1);
        btn_hangup.setOnClickListener(this);
        btn_unlock = parentView.findViewById(R.id.ll_unlock_1);
        btn_unlock.setOnClickListener(this);

        btn_answer_3 = parentView.findViewById(R.id.ll_answer_3);
        btn_answer_3.setOnClickListener(this);
        btn_hangup_3 = parentView.findViewById(R.id.ll_hangup_3);
        btn_hangup_3.setOnClickListener(this);
        btn_unlock_3 = parentView.findViewById(R.id.ll_unlock_3);
        btn_unlock_3.setOnClickListener(this);

        container = parentView.findViewById(R.id.opposite_surface);
        container.setVisibility(View.GONE);
        tickTime = parentView.findViewById(R.id.tv_call_time);
        tickTime1 = parentView.findViewById(R.id.tv_call_time_1);
        tvCloudStatus = parentView.findViewById(R.id.tv_status);
        tvCloudStatus.setVisibility(View.INVISIBLE);
        tickTime.setFormat(mContext.getResources().getString(R.string.cloud_talk_tip14));
        tickTime1.setFormat(mContext.getResources().getString(R.string.cloud_talk_tip14));
    }

    public void onStart() {
        active = true;
        accepting = false;
        reportToServer();
        onResume();
    }

    public void onStop() {
        active = false;
        accepting = false;
        silenceExit = true;
        hangUp();
    }

    private void reportToServer() {
        TalkHelper.HELPER.checkTalkMsg(user_mobile, msgId, 1, new CallBack<String>() {
            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject data = new JSONObject(str);
                    JSONObject ext_parameters = data.getJSONObject("ext_parameters");
                    long server_time = data.getLong("server_time");
                    long push_time = ext_parameters.getLong("push_time");
                    if (server_time - push_time > 60) {
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TkNetSocketOpt.ViperLogI(TAG, "cloud talk report to server success 对方已挂断");
                                exitRoom(mContext.getResources().getString(R.string.cloud_talk_tip8));
                            }
                        });
                    }
                } catch (JSONException e) {
                    TkNetSocketOpt.ViperLogI(TAG, "reportToServer json parse error");
                }
            }

            @Override
            public void onFail(int errorCode, String err) {
                TkNetSocketOpt.ViperLogI(TAG, "reportToServer " + err);
            }
        });
    }


    private void getRoomAccount() {
        processHandler.sendEmptyMessageDelayed(MSG_INIT_TIME_OUT, ENTER_ROOM_TIME);
        TalkHelper.HELPER.getChannelToken(roomId, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject obj) {
                processHandler.removeMessages(MSG_INIT_TIME_OUT);
                try {
                    SDK_USER_TOKEN = obj.getString("token");
                    SDK_USER_UID = obj.getInt("uid");
                    initTalk();
                } catch (Exception e) {
                    Log.d(TAG, "json 解析错误");
                    fail();
                }
            }

            @Override
            public void onFail(int code, String msg) {
                processHandler.removeMessages(MSG_INIT_TIME_OUT);
                Log.d(TAG, msg);
                fail();
            }
        });
    }


    private void initTalk() {
        if (status >= TalkConstants.STATUS_HANGING) return;
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                requestPermission();
            }
        });
    }

    private void initTRTC() {
        if (status >= TalkConstants.STATUS_HANGING) return;
        TkNetSocketOpt.ViperLogI(TAG, "start init talk");
        TalkHelper.HELPER.putSpString("msg_id", msgId);
        startWorkThread();
        processHandler.sendEmptyMessageDelayed(MSG_INIT_TIME_OUT, ENTER_ROOM_TIME);
        if (tickTime != null) {
            tickTime.setBase(SystemClock.elapsedRealtime());
            tickTime.start();
            tickTime1.setBase(SystemClock.elapsedRealtime());
            tickTime1.start();
        }
        status = TalkConstants.STATUS_INIT;
    }


    private void fail() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mContext.isFinishing() || mContext.isDestroyed())
                    return;
                Toast.makeText(mContext, mContext.getResources().getString(R.string.cloud_talk_tip15), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void exit() {
        if (status >= TalkConstants.STATUS_HANGING
                || status == TalkConstants.STATUS_DEFAULT) return;
        hangUp();
    }


    private void finish() {
        onPause();
        onDestroy();
    }

    /**
     * 主动挂断电话
     */
    private void hangUp() {
        if (status >= TalkConstants.STATUS_HANGING) return;
        TkNetSocketOpt.ViperLogI(TAG, "TalkConstants.STATUS_HANGING");
        status = TalkConstants.STATUS_HANGING;
        dismissMyDialog();
        removeMessages();
        closeSpeakerOn();
        stopPlayRing();
        if (tickTime != null) {
            tickTime.stop();
            tickTime1.stop();
        }
        if (inRoom) {
            hanging = true;
            TalkHelper.HELPER.sendCallStop();
            processHandler.removeCallbacks(runnable);
            processHandler.postDelayed(runnable, 2000);
        } else {
            TkNetSocketOpt.ViperLogI(TAG, "not in room hangup");
            exitRoom("");
        }
    }

    private final Runnable runnable = new Runnable() {//两秒后强制退出
        @Override
        public void run() {
            TkNetSocketOpt.ViperLogI(TAG, "runnable hangup");
            exitRoom("");
        }
    };


    /**
     * 主动接听电话
     */
    private void answer() {
        acceptClick = true;
        status = TalkConstants.STATUS_USER_ACCEPT;
        btn_answer.setEnabled(false);
        if (hanging) return;
        closeSpeakerOn();
        stopPlayRing();
        accept();
    }

    /**
     * 开锁
     */
    private void unlock() {
        TalkHelper.HELPER.sendCallUnlock();
    }


    /**
     * 退出视频房间
     */
    private void exitRoom(String msg) {
        if (status >= TalkConstants.STATUS_HANGUP) return;
        TkNetSocketOpt.ViperLogI(TAG, "TalkConstants.STATUS_HANGUP");
        status = TalkConstants.STATUS_HANGUP;
        inRoom = false;
        status = TalkConstants.STATUS_HANGUP;
        sendAction("finish");
        stopWorkThread(msg);
    }


    private void enterRoomSuccess() {
        if (status >= TalkConstants.STATUS_HANGING) return;
        inRoom = true;
        processHandler.removeMessages(MSG_INIT_TIME_OUT);
        processHandler.sendEmptyMessageDelayed(MSG_WAIT_ACCEPT_TIME_OUT, CALLING_COUNT_TIME);
        btn_answer.setEnabled(true);
        btn_unlock.setEnabled(true);
        TalkHelper.HELPER.sendCallEnter();
        processHandler.sendEmptyMessageDelayed(MSG_ENTER_TIME_OUT, ENTER_COUNT_TIME);
        TalkHelper.HELPER.openVideo();
    }

    private void accept() {
        accepting = true;
        status = TalkConstants.STATUS_USER_ACCEPT;
        TalkHelper.HELPER.sendCallAccept();
        TalkHelper.HELPER.sendAcceptToServer(user_mobile, msgId, roomId, new CallBack<String>() {

            @Override
            public void onSuccess(String obj) {
                TkNetSocketOpt.ViperLogI(TAG, "accept report success ");
            }

            @Override
            public void onFail(int code, String msg) {
                TkNetSocketOpt.ViperLogI(TAG, "accept report fail:" + msg);
            }
        });
        processHandler.postDelayed(acceptRunnable, 200);
    }

    private Runnable acceptRunnable = new Runnable() {
        @Override
        public void run() {
            if (accepting) {
                accept();
            }
        }
    };

    private void acceptOk() {
        accepting = false;
        processHandler.removeCallbacks(acceptRunnable);
        if (tickTime != null) {
            tickTime.setFormat(mContext.getResources().getString(R.string.cloud_talk_tip16));
            tickTime.setBase(SystemClock.elapsedRealtime());
            tickTime.start();
            tickTime1.setFormat(mContext.getResources().getString(R.string.cloud_talk_tip16));
            tickTime1.setBase(SystemClock.elapsedRealtime());
            tickTime1.start();
        }
        processHandler.removeMessages(MSG_WAIT_ACCEPT_TIME_OUT);
        processHandler.sendEmptyMessageDelayed(MSG_TALKING_TIME_OUT, TALKING_COUNT_TIME);
        TalkHelper.HELPER.accept();
    }

    private void sendAction(String action) {
        if (TalkHelper.HELPER.isImLogined() && !TextUtils.isEmpty(peerId)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("msg_id", msgId);
                jsonObject.put("msg_type", 0);
                JSONObject content = new JSONObject();
                content.put("action", action);
                jsonObject.put("msg_data", content);
            } catch (JSONException e) {
                Log.e(TAG, e.toString());
            }
            TalkHelper.HELPER.sendIMMessage(peerId, jsonObject.toString(), null);
        }
    }

    private void requestPermission() {
        TkNetSocketOpt.ViperLogI(TAG, "requestPermission");
        List<String> p = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                p.add(permission);
            }
        }
        if (p.size() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mContext.requestPermissions(PERMISSIONS, PERMISSION_CODES);
        } else {
            initTRTC();
        }
    }


    private void checkPermission() {
        TkNetSocketOpt.ViperLogI(TAG, "checkPermission");
        List<String> p = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                p.add(permission);
            }
        }
        if (p.size() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //之前已经申请过权限了，这里做二次判断及提醒
            Toast.makeText(mContext.getApplicationContext(), mContext.getResources().getString(R.string.cloud_talk_tip17), Toast.LENGTH_SHORT).show();
        }
        initTRTC();
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.cloud_talk_tip17));
                finish();
            } else {
                checkPermission();
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view == btn_answer_3 || view == btn_answer) {
            TkNetSocketOpt.ViperLogI(TAG, "按下 接听 按钮");
            btn_answer.setVisibility(View.GONE);
            btn_answer_3.setVisibility(View.GONE);
            answer();
        } else if (view == btn_hangup || view == btn_hangup_3) {
            TkNetSocketOpt.ViperLogI(TAG, "按下 挂断 按钮");
            String tipHangUp = mContext.getResources().getString(R.string.cloud_talk_tip10);
            showMyDialog(tipHangUp);
            hangUp();
        } else if (view == btn_unlock || view == btn_unlock_3) {
            TkNetSocketOpt.ViperLogI(TAG, "按下 开锁 按钮");
            if ((System.currentTimeMillis() - lastTime) < 5000) {
                ToastUtil.show(mContext.getApplicationContext(), mContext.getResources().getString(R.string.cloud_talk_tip18));
                return;
            }
            lastTime = System.currentTimeMillis();
            View btnUnlock = view.findViewById(R.id.btn_unlock);
            startUnlockAnim(btnUnlock);
            unlock();
        } else if (view == iv_back) {
            TkNetSocketOpt.ViperLogI(TAG, "设置竖屏");
            clickTime = System.currentTimeMillis();
        }
    }


    private void startUnlockAnim(View btn) {
        float cX = btn.getWidth() / 2.0f;
        float cY = btn.getHeight() / 2.0f;
        UnlockAnimation unlockAnim = new UnlockAnimation(cX, cY, UnlockAnimation.ROTATE_DECREASE);
        AnimListener al = new AnimListener(btn);
        unlockAnim.setAnimationListener(al);
        unlockAnim.setInterpolator(new LinearInterpolator());
        unlockAnim.setFillAfter(true);
        unlock = true;
        btn.startAnimation(unlockAnim);
        processHandler.removeMessages(MSG_UNLOCK_TIME_OUT);
        processHandler.sendEmptyMessageDelayed(MSG_UNLOCK_TIME_OUT, UNLOCK_COUNT_TIME);
    }

    private void stopUnlockAnim() {
        unlock = false;
        processHandler.removeMessages(MSG_UNLOCK_TIME_OUT);
    }

    private void startWorkThread() {
        TalkHelper.HELPER.startCall(SDK_USER_UID, SDK_USER_TOKEN, roomId, channel_profile, processHandler);
    }

    private void stopWorkThread(String msg) {
        TalkHelper.HELPER.stopCall();
        if (!TextUtils.isEmpty(msg) && !silenceExit) {
            showMyDialog(msg);
        }
        processHandler.removeCallbacks(acceptRunnable);
        processHandler.removeCallbacks(finishRunnable);
        processHandler.postDelayed(finishRunnable, 2000);
    }

    private final Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {
            TalkHelper.HELPER.release();
            if (mContext.isFinishing() || mContext.isDestroyed())
                return;
            finish();
        }
    };

    private void showMyDialog(final String msg) {
        if (!mContext.isFinishing() && !mContext.isDestroyed()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progress == null) {
                        progress = new ProgressDialog(mContext);
                    }
                    progress.setMessage(msg);
                    progress.setCancelable(false);
                    progress.show();
                }
            });
        }
    }

    private void dismissMyDialog() {
        if (!mContext.isFinishing() && !mContext.isDestroyed()) {
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progress != null) {
                        progress.dismiss();
                    }
                }
            });
        }
    }


    public void onResume() {
        TkNetSocketOpt.ViperLogI(TAG, "onResume");
        registerHomeKeyReceiver(mContext);
        lastPressed = System.currentTimeMillis();
        getRoomAccount();
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) manager.cancelAll();
    }

    private void removeMessages() {
        TkNetSocketOpt.ViperLogI(TAG, "removeMessages");
        processHandler.removeCallbacks(acceptRunnable);
        processHandler.removeMessages(MSG_SHOW_SURFACEVIEW);
        processHandler.removeMessages(MSG_HIDE_TOAST_MESSAGE);
        processHandler.removeMessages(MSG_TALKING_TIME_OUT);
        processHandler.removeMessages(MSG_WAIT_ACCEPT_TIME_OUT);
    }

    public void onPause() {
        TkNetSocketOpt.ViperLogI(TAG, "onPause");
        unregisterHomeKeyReceiver(mContext);
    }

    public void onDestroy() {
        if (status == TalkConstants.STATUS_FINISH) return;
        TkNetSocketOpt.ViperLogI(TAG, "onDestroy TalkConstants.STATUS_FINISH");
        status = TalkConstants.STATUS_FINISH;
        TalkHelper.HELPER.stopCall();
        TalkHelper.HELPER.release();
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
        dismissMyDialog();
        removeMessages();
        closeSpeakerOn();
        stopPlayRing();
        if (rootView != null && parentView != null) {
            parentView.setVisibility(View.GONE);
            rootView.removeView(parentView);
        }
        TalkHelper.HELPER.setTalking(false);
        mContext.finish();
    }

    private long lastPressed;

    public void onBackPressed() {
        if (System.currentTimeMillis() - lastPressed < 1000) {
            TkNetSocketOpt.ViperLogI(TAG, "双击 返回 按钮");
            String tipHangUp = mContext.getResources().getString(R.string.cloud_talk_tip10);
            showMyDialog(tipHangUp);
            hangUp();
        } else {
            lastPressed = System.currentTimeMillis();
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.cloud_talk_tip19));
        }
    }

    private final ContextHandler processHandler = new ContextHandler(this);
    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private void registerHomeKeyReceiver(Context context) {
        TkNetSocketOpt.ViperLogI(TAG, "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.registerReceiver(mHomeKeyReceiver, homeFilter);
        regested = true;
    }

    private void unregisterHomeKeyReceiver(Context context) {
        TkNetSocketOpt.ViperLogI(TAG, "unregisterHomeKeyReceiver");
        if (regested && null != mHomeKeyReceiver) {
            regested = false;
            context.unregisterReceiver(mHomeKeyReceiver);
            mHomeKeyReceiver = null;
        }
    }

    public class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TkNetSocketOpt.ViperLogI(TAG, "onReceive: action: " + action);
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                TkNetSocketOpt.ViperLogI(TAG, "reason: " + reason);
                hangUp();
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    TkNetSocketOpt.ViperLogI(TAG, "homekey");
                } else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    TkNetSocketOpt.ViperLogI(TAG, "long press home key or activity switch");
                } else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    TkNetSocketOpt.ViperLogI(TAG, "lock");
                } else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    TkNetSocketOpt.ViperLogI(TAG, "assist");
                }
            }
        }
    }

    /**
     * 开锁动画
     */
    class AnimListener implements Animation.AnimationListener {
        private final View v;

        AnimListener(View v) {
            super();
            this.v = v;
        }

        @Override
        public void onAnimationEnd(Animation arg0) {
            if (unlock)
                v.startAnimation(arg0);
            else {
                arg0.cancel();
            }
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {

        }

        @Override
        public void onAnimationStart(Animation arg0) {

        }
    }

    private boolean isVirating;

    private void stopPlayRing() {
        if (ringtone != null) {
            ringtone.stop();
            ringtone = null;
        }
        if (isVirating) {//防止多次关闭抛出异常，这里加个参数判断一下
            isVirating = false;
            SystemUtil.virateCancle(mContext);
        }
    }

    /**
     * 播放铃声
     */
    private void startPlayRing() {
        openSpeakerOnForRingtone();
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        try {
            ringtone = RingtoneManager.getRingtone(mContext, ringUri);
//            setRingtoneRepeat(ringtone);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ringtone.setLooping(true);
            }
            ringtone.play();
        } catch (Exception e) {
            TkNetSocketOpt.ViperLogE(TAG, "Play Ring Error: " + e.getMessage());
        }
        isVirating = true;
        SystemUtil.vibrate(mContext, new long[]{1000, 2000, 1000, 2000}, 0);
    }

    //反射设置闹铃重复播放
    @SuppressWarnings("JavaReflectionMemberAccess")
    private void setRingtoneRepeat(Ringtone ringtone) {
        Class<Ringtone> clazz = Ringtone.class;
        try {
            Field field = clazz.getDeclaredField("mLocalPlayer");//返回一个 Field 对象，它反映此 Class 对象所表示的类或接口的指定公共成员字段（※这里要进源码查看属性字段）
            field.setAccessible(true);
            MediaPlayer target = (MediaPlayer) field.get(ringtone);//返回指定对象上此 Field 表示的字段的值
            target.setLooping(true);//设置循环
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void openSpeakerOnForRingtone() {
        Log.i(TAG, "openSpeakerOnForRingtone");
        try {
            if (audioManager != null) {
                int mode = audioManager.getMode();
                audioManager.setMode(AudioManager.MODE_RINGTONE);
                audioManager.setSpeakerphoneOn(true);
                int result = audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        Log.i(TAG, "onAudioFocusChange:" + focusChange);
                    }
                }, AudioManager.STREAM_RING, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                int mode2 = audioManager.getMode();
                Log.i(TAG, "setMode MODE_RINGTONE:" + result + "  mode old:" + mode + " mode new:" + mode2);
            }
        } catch (Exception e) {
            Log.e(TAG, "openSpeakerOnForRingtone" + e.getMessage());
        }
    }

    private void closeSpeakerOn() {
        Log.i(TAG, "closeSpeakerOn");
        try {
            if (audioManager != null) {
                audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.abandonAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        Log.i(TAG, "onAudioFocusChange:" + focusChange);
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "closeSpeakerOn" + e.getMessage());
        }
    }
}
