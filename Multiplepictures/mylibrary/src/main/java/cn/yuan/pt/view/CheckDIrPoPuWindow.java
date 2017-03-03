package cn.yuan.pt.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.yuan.pt.R;
import cn.yuan.pt.bean.FolderBean;
import cn.yuan.pt.utils.ScreenUtils;

/**
 * Created by yukuo on 2016/5/1.
 * 这是一个选择相册文件夹的弹窗
 */
public class CheckDIrPoPuWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mDatas;
    private Context context;
    public OnDirSelectdListener onDirSelectdListener;

    public interface OnDirSelectdListener {
        void onSelected(FolderBean folderBean);

    }


    public void setOnDirSelectdListener(OnDirSelectdListener onDirSelectdListener) {
        this.onDirSelectdListener = onDirSelectdListener;
    }

    public CheckDIrPoPuWindow(Context context, List<FolderBean> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
        calWidthAndHeight(context);
        mConvertView = LayoutInflater.from(context).inflate(R.layout.popu_check_dir, null);
        setContentView(mConvertView);
        setWidth(mWidth);//设置宽度
        setHeight(mHeight);//设置高度
        setFocusable(true);//设置获取焦点
        setTouchable(true);//设置可以触摸
        setOutsideTouchable(true);//设置外边可以点击
        setBackgroundDrawable(new BitmapDrawable());
        //设置点击事件的监听
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    //不在该界面的点击事件
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        intitViews();
        initEvent();
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onDirSelectdListener != null) {
                    onDirSelectdListener.onSelected(mDatas.get(position));
                }
            }
        });
    }

    private void intitViews() {
        mListView = (ListView) mConvertView.findViewById(R.id.lv_muph_dir_list);
        mListView.setAdapter(new DirListsAdapter());
    }

    /**
     * 计算popuwindow的宽度和高度
     *
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        mWidth = ScreenUtils.getScreenWidth(context);
        mHeight = (int) (ScreenUtils.getScreenHeight(context) * 0.7);
    }

    class DirListsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.popu_mainmultipe, null);
                myHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_image_popu_dir);
                myHolder.textViewname = (TextView) convertView.findViewById(R.id.tv_popu_item_dirname);
                myHolder.textViewcount = (TextView) convertView.findViewById(R.id.tv_popu_item_count);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.imageView.setImageResource(R.drawable.pictures_no);
            FolderBean folderBean = mDatas.get(position);
            Glide.with(context).load(folderBean.getFirstImgPath()).into(myHolder.imageView);
            myHolder.textViewname.setText(folderBean.getName());
            myHolder.textViewcount.setText(folderBean.getCount() + "");
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    class MyHolder {
        ImageView imageView;
        TextView textViewname;
        TextView textViewcount;
    }
}
