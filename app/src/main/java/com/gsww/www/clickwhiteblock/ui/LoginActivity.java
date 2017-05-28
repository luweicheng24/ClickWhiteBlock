package com.gsww.www.clickwhiteblock.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsww.www.clickwhiteblock.R;
import com.gsww.www.clickwhiteblock.bean.Person;
import com.gsww.www.clickwhiteblock.utils.L;
import com.gsww.www.clickwhiteblock.view.CircleImageView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Author   : luweicheng on 2017/5/26 0026 14:30
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 登录界面
 */

public class LoginActivity extends AppCompatActivity implements View.OnLayoutChangeListener, View.OnClickListener {
    private TextView regist,rember;
    private RelativeLayout root; //根布局
    private CircleImageView header;//圆形头像
    private LinearLayout ll_bottom;//底部布局
    private int changeHeight; //屏幕高度的1/3
    private ScrollView scrollView;//滑动布局
    private EditText et_name;
    private EditText et_psw;
    private Button login;
    public static final String TAG = "MainActivity";
    private float scale = 0.7f; //缩放比例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_layout);
        bindView();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String psw = intent.getStringExtra("psw");
        L.e("注册信息："+name+":"+psw,this);
        if(name!=null&&psw!=null){
            InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            i.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
           et_name.setText(name);
           et_psw.setText(psw);
        }
    }

    /**
     * 绑定布局
     */
    private void bindView() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        changeHeight = dm.heightPixels / 3;

        root = (RelativeLayout) findViewById(R.id.root);
        header = (CircleImageView) findViewById(R.id.header);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        scrollView = (ScrollView) findViewById(R.id.scroll);
        regist = (TextView) findViewById(R.id.tv_regist);
        rember = (TextView) findViewById(R.id.tv_rember);
        login = (Button) findViewById(R.id.but_login);
        et_name = (EditText) findViewById(R.id.et_name);
        et_psw = (EditText) findViewById(R.id.et_psw);
        initListener();

        root.addOnLayoutChangeListener(this);//对根布局变化时候监听
    }

    private void initListener() {
        header.setOnClickListener(this);
        login.setOnClickListener(this);
        regist.setOnClickListener(this);
        rember.setOnClickListener(this);


    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.e(TAG, "onLayoutChange:bottom " + bottom + "oldBottom" + oldBottom);
        final int change = bottom - oldBottom;//键盘弹出时候布局的变化值
        if (bottom != 0 && oldBottom != 0 && change > changeHeight) {//键盘落下
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, scrollView.getHeight());
                }
            });
            int length = change - changeHeight;
            ZoomOut(header, length);
            ll_bottom.setVisibility(View.VISIBLE);


        } else if (bottom != 0 && oldBottom != 0 && -change > changeHeight) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, scrollView.getHeight());
                }
            });
            int length = -change - changeHeight;
            Log.e(TAG, "onLayoutChange: length=" + length);
            ZoomIn(header, length);
            ll_bottom.setVisibility(View.GONE);

        }

    }

    /**
     * 图像放大下移
     *
     * @param view
     * @param length
     */
    private void ZoomOut(CircleImageView view, int length) {


        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(200);
        mAnimatorSet.start();
    }

    /**
     * 头像缩小上移
     *
     * @param view
     * @param length
     */

    private void ZoomIn(CircleImageView view, int length) {
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0, -length);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX);
        mAnimatorSet.play(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(200);
        mAnimatorSet.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_regist:
                startActivity(new Intent(this,RegistActivity.class));
                finish();
                break;
            case R.id.tv_rember:
                break;
            case R.id.but_login:
                final String name = et_name.getText().toString().trim();
                final String psw = et_psw.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(name)) {

                        BmobQuery<Person> query = new BmobQuery<>();
                        query.findObjects(new FindListener<Person>() {
                            @Override
                            public void done(List<Person> list, BmobException e) {
                                if(e!=null){
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                for (int i = 0; i <list.size() ; i++) {
                                    if(list.get(i).getName().equals(name)&&list.get(i).getPassword().equals(psw)){
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        Toast.makeText(LoginActivity.this, "欢迎回来："+name, Toast.LENGTH_SHORT).show();
                                        finish();
                                        return;
                                    }
                                }
                                Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();


                            }
                        });
                    } else {
                        Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.header:
                break;


        }
    }

}
