package cn.yuan.mutiph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.yuan.pt.activity.MultiplePicturesActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ImgsAdapter.OnDeleteListener {

    private Intent intent;
    private ImageView iv_test;
    private String cameraPath;
    private int PHOTO_REQUEST_TAKEPHOTO = 3;
    private String imageFile;
    private RecyclerView rcv_image_list;
    private List<String> imagelist = new ArrayList<>();
    private ImgsAdapter imgsAdapter;
    private int numbers = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_start_one = (Button) findViewById(R.id.bt_start_one);
        Button bt_start_more = (Button) findViewById(R.id.bt_start_more);
        iv_test = (ImageView) findViewById(R.id.iv_test);
        rcv_image_list = (RecyclerView) findViewById(R.id.rcv_image_list);
        bt_start_one.setOnClickListener(this);
        bt_start_more.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rcv_image_list.setLayoutManager(gridLayoutManager);
        imagelist.add("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png");
        imgsAdapter = new ImgsAdapter(imagelist, this, numbers);
        imgsAdapter.setOnDeleteListener(this);
        rcv_image_list.setAdapter(imgsAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x001) {
                if (data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra("select_result");
                    //创建图片的存储路径
                    //   imageFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qiezzi/" + "headIcon.png";
                    Log.d("图片的路径*****", list.get(0));
                    imgsAdapter.addData(list);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_one:
                intent = new Intent(this, MultiplePicturesActivity.class);
                intent.putExtra("number", 1);
                startActivityForResult(intent, 0x001);
                break;
            case R.id.bt_start_more:
                intent = new Intent(this, MultiplePicturesActivity.class);
                intent.putExtra("number", 6 - imagelist.size());
                startActivityForResult(intent, 0x001);
                break;
        }
    }


    @Override
    public void onDelte(int position) {
        imgsAdapter.remove(position);
    }

    @Override
    public void onAdd() {
        int number = imagelist.size();
        for (String dates : imagelist) {
            if (dates.equals("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png")) {
                number = number - 1;
            }
        }
        intent = new Intent(this, MultiplePicturesActivity.class);
        intent.putExtra("number", numbers - number);
        startActivityForResult(intent, 0x001);
    }
}
