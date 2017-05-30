package com.gsww.www.clickwhiteblock.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gsww.www.clickwhiteblock.R;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * Author   : luweicheng on 2017/5/27 0027 14:45
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:
 */

public class MyPush extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String msg = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String content = null;
            try {
                JSONObject object = new JSONObject(msg);
                content  = object.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NotificationManager manager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notification= new Notification.Builder(context).
                    setSmallIcon(R.drawable.icon).//设置推送消息的图标
                    setContentTitle("您收到一条推送信息！") .//设置推送标题
                    setContentText(content).build();//设置推送内容
            manager.notify(1,notification);//设置通知栏
        }
    }
}
