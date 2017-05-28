package com.gsww.www.clickwhiteblock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.gsww.www.clickwhiteblock.bean.PiecesRectF;
import com.gsww.www.clickwhiteblock.utils.L;
import com.gsww.www.clickwhiteblock.utils.SoundUtils;

import static android.view.MotionEvent.ACTION_DOWN;

/**
 * Author   : luweicheng on 2017/4/30 0030 13:50
 * E-mail   ：1769005961@qq.com
 * GitHub   : https://github.com/luweicheng24
 * funcation: 自定义踩白块的ViewGroup
 */

public class StiackBlockView extends View {

    public static final int SPEED_PRIMARY = 5;//初级速度
    public static final int SPEED_MIDDLE = 15;//中级速度
    public static final int SPEED_HIGH = 18;//高级速度
    public static final int SPEED_MORE_HIGH = 22;//超高级速度
    private PiecesRectF[][] recfs = new PiecesRectF[5][4];//五行四列存矩形块
    private SparseArray<PiecesRectF> selectRecfs = new SparseArray<>();//储存点击的矩形块
    private int topRectHeight = 0;
    private Paint paint;
    private boolean isGameOver = false;
    private boolean isStop = true;
    private int score;
    private int speed;//默认的滑动速度
    private SoundUtils soundUtils;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public StiackBlockView(Context context) {
        this(context, null);

    }

