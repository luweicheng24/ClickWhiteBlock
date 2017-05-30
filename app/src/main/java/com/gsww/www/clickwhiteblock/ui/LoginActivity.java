package com.gsww.www.clickwhiteblock.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.gsww.www.clickwhiteblock.utils.SPUtils;
import com.gsww.www.clickwhiteblock.view.CircleImageView;
import com.gsww.www.clickwhiteblock.view.CustomProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.gsww.www.clickwhiteblock.R.id.iv_header;


/**
 * Author   : luweicheng on 2017/5/26 0026 14:30
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 登录界面
 */

public class LoginActivity extends AppCompatActivity implements View.OnLayoutChangeListener, View.OnClickListener {
    private static final int RESIZE_REQUEST_CODE = 0x00;
    private static final int REQUEST_ALBUM = 0X01;
    private static final int REQUEST_CAMERA = 0X02;
    private TextView regist, rember;
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
    private Dialog mDialog;
    private TextView camera;
    private TextView album;
    private TextView cancel;
    private Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_layout);
        bindView();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String psw = intent.getStringExtra("psw");
        L.e("注册信息：" + name + ":" + psw, this);
        if (name != null && psw != null) {
            InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            i.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            et_name.setText(name);
            et_psw.setText(psw);
        }
        if(!SPUtils.getHeader(this).equals("默认值")){
            String path = SPUtils.getHeader(this);
            Bitmap bmp = BitmapFactory.decodeFile(path);
            header.setImageBitmap(bmp);
        }
    }

    /**
     * 绑定布局
     */
    private void bindView() {
        //文字即为显示的内容
        mDialog = CustomProgressDialog.createLoadingDialog(this, "正在登录...");
        mDialog.setCancelable(true);//允许返回

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        changeHeight = dm.heightPixels / 3;

        root = (RelativeLayout) findViewById(R.id.root);
        header = (CircleImageView) findViewById(iv_header);
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
        /**
         * 初始化底部Dialog
         */
        initBottomDialog();


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_regist:
                startActivity(new Intent(this, RegistActivity.class));
                finish();
                break;
            case R.id.tv_rember:
                Toast.makeText(this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.but_login:
                final String name = et_name.getText().toString().trim();
                final String psw = et_psw.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(name)) {
                        mDialog.show();//显示

                        BmobQuery<Person> query = new BmobQuery<>();
                        query.findObjects(new FindListener<Person>() {
                            @Override
                            public void done(List<Person> list, BmobException e) {
                                mDialog.dismiss();
                                if (e != null) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getName().equals(name) && list.get(i).getPassword().equals(psw)) {
                                        startActivity(new Intent(LoginActivity.this, SettingActivity.class));
                                        SPUtils.writeUser("name", name, LoginActivity.this);
                                        SPUtils.writeUser("password", psw, LoginActivity.this);
                                        Toast.makeText(LoginActivity.this, "欢迎回来：" + name, Toast.LENGTH_SHORT).show();
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
            case iv_header:

                //dialog.show();
                break;
            /*case R.id.tv_dialog_album:
                Intent galleryIntent =
                        new Intent(Intent.ACTION_PICK, null);
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image*//*");
                startActivityForResult(galleryIntent, REQUEST_ALBUM);

                dialog.dismiss();
                break;
            case R.id.tv_dialog_camera:
                if (isSdcardExisting()) {
                    Intent cameraIntent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");//拍照
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(this, "请插入sd卡", Toast.LENGTH_LONG)
                            .show();
                }
                dialog.dismiss();
                break;
            case R.id.tv_dialog_cancel:
                dialog.dismiss();
                break;*/


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_ALBUM:
                    resizeImage(data.getData());
                    break;
                case REQUEST_CAMERA:
                    Toast.makeText(this, "相机回传", Toast.LENGTH_SHORT).show();
                    L.e("相机回传",this);
                    /**
                     * 相机回传
                     */
                    if (isSdcardExisting()) {
                        resizeImage(getImageUri());
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！",
                                Toast.LENGTH_LONG).show();
                    }

                    break;
                case RESIZE_REQUEST_CODE:
                    Toast.makeText(this, "裁剪图片回传"+getImageUri().toString(), Toast.LENGTH_SHORT).show();
                    Bitmap photo =  data.getExtras().getParcelable("data");
                    SPUtils.saveHeader(Environment.getExternalStorageDirectory()+"/headers"+File.separator+"header.jpg",this);
                    header.setImageBitmap(photo);
                    break;
            }
        }

    }

    /*private void upLoadHeader() {
        File file = new File(Environment.getExternalStorageDirectory()+"/headers"+File.separator+SPUtils.getUser("name",this)+".jpg");
        L.e("file存在不"+file.exists(),this);
        BmobFile bf = new BmobFile(file);
        bf.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
            if(e==null){
                Toast.makeText(LoginActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(LoginActivity.this, "头像上传失败"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            }
        });

    }*/

    private Uri getImageUri() {
        String path = Environment.getExternalStorageDirectory()+"/headers";
        File file = new File(path);
        String dir = file.getAbsolutePath();
        if(!file.exists()){
            file.mkdir();
            L.e("头像文件夹创建成功",this);
        }
        String headerPath = dir+File.separator+SPUtils.getUser("name",this)+".jpg";
        L.e("头像文件uri=="+headerPath,this);
        File header = new File(headerPath);
        if(!header.exists()){
            try {
                Boolean success = header.createNewFile();
                L.e("头像文件创建成功与否="+success,this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       return Uri.fromFile(header);


    }

    private boolean isSdcardExisting() {
       Boolean isExist =  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return isExist;
    }

    private void initBottomDialog(){
        dialog = new Dialog(this, R.style.dialog_bottom);
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom, null);
        camera = (TextView) view.findViewById(R.id.tv_dialog_camera);
        album = (TextView) view.findViewById(R.id.tv_dialog_album);
        cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        camera.setOnClickListener(this);
        album.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.y = 20;
        window.setAttributes(lp);
    }


    public void resizeImage(Uri uri) {//重塑图片大小
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可以裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }

}
