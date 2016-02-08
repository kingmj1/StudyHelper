package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private static final String FIREBASE_REPO = "study-helper-rose";
    private static final String FIREBASE_URL = "https://" + FIREBASE_REPO + ".firebaseio.com";
    private static final String TASKS_PATH = FIREBASE_URL + "/tasks";

    private ArrayList<Task> mTasks = new ArrayList<>();
    //private Course mCourse;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private TaskCallback mTaskCallback;
    private String mCourseKey;
    private Firebase mTasksRef;

    public static final String TASK_EXTRA_KEY = "TASK";

    public TaskAdapter() {
        //blank constructor
    }

    public TaskAdapter(Context context, RecyclerView recyclerView, TaskCallback taskCallback, String courseKey) {
        mContext = context;
        mRecyclerView = recyclerView;
        mCourseKey = courseKey;
        //createTestTasks();
        mTaskCallback = taskCallback;
        mTasksRef = new Firebase(TASKS_PATH);
        mTasksRef.keepSynced(true);
        //mTasksRef.addChildEventListener(new TaskChildEventListener());

        Query query = mTasksRef.orderByChild("courseKey").equalTo(mCourseKey);
        query.addChildEventListener(new TaskChildEventListener());
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
        holder.taskTypeView.setText(task.formatTypeString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent taskDetailIntent = new Intent(mContext, TaskDetailActivity.class);
                taskDetailIntent.putExtra(TASK_EXTRA_KEY, mTasks.get(position));
                mContext.startActivity(taskDetailIntent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("StudyHelperError", "TaskAdapter: current position: " + position);
                mTaskCallback.onEdit(mTasks.get(position));
                return true;
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
        mTasks.add(new Task("Homework 4", new Date(), Task.TaskType.HOMEWORK, 10));
        mTasks.add(new Task("Exam 3", new Date(), Task.TaskType.EXAM, 20));
    }

    public void add(Task newTask) {
//        mTasks.add(0, newTask);
//        notifyDataSetChanged();
        mTasksRef.push().setValue(newTask);
    }

    public void update(Task task, String newName, Date newDate, Task.TaskType newType, int newProgress) {
        task.setName(newName);
        task.setDueDate(newDate);
        task.setDateLong(newDate.getTime());
        task.setType(newType);
        task.setTypeInt(newType.ordinal());
        task.setProgress(newProgress);
        mTasksRef.child(task.getKey()).setValue(task);
    }

    public void remove(Task task) {
        mTasksRef.child(task.getKey()).removeValue();
    }

    public interface TaskCallback {
        public void onEdit(Task task);
    }

    public class TaskChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Task task = dataSnapshot.getValue(Task.class);
            task.setKey(dataSnapshot.getKey());
            task.setDueDate(new Date(task.getDateLong()));
            task.setType(Task.TaskType.values()[task.getTypeInt()]);
            mTasks.add(0, task);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Task newTask = dataSnapshot.getValue(Task.class);
            for (Task t : mTasks) {
                if(t.getKey().equals(key)) {
                    t.setValues(newTask);
                    notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Task t : mTasks) {
                if (key.equals(t.getKey())) {
                    mTasks.remove(t);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //Not needed
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("Task", firebaseError.getMessage());
            //Log.e("Task", firebaseError.getDetails());
        }
    }

}
