package cn.yuan.pt.bean;

/**
 * Created by yukuo on 2016/4/3.
 * 这是一个文件的实体
 */
public class FolderBean {
    /**
     * 当前文件夹的路径
     */
    public String dir;
    /**
     * 第一张图片的路径
     */
    public String firstImgPath;
    /**
     * w文件夹的名字
     */
    public String name;
    /**
     * 文件夹图片的个数
     */
    public int count;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf);
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }

    public void setFirstImgPath(String firstImgPath) {
        this.firstImgPath = firstImgPath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
