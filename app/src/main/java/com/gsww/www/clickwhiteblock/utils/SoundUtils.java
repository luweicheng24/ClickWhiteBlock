package com.gsww.www.clickwhiteblock.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.gsww.www.clickwhiteblock.R;

/**
 * Author   : luweicheng on 2017/5/27 0027 12:08
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 音效工具类
 */

public class SoundUtils {
    private SoundPool sp;

    private SoundUtils() {

    }

    private int[] sounds = {R.raw.a, R.raw.a1, R.raw.a3, R.raw.a4,
            R.raw.b, R.raw.b1, R.raw.b3, R.raw.b4,
            R.raw.c, R.raw.c1, R.raw.c3, R.raw.c4,
            R.raw.d, R.raw.d1, R.raw.d3, R.raw.d4,
            R.raw.e,R.raw.e1,R.raw.e3,R.raw.e4,
            R.raw.f1,R.raw.f2,R.raw.f3,R.raw.f4,
            R.raw.g,R.raw.g1,R.raw.g3,R.raw.g4,
    };
    public static SoundUtils soundUtils = new SoundUtils();
    public static SoundUtils getInstance() {

        return soundUtils;
    }

    public  void init(Context context) {
        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                L.e("音效加载成功",this);
            }
        });
        for (int i = 0; i <sounds.length ; i++) {
            sp.load(context,sounds[i],1);
        }
        sp.load(context,R.raw.failure,1);
        sp.load(context,R.raw.start,1);
    }

    /**
     * 随机播放
     */
    public void play(){
        int soundId  = (int) (Math.random()*100%28);
        L.e("随机选取的音乐id=="+soundId,this);
        sp.play(soundId,1,1,0,0,1);
    }
    /**
     * 失败的音效播放
     */
    public void playFail(){

        sp.play(29,1,1,0,0,1);
    }
 /**
     * 开始的音效播放
     */
    public void playStart(){

        sp.play(30,1,1,0,0,1);
    }

    /**
     * 释放资源
     */
    public  void release(){
        sp.release();
    }

}
