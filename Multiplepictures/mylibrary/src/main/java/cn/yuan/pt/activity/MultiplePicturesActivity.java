package cn.yuan.pt.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.yuan.pt.R;
import cn.yuan.pt.adapter.ImageAdapter;
import cn.yuan.pt.bean.FolderBean;
import cn.yuan.pt.utils.FileUtis;
import cn.yuan.pt.view.CheckDIrPoPuWindow;

/**
 * Created by yukuo on 2016/5/1.
 * 这是一个多选图片的界面
 */
public class MultiplePicturesActivity extends AppCompatActivity implements View.OnClickListener {

    private GridView gv_mutipleph;
    private TextView tv_muph_right;
    private TextView tv_muph_dir;
    private TextView tv_muph_dirnumber;
    private RelativeLayout rl_muph_popu;
    private CheckDIrPoPuWindow checkDIrPoPuWindow;
    /**
     * 这是一个图片列表的适配器
     */
    private ImageAdapter imageAdapter;
    private List<String> mImages;//图片集合
    private File mCurrentDir;
    private int mMaxCount;   //图片个数
    private List<FolderBean> mFolderBean = new ArrayList<FolderBean>();//文件集合
    private static final int DATA_LOAD = 0x110;
    private Set<String> checkImg = new HashSet<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DATA_LOAD) {
                /**
                 * 绑定数据到view\中
                 */
                Data2View();
                //初始化popwindow
                initpopuwindow();
            }
        }
    };
    private int maxnumber;
    private ImageView iv_back;
    private Button bt_check_ok;
    private int PHOTO_REQUEST_TAKEPHOTO = 3;
    private String cameraPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplepictures);
        maxnumber = getIntent().getIntExtra("number", 0);
        initView();
        initData();
    }

    private void initData() {
        /**
         * 如果不存在内存卡的话弹出该提示
         */
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = MultiplePicturesActivity.this.getContentResolver();
                Cursor cursor = cr.query(mImgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths = new HashSet<String>();
                /**
                 * 遍历查询到的游标
                 */
                while (cursor.moveToNext()) {
                    //得到路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    //获得父路径
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderBean folderBean = null;
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if (parentFile.list() == null) {
                        continue;
                    }
                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg") || filename.endsWith("jpeg") || filename.endsWith("png")) {
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderBean.add(folderBean);
                    if (picSize > mMaxCount) {
                        mMaxCount = picSize;
                        mCurrentDir = parentFile;
                    }
                }
                cursor.close();
                //通知hanlder扫描完毕
                handler.sendEmptyMessage(DATA_LOAD);
            }
        }.start();
    }

    private void initView() {
        gv_mutipleph = (GridView) findViewById(R.id.gv_mutipleph);
        rl_muph_popu = (RelativeLayout) findViewById(R.id.rl_muph_popu);
        tv_muph_right = (TextView) findViewById(R.id.tv_muph_right);
        tv_muph_dir = (TextView) findViewById(R.id.tv_muph_dir);
        tv_muph_dirnumber = (TextView) findViewById(R.id.tv_muph_dirnumber);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_check_ok = (Button) findViewById(R.id.bt_check_ok);
        bt_check_ok.setOnClickListener(this);
        rl_muph_popu.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_muph_right.setText("0/" + maxnumber + "张");
    }

    private void initpopuwindow() {
        checkDIrPoPuWindow = new CheckDIrPoPuWindow(this, mFolderBean);
        checkDIrPoPuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
            }
        });
        checkDIrPoPuWindow.setOnDirSelectdListener(new CheckDIrPoPuWindow.OnDirSelectdListener() {
            @Override
            public void onSelected(FolderBean folderBean) {
                tv_muph_right.setText("0/" + maxnumber + "张");
                bt_check_ok.setClickable(false);
                bt_check_ok.setBackgroundColor(getResources().getColor(R.color.black_gray));
                imageAdapter.cleanSelectedData();
                mCurrentDir = new File(folderBean.getDir());
                mImages = Arrays.asList(mCurrentDir.list());
                imageAdapter = new ImageAdapter(mImages, mCurrentDir.getAbsolutePath(), maxnumber);
                gv_mutipleph.setAdapter(imageAdapter);
                imageAdapter.setOnImageSelectedListener(new ImageAdapter.OnImageSelectedListener() {
                    @Override
                    public void selected(Set<String> mSelectImg) {
                        if (mSelectImg.size() == 0) {
                            bt_check_ok.setClickable(false);
                            bt_check_ok.setBackgroundColor(getResources().getColor(R.color.black_gray));
                        } else {
                            bt_check_ok.setBackgroundColor(getResources().getColor(R.color.white));
                            bt_check_ok.setClickable(true);
                        }
                        checkImg = mSelectImg;
                        tv_muph_right.setText(mSelectImg.size() + "/" + maxnumber + "张");
                    }

                    /**
                     * 当相机选中后的事件监听
                     */
                    @Override
                    public void onCameraClick() {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraPath = "/xiaoyu/" + System.currentTimeMillis() + ".jpg";
                        FileUtis.createDir();
                        File photoFile = new File(Environment.getExternalStorageDirectory(), cameraPath);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                });
                tv_muph_dir.setText(mCurrentDir.getName());
                tv_muph_dirnumber.setText(mMaxCount + "张");
                checkDIrPoPuWindow.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_TAKEPHOTO) {
                ArrayList<String> list = new ArrayList();
                Log.d("图片的路径", Environment.getExternalStorageDirectory() + cameraPath);
                list.add(Environment.getExternalStorageDirectory() + cameraPath);
                // 返回已选择的图片数据
                Intent data2 = new Intent();
                data2.putStringArrayListExtra("select_result", list);
                setResult(RESULT_OK, data);
                if (imageAdapter != null) {
                    imageAdapter.cleanSelectedData();
                }
                finish();
            }
        }
    }

    /**
     * 内容区域变亮
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;//透明度
        getWindow().setAttributes(lp);
    }

    private void lightof() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;//透明度
        getWindow().setAttributes(lp);
    }

    /**
     * 填充数据到界面上
     */
    private void Data2View() {
        if (mCurrentDir == null) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }
        mImages = Arrays.asList(mCurrentDir.list());
        imageAdapter = new ImageAdapter(mImages, mCurrentDir.getAbsolutePath(), maxnumber);
        gv_mutipleph.setAdapter(imageAdapter);
        tv_muph_dir.setText(mCurrentDir.getName());
        tv_muph_dirnumber.setText(mMaxCount + "张");
        imageAdapter.setOnImageSelectedListener(new ImageAdapter.OnImageSelectedListener() {

            @Override
            public void selected(Set<String> mSelectImg) {
                if (mSelectImg.size() == 0) {
                    bt_check_ok.setClickable(false);
                    bt_check_ok.setBackgroundColor(getResources().getColor(R.color.black_gray));
                } else {
                    bt_check_ok.setBackgroundColor(getResources().getColor(R.color.white));
                    bt_check_ok.setClickable(true);
                }
                checkImg = mSelectImg;
                tv_muph_right.setText(mSelectImg.size() + "/" + maxnumber + "张");
            }

            /**
             * 当相机选中后的事件监听
             */
            @Override
            public void onCameraClick() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraPath = "/xiaoyu/" + System.currentTimeMillis() + ".jpg";
                FileUtis.createDir();
                File photoFile = new File(Environment.getExternalStorageDirectory(), cameraPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (imageAdapter != null) {
            imageAdapter.cleanSelectedData();
        }
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.rl_muph_popu) {
            //展示popuwindow
            checkDIrPoPuWindow.showAsDropDown(rl_muph_popu);
            //内容背景变黑
            lightof();
        } else if (i == R.id.iv_back) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (i == R.id.bt_check_ok) {
            ArrayList<String> list = new ArrayList();
            Iterator it = checkImg.iterator();
            while (it.hasNext()) {
                list.add((String) it.next());
            }
            // 返回已选择的图片数据
            Intent data = new Intent();
            data.putStringArrayListExtra("select_result", list);
            setResult(RESULT_OK, data);
            if (imageAdapter != null) {
                imageAdapter.cleanSelectedData();
            }
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        mFolderBean.clear();
    }
}
