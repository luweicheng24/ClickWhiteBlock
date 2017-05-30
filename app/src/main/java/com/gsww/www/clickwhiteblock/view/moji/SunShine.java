package com.gsww.www.clickwhiteblock.view.moji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.gsww.www.clickwhiteblock.R;

/**
 * Created by wujiajun
 * @author 928320442@qq.com
 */
public class SunShine extends Actor {

    float initPositionX;
    float initPositionY;
    boolean isInit;
    Bitmap frame;
    RectF box;
    RectF targetBox;
    Paint paint = new Paint();
    int alpha;
    boolean alphaUp = true;

    protected SunShine(Context context) {
        super(context);
        box = new RectF();
        targetBox = new RectF();
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas, int width, int height) {
        //逻辑处理
        //初始化
        if (!isInit) {
            initPositionX = width * 0.275F;
            initPositionY = height * 0.365F;
            frame = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunshine);
            box.set(0, 0, frame.getWidth(), frame.getHeight());
            matrix.reset();
            matrix.setScale(2f, 2f);
            matrix.mapRect(targetBox, box);
            matrix.postTranslate(initPositionX - targetBox.width() / 2, initPositionY - targetBox.height() / 2);
            isInit = true;
            return;
        }
        //旋转
        matrix.mapRect(targetBox, box);
        matrix.postRotate(0.5F, targetBox.centerX(), targetBox.centerY());
        //透明度变化
        if (alphaUp) {
            alpha++;
        } else {
            alpha--;
        }
        if (alpha >= 255) {
            alphaUp = false;
        }
        if (alpha <= 0) {
            alphaUp = true;
        }
        paint.setAlpha(alpha);
        //绘制
        canvas.drawBitmap(frame, matrix, paint);
    }
}
