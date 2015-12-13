package com.cui.librarys.bean;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by cuiyang on 15/12/10.
 * 对图片进行自定义
 */
public  class CropSquareTansformation implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "square()";
    }
}
