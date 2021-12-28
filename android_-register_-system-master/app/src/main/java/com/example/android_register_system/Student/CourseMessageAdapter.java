package com.example.android_register_system.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_register_system.R;

import java.util.List;

public class CourseMessageAdapter extends ArrayAdapter<CourseMessage> {
    //课程信息列表-数据
    private List<CourseMessage> courseMessages;
    //尚未知
    private Context mContext;
    //尚未知
    private int resourceId;

    public CourseMessageAdapter(Context context, int resourceId, List<CourseMessage> courseMessages){
        super(context, resourceId, courseMessages);
        this.mContext = context;
        this.courseMessages = courseMessages;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseMessage courseMessage = getItem(position);
        View view;
        //为listView滚动的时候快速设置值，否则需要一次加载全部数据降低性能。
        final CourseMessageViewHolder vh;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
            vh = new CourseMessageViewHolder();
            vh.tv_course_num=view.findViewById(R.id.tv_course_num);
            vh.tv_course_name=view.findViewById(R.id.tv_course_name);
            //vh.tv_teacher_name=view.findViewById(R.id.tv_teacher_name);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (CourseMessageViewHolder) view.getTag();
        }

        vh.tv_course_num.setText("课号："+courseMessage.getCourseId());
        vh.tv_course_name.setText("课程名称："+courseMessage.getCourseName());
        //vh.tv_teacher_name.setText("教师名称"+courseMessage.getTeacherName());
        return view;
    }

    class CourseMessageViewHolder{
        TextView tv_course_num;
        TextView tv_course_name;
        //TextView tv_teacher_name;
    }
}


