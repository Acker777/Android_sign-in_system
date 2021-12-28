package com.example.android_register_system;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_register_system.constant.Constants;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Boolean bPwdSwitch = false;
//    private EditText etPwd;

    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       init();
    }

    @Override
    public void onClick(View view) {
        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        String accountKey = getResources().getString(R.string.login_account_name);
        String passwordKey = getResources().getString(R.string.login_password);
        String rememberPasswordKey = getResources().getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spFile.edit();

        String account = etAccount.getText().toString();
        String password = etPwd.getText().toString();

        System.out.println(password+"密码"+account);



        if(cbRememberPwd.isChecked()){

            editor.putString(accountKey,account);
            editor.putString(passwordKey,password);
            editor.putBoolean(rememberPasswordKey,true);
            editor.apply();
        }else{
            editor.remove(accountKey);
            editor.remove(passwordKey);
            editor.remove(rememberPasswordKey);
            editor.apply();
        }

        //此处应该进行get请求进行登陆
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(Constants.getLoginUrl(account,password)).get().build();
                try {
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
            Log.e(null, "Failed to connect server!");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "服务器未响应", Toast.LENGTH_SHORT).show();
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
                        if ("-1".equals(body)){
                            Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }else if("1".equals(body)){
                            Intent intent = new Intent(LoginActivity.this,CourseListActivity.class);
                            intent.putExtra("user_num",etAccount.getText().toString());
                            startActivity(intent);
                        }else if ("0".equals(body)){
                            Intent intent = new Intent(LoginActivity.this,TeacherMainActivity.class);
                            intent.putExtra("user_num",etAccount.getText().toString());
                            startActivity(intent);
                        }


                    }
                });
            } else {
            }
        }
    };


    public void init(){
        final ImageView ivPwdSwitch = findViewById(R.id.iv_pwd_switch);
        final TextView tvSignUp = findViewById(R.id.tv_sign_up);
        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        Button btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        String spFileName = getResources().getString(R.string.shared_preferences_file_name);
        String accountKey = getResources().getString(R.string.login_account_name);
        String passwordKey = getResources().getString(R.string.login_password);
        String rememberPasswordKey = getResources().getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(spFileName,MODE_PRIVATE);
        String account = spFile.getString(accountKey,null);
        String password = spFile.getString(passwordKey , null);
        Boolean rememberPassword = spFile.getBoolean(rememberPasswordKey,false);
        if (account != null && !TextUtils.isEmpty(account)){
            etAccount.setText(account);
        }
        if (password != null && !TextUtils.isEmpty(password)){
            etPwd.setText(password);
        }
        cbRememberPwd.setChecked(rememberPassword);

        //显示不同的图标以及是否显示密码内容
        ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bPwdSwitch = !bPwdSwitch;
                if(bPwdSwitch){
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_black_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    ivPwdSwitch.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    etPwd.setTypeface(Typeface.DEFAULT);
                }
            }
        });

        //设置登陆按钮事件
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册页面
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}