package com.appmon.cloudtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

public class TaskListAdapter extends ArrayAdapter<TaskInfo> {

    private ArrayList<TaskInfo> mTaskList;
    private Context mContext;
    private int mLayout;
    OnTaskItemClickListener listener;
    public TaskListAdapter(Context context, int resource, ArrayList<TaskInfo> objects) {
        super(context, resource, objects);
        mTaskList = new ArrayList<>();
        mTaskList.addAll(objects);
        mContext = context;
        mLayout = resource;
        listener = null;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CheckBox checkBox = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(mLayout, null);
            checkBox = (CheckBox) convertView.findViewById(R.id.taskCheckBox);
            convertView.setTag(checkBox);
        } else {
            checkBox = (CheckBox) convertView.getTag();
        }
        final TaskInfo taskInfo = mTaskList.get(position);
        checkBox.setText(taskInfo.getText());
        checkBox.setChecked(taskInfo.isChecked());
        checkBox.setTag(taskInfo);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (v.getTag() instanceof TaskInfo) {
                    TaskInfo taskInfo = (TaskInfo) checkBox.getTag();
                    taskInfo.setChecked(checkBox.isChecked());
                }
                if (listener != null) listener.onClick(position, taskInfo);
            }
        });
        return convertView;
    }

    public int itemsCount() {
        return mTaskList.size();
    }

    public void setOnClickListener(@Nullable OnTaskItemClickListener func) {
        listener = func;
    }

    public void setChecked(int pos, boolean value) {
        mTaskList.get(pos).setChecked(value);
        notifyDataSetInvalidated();
    }

    public void clear() {
        mTaskList.clear();
        notifyDataSetInvalidated();
    }

    public void add(TaskInfo task) {
        super.add(task);
        mTaskList.add(task);
        notifyDataSetChanged();
    }

    public TaskInfo get(int pos) {
        TaskInfo ti = mTaskList.get(pos);
        return new TaskInfo(ti.getText(), ti.isChecked());
    }
}
