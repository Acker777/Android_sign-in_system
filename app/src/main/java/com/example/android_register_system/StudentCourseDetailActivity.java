package com.example.android_register_system;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_register_system.Student.CourseMessage;
import com.example.android_register_system.Student.CourseMessageAdapter;
import com.example.android_register_system.Student.StudentRegisterMessage;
import com.example.android_register_system.Student.StudentRegisterMessageAdapter;
import com.example.android_register_system.constant.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StudentCourseDetailActivity extends AppCompatActivity {
    private ListView lvStudentRegisterMessages;
    private List<StudentRegisterMessage> studentRegisterMessageData;
    private StudentRegisterMessageAdapter adapter;

    String courseNum = null;
    String courseName = null;
    String teacherName = null;
    public static String studentNum = null;
    Map<Object, Object> judgeIsRegister;
    int register;
    TextView et_courseNum;
    EditText et_courseName;
    EditText et_teacherName;

    public AlertDialog.Builder alterDiaglog;

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_register);
        //initView();
        et_courseNum = findViewById(R.id.courseNum);
        et_courseName = findViewById(R.id.courseName);
        et_teacherName = findViewById(R.id.teacherName);
        Intent intent = getIntent();
        courseNum = intent.getStringExtra("courseNum");
        courseName = intent.getStringExtra("courseName");
        et_courseNum.setText("课号：" + courseNum);
        et_courseName.setText("课程名称：" + courseName);
        studentNum = intent.getStringExtra("studentNum");

        alterDiaglog = new AlertDialog.Builder(this);
        try {
            System.out.println("*****************************"+getTeacherNameByCourseId(courseNum));
            et_teacherName.setText("教师姓名：" + getTeacherNameByCourseId(courseNum));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {

            initData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    //根据课号获得老师姓名
    public String getTeacherNameByCourseId(String courseId) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "getTeacherNameByCourseId/" + courseId)
                        .get().build();
                try {
                    //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("", "Failed to connect server!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response)
                                throws IOException {
                            if (response.isSuccessful()) {
                                teacherName = response.body().string();
                            }
                        }
                    });
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100);
        return teacherName;
    }

    //判断课程是否正在签到
    public Map<Object, Object> judgeIsRegister(String courseId, String studentId) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "judgeIsRegister/" + courseId + "/" + studentId)
                        .get().build();
                try {
                    //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("", "Failed to connect server!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response)
                                throws IOException {
                            if (response.isSuccessful()) {
                                String body = response.body().string();
                                Gson gson = new Gson();
                                //将请求得到的数据转化为对象
                                Type jsonType = new TypeToken<Map<Object, Object>>() {
                                }.getType();
                                judgeIsRegister = gson.fromJson(body, jsonType);
                            }
                        }
                    });
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100);
        return judgeIsRegister;
    }

    public int Register(String registerId, String studentNum) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "studentRegister/" + registerId + "/" + studentNum)
                        .get().build();
                try {
                    //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("", "Failed to connect server!");
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response)
                                throws IOException {
                            if (response.isSuccessful()) {
                                register = Integer.parseInt(response.body().string());
                            }
                        }
                    });
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100);
        return register;
    }

    //给签到按钮绑定事件
//    private void initView(){
//        System.out.println("执行了init方法-------------------");
//        lvStudentRegisterMessages=findViewById(R.id.register_list);
//        lvStudentRegisterMessages.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView,
//                                            View view, int i, long l) {
//                        //访问签到接口
//                        try {
//                            System.out.println("执行了签到点击事件-----------------------");
//                            int result=Register("44444",studentNum);
//                            refreshData(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//    }

    //以下主要是为了获得签到列表数据
    private void initData() throws InterruptedException {
        lvStudentRegisterMessages = findViewById(R.id.register_list);
        studentRegisterMessageData = new ArrayList<>();
        adapter = new StudentRegisterMessageAdapter(StudentCourseDetailActivity.this,
                R.layout.student_course_register_item, studentRegisterMessageData);
        lvStudentRegisterMessages.setAdapter(adapter);
        refreshData(1);
    }

    public void refreshData(final int page) throws InterruptedException {
        if (judgeIsRegister != null && Double.parseDouble(judgeIsRegister(courseNum, studentNum).get("courseIsRegister").toString()) == 1.0
                && Double.parseDouble(judgeIsRegister(courseNum, studentNum).get("studentIsRegister").toString()) == 0.0) {
            showAlterDialog("您有课程正在签到哦！");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "getStudentRegisterMessages/" + courseNum + "/" + studentNum)
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

    private Callback callback = new Callback() {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        //将请求得到的数据转化为对象
                        Type jsonType = new TypeToken<List<StudentRegisterMessage>>() {
                        }.getType();
                        List<StudentRegisterMessage> studentRegisterMessages = gson.fromJson(body, jsonType);
                        //将对象加到适配器中，适配器再绑定到控件中
                        for (StudentRegisterMessage studentRegisterMessage : studentRegisterMessages) {
                            adapter.add(studentRegisterMessage);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    //弹窗方法
    public void showAlterDialog(String message) {
        alterDiaglog.setIcon(R.mipmap.logo);//图标
        alterDiaglog.setTitle("");//标题
        alterDiaglog.setMessage(message);//提示消息
        //
        alterDiaglog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //显示弹弹窗
        if (!isFinishing()) {
            AlertDialog alertDialog2 = alterDiaglog.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    alertDialog2.dismiss();
                }
            }, 3000);
        }
    }


    //每10秒刷新页面
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            try {
                refreshData(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage());
            }
        }
    };
}
