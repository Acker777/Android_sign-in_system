package com.example.android_register_system;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android_register_system.Teacher.Course;
import com.example.android_register_system.Teacher.RegisterInfo;
import com.example.android_register_system.Teacher.RegisterInfoAdapter;
import com.example.android_register_system.Teacher.RegisterPageInfo;
import com.example.android_register_system.Teacher.RegisterStudentInfo;
import com.example.android_register_system.Teacher.RegisterStudentInfoAdapter;
import com.example.android_register_system.constant.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterDetailActivity extends AppCompatActivity {

    private List<RegisterStudentInfo> list;
    private RegisterPageInfo info;
    private RegisterStudentInfoAdapter adapter;
    private String registerId;
    private String regName;
    private ListView regStudentView;

    private TextView tvTotal;
    private TextView tvRate;
    private TextView tvState;
    private TextView tvNoRegNum;


    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        Intent intent = getIntent();
        registerId = intent.getStringExtra("register_id");
        regName = intent.getStringExtra("regName");
        System.out.println("registerId" + registerId);

        init();
        initData();

        thread = new Thread(runnable);
        thread.start();
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
        regStudentView = findViewById(R.id.reg_detail_lv);
        tvTotal = findViewById(R.id.reg_detail_total);
        tvRate = findViewById(R.id.reg_detail_rate);
        tvState = findViewById(R.id.reg_detail_state);
        tvNoRegNum = findViewById(R.id.reg_detail_noRegNum);
    }


    public void initData() {

        list = new ArrayList<>();
        adapter = new RegisterStudentInfoAdapter(registerId,RegisterDetailActivity.this, R.layout.register_student_list, list);
        regStudentView.setAdapter(adapter);
        refreshList();
    }


    public void refreshList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(Constants.baseUrl + "register/" + registerId)
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
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        Gson gson = new Gson();
                        //将请求得到的数据转化为对象
                        Type jsonType = new TypeToken<List<RegisterPageInfo>>() {
                        }.getType();
                        List<RegisterPageInfo> info = gson.fromJson(body, jsonType);
                        //将对象加到适配器中，适配器再绑定到控件中
                        for (RegisterStudentInfo t : info.get(0).getList()) {
                            System.out.println("regStuInfo" + t.toString());
                            adapter.add(t);
                        }
                        tvTotal.setText(info.get(0).getIsRegNum() + " / " + info.get(0).getStuNum());
                        tvRate.setText("到课率：" + info.get(0).getRegRate() * 100 + "%");
                        tvNoRegNum.setText("缺勤人数：" + info.get(0).getNoRegNum());
                        tvState.setText("签到名称：" + regName);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };


}