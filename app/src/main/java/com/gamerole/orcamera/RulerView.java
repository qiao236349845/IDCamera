package com.gamerole.orcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * create by kevinqiao
 * 2020/4/20
 */
public class RulerView extends View {

    private Paint mPaint;

    private Path path;
    private String color = "#FF4081";
    //尺子Y轴水平基点
    private int startYDraw;
    //矫正刻度起始位置
    private float firststep = 0;
    //每个刻度起始位置
    private float cachStep = 0;
    //尺子长度
    private int ruleSize = 501;
    //一屏幕的刻度数
    private int viewAll = 60;
    private NumListener numListener;

    private boolean isScrowComplent = false;

    public RulerView(Context context) {
        super(context);
        initView();
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mPaint = new Paint();
        path = new Path();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.parseColor(color));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(35);
    }


    protected void setScrowComplent(boolean isScrowComplent) {
        this.isScrowComplent = isScrowComplent;
        invalidate();
    }

    /**
     * 滚动时调用（给RuleViewGroup调用，不要私自调用）
     *
     * @param changeX
     */
    protected void setChangeX(float changeX) {
        firststep = firststep + changeX;
        invalidate();
    }


    /**
     * 手动输入时，设置当前刻度值
     *
     * @param rule
     */
    public void setNowRule(int rule) {
        firststep = getWidth() / 2 - (getWidth() / viewAll * rule);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        startYDraw = (getHeight() / 2 - 20);

        path.moveTo(getWidth() / 2, (startYDraw));
        path.lineTo((getWidth() / 2) + 10, (startYDraw) - 30);
        path.lineTo((getWidth() / 2) - 10, (startYDraw) - 30);
        path.lineTo(getWidth() / 2, (startYDraw));
        canvas.drawPath(path, mPaint);


        //当起始位置超过中点时，则切换为中点，这样起始位置就不会跨过中点
        if (firststep > getWidth() / 2) {
            firststep = getWidth() / 2;
        }


        //-((getWidth()/viewAll)*(ruleSize-1)-(getWidth()/2))的计算结果为终点滑到中点时，起始位置的结果，
        // 控制器不要超过此点，能保证终点不划过中点
        if (firststep <= -((getWidth() / viewAll) * (ruleSize - 1) - (getWidth() / 2))) {
            firststep = -((getWidth() / viewAll) * (ruleSize - 1) - (getWidth() / 2));
        }

        cachStep = firststep;
        for (int i = 0; i < ruleSize; i++) {

            if ((i % 5) == 0) {
                canvas.drawLine(cachStep, startYDraw, cachStep, (startYDraw) + 80, mPaint);
                canvas.drawText(i + "", cachStep, (startYDraw) + 120, mPaint);
            } else {
                canvas.drawLine(cachStep, startYDraw, cachStep, (startYDraw) + 55, mPaint);
            }

            //当前刻度在中点
            if (cachStep <= (getWidth() / 2) && (cachStep + (getWidth() / viewAll)) >= (getWidth() / 2)) {
                if (null != numListener) {
                    numListener.getnum(i);

                    //滚动完成
                    if (isScrowComplent) {
                        if(cachStep+(getWidth() / viewAll/2)>=(getWidth() / 2)){
                            setChangeX((getWidth() / 2)-cachStep);
                        }else{
                            setChangeX((getWidth() / 2)-(getWidth() / viewAll)-cachStep);
                        }
                    }
                }
            }
            cachStep = cachStep + (getWidth() / viewAll);
        }
    }

    public void setNumListener(NumListener numListener) {
        this.numListener = numListener;
    }

    public interface NumListener {
        void getnum(int num);
    }
}

