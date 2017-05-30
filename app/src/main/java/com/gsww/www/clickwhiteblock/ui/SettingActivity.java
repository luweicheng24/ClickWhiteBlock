package com.gsww.www.clickwhiteblock.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gsww.www.clickwhiteblock.R;
import com.gsww.www.clickwhiteblock.utils.L;
import com.gsww.www.clickwhiteblock.utils.SPUtils;
import com.gsww.www.clickwhiteblock.view.CircleImageView;
import com.gsww.www.clickwhiteblock.view.CustomProgressDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Author   : luweicheng on 2017/5/26 0026 14:35
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 设置界面
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESIZE_REQUEST_CODE = 0x00;
    private static final int REQUEST_ALBUM = 0X01;
    private static final int REQUEST_CAMERA = 0X02;
    private Button startGame;
    private Button logOff;
    private Dialog mDialog;
    private Dialog lDialog;
    private CircleImageView iv_header;
    private Dialog dialog;
    private TextView camera;
    private TextView album;
    private TextView cancel;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            getWindow().setStatusBarColor(Color.parseColor("#f44075fd"));
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_setting_layout);
        //文字即为显示的内容
        mDialog = CustomProgressDialog.createUnLoadingDialog(this, "游戏加载中...");
        mDialog.setCancelable(true);//允许返回
        lDialog = CustomProgressDialog.createLoadingDialog(this, "头像上传中...");
        lDialog.setCancelable(false);//不允许返回
        lDialog.setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        startGame = (Button) findViewById(R.id.but_startGame);
        iv_header = (CircleImageView) findViewById(R.id.setting_header);
        logOff = (Button) findViewById(R.id.but_logOff);
        findViewById(R.id.iv_back).setOnClickListener(this);
        /**
         * 初始化底部Dialog
         */
        initBottomDialog();
        startGame.setOnClickListener(this);
        iv_header.setOnClickListener(this);
        logOff.setOnClickListener(this);
        if (!SPUtils.getHeader(this).equals("默认值")) {
            Bitmap bmp = BitmapFactory.decodeFile(SPUtils.getHeader(this));
            iv_header.setImageBitmap(bmp);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_startGame:
                mDialog.show();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.but_logOff:
                unRegist();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.setting_header:
                dialog.show();
                break;
            case R.id.tv_dialog_album:
                Intent galleryIntent =
                        new Intent(Intent.ACTION_PICK, null);
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
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
                break;
        }
    }

    private void unRegist() {
        if (SPUtils.clearUser(this)) {
            Toast.makeText(this, "退出登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "退出登录失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDialog.dismiss();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ALBUM:
                    resizeImage(data.getData());
                    break;
                case REQUEST_CAMERA:
                    Toast.makeText(this, "相机回传", Toast.LENGTH_SHORT).show();
                    L.e("相机回传", this);
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
                    lDialog.show();

                    Toast.makeText(this, "裁剪图片回传" + getImageUri().toString(), Toast.LENGTH_SHORT).show();
                    Bitmap photo = data.getExtras().getParcelable("data");
                    iv_header.setImageBitmap(photo);
                    saveFile(photo);
                    SPUtils.saveHeader(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator+"headers" + File.separator + SPUtils.getUser("name", this) + ".jpg", this);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            upLoadHeader();
                        }
                    }.start();
                    break;
            }
        }

    }

    /**
     * 将Bitmap存到文件里面
     *
     * @param photo
     */
    private void saveFile(Bitmap photo) {
        //压缩图片到字节数组的输出流里面
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;
        photo.compress(Bitmap.CompressFormat.JPEG, options, baos);
        //循环压缩直到小于100k
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            photo.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "headers" +File.separator+ SPUtils.getUser("name", this) + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            L.e("图片写入成功"+file.getAbsolutePath() + file.length(), this);
            fos.close();
            baos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void upLoadHeader() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/headers" + File.separator + SPUtils.getUser("name", this) + ".jpg");
        L.e("file存在不" + file.exists(), this);
        BmobFile bf = new BmobFile(file);
        bf.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                lDialog.dismiss();
                if (e == null) {
                    Toast.makeText(SettingActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingActivity.this, "头像上传失败" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private Uri getImageUri() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "headers";
        File file = new File(path);
        String dir = file.getAbsolutePath();
        if (!file.exists()) {
            file.mkdir();
            L.e("头像文件夹创建成功", this);
        }
        String headerPath = dir + File.separator + SPUtils.getUser("name", this) + ".jpg";
        L.e("头像文件uri==" + headerPath, this);
        File header = new File(headerPath);
        if (!header.exists()) {
            try {
                Boolean success = header.createNewFile();
                L.e("头像文件创建成功与否=" + success, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Uri.fromFile(header);


    }

    private boolean isSdcardExisting() {
        Boolean isExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        return isExist;
    }

    private void initBottomDialog() {
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

  private Long secTime = 0l;
    @Override
    public void onBackPressed() {

        long oneTime = System.currentTimeMillis();
        if(oneTime-secTime<2000&&secTime!=0l){
            finish();
        }else{
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            secTime = System.currentTimeMillis();
        }


    }

}
