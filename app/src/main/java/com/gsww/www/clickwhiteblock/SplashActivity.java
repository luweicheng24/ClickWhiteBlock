package com.gsww.www.clickwhiteblock;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Author   : luweicheng on 2017/5/2 0002 18:07
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 
 */

public class SplashActivity extends AppCompatActivity {
    private AlphaAnimation anim;
    private int rank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

         anim = new AlphaAnimation(0,1);
        anim.setDuration(2000);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                AlertDialog.Builder build = new AlertDialog
                        .Builder(SplashActivity.this,R.style.baseDialog);
                build.setSingleChoiceItems(new String[]{"菜鸟","小鸟","老鸟","骨灰鸟"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rank = which;
                    }
                });
                build.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("rank",rank);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = build.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("请选择等级");
                dialog.show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
