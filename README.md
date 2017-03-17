# Multiplepictures
这是一个自定义相册的控件,支持gradlew的引用方式
###上效果图了
##
![这里写图片描述](http://img.blog.csdn.net/20170317153553443?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRWFza1NoYXJr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

![这里写图片描述](http://img.blog.csdn.net/20170317154016644?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvRWFza1NoYXJr/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



##maven引用方式
```
<dependency>
  <groupId>cn.yuan.yu.mutiplepicture</groupId>
  <artifactId>mylibrary</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

##gradle引用方式

```
compile 'cn.yuan.yu.mutiplepicture:mylibrary:1.0.0'
```
###代码使用方法
```
 intent = new Intent(this, MultiplePicturesActivity.class);
                intent.putExtra("number", 1);
                startActivityForResult(intent, 0x001);
                ```
                
                并且在回调监听获取数据
                ```
                
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
                 ```
                 谢谢大家的使用,欢迎start和fork
