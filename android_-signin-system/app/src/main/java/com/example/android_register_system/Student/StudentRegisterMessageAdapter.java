package com.example.android_register_system.Student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_register_system.LoginActivity;
import com.example.android_register_system.R;
import com.example.android_register_system.StudentCourseDetailActivity;
import com.example.android_register_system.StudentMainActivity;
import com.example.android_register_system.constant.Constants;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentRegisterMessageAdapter extends ArrayAdapter<StudentRegisterMessage> {
    class StudentRegisterMessageViewHolder {
        TextView register_name;
        TextView register_time;
        Button is_register;
    }

    //课程信息列表-数据
    private List<StudentRegisterMessage> studentRegisterMessages;
    //尚未知
    private Context mContext;
    //尚未知
    private int resourceId;

    private boolean flag = true;

    public android.support.v7.app.AlertDialog.Builder alterDiaglog;

    public StudentRegisterMessageAdapter(Context context, int resourceId, List<StudentRegisterMessage> studentRegisterMessages) {
        super(context, resourceId, studentRegisterMessages);
        this.mContext = context;
        this.studentRegisterMessages = studentRegisterMessages;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentRegisterMessage studentRegisterMessage = getItem(position);
        View view;
        //为listView滚动的时候快速设置值，否则需要一次加载全部数据降低性能。
        final StudentRegisterMessageAdapter.StudentRegisterMessageViewHolder vh;
        if (convertView == null) {
            //resourceId指的是某一项的id
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            vh = new StudentRegisterMessageAdapter.StudentRegisterMessageViewHolder();
            vh.register_name = view.findViewById(R.id.register_name);
            vh.register_time = view.findViewById(R.id.register_time);
            vh.is_register = view.findViewById(R.id.is_register);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (StudentRegisterMessageAdapter.StudentRegisterMessageViewHolder) view.getTag();
        }

        vh.register_name.setText(studentRegisterMessage.getRegisterName());
        vh.register_time.setText(studentRegisterMessage.getRegisterDateTime());

        if (studentRegisterMessage.isregister == 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            Date now = new Date();
            Date registerDate = null;
            try {
                registerDate = simpleDateFormat.parse(studentRegisterMessage.getRegisterDateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //用毫秒表示
            long nowM = now.getTime();
            long registerM = registerDate.getTime();
            //得到时间差，用秒表示。
            long gap = (nowM - registerM) / 1000;
            if (gap > 600) {
                vh.is_register.setText("缺勤");
                vh.is_register.setEnabled(false);
            } else {
                vh.is_register.setText("点击签到");

                alterDiaglog = new android.support.v7.app.AlertDialog.Builder(mContext);
                if (flag) {
                    showAlterDialog("温馨提醒！您有课程要签到");
                }


                vh.is_register.setEnabled(true);
                vh.is_register.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String registerId = studentRegisterMessage.getRegisterId();

                                EditText editText = new EditText(mContext);
                                AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(mContext);
                                //alterDiaglog.setIcon(R.mipmap.logo);//图标
                                alterDiaglog.setTitle("");//标题
                                alterDiaglog.setView(editText);
                                alterDiaglog.setMessage("请输入验证码!");//提示消息
                                alterDiaglog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (editText.getText().toString().equals(registerId)) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Request request = new Request.Builder()
                                                            .url(Constants.baseUrl + "studentRegister/" + registerId + "/" + StudentCourseDetailActivity.studentNum)
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
                                                                    int result = Integer.parseInt(response.body().string());
                                                                }
                                                            }
                                                        });
                                                    } catch (NetworkOnMainThreadException ex) {
                                                        AlertDialog.Builder alterDiaglog3 = new AlertDialog.Builder(mContext);
                                                        //alterDiaglog.setIcon(R.mipmap.logo);//图标
                                                        alterDiaglog3.setTitle("提示");//标题
                                                        alterDiaglog3.setMessage("验证码错误");//提示消息
                                                        alterDiaglog3.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });
                                                        alterDiaglog3.show();
                                                    }
                                                }
                                            }).start();
                                            AlertDialog.Builder alterDiaglog2 = new AlertDialog.Builder(mContext);
                                            //alterDiaglog2.setIcon(R.mipmap.logo);//图标
                                            alterDiaglog2.setTitle("提示");//标题
                                            alterDiaglog2.setMessage("签到成功!");//提示消息
                                            alterDiaglog2.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    flag = true;
                                                }
                                            });
                                            alterDiaglog2.show();
                                        }
                                        //输入的验证码不对
                                        else {
                                            AlertDialog.Builder alterDiaglog4 = new AlertDialog.Builder(mContext);
                                            //alterDiaglog.setIcon(R.mipmap.logo);//图标
                                            alterDiaglog4.setTitle("提示");//标题
                                            alterDiaglog4.setMessage("验证码错误");//提示消息
                                            alterDiaglog4.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                            alterDiaglog4.show();
                                        }

                                    }
                                });

                                alterDiaglog.show();

                            }
                        });
            }
        } else {
            vh.is_register.setText("已签到");
            vh.is_register.setEnabled(false);
        }
        return view;
    }

    //弹窗方法
    public void showAlterDialog(String message) {
        flag = false;
        alterDiaglog.setTitle("提示");//标题
        alterDiaglog.setMessage(message);//提示消息
        alterDiaglog.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alterDiaglog.show();
    }

}
