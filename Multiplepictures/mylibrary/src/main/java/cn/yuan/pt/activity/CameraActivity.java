package cn.yuan.pt.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import cn.yuan.pt.R;
import cn.yuan.pt.utils.CameraUtil;


/**
 * @author yukuoyuan
 * @date 2017/9/14
 * 这是一个自定义相机的界面
 */
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {


    private SurfaceHolder mHolder;
    /**
     * 哪个摄像头
     */
    private int mCameraId = 0;
    private int picHeight;
    //屏幕宽高
    private int screenWidth;
    private int screenHeight;
    /**
     * 相机实例
     */
    private Camera mCamera;
    /**
     * 定义的图片路径
     */
    private String IMGPATH;
    private SurfaceView diyCameraSfvCamera;
    private TextView diyCameraBack;
    private TextView diyCameraSwitchCamera;
    private TextView diyCameraTakePicture;

    /**
     * 以下是把相机实例绑定到Activity的生命周期上
     */

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = CameraUtil.getInstance().getCameraInstance(mCameraId);
            if (mHolder != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        initView();
        initData();
    }

    private void initView() {

        diyCameraSfvCamera = (SurfaceView) findViewById(R.id.diy_camera_sfv_camera);
        diyCameraBack = (TextView) findViewById(R.id.diy_camera_back);
        diyCameraSwitchCamera = (TextView) findViewById(R.id.diy_camera_switch_camera);
        diyCameraTakePicture = (TextView) findViewById(R.id.diy_camera_take_picture);
        diyCameraBack.setOnClickListener(this);
        diyCameraSwitchCamera.setOnClickListener(this);
        diyCameraTakePicture.setOnClickListener(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera, holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


    /**
     * 预览相机
     *
     * @param camera 相机
     * @param holder
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        if (camera == null) {
            Toast.makeText(this,"请在设置中打开拍照权限",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            setCamera(camera);
            camera.setPreviewDisplay(holder);
            /**
             * 矫正先不挂机的预览方向
             */
            CameraUtil.getInstance().setCameraDisplayOrientation(this, mCameraId, camera);
            /**
             * 开始预览相机
             */
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置相机的各项参数
     *
     * @param camera 相机实例
     */
    private void setCamera(Camera camera) {
        Camera.Parameters  parameters = camera.getParameters();
        /**
         * 设置对角模式
         */
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        Camera.Size previewSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPreviewSizes(), 800);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = CameraUtil.getInstance().getPropSizeForHeight(parameters.getSupportedPictureSizes(), 800);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);

        camera.setParameters(parameters);
        /**
         * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
         * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
         * previewSize.width才是surfaceView的高度
         * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
         *
         */

        picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;
        //diyCameraSfvCamera.setLayoutParams(params);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth, (screenWidth * pictrueSize.width) / pictrueSize.height);
        diyCameraSfvCamera.setLayoutParams(params);
    }

    /**
     * 切换摄像头
     */
    public void switchCamera() {
        releaseCamera();
        mCameraId = (mCameraId + 1) % mCamera.getNumberOfCameras();
        mCamera = CameraUtil.getInstance().getCameraInstance(mCameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * 拍照保存图片并返回
     */
    private void TakeCapture() {
        if (mCamera == null) {
            return;
        }
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, bitmap);
                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, picHeight, true);
                /**
                 * 如果没有自己定义拍照的路径,那么我们就随机生成一个
                 */
                if (TextUtils.isEmpty(IMGPATH)) {
                    IMGPATH = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +
                            File.separator + System.currentTimeMillis() + ".jpeg";
                }
                CameraUtil.saveJPGE_After(CameraActivity.this, saveBitmap, IMGPATH, 100);
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }
                Log.d("图片的路径", IMGPATH);
                Intent intent = new Intent();
                intent.putExtra("IMGPATH", IMGPATH);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    public int setContentView() {
        return R.layout.activity_diy_camera;

    }

    public void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            IMGPATH = extras.getString("IMGPATH", "");
        }
        mHolder = diyCameraSfvCamera.getHolder();
        mHolder.addCallback(this);
        /**
         * 获取屏幕的宽高
         */
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.diy_camera_back) {
            onBackPressed();

        } else if (i == R.id.diy_camera_switch_camera) {
            switchCamera();

        } else if (i == R.id.diy_camera_take_picture) {
            TakeCapture();

        }
    }
}
