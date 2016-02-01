package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {

    private Task mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        mTask = intent.getParcelableExtra(TaskAdapter.TASK_EXTRA_KEY);
        TextView titleText = (TextView)findViewById(R.id.task_detail_title);
        titleText.setText(mTask.getName());
        TextView typeText = (TextView)findViewById(R.id.task_detail_type);
        typeText.setText(mTask.getTypeString());
        TextView dateText = (TextView)findViewById(R.id.task_detail_date);
        dateText.setText(mTask.getDateString());
    }
}
