package com.example.android_register_system;

import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.example.android_register_system.Student.CourseMessage;
import com.example.android_register_system.Teacher.Course;
import com.example.android_register_system.Teacher.CourseAdapter;
import com.example.android_register_system.constant.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_addCourse;
    private ListView lvCourse;
    private List<Course> courseData;
    //用于获取登陆传递过来的用户名
    public String user_num = null;

    private CourseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        Intent intent = getIntent();
        user_num = intent.getStringExtra("user_num");

        init();
        initData();

        System.out.println("TeacherMainActivity");
    }

    public void init() {
        //初始化控件
        btn_addCourse = findViewById(R.id.btn_addCourse);
        btn_addCourse.setOnClickListener(this);

        lvCourse = findViewById(R.id.lv_course_list);

        lvCourse.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //这里设置点击跳转页面

                        Intent intent = new Intent(TeacherMainActivity.this, TeacherCourseDetailActivity.class);
                        Course course = adapter.getItem(i);
                        intent.putExtra("course_id", course.getCourseId());
                        startActivity(intent);
                    }
                });
    }

    public void initData() {

        courseData = new ArrayList<>();
        adapter = new CourseAdapter(TeacherMainActivity.this, R.layout.student_course_list, courseData);
        lvCourse.setAdapter(adapter);
        refreshData();
    }

    public void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "queryCourseByTeacherNum/" + user_num)
                        .get().build();
                try {
                    //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(callback);
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private okhttp3.Callback callback = new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e("", "Failed to connect server!");
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response)
                throws IOException {
            if (response.isSuccessful()) {
                final String body = response.body().string();
                System.out.println("body" + body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("body"+body);
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        //将请求得到的数据转化为对象
                        Type jsonType = new TypeToken<List<Course>>() {
                        }.getType();
                        List<Course> courses = gson.fromJson(body, jsonType);
                        //将对象加到适配器中，适配器再绑定到控件中
                        for (Course course : courses) {
                            System.out.println("course" + course.toString());
                            adapter.add(course);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View v) {
        //跳转到新建课程的表单页面
        Intent intent = new Intent(TeacherMainActivity.this, AddCourseActivity.class);
        intent.putExtra("user_num", user_num);
        startActivity(intent);
    }
}