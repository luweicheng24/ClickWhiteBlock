package com.gsww.www.clickwhiteblock.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Author   : luweicheng on 2017/5/29 0029 17:33
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation:
 */

public class InputManager {

    public static void toggle(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
