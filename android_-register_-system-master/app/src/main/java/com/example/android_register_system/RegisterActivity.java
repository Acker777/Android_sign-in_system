package com.example.android_register_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android_register_system.constant.Constants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    boolean flag = true;
    private EditText et_userNum;
    private EditText et_username;
    private EditText et_password_1;
    private EditText et_password_2;
    private RadioButton rb_student;
    private RadioButton rb_teacher;
    private Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();


    }




    private okhttp3.Callback callback = new okhttp3.Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(null, "Failed to connect server!");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, "服务器未响应", Toast.LENGTH_SHORT).show();
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
                        //根据返回值进行跳转
                        if (!flag) {
                            Toast.makeText(RegisterActivity.this, "账号已被注册！", Toast.LENGTH_SHORT).show();
                            System.out.println("登录失败");
                        }  else if (flag) {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            System.out.println("登录成功");
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            } else {
            }
        }
    };

    public void init() {
        //初始化控件
        et_userNum = findViewById(R.id.et_usernum);
        et_username = findViewById(R.id.et_username);
        et_password_1 = findViewById(R.id.et_password_1);
        et_password_2 = findViewById(R.id.et_password_2);
        rb_student = findViewById(R.id.rb_student);
        rb_teacher = findViewById(R.id.rb_teacher);
        bt_register = findViewById(R.id.bt_register);
        //bt_register.setOnClickListener(this);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到控件中的数据
                String userNum = et_userNum.getText().toString();
                String username = et_username.getText().toString();
                String password_1 = et_password_1.getText().toString();
                String password_2 = et_password_2.getText().toString();
                boolean student = rb_student.isChecked();
                boolean teacher = rb_teacher.isChecked();

                if (userNum.length()==0) {
                    Toast.makeText(RegisterActivity.this, "学号不能为空", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (username.length() == 0 && flag) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (password_1.length() == 0 && flag) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (password_2.length() == 0 && flag) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (!(student || teacher) && flag) {
                    Toast.makeText(RegisterActivity.this, "必须选择一个身份", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                int isStudent = 1;

                if (student) {
                    isStudent = 1;
                }
                if (teacher) {
                    isStudent = 0;
                }

                //此处应该进行get请求进行注册请求
                int finalIsStudent = isStudent;

                if (password_1!=password_2) {
                    Toast.makeText(RegisterActivity.this, "两次密码输入的不一致！", Toast.LENGTH_SHORT).show();
                    //isOk = false;
                }
                if (flag) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Request request = new Request.Builder().url(Constants.getRegisterUrl(userNum, password_1, username, finalIsStudent)).get().build();
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

        });
    }
}