package com.example.androidstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private  String urlStr = "https://img-my.csdn.net/uploads/201408/25/1408936379_4781.png";
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.dialog_imageview);
    }
    public void mainHanlderMainLooper(View view){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap mBitmap = HttpUtils.getBitmapFromNetWork(urlStr);
                    //1.实例化一个message对象
                    Message message=Message.obtain();
                    //2.将图片流赋值给message对象
                    message.obj=mBitmap;
                    //3.定义message标签
                    message.what=0;
                    //4.发送消息到主线程的handler中

                    handler1.sendMessage(message);
                }
            }).start();

    }

    Handler handler1=new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //5.接受消息并更新ui操作
            if(null!=msg.obj){
                Bitmap bitmap=(Bitmap)msg.obj;
                imageView.setImageBitmap(bitmap);
                Log. i( TAG, "使用主线程的Handler和主线程的Looper" );
            }else {
                Log.d(TAG, "handleMessage: 获取不到图片资源");
            }
        }
    };

    public void subHanlderMainLooper(View view){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap mBitmap = HttpUtils.getBitmapFromNetWork(urlStr);
                //1.实例化一个message对象
                Message message=Message.obtain();
                //2.将图片流赋值给message对象
                message.obj=mBitmap;

                Handler handler2=new Handler(Looper.getMainLooper()){

                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        //5.接受消息并更新ui操作
                        if(null!=msg.obj){
                            Bitmap bitmap=(Bitmap)msg.obj;
                            imageView.setImageBitmap(bitmap);
                            Log. i( TAG, "使用子线程的Handler和主线程的Looper" );
                        }else {
                            Log.d(TAG, "handleMessage: 获取不到图片资源");
                        }
                    }
                };

                handler2.sendMessage(message);
            }
        }).start();
    }

    public void subHanlderSubLooper(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap mBitmap = HttpUtils.getBitmapFromNetWork(urlStr);
                //创建Looper和MessageQueue对象
                Looper.prepare();
                Handler handler3=new Handler(){

                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(null!=msg.obj){
                            Bitmap bitmap=(Bitmap)msg.obj;
                            imageView.setImageBitmap(bitmap);
                            Log. i( TAG, "使用子线程的Handler和子线程的Looper" );
                        }else {
                            Log.d(TAG, "handleMessage: 获取不到图片资源");
                        }
                    }
                };
                Message message=Message.obtain();
                //将图片流赋值给message对象
                message.obj=mBitmap;
                handler3.sendMessage(message);
                Looper.loop();
            }
        }).start();

    }
}
