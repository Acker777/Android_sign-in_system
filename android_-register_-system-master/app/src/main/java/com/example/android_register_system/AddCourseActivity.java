package com.example.android_register_system;

import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_register_system.constant.Constants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddCourseActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_courseNum;
    private EditText et_courseName;
    private Button bt_addCourse;
    //用于获取登陆传递过来的用户名
    public String user_num = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user_num = intent.getStringExtra("user_num");

        setContentView(R.layout.activity_add_course);

        init();
    }

    public void init() {
        et_courseNum = findViewById(R.id.et_courseNum);
        et_courseName = findViewById(R.id.et_courseName);
        bt_addCourse = findViewById(R.id.bt_addCourse);
        bt_addCourse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        boolean isOk = true;

        String courseName = et_courseName.getText().toString();
        String courseNum = et_courseNum.getText().toString();
        System.out.println(user_num);
        System.out.println(courseName);
        System.out.println(courseNum);

        if (et_courseName.length() == 0 && isOk) {
            Toast.makeText(AddCourseActivity.this, "课程名称不能为空", Toast.LENGTH_SHORT).show();
            isOk = false;
        }
        if (et_courseNum.length() == 0 && isOk) {
            Toast.makeText(AddCourseActivity.this, "课号不能为空", Toast.LENGTH_SHORT).show();
            isOk = false;
        }

        if (isOk) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Request request = new Request.Builder().url(Constants.baseUrl + "addCourse?courseId="
                            + courseNum + "&courseNum=" + courseNum + "&courseName="
                            + courseName + "&teacherNum=" + user_num).get().build();
                    try {
                        OkHttpClient client = new OkHttpClient();
                        client.newCall(request).enqueue(callback);
                    } catch (NetworkOnMainThreadException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }


    }

    private okhttp3.Callback callback = new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(null, "Failed to connect server!");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AddCourseActivity.this, "服务器未响应", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                final String body = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(body);
                        //根据返回值进行跳转
                        if ("2".equals(body)) {
                            Toast.makeText(AddCourseActivity.this, "该课程课号已被创建！", Toast.LENGTH_SHORT).show();
                        } else if ("1".equals(body)) {
                            Toast.makeText(AddCourseActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddCourseActivity.this, TeacherMainActivity.class);
                            intent.putExtra("user_num",user_num);
                            startActivity(intent);
                        }
                    }
                });
            } else {
            }
        }
    };
}