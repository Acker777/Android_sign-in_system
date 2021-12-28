//package com.example.android_register_system;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.NetworkOnMainThreadException;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import com.example.android_register_system.Student.CourseMessage;
//import com.example.android_register_system.Student.CourseMessageAdapter;
//import com.example.android_register_system.constant.Constants;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class CourseListActivity extends AppCompatActivity  {
//    private ListView lvCourseMessages;
//    private List<CourseMessage> courseMessageData;
//    private CourseMessageAdapter adapter;
//    //用于获取登陆传递过来的用户名
//    public String user_num=null;
//    //用户输入的课号
//    EditText et_course_num;
//    //弹窗提示
//    public String message=null;
//
//    //生命周期-初始化
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_student_main);
//        Intent intent=getIntent();
//        user_num=intent.getStringExtra("user_num");
//        initView();
//        initData();
//        Button btn_add=findViewById(R.id.btn_add);
//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                et_course_num=findViewById(R.id.et_courseNum);
//                //弹窗
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Request request = new Request.Builder()
//                                .url(Constants.baseUrl+"studentEnterCourse/" + et_course_num.getText().toString() + "/" + user_num)
//                                .get().build();
//                        try {
//                            //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
//                            OkHttpClient client = new OkHttpClient();
//                            client.newCall(request).enqueue(new Callback() {
//
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    Log.e("", "Failed to connect server!");
//                                    e.printStackTrace();
//                                }
//
//                                @Override
//                                public void onResponse(Call call, Response response) throws IOException {
//                                    message = response.body().string();
//
//                                }
//                            });
//                        } catch (NetworkOnMainThreadException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }).start();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if(et_course_num.getText().toString().length()==0){
//                    message="课号不能为空！";
//                }
//                else if ("false".equals(message)){
//                    message="课程不存在或者已经加入了课程！";
//                }
//                else {
//                    message="加入课程成功!";
//                }
//                showAlterDialog(message);
//                refreshData(1);
//            }
//        });
//    }
//    private void initView() {
//        lvCourseMessages = findViewById(R.id.lv_course_list);
//        lvCourseMessages.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView,
//                                            View view, int i, long l) {
//                        Intent intent = new Intent(CourseListActivity.this, StudentMainActivity.class);
//                        CourseMessage courseMessage = adapter.getItem(i);
//                        intent.putExtra("courseNum", courseMessage.getCourseId());
//                        intent.putExtra("courseName", courseMessage.getCourseName());
//                        intent.putExtra("studentNum",user_num);
//                        startActivity(intent);
//                    }
//                });
//    }
//
//    private void initData() {
//        courseMessageData = new ArrayList<>();
//        adapter = new CourseMessageAdapter(CourseListActivity.this ,
//                R.layout.student_course_list , courseMessageData);
//        lvCourseMessages.setAdapter(adapter);
//        refreshData(1);
//    }
//
//    private void refreshData(final int page) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Request request = new Request.Builder()
//                        .url(Constants.baseUrl+"queryCourse/")
//                        .get().build();
//                try {
//                    //将调用接口得到的数据绑定上面的callback，callback在将数据绑定到新闻适配器
//                    OkHttpClient client = new OkHttpClient();
//                    client.newCall(request).enqueue(callback);
//                } catch (NetworkOnMainThreadException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private okhttp3.Callback callback = new okhttp3.Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            Log.e("", "Failed to connect server!");
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onResponse(Call call, Response response)
//                throws IOException {
//            if (response.isSuccessful()) {
//                final String body = response.body().string();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.clear();
//                        adapter.notifyDataSetChanged();
//                        Gson gson = new Gson();
//                        //将请求得到的数据转化为对象
//                        Type jsonType = new TypeToken<List<CourseMessage>>() {}.getType();
//                        List<CourseMessage> courseMessages = gson.fromJson(body, jsonType);
//                        //将对象加到适配器中，适配器再绑定到控件中
//                        for (CourseMessage courseMessage:courseMessages) {
//                            adapter.add(courseMessage);
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        }
//    };
//
//    //弹窗方法
//    public void showAlterDialog(String message){
//        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(CourseListActivity.this);
//        alterDiaglog.setIcon(R.mipmap.logo);//图标
//        alterDiaglog.setTitle("");//标题
//        alterDiaglog.setMessage(message);//提示消息
//        //
//        alterDiaglog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        //显示弹弹窗
//        alterDiaglog.show();
//    }
//
//}