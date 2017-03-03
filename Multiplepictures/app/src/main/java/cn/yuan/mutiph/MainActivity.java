package cn.yuan.mutiph;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.yuan.pt.activity.MultiplePicturesActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private ImageView iv_test;
    private String cameraPath;
    private int PHOTO_REQUEST_TAKEPHOTO=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_start_one = (Button) findViewById(R.id.bt_start_one);
        Button bt_start_more = (Button) findViewById(R.id.bt_start_more);
        iv_test = (ImageView) findViewById(R.id.iv_test);
        bt_start_one.setOnClickListener(this);
        bt_start_more.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x001) {
                if (data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra("select_result");
                    Log.d("图片的路径*****", list.get(0));
                    Glide.with(MainActivity.this).load(list.get(0)).into(iv_test);
                    //  Toast.makeText(MainActivity.this, list.size() + "", Toast.LENGTH_LONG).show();
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
                intent.putExtra("number", 6);
                startActivityForResult(intent, 0x001);
                break;
        }
    }
}
