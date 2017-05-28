package com.gsww.www.clickwhiteblock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gsww.www.clickwhiteblock.R;
import com.gsww.www.clickwhiteblock.utils.L;
import com.gsww.www.clickwhiteblock.utils.SPUtils;

/**
 * Author   : luweicheng on 2017/5/2 0002 18:07
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 开始界面
 */

public class SplashActivity extends AppCompatActivity {
    private AlphaAnimation anim;
    private int rank;
    private AlertDialog dialog;
    private TextView start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initAnim();

    }


    @Override
    protected void onResume() {
        super.onResume();
        ImageView view = (ImageView) findViewById(R.id.iv_splash);
        view.setAnimation(anim);
        anim.start();

    }

    private void initAnim() {
        anim = new AlphaAnimation(0, 1);
        anim.setDuration(2000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               String name =  SPUtils.getUser("name",SplashActivity.this);
                if(!name.equals("默认值")&&name!=null){
                    //跳过登录界面
                    L.e("登陆过",this);
                    Log.e("a", "onAnimationEnd:登陆过 " );
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else
                {
                    //去登陆
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    Log.e("a", "onAnimationEnd:未登陆过 " );

                    L.e("为登陆过",this);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


}
