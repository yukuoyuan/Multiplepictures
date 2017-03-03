package cn.yuan.pt.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yukuo on 2016/5/15.
 */
public class FileUtis {
    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 创建文件夹
     */
    public static void createDir() {
        String destDirName = Environment.getExternalStorageDirectory()+"/xiaoyu";
        File dir = new File(destDirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static String getDirectory() {
        return Environment.getExternalStorageDirectory() + File.separator + "xiaoyu";
    }

    public static File saveFile(Bitmap bm, String fileName) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/xiaoyu/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }

}
