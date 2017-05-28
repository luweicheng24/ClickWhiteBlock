package com.gsww.www.clickwhiteblock.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gsww.www.clickwhiteblock.R;
import com.gsww.www.clickwhiteblock.bean.Person;
import com.gsww.www.clickwhiteblock.utils.SPUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Author : luweicheng on 2017/3/29 0029 09:50
 * E-mail ：1769005961@qq.com
 * GitHub : https://github.com/luweicheng24
 */

public class RegistActivity extends AppCompatActivity implements View.OnClickListener, View.OnLayoutChangeListener {
    private EditText et_phone;
    private RelativeLayout root;
    private EditText et_psw;
    private Button but_regist;
    private String name;
    private String psw;
    private ProgressDialog dialog;
    private int changeHeight;
    private ScrollView sv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        root = (RelativeLayout) findViewById(R.id.rl_root);
        et_phone = (EditText) findViewById(R.id.et_reg_name);
        et_psw = (EditText) findViewById(R.id.et_reg_psw);
        sv = (ScrollView) findViewById(R.id.sv);
        but_regist = (Button) findViewById(R.id.but_regist);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        changeHeight = dm.heightPixels / 3;
        but_regist.setOnClickListener(this);
        root.addOnLayoutChangeListener(this);
    }

    @Override
    public void onClick(View v) {
         InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        dialog = new ProgressDialog(this);
        dialog.setMessage("注册中...");
        dialog.setCanceledOnTouchOutside(false);
        name = et_phone.getText().toString();
        psw = et_psw.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(name)) {
                dialog.show();
                Person p = new Person();
                p.setPassword(psw);
                p.setName(name);
                regist(p);
            } else {
                Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void regist(Person p) {

        p.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
              dialog.dismiss();
              if(e!=null){
                  Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                  intent.putExtra("name", name);
                  intent.putExtra("psw", psw);
                  startActivity(intent);
                  saveAccounts();
                  finish();
              }else{
                  Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
              }
            }
        });

    }

    /**
     *
     * 保存账号在SharePreference
     */
    private void saveAccounts() {
        SPUtils.writeUser("name",name,this);
        SPUtils.writeUser("password",psw,this);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        final int change = bottom - oldBottom;//键盘弹出时候布局的变化值
        if (bottom != 0 && oldBottom != 0 && change > changeHeight) {//键盘落下
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sv.smoothScrollTo(0, sv.getHeight());
                }
            });



        } else if (bottom != 0 && oldBottom != 0 && -change > changeHeight) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sv.smoothScrollTo(0, sv.getHeight());
                }
            });


        }

    }
}