package cn.yuan.pt.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.yuan.pt.R;


/**
 * Created by yukuo on 2016/4/4.
 * 这是一个图片显示的适配器
 */
public class ImageAdapter extends BaseAdapter {
    private List<String> mDatas;
    private String dirPath;
    private static Set<String> mSelectImg = new HashSet<String>();
    private OnImageSelectedListener onImageSelectedListener;
    private int maxnumber;

    public ImageAdapter(List<String> mDatas, String dirPath, int maxnumber) {
        this.mDatas = mDatas;
        this.dirPath = dirPath;
        this.maxnumber = maxnumber;
    }

    @Override
    public int getCount() {
        return mDatas.size() + 1;
    }

    public void setOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
        this.onImageSelectedListener = onImageSelectedListener;
    }

    public interface OnImageSelectedListener {
        void selected(Set<String> mSelectImg);

        void onCameraClick();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final MyHolder myHolder;
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_photos, null);
            myHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_image);
            myHolder.imageButton = (ImageButton) convertView.findViewById(R.id.ib_item_check);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        if (position == 0) {
            Glide.with(parent.getContext()).load(R.drawable.camera).into(myHolder.imageView);
            myHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0 && onImageSelectedListener != null) {
                        onImageSelectedListener.onCameraClick();
                    }
                }
            });


        } else {
            //重置状态
            myHolder.imageView.setImageResource(R.drawable.pictures_no);
            myHolder.imageButton.setImageResource(R.drawable.picture_unselected);
            myHolder.imageView.setColorFilter(null);
            //加载图片
            Glide.with(parent.getContext()).load(dirPath + "/" + mDatas.get(position - 1)).centerCrop().into(myHolder.imageView);
            myHolder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //已经被选择
                    if (mSelectImg.contains(dirPath + "/" + mDatas.get(position - 1))) {
                        mSelectImg.remove(dirPath + "/" + mDatas.get(position - 1));
                        myHolder.imageButton.setImageResource(R.drawable.picture_unselected);
                        myHolder.imageView.setColorFilter(null);
                        if (onImageSelectedListener != null) {
                            onImageSelectedListener.selected(mSelectImg);
                        }
                    } else {
                        if (mSelectImg.size() == maxnumber) {
                            Toast.makeText(parent.getContext(), "只能选择" + maxnumber + "张图片", Toast.LENGTH_LONG).show();
                            return;
                        }
                        mSelectImg.add(dirPath + "/" + mDatas.get(position - 1));
                        myHolder.imageButton.setImageResource(R.drawable.pictures_selected);
                        //设置滤镜效果颜色
                        myHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
                        if (onImageSelectedListener != null) {
                            onImageSelectedListener.selected(mSelectImg);
                        }
                    }
                }
            });
            //已经被选择
            if (mSelectImg.contains(dirPath + "/" + mDatas.get(position - 1))) {
                myHolder.imageButton.setImageResource(R.drawable.pictures_selected);
                //设置滤镜效果颜色
                myHolder.imageView.setColorFilter(Color.parseColor("#77000000"));
            }
        }

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void cleanSelectedData() {
        mSelectImg.clear();
    }

    class MyHolder {
        ImageView imageView;//图片
        ImageButton imageButton;//选中状态
    }
}
