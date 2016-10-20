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

/// Implements List adapter for managing "task items" with
/// CheckBox element
public class TaskListAdapter extends ArrayAdapter<TaskInfo> {

    private ArrayList<TaskInfo> mTaskList;
    private Context mContext;
    private int mLayout;

    private OnTaskItemClickListener listener;

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
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        CheckBox checkBox;
        // if no elements are already created at all then make some
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(mLayout, null);
            checkBox = (CheckBox) convertView.findViewById(R.id.taskCheckBox);
            convertView.setTag(checkBox);
        } else {
            // or just get by tag if exists
            checkBox = (CheckBox) convertView.getTag();
        }
        final TaskInfo taskInfo = mTaskList.get(position);
        // update info
        checkBox.setText(taskInfo.getText());
        checkBox.setChecked(taskInfo.isChecked());
        checkBox.setTag(taskInfo);
        // setup listener
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                if (v.getTag() instanceof TaskInfo) {
                    TaskInfo taskInfo = (TaskInfo) checkBox.getTag();
                    taskInfo.setChecked(checkBox.isChecked());
                }
                // redirect to user-defined listener
                if (listener != null) listener.onClick(position, taskInfo);
            }
        });
        return convertView;
    }

    /// returns list items count
    public int itemsCount() {
        return mTaskList.size();
    }

    /// changes current checkbox click listener
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
