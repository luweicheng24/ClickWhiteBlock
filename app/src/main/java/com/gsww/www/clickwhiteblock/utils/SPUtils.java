package com.gsww.www.clickwhiteblock.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Author   : luweicheng on 2017/5/25 0025 17:15
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: SharePerference工具类
 */

public class SPUtils {
    public static final String TAG = "SharePreference";
    public static final String LOGIN = "login";
    public static final String  FILE = "file";

    private static  final String HEADER = "header";

    public static void writeUser(String key, String value, Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN, Context.MODE_PRIVATE);
        Boolean success = sp.edit().putString(key, value).commit();
        Log.i("sp", "writeMSG: 数据写入：" + success);
    }

    public static String getUser(String key, Context context) {

        SharedPreferences sp = context.getSharedPreferences(LOGIN, context.MODE_PRIVATE);
        String val = sp.getString(key, "默认值");
        return val;
    }
    public  static Boolean clearUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(LOGIN,context.MODE_PRIVATE);
        Boolean success = sp.edit().clear().commit();
        return success;
    }

    public static void saveHeader(String value,Context context){
        SharedPreferences sp = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
        Boolean success = sp.edit().putString(HEADER, value).commit();
        Log.i("sp", "writeMSG: 数据写入：" + success);
    }
    public static String getHeader( Context context) {

        SharedPreferences sp = context.getSharedPreferences(FILE, context.MODE_PRIVATE);
        String val = sp.getString(HEADER, "默认值");
        return val;
    }
}
