package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private TaskAdapter mTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        mTaskAdapter = new TaskAdapter(this, recyclerView);
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
            startActivity(classmatesIntent);
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
                final DatePicker taskDatePicker = (DatePicker) view.findViewById(R.id.datePicker_task);
                Log.d("StudyhelperError", "at the date Picker switch");
                taskDatePicker.setCalendarViewShown(false);
                taskDatePicker.setSpinnersShown(true);
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
                            mTaskAdapter.update(task, taskName);
                        }
                    };
                    taskEditText.addTextChangedListener(textWatcher);
                }
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(task == null) {
                            String name = taskEditText.getText().toString();
                            mTaskAdapter.update(task, name);
                            mTaskAdapter.createTask(name, new Date(), Task.TaskType.HOMEWORK);
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);

//                public void taskRadioButtonClicked (View view) {
//                    boolean checked = ((RadioButton) view).isChecked();
//                    RadioButton examRadio = (RadioButton) findViewById(R.id.radioButton_task_exam);
//                    RadioButton homeworkRadio = (RadioButton) findViewById(R.id.radioButton_task_homework);
//                    RadioButton meetingRadio = (RadioButton) findViewById(R.id.radioButton_task_meeting);
//
//                    Log.d("StudyhelperError", "at the task radio button switch statement");
//                    switch(view.getId()) {
//                        case R.id.radioButton_task_exam:
//                            examRadio.setChecked(true);
//                            homeworkRadio.setChecked(false);
//                            meetingRadio.setChecked(false);
//                            break;
//                        case R.id.radioButton_task_homework:
//                            examRadio.setChecked(false);
//                            homeworkRadio.setChecked(true);
//                            meetingRadio.setChecked(false);
//                            break;
//                        case R.id.radioButton_task_meeting:
//                            examRadio.setChecked(false);
//                            homeworkRadio.setChecked(false);
//                            meetingRadio.setChecked(true);
//                            break;
//                    }
//                }

                return builder.create();
            }
        };



        df.show(getSupportFragmentManager(), "add");
    }


}
