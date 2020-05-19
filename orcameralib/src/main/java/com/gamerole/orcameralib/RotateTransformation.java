package com.gamerole.orcameralib;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * create by kevinqiao
 * 2020/5/19
 */
public class RotateTransformation extends BitmapTransformation {

    public static final String ID = "com.bumptech.glide.transformations.FillSpace";
    public static final byte[] ID_BYTES = ID.getBytes(Charset.forName((Key.STRING_CHARSET_NAME)));
    private Float angle;
    public RotateTransformation(Float angle){
        this.angle = angle;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}