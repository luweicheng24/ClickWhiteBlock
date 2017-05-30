package com.gsww.www.clickwhiteblock.view.moji;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.gsww.www.clickwhiteblock.R;

/**
 * Created by wujiajun
 * @author 928320442@qq.com
 */
public class RenderThread extends Thread {

    private Context context;
    private SurfaceHolder surfaceHolder;
    private RenderHandler renderHandler;
    private Scene scene;

    public RenderThread(SurfaceHolder surfaceHolder, Context context) {
        this.context = context;
        this.surfaceHolder = surfaceHolder;
        scene = new Scene(context);
        //add scene/actor
        scene.setBg(BitmapFactory.decodeResource(context.getResources(), R.drawable.bg0_fine_day));
        scene.add(new BirdUp(context));
        scene.add(new CloudLeft(context));
        scene.add(new CloudRight(context));
        scene.add(new BirdDown(context));
        scene.add(new SunShine(context));
    }

    @Override
    public void run() {
        Log.d("weather", "run");
        //在非主线程使用消息队列
        Looper.prepare();
        renderHandler = new RenderHandler();
        renderHandler.sendEmptyMessage(0);
        Looper.loop();
    }

    public RenderHandler getRenderHandler() {
        return renderHandler;
    }

    public class RenderHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (scene.getWidth() != 0 && scene.getHeight() != 0) {
                        draw();
                    }
                    renderHandler.sendEmptyMessage(0);
                    break;
                case 1:
                    Looper.myLooper().quit();
                    break;
            }
        }
    }


    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            scene.draw(canvas);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void setWidth(int width) {
        scene.setWidth(width);
    }

    public void setHeight(int height) {
        scene.setHeight(height);
    }
}
