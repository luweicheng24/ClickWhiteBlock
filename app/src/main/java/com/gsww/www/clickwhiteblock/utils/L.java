package com.gsww.www.clickwhiteblock.utils;

import android.util.Log;

/**
 * Author   : luweicheng on 2017/5/26 0026 14:26
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: log日志类
 */

public class L {
    public static final Boolean DEBUG = true;
    public static void e(String msg,Object object){
      if(DEBUG){
          Log.e(object.getClass().getSimpleName(), "e: "+msg );
      }

    }
}
