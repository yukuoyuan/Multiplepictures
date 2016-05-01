package cn.yuan.mutiph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.yuan.pt.activity.MultiplePicturesActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt_start_one = (Button) findViewById(R.id.bt_start_one);
        Button bt_start_more = (Button) findViewById(R.id.bt_start_more);
        bt_start_one.setOnClickListener(this);
        bt_start_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start_one:
                intent = new Intent(this, MultiplePicturesActivity.class);
                intent.putExtra("number", 0);
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
