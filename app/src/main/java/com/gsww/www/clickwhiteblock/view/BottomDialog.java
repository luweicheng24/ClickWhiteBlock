package com.gsww.www.clickwhiteblock.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gsww.www.clickwhiteblock.R;

/**
 * Author   : luweicheng on 2017/5/29 0029 15:23
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 底部弹出对话框
 */

public class BottomDialog extends Dialog implements View.OnClickListener {
    private TextView camera;
    private TextView album;
    private TextView cancel;

    public BottomDialog(@NonNull Context context) {
        super(context, R.style.dialog_bottom);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.dialog_bottom);
        initView();
    }

    private void initView() {
        camera = (TextView) findViewById(R.id.tv_dialog_camera);
        album = (TextView) findViewById(R.id.tv_dialog_album);
        cancel = (TextView) findViewById(R.id.tv_dialog_cancel);
        camera.setOnClickListener(this);
        album.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (dialogOnClickListener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_dialog_album:
                dialogOnClickListener.onAlbum();
                break;
            case R.id.tv_dialog_camera:
                dialogOnClickListener.onCamera();
                break;
            case R.id.tv_dialog_cancel:
                dialogOnClickListener.onCancel();
                break;

        }
    }

    public interface DialogOnClickListener {
        void onCamera();

        void onAlbum();

        void onCancel();
    }

    public void setDialogOnClickListener(DialogOnClickListener dialogOnClickListener) {
        this.dialogOnClickListener = dialogOnClickListener;
    }

    private DialogOnClickListener dialogOnClickListener;

}
