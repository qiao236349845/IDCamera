package com.gamerole.orcamera;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.RelativeLayout;

/**
 * create by kevinqiao
 * 2020/4/20
 */
public class RulerViewGroup extends RelativeLayout implements Runnable {

    private VelocityTracker mVelocityTracker;
    //手指离开时的滚动速度
    private float velocityX;
    //当前的触摸点的X值
    private float nowtouchX = 0;
    //加速度
    private int speedAdd = 1;
    //单位时间
    private int unitTime = 5;
    //手指滑动方向
    private boolean isLeft = false;

    public RulerViewGroup(Context context) {
        super(context);
        intView();
    }

    public RulerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        intView();
    }

    public RulerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView();
    }


    private void intView() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        final MotionEvent vtev = MotionEvent.obtain(event);


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                nowtouchX = event.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                ((RulerView) getChildAt(0)).setScrowComplent(false);
                try {
                    ((RulerView) getChildAt(0)).setChangeX((event.getX() - nowtouchX));
                    Log.e("ChangeX", (event.getX() - nowtouchX) + "");
                    nowtouchX = event.getX();
                } catch (Exception e) {
                    Log.e("RuleViewGroup", "布局错误");
                }


                //计算实时滑动速度
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(unitTime, 1000);
                velocityX = mVelocityTracker.getXVelocity(event.getPointerId(0));

                //用于run方法判断加速度的正负
                if (velocityX >= 0) {
                    isLeft = false;
                } else {
                    isLeft = true;
                }

                break;

            case MotionEvent.ACTION_UP:
                //启动惯性滚动
                postDelayed(this, unitTime);
                break;

            case MotionEvent.ACTION_CANCEL:
                releaseVelocityTracker();
                break;

            default:
                break;
        }

        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }


    @Override
    public void run() {
        int x = (int) (velocityX * unitTime / 5);

        if (isLeft) {
            velocityX = velocityX + speedAdd;
            if (velocityX >= 0) {
                velocityX = 0;
            }
        } else {
            velocityX = velocityX - speedAdd;
            if (velocityX <= 0) {
                velocityX = 0;
            }
        }

        try {
            ((RulerView) getChildAt(0)).setChangeX(x);
        } catch (Exception e) {
            Log.e("RuleViewGroup", "布局错误");
        }

        if (velocityX != 0) {
            postDelayed(this, unitTime);
        } else {

            try {
                ((RulerView) getChildAt(0)).setScrowComplent(true);
            } catch (Exception e) {
                Log.e("RuleViewGroup", "布局错误");
            }
        }
    }


    //释放VelocityTracker
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}