    public StiackBlockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StiackBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        soundUtils = SoundUtils.getInstance();
        soundUtils.init(getContext());
        paint = new Paint();
        initRect();
    }

    /**
     * 初始化5行4列的矩形框
     */
    private void initRect() {

        /**
         * 创建4行4列的方块(每行1,2列最少有一个黑块，3,4列最少有一个黑块)
         */

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                recfs[i][j] = new PiecesRectF();
                if (j == 1) {

                    if (recfs[i][j - 1].getType() == PiecesRectF.BLAKE || recfs[i][j - 1].getType() == PiecesRectF.START) {
                        recfs[i][j].setType(PiecesRectF.WRITE);
                    }
                } else if (j == 3) {
                    if (recfs[i][j - 1].getType() == PiecesRectF.BLAKE || recfs[i][j - 1].getType() == PiecesRectF.START) {
                        recfs[i][j].setType(PiecesRectF.WRITE);
                    }
                    if (recfs[i][j - 2].getType() == PiecesRectF.WRITE && recfs[i][j - 3].getType() == PiecesRectF.WRITE) {
                        recfs[i][j].setType(PiecesRectF.BLAKE);
                    }
                }


            }
        }//创建第五行的数据，只需要一个类型为开始的矩形，其他的都是白色
        for (int i = 0; i < 4; i++) {
            recfs[4][i] = new PiecesRectF(true);
            if (i == 1) {
                recfs[4][i].setType(PiecesRectF.START);
            } else {
                recfs[4][i].setType(PiecesRectF.WRITE);
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRects(canvas);
    }

    /**
     * 绘制矩形框
     *
     * @param canvas
     */
    private void drawRects(Canvas canvas) {
        int w = getWidth() / 4;
        int h = getHeight() / 4;
        if (isGameOver) {
            if (onBalckCheckListener != null) {
                onBalckCheckListener.gameOver();
            }
            isGameOver = false;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                recfs[i][j].left = w * j;
                recfs[i][j].right = w * (j + 1);
                recfs[i][j].bottom = topRectHeight + i * h;
                recfs[i][j].top = recfs[i][j].bottom - h;
                paint.setStyle(Paint.Style.FILL);//设置画笔为填充整个矩形
                if (recfs[i][j].getType() == PiecesRectF.WRITE) {

                    paint.setColor(Color.WHITE);
                    canvas.drawRect(recfs[i][j], paint);

                } else if (recfs[i][j].getType() == PiecesRectF.BLAKE) {

                    paint.setColor(Color.BLACK);
                    canvas.drawRect(recfs[i][j], paint);

                } else if (recfs[i][j].getType() == PiecesRectF.BLUE) {

                    paint.setColor(Color.BLUE);
                    canvas.drawRect(recfs[i][j], paint);

                } else if (recfs[i][j].getType() == PiecesRectF.RED) {

                    paint.setColor(Color.RED);
                    canvas.drawRect(recfs[i][j], paint);

                } else if (recfs[i][j].getType() == PiecesRectF.START) {

                    paint.setColor(Color.BLACK);
                    canvas.drawRect(recfs[i][j], paint);

                    //在绘制文字
                    paint.setColor(Color.parseColor("#ffffff"));
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setTextSize(50);

                    String start = "开始";
                    Rect bounds = new Rect();
                    paint.getTextBounds(start, 0, start.length(), bounds);
                    float x = recfs[i][j].left / 2 + recfs[i][j].right / 2;
                    float y = recfs[i][j].top / 2 + recfs[i][j].bottom / 2 + bounds.bottom / 2 - bounds.top / 2;
                    canvas.drawText(start, x, y, paint);
                }
                /**
                 * 绘制边框
                 */
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.parseColor("#cccccc"));
                paint.setStrokeWidth(3);
                canvas.drawRect(recfs[i][j], paint);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        switch (event.getActionMasked()) {
            case ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int id = event.getPointerId(index);
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 4; j++) {
                        PiecesRectF f = recfs[i][j];
                        if (event.getX() > f.left && event.getX() < f.right
                                && event.getY() < f.bottom && event.getY() > f.top) {
                            selectRecfs.put(id, f);
                            if (f.getType() == PiecesRectF.BLAKE) {
                                f.setType(PiecesRectF.BLUE);
                                soundUtils.play();
                                score++;
                            } else if (f.getType() == PiecesRectF.WRITE) {
                                soundUtils.playFail();
                                f.setType(PiecesRectF.RED);
                                isGameOver = true;
                                postInvalidate();
                            } else if (f.getType() == PiecesRectF.START) {
                                f.setType(PiecesRectF.BLUE);
                                soundUtils.playStart();
                                startThread();
                            }
                            if (onBalckCheckListener != null) {
                                onBalckCheckListener.score(score);
                            }
                        }

                    }
                }

            }

            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int id = event.getPointerId(index);
                PiecesRectF f = selectRecfs.get(id, null);//得到某个手指选中的方块
                if (f != null && f.getType() == PiecesRectF.BLUE) {
                    //手指抬起后，将蓝色重新变红
                    f.setType(PiecesRectF.WRITE);
                }
            }
            break;

        }
        return true;
    }

    /**
     * 开启线程不断的对界面进行绘制
     */
    private void startThread() {

        new Thread() {
            @Override
            public void run() {

                while (true) {
                    /**
                     * 判断是否含有黑块接触屏幕底端
                     */
                    if (topRectHeight > 0) {
                        if (checkHasBlack()) {
                            isGameOver = true;
                            soundUtils.playFail();
                            postInvalidate();
                            return;
                        }
                    }
                    topRectHeight += getSpeed();//固定的矩形下滑速度
                    if (isGameOver) {
                        return;
                    }
                    if (topRectHeight > getHeight() / 4) {//如果顶层的方块高度超出清零
                        topRectHeight = 0;
                        updateView();//更新矩形块
                    }
                    try {
                        sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();//重绘
                }
            }
        }.start();
    }

    /**
     * 检查最后一行是否含有黑块
     *
     * @return
     */
    private Boolean checkHasBlack() {
        Boolean hasBlack = false;
        for (int i = 0; i < 4; i++) {
            if (recfs[4][i].getType() == PiecesRectF.BLAKE || recfs[4][i].getType() == PiecesRectF.START) {
                recfs[4][i].setType(PiecesRectF.RED);
                hasBlack = true;
            }
        }
        return hasBlack;
    }

    /**
     * 从倒数第二行开始跟新到未出现的第一行
     */
    private void updateView() {

        for (int i = 4; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (i == 0) {
                    recfs[i][j] = new PiecesRectF();
                    if (j == 1) {
                        if (recfs[i][j - 1].getType() == PiecesRectF.BLAKE)
                            recfs[i][j].setType(PiecesRectF.WRITE);
                    } else if (j == 3) {
                        if (recfs[i][j - 1].getType() == PiecesRectF.BLAKE)
                            recfs[i][j].setType(PiecesRectF.WRITE);
                        else if (recfs[i][j - 2].getType() == PiecesRectF.WRITE &&
                                recfs[i][j - 3].getType() == PiecesRectF.WRITE)
                            recfs[i][j].setType(PiecesRectF.BLAKE);
                    }
                } else {
                    recfs[i][j] = recfs[i - 1][j];
                }
            }
        }

    }

    /**
     * 重新开始游戏
     */
    public void restart() {
        isGameOver = false;
        topRectHeight = 0;//顶层高度归零
        score = 0;//分数归零
        onBalckCheckListener.score(score);
        initRect();//重新初始化
        invalidate();//再次绘制
    }

    public interface OnBalckCheckListener {
        void score(int score);

        void gameOver();
    }

    private OnBalckCheckListener onBalckCheckListener;

    public void setOnBalckCheckListener(OnBalckCheckListener onBalckCheckListener) {
        this.onBalckCheckListener = onBalckCheckListener;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        L.e("资源释放",this);
        soundUtils.release();
    }
}
