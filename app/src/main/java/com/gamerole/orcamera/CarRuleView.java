package com.gamerole.orcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * create by kevinqiao
 * 2020/4/20
 */
public class CarRuleView extends View implements Runnable{


    private int width = 0;

    private Context context;

    private boolean isMeasure = false;

    private Drawable locatorDrawable;
    private Paint paint;

    private int smallR;
    private int bigR;
    private int textSize_12;
    private int textSize_14;
    private int textSize_10;
    Paint paint1;
    Paint paint2;
    private int minX;
    private int add ;
    private int end;
    private int spaceUnit;
    private int current;
    private int remain;

    private List<String> dateList;

    private String start = "";
    private String strEnd = "";
    private String realEnd = "";


    private VelocityTracker velocityTracker = null;//速度监测
    private float velocity;//当前滑动速度
    private float a = 1000000;//加速度
    private boolean continueScroll;//是否继续滑动
    private float lastX;


    public CarRuleView(Context context) {
        super(context);
        init(context,null);
    }

    public CarRuleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CarRuleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        paint = new Paint();

        paint.setColor(Color.parseColor("#ff092e5d"));
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
        locatorDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_car, null);
        this.context = context;

        dateList = new ArrayList<>();

        smallR = UiUtils.dipToPx(context,4);
        bigR = UiUtils.dipToPx(context,15);
        textSize_12 = UiUtils.sp2px(context,12);
        textSize_14 = UiUtils.sp2px(context,14);
        textSize_10 = UiUtils.sp2px(context,10);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint1 = new Paint();;
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.parseColor("#ff092e5d"));
        paint1.setAntiAlias(true);
    }

    private int top ;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("infos",widthMeasureSpec + "==" + heightMeasureSpec);
        if(!isMeasure){
            width = getMeasuredWidth() ;
            minX = 0;
            isMeasure = true;
        }
    }



    public void setRecoverListDays(final List<String> list) {

        postDelayed(new Runnable() {
            @Override
            public void run() {
                handleData(list);
            }
        },500);

    }


    private void handleData(List<String> list){
        dateList.clear();
        if(list != null && !list.isEmpty()){
            dateList.addAll(list);
            if(dateList.size() >= 2){
                start = dateList.remove(0);
                strEnd = dateList.get(0);
                end = UiUtils.getDays(start,strEnd);
                spaceUnit = width / end;

                if(!dateList.isEmpty()){
                    String last = dateList.get(dateList.size() - 1);
                    add = UiUtils.getDays(strEnd,last);
                    realEnd = last;
                    String strCurrent = UiUtils.getTimes();
                    current = UiUtils.getDays(start,strCurrent);
                    remain = UiUtils.getDays(strCurrent,realEnd);
                }else {
                    add = 0;
                }
            }else {
                try {
                    throw new Exception("setRecoverListDays.size() 必须 >= 2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        postInvalidate();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();

        // 画续租时间点
        paint2.setTextSize(textSize_12);
        paint2.setColor(Color.parseColor("#333333"));
        Rect rect = new Rect();

        List<Integer> circleList = new ArrayList<>();
        circleList.clear();
        for(int i = 0; i < dateList.size() - 1; i ++){
            String last = dateList.get(i);
            int detal = UiUtils.getDays(start,last);
            int flax = minX + detal * spaceUnit;
            canvas.drawCircle(flax,height / 2 ,smallR,paint);

            paint2.getTextBounds(last, 0, last.length(), rect);
            canvas.drawText(last,flax - rect.width() / 2,height / 2 - bigR,paint2);
            circleList.add(flax);
        }

        // 画当前时间点
        int flax = minX + current * spaceUnit ;
        canvas.drawCircle(flax,height / 2 ,smallR,paint);
        circleList.add(flax);
        Collections.sort(circleList);

        int carWidth = UiUtils.dipToPx(context,36);
        int carHeight = UiUtils.dipToPx(context,19);

        int le = flax - carWidth / 2;
        int ri = flax + carHeight / 2;
        int to = height / 2  - carHeight * 3 / 2;
        int bo = height /2 - carHeight / 2;
        locatorDrawable.setBounds(le,to,ri,bo);
        locatorDrawable.draw(canvas);


        // 画线
        int stx = add * spaceUnit + width + minX ;
//        canvas.drawLine(minX,height / 2,stx ,height / 2,paint);
        int circleX = minX;
        int detalR = 0;
        for(int i = 0; i < circleList.size(); i++){
            int pointX = circleList.get(i);
            if(i == 0){
                detalR = 0;
            }else {
                detalR = smallR;
            }
            canvas.drawLine(circleX + detalR,height / 2,pointX - smallR ,height / 2,paint);
            circleX = pointX;
        }
        canvas.drawLine(circleX + smallR,height / 2,stx ,height / 2,paint);

        // 画起始位置

        // 起始位置圆
        paint1.setColor(Color.parseColor("#ff092e5d"));
        canvas.drawCircle( minX + bigR,height / 2 ,bigR,paint1);
        canvas.drawCircle( stx - bigR ,height / 2 ,bigR,paint1);

        // 起始日期
        paint2.setTextSize(textSize_12);
        paint2.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(realEnd,stx ,height / 2 - bigR * 2 ,paint2);
        paint2.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(start,minX,height / 2 - bigR * 2,paint2);



        // 绘制提示信息，使用时长，剩余时长
        canvas.drawText("已用" + current + "天",bigR * 2,height / 2 + bigR * 2,paint2);
        Rect rect1 = new Rect();
        String strRemain = "剩余" + remain + "天";
        paint2.getTextBounds(strRemain, 0, strRemain.length(), rect1);
        canvas.drawText(strRemain,width - bigR * 2 - rect1.width(),height / 2 + bigR * 2 ,paint2);

        // 画文字
        Rect bounds = new Rect();
        String get = "取";
        paint1.setTextAlign(Paint.Align.LEFT);
        paint1.setTextSize(textSize_14);
        paint1.setColor(Color.parseColor("#ffffff"));
        paint1.getTextBounds(get, 0, get.length(), bounds);
        canvas.drawText(get,minX + bigR - bounds.width() / 2,height / 2 + bounds.height() / 2,paint1);

        paint1.setTextAlign(Paint.Align.RIGHT);
        String back = "还";
        canvas.drawText(back,stx - bigR + bounds.width() / 2,height / 2 + bounds.height() / 2,paint1);


    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                if(velocityTracker == null){
                    velocityTracker = VelocityTracker.obtain();
                }else {
                    velocityTracker.clear();
                }
                continueScroll = false;
                break;

            case MotionEvent.ACTION_MOVE:
                // 加入事件追踪
                velocityTracker.addMovement(event);

                // 获取偏移量
                int offsetX = (int) (lastX - x);
                minX = minX - offsetX;
                invalidate();
                lastX = x;
                break;

            case MotionEvent.ACTION_UP:
                confirmBorder();
                // 判断当前滑动速度
                velocityTracker.computeCurrentVelocity(1000);
                velocity = velocityTracker.getXVelocity();

                float minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
                if (Math.abs(velocity) > minVelocity) {
                    continueScroll = true;
                    postDelayed(this,10);
                    // 继续滚动
                } else {
                    recycle();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                recycle();
                break;
            default:
                break;
        }

        return true;
    }

    private void recycle(){
        velocityTracker.recycle();
        velocityTracker = null;
    }

    private void confirmBorder(){
        int detal = add * spaceUnit;
        if(minX < 0){
            if(minX < - detal){
                minX = - detal;
            }
            postInvalidate();
        }else {
            minX = 0;
            postInvalidate();
        }
    }

    @Override
    public void run() {
        float velocityAbs = 0;//速度绝对值
        if (velocity > 0 && continueScroll) {
            velocity -= 50;
            minX += velocity * velocity / a;
            velocityAbs = velocity;
        } else if (velocity < 0 && continueScroll) {
            velocity += 50;
            minX -= velocity * velocity / a;
            velocityAbs = -velocity;
        }
        confirmBorder();
        postInvalidate();
        if (continueScroll && velocityAbs > 0) {
            postDelayed(this,10);
        } else {
            continueScroll = false;
        }
    }
}
