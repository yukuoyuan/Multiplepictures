package cn.yuan.mutiph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yukuoyuan on 2017/3/27.
 */

public class ImageUtils {

    public ImageUtils() {
    }
    /**
     * 这是一个保存bitmap为一个文件的方法
     *
     * @param bitmap
     * @return
     */
    public static File saveBitmapFile(Bitmap bitmap) {
        File file = new File("/mnt/sdcard/" + System.currentTimeMillis()
                + ".jpg");// 将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Bitmap decodeScaleImage(String var0, int var1, int var2) {
        BitmapFactory.Options var3 = getBitmapOptions(var0);
        int var4 = calculateInSampleSize(var3, var1, var2);
        var3.inSampleSize = var4;
        var3.inJustDecodeBounds = false;
        Bitmap var5 = BitmapFactory.decodeFile(var0, var3);
        int var6 = readPictureDegree(var0);
        Bitmap var7 = null;
        if (var5 != null && var6 != 0) {
            var7 = rotaingImageView(var6, var5);
            var5.recycle();
            var5 = null;
            return var7;
        } else {
            return var5;
        }
    }

    public static Bitmap decodeScaleImage(Context var0, int var1, int var2, int var3) {
        BitmapFactory.Options var5 = new BitmapFactory.Options();
        var5.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(var0.getResources(), var1, var5);
        int var6 = calculateInSampleSize(var5, var2, var3);
        var5.inSampleSize = var6;
        var5.inJustDecodeBounds = false;
        Bitmap var4 = BitmapFactory.decodeResource(var0.getResources(), var1, var5);
        return var4;
    }

    public static int calculateInSampleSize(BitmapFactory.Options var0, int var1, int var2) {
        int var3 = var0.outHeight;
        int var4 = var0.outWidth;
        int var5 = 1;
        if (var3 > var2 || var4 > var1) {
            int var6 = Math.round((float) var3 / (float) var2);
            int var7 = Math.round((float) var4 / (float) var1);
            var5 = var6 > var7 ? var6 : var7;
        }

        return var5;
    }

    public static String getScaledImage(Context var0, String var1, int var2) {
        File var3 = new File(var1);
        if (var3.exists()) {
            long var4 = var3.length();
            if (var4 > 102400L) {
                Bitmap var6 = decodeScaleImage(var1, 640, 960);

                try {
                    File var7 = new File(var0.getExternalCacheDir(), "eaemobTemp" + var2 + ".jpg");
                    FileOutputStream var8 = new FileOutputStream(var7);
                    var6.compress(Bitmap.CompressFormat.JPEG, 60, var8);
                    var8.close();
                    return var7.getAbsolutePath();
                } catch (Exception var9) {
                    var9.printStackTrace();
                }
            }
        }

        return var1;
    }

    public static int readPictureDegree(String var0) {
        short var1 = 0;

        try {
            ExifInterface var2 = new ExifInterface(var0);
            int var3 = var2.getAttributeInt("Orientation", 1);
            switch (var3) {
                case 3:
                    var1 = 180;
                case 4:
                case 5:
                case 7:
                default:
                    break;
                case 6:
                    var1 = 90;
                    break;
                case 8:
                    var1 = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return var1;
    }

    public static Bitmap rotaingImageView(int var0, Bitmap var1) {
        Matrix var2 = new Matrix();
        var2.postRotate((float) var0);
        Bitmap var3 = Bitmap.createBitmap(var1, 0, 0, var1.getWidth(), var1.getHeight(), var2, true);
        return var3;
    }

    public static BitmapFactory.Options getBitmapOptions(String var0) {
        BitmapFactory.Options var1 = new BitmapFactory.Options();
        var1.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(var0, var1);
        return var1;
    }
}
