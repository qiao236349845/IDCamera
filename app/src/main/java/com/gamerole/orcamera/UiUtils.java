
package com.gamerole.orcamera;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UiUtils {

    static public int getScreenWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    static public int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    static public float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }


    public static int getDays(String start,String end,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            long s = sdf.parse(start).getTime();
            long e = sdf.parse(end).getTime();
            int days = (int) ((e - s) / (1000 * 60 * 60 * 24));
            Log.i("infos","day: " + days);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public static int getDays(String start,String end){
        return getDays(start,end,"yyyy-MM-dd");
    }
    /**
     * 当前时间转换为字符串 年月日时分秒
     *
     * @return
     */
    public static String getTimes() {
        return getTimes("yyyy-MM-dd");
    }

    public static String getTimes(String format) {
        String str = "";
        SimpleDateFormat newFormat = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        str = newFormat.format(date);
        return str;
    }

}
