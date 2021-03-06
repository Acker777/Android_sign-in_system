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
import com.example.android_register_system.Student.CourseMessage;
import com.example.android_register_system.Student.CourseMessageAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {

    //课程信息列表-数据
    private List<Course> course;
    private Context mContext;
    private int resourceId;

    public CourseAdapter(Context context, int resourceId, List<Course> course) {
        super(context, resourceId, course);
        this.course = course;
        this.mContext = context;
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);
        View view;
        //为listView滚动的时候快速设置值，否则需要一次加载全部数据降低性能。
        final CourseViewHolder vh;
        if (convertView == null) {
            //resourceId指的是某一项需新闻的id
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);
            vh = new CourseViewHolder();
            vh.tv_course_num=view.findViewById(R.id.tv_course_num);
            vh.tv_course_name=view.findViewById(R.id.tv_course_name);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (CourseViewHolder) view.getTag();
        }

        vh.tv_course_num.setText("课号："+course.getCourseId());
        vh.tv_course_name.setText("课程名称："+course.getCourseName());
        return view;
    }

    class CourseViewHolder {
        TextView tv_course_num;
        TextView tv_course_name;
    }

}
