package com.gsww.www.clickwhiteblock.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gsww.www.clickwhiteblock.R;
import com.gsww.www.clickwhiteblock.utils.SPUtils;
import com.gsww.www.clickwhiteblock.view.StiackBlockView;

public class MainActivity extends AppCompatActivity implements StiackBlockView.OnBalckCheckListener {

    private TextView scoreText;
    private StiackBlockView stiackBlockView;
    private int rank = 0;//等级
    private AlertDialog dialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){

            getWindow().setStatusBarColor(Color.parseColor("#f44075fd"));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        stiackBlockView = (StiackBlockView) findViewById(R.id.write_pieces);
        stiackBlockView.setSpeed(StiackBlockView.SPEED_PRIMARY);
        scoreText = (TextView) findViewById(R.id.score);
        stiackBlockView.setOnBalckCheckListener(this);
        initDialog();




    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog.show();

    }

    private void initDialog() {
        dialog = new AlertDialog
                .Builder(this, R.style.baseDialog)
                .setTitle("   " + SPUtils.getUser("name", this) + " : 请选择等级")
                .setSingleChoiceItems(new String[]{"菜鸟", "小鸟", "老鸟", "骨灰鸟"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rank = which;
                        switch (rank) {
                            case 0:
                                stiackBlockView.setSpeed(StiackBlockView.SPEED_PRIMARY);
                                break;
                            case 1:
                                stiackBlockView.setSpeed(StiackBlockView.SPEED_MIDDLE);

                                break;
                            case 2:
                                stiackBlockView.setSpeed(StiackBlockView.SPEED_HIGH);

                                break;
                            case 3:
                                stiackBlockView.setSpeed(StiackBlockView.SPEED_MORE_HIGH);
                                break;
                        }
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setCancelable(false)
                .create();
        dialog.setIcon(R.drawable.girl_4);


    }


    @Override
    public void score(int score) {
        scoreText.setText(score + "");
    }


    @Override
    public void gameOver() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        String name = SPUtils.getUser("name", this);
        dialog.setTitle(name);
        dialog.setMessage("当前得分：" + scoreText.getText().toString());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                stiackBlockView.restart();
            }
        });
        dialog.show();
    }


}
