package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> mTasks = new ArrayList<>();
    private Context mContext;
    private RecyclerView mRecyclerView;

    public static final String TASK_EXTRA_KEY = "TASK";

    public TaskAdapter() {
        //blank constructor
    }

    public TaskAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        createTestTasks();
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskAdapter.TaskViewHolder holder, final int position) {
        Task task = mTasks.get(position);
        holder.taskNameView.setText(task.getName());
        holder.taskTypeView.setText(task.getTypeString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskDetailIntent = new Intent(mContext, TaskDetailActivity.class);
                taskDetailIntent.putExtra(TASK_EXTRA_KEY, mTasks.get(position));
                mContext.startActivity(taskDetailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView taskNameView;
        private TextView taskTypeView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskNameView = (TextView)itemView.findViewById(R.id.task_name_view);
            taskTypeView = (TextView)itemView.findViewById(R.id.task_type_text);
        }
    }

    private void createTestTasks() {
        mTasks.add(new Task("Homework 4", new Date(), Task.TaskType.HOMEWORK));
        mTasks.add(new Task("Exam 3", new Date(), Task.TaskType.EXAM));
    }

    public void createTask(String name, Date date, Task.TaskType taskType) {
        Task newTask = new Task(name, date, taskType);
        mTasks.add(newTask);
    }

    public void update(Task task, String newName) {

    }

}
