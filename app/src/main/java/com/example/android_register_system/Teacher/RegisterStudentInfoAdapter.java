package com.example.android_register_system.Teacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.NetworkOnMainThreadException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android_register_system.R;
import com.example.android_register_system.constant.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterStudentInfoAdapter extends ArrayAdapter<RegisterStudentInfo> {

    private List<RegisterStudentInfo> list;
    private Context mContext;
    private int resourceId;
    private String registerId;
    private String studentNum;
    private int register;

    public RegisterStudentInfoAdapter(String registerId,@NonNull @NotNull Context context, int resource, @NonNull @NotNull List<RegisterStudentInfo> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.mContext = context;
        this.resourceId = resource;
        this.registerId = registerId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        RegisterStudentInfo info = getItem(position);
        View view;
        //为listView滚动的时候快速设置值，否则需要一次加载全部数据降低性能。
        final RegStudentInfoHolder vh;
        if (convertView == null) {
            //resourceId指的是某一项需新闻的id
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
            vh = new RegStudentInfoHolder();
            vh.reg_stu_list_no=view.findViewById(R.id.reg_stu_list_no);
            vh.reg_stu_list_name=view.findViewById(R.id.reg_stu_list_name);
            vh.reg_stu_list_date=view.findViewById(R.id.reg_stu_list_date);
            vh.reg_stu_list_state=view.findViewById(R.id.reg_stu_list_state);
            vh.reg_stu_list_state.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("**********************************");
                            AlertDialog.Builder alterDiaglog2=new AlertDialog.Builder(mContext);
                            //alterDiaglog2.setIcon(R.mipmap.logo);//图标
                            alterDiaglog2.setTitle("提示");//标题
                            alterDiaglog2.setMessage("是否帮该学生签到!");//提示消息
                            alterDiaglog2.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println("-------------------------");
                                    studentNum = info.getStudentNum();
                                    System.out.println(registerId+"****************"+studentNum);
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
                                }
                            });
                            alterDiaglog2.show();

                        }
            });

            view.setTag(vh);
        } else {
            view = convertView;
            vh = (RegStudentInfoHolder) view.getTag();
        }
        vh.reg_stu_list_no.setText(info.getStudentNum());
        vh.reg_stu_list_name.setText(info.getUserName());
        String time = stampToDate(info.getStudentRegisterTime());
        vh.reg_stu_list_date.setText(time);
        if("未签到".equals(time)){
            vh.reg_stu_list_state.setText("未签到");
        }else{
            vh.reg_stu_list_state.setText("已签到");
            vh.reg_stu_list_state.setEnabled(false);
        }
        return view;

    }

    class RegStudentInfoHolder {
        TextView reg_stu_list_no;
        TextView reg_stu_list_name;
        TextView reg_stu_list_date;
        TextView reg_stu_list_state;
    }

    public static String stampToDate(Date date) {
        if(date == null)
            return "未签到";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

}
