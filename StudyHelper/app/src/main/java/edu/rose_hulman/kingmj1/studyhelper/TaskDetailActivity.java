package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class TaskDetailActivity extends AppCompatActivity {

    private Task mTask;
    private Firebase mTaskRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        mTask = intent.getParcelableExtra(TaskAdapter.TASK_EXTRA_KEY);

        mTaskRef = new Firebase(Constants.TASKS_PATH);

        TextView titleText = (TextView)findViewById(R.id.task_detail_title);
        titleText.setText(mTask.getName());
        TextView typeText = (TextView)findViewById(R.id.task_detail_type);
        typeText.setText(mTask.formatTypeString());
        TextView dateText = (TextView)findViewById(R.id.task_detail_date);
        dateText.setText(mTask.formatDateString());
//        TextView progressText = (TextView) findViewById(R.id.task_detail_progress);
//        progressText.setText(mTask.getProgress() + "%");
        SeekBar progressBar = (SeekBar) findViewById(R.id.task_detail_progress_bar);
        progressBar.setProgress(mTask.getProgress());
        progressBar.setEnabled(false);
        final EditText notesText = (EditText) findViewById(R.id.task_notes_edit_text);
        notesText.setText(mTask.getNotes());
        notesText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTask.setNotes(s.toString());
                mTaskRef.child(mTask.getKey()).setValue(mTask);
            }
        });
    }
}
