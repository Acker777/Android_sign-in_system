package com.example.android_register_system;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_register_system.Teacher.Course;
import com.example.android_register_system.Teacher.CourseAdapter;
import com.example.android_register_system.Teacher.CourseInfo;
import com.example.android_register_system.Teacher.RegisterInfo;
import com.example.android_register_system.Teacher.RegisterInfoAdapter;
import com.example.android_register_system.constant.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherCourseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private List<RegisterInfo> regList;
    private CourseInfo courseInfo;
    private RegisterInfoAdapter adapter;
    private String courseId;
    private ListView teacherCourseDetail;
    private TextView tvCourseName;
    private TextView tvCourseId;
    private TextView tvStudentNum;
    private Button bt_addRegister;
    private AlertDialog.Builder alterDiaglog;
    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course_detail);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("course_id");
        System.out.println("courseID" + courseId);

        init();
        initData();

        thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("restart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(handler.obtainMessage());
            }
        }
    };

    //每10秒刷新页面
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            refreshList();
        }
    };

    public void init() {
        alterDiaglog = new AlertDialog.Builder(this);

        teacherCourseDetail = findViewById(R.id.register_view);
        tvCourseId = findViewById(R.id.couse_detail_id);
        tvCourseName = findViewById(R.id.couse_detail_name);
        tvStudentNum = findViewById(R.id.couse_detail_stuNum);
        bt_addRegister = findViewById(R.id.bt_addRegister);
        System.out.println(bt_addRegister);
        bt_addRegister.setOnClickListener(this);

        teacherCourseDetail.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //这里设置点击跳转页面

                        Intent intent = new Intent(TeacherCourseDetailActivity.this, RegisterDetailActivity.class);
                        RegisterInfo registerInfo = adapter.getItem(i);
                        intent.putExtra("register_id", registerInfo.getRegisterId());
                        intent.putExtra("regName", registerInfo.getRegisterName());
                        startActivity(intent);
                    }
                });
    }


    public void initData() {

        regList = new ArrayList<>();
        adapter = new RegisterInfoAdapter(TeacherCourseDetailActivity.this, R.layout.register_list_item, regList);
        teacherCourseDetail.setAdapter(adapter);
        refreshList();
        refreshCourseInfo();
    }

    public void refreshList() {
        System.out.println("*****************页面刷新*****************");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "register/" + courseId + "/regList")
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        //将请求得到的数据转化为对象
                        Type jsonType = new TypeToken<List<RegisterInfo>>() {
                        }.getType();
                        List<RegisterInfo> info = gson.fromJson(body, jsonType);
                        //将对象加到适配器中，适配器再绑定到控件中
                        for (RegisterInfo t : info) {
                            adapter.add(t);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };


    public void refreshCourseInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "register/" + courseId + "/courseInfo")
                        .get().build();
                try {
                    //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(callback2);
                } catch (NetworkOnMainThreadException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private okhttp3.Callback callback2 = new okhttp3.Callback() {
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
                        Gson gson = new Gson();
                        //将请求得到的数据转化为对象
                        Type jsonType = new TypeToken<List<CourseInfo>>() {
                        }.getType();
                        List<CourseInfo> info = gson.fromJson(body, jsonType);
                        //将对象加到适配器中，适配器再绑定到控件中
                        tvCourseId.setText("课程编号：" + courseId);
                        tvCourseName.setText("课程名称：" + info.get(0).getCourseName());
                        tvStudentNum.setText("学生数：" + info.get(0).getStudentNum());
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };


    //弹窗方法
    public void showAlterDialog(String message) {
        EditText editText = new EditText(this);
        alterDiaglog.setIcon(R.mipmap.logo);//图标
        alterDiaglog.setTitle("");//标题
        alterDiaglog.setView(editText);
        alterDiaglog.setMessage(message);//提示消息
        //
        alterDiaglog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().length() != 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Request request = new Request.Builder()
                                    .url(Constants.baseUrl + "register?registerName=" + editText.getText().toString() + "&&courseId=" + courseId)
                                    .get().build();
                            try {
                                //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
                                OkHttpClient client = new OkHttpClient();
                                client.newCall(request).enqueue(callback3);
                            } catch (NetworkOnMainThreadException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(TeacherCourseDetailActivity.this, "签到名称不能为空", Toast.LENGTH_SHORT).show();
                }
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
            }, 60000);
        }
    }

    private okhttp3.Callback callback3 = new okhttp3.Callback() {
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
                        //adapter.clear();
                        //adapter.notifyDataSetChanged();
                        //Gson gson = new Gson();
                        ////将请求得到的数据转化为对象
                        //Type jsonType = new TypeToken<List<CourseInfo>>() {
                        //}.getType();
                        //List<CourseInfo> info = gson.fromJson(body, jsonType);
                        ////将对象加到适配器中，适配器再绑定到控件中
                        //tvCourseId.setText("课程编号：" + courseId);
                        //tvCourseName.setText("课程名称：" + info.get(0).getCourseName());
                        //tvStudentNum.setText("学生数：" + info.get(0).getStudentNum());
                        //adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View v) {
        System.out.println("发布签到");
        showAlterDialog("请输入签到名称");
    }
}