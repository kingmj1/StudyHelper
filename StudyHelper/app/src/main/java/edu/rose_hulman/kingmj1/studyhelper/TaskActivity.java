package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class TaskActivity extends AppCompatActivity implements TaskAdapter.TaskCallback {

    private TaskAdapter mTaskAdapter;
    private String mCourseKey;
    private String mUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCourseKey = intent.getStringExtra(Constants.COURSE_KEY_EXTRA_KEY);
        mUID = intent.getStringExtra(Constants.UID_EXTRA_KEY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditTaskDialog(null);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter(this, recyclerView, this, mCourseKey, mUID);
        recyclerView.setAdapter(mTaskAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_classmates) {
            Intent classmatesIntent = new Intent(this, ClassmatesActivity.class);
            classmatesIntent.putExtra(Constants.COURSE_KEY_EXTRA_KEY, mCourseKey);
            startActivity(classmatesIntent);
            return true;
        }

        if (id == R.id.action_add_by_key) {
            mTaskAdapter.prepForAddByKey();
            showAddByKeyDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showAddEditTaskDialog(final Task task) {
        DialogFragment df = new DialogFragment() {

            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(task == null ? R.string.dialog_add_task_title : R.string.dialog_edit_task_title));
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_task, null, false);
                builder.setView(view);
                final EditText taskEditText = (EditText) view.findViewById(R.id.dialog_add_task_text);
                final DatePicker taskDatePicker = (DatePicker) view.findViewById(R.id.dialog_task_datePicker);
                final RadioButton taskTypeExam = (RadioButton) view.findViewById(R.id.radioButton_task_exam);
                final RadioButton taskTypeHomework = (RadioButton) view.findViewById(R.id.radioButton_task_homework);
                final RadioButton taskTypeMeeting = (RadioButton) view.findViewById(R.id.radioButton_task_meeting);
                final TextView taskProgessText = (TextView) view.findViewById(R.id.dialog_task_progressText);
                final SeekBar taskProgressBar = (SeekBar) view.findViewById(R.id.dialog_task_progressBar);

                //taskProgessText.bringToFront();

                if(task != null) {
                    taskEditText.setText(task.getName());

                    TextWatcher textWatcher = new TextWatcher() {

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            String taskName = taskEditText.getText().toString();
                            //mTaskAdapter.update(task, taskName);
                        }
                    };
                    taskEditText.addTextChangedListener(textWatcher);

                    switch(task.formatTypeString()) {
                        case("Exam"):
                            taskTypeExam.setChecked(true);
                            break;
                        case("Homework"):
                            taskTypeHomework.setChecked(true);
                            break;
                        case("Meeting"):
                            taskTypeMeeting.setChecked(true);
                            break;
                        default:
                            taskTypeHomework.setChecked(true);
                            break;
                    }

                    Calendar cal=Calendar.getInstance();

                    cal.setTime(task.getDueDate());

                    int year=cal.get(Calendar.YEAR);
                    int month=cal.get(Calendar.MONTH);
                    int day=cal.get(Calendar.DAY_OF_MONTH);

                    taskDatePicker.updateDate(year, month, day);

                    taskProgressBar.setProgress(task.getProgress());
                }
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(task != null) {
                            String name = taskEditText.getText().toString();
                            if(name == null) {
                                name = "Default";
                            }
                            int month = taskDatePicker.getMonth();
                            int day = taskDatePicker.getDayOfMonth();
                            int year = taskDatePicker.getYear();
                            Date date = new Date(year - 1900, month, day);
                            Task.TaskType taskType = getTaskType(taskTypeExam.isChecked(), taskTypeHomework.isChecked(), taskTypeMeeting.isChecked());
                            int progress = taskProgressBar.getProgress();
                            mTaskAdapter.update(task, name, date, taskType, progress);
                        } else {
                            String name = taskEditText.getText().toString();
                            if(name == null) {
                                name = "Default";
                            }
                            int month = taskDatePicker.getMonth();
                            int day = taskDatePicker.getDayOfMonth();
                            int year = taskDatePicker.getYear();
                            Date date = new Date(year - 1900, month, day);
                            Task.TaskType taskType = getTaskType(taskTypeExam.isChecked(), taskTypeHomework.isChecked(), taskTypeMeeting.isChecked());
                            int progress = taskProgressBar.getProgress();
                            Task newTask = new Task(name, date, taskType, progress, mUID);
                            newTask.setCourseKey(mCourseKey);
                            mTaskAdapter.add(newTask);
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);
                if(task != null) {
                    builder.setNeutralButton(R.string.delete_title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mTaskAdapter.remove(task);
                        }
                    });
                }


                return builder.create();
            }
        };



        df.show(getSupportFragmentManager(), "add");
    }

    public Task.TaskType getTaskType(boolean examChecked, boolean homeworkChecked, boolean meetingChecked) {
        if(examChecked == true) {
            return Task.TaskType.EXAM;
        } else if(meetingChecked == true) {
            return Task.TaskType.MEETING;
        } else {
            return Task.TaskType.HOMEWORK;
        }
    }

    @Override
    public void onEdit(final Task task) {
        showAddEditTaskDialog(task);
    }

    private void showAddByKeyDialog() {
        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TaskActivity.this);
                builder.setTitle(getString(R.string.add_by_key_title));
                final EditText keyEntry = new EditText(TaskActivity.this);
                keyEntry.setHint(getString(R.string.add_by_key_hint));
                builder.setView(keyEntry);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = keyEntry.getText().toString();
                        mTaskAdapter.addByKey(key);
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, null);

                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "Add by key");
    }

}
