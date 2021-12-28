package com.example.android_register_system.Teacher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android_register_system.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RegisterInfoAdapter extends ArrayAdapter<RegisterInfo> {

    private List<RegisterInfo> regData;
    private Context context;
    private int resourceId;

    public RegisterInfoAdapter(@NonNull Context context, int resourceId, @NonNull List<RegisterInfo> data) {
        super(context, resourceId, data);
        this.context = context;
        this.regData = data;
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RegisterInfo info = getItem(position);
        View view;
        //为listView滚动的时候快速设置值，否则需要一次加载全部数据降低性能。
        final RegInfoHolder vh;
        if (convertView == null) {
            //resourceId指的是某一项需新闻的id
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            vh = new RegInfoHolder();
            vh.register_list_no = view.findViewById(R.id.register_list_no);
            vh.register_list_name = view.findViewById(R.id.register_list_name);
            vh.register_list_date = view.findViewById(R.id.register_list_date);

            view.setTag(vh);
        } else {
            view = convertView;
            vh = (RegInfoHolder) view.getTag();
        }
        vh.register_list_no.setText(info.getRegisterId());
        vh.register_list_name.setText(info.getRegisterName());
        vh.register_list_date.setText(info.getRegisterTime());
        return view;
    }

    class RegInfoHolder {
        TextView register_list_no;
        TextView register_list_name;
        TextView register_list_date;
    }

    //public static String stampToDate(Date date) {
    //    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    String dateString = simpleDateFormat.format(date);
    //    return dateString;
    //}

}
