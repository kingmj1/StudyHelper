package edu.rose_hulman.kingmj1.studyhelper;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.client.Firebase;


public class TaskDetailActivity extends AppCompatActivity {

    static final String KEY_NOTIFICATION = "KEY_NOTIFICATION";
    public static final int NOTIFICATION_ID = 17;

    private Task mTask;
    private Firebase mTaskRef;

    private NumberPicker mNumberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mTask = intent.getParcelableExtra(TaskAdapter.TASK_EXTRA_KEY);

        mTaskRef = new Firebase(Constants.TASKS_PATH);

        TextView titleText = (TextView)findViewById(R.id.task_detail_title);
        titleText.setText(mTask.getName());

        TextView typeText = (TextView)findViewById(R.id.task_detail_type);
        typeText.setText(mTask.formatTypeString());

        TextView dateText = (TextView)findViewById(R.id.task_detail_date);
        dateText.setText(mTask.formatDateString());

        SeekBar progressBar = (SeekBar) findViewById(R.id.task_detail_progress_bar);
        progressBar.setProgress(mTask.getProgress());
        progressBar.setEnabled(false);

        Button setNotificationButton = (Button) findViewById(R.id.task_detail_set_noti_button);
        setNotificationButton.setText(R.string.dialog_set_noti_title);
        setNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetNotificationDialog();
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d("Studyhelper", "TaskDetail item selected: " + id);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_set_notification) {

            showSetNotificationDialog();

        }


        return super.onOptionsItemSelected(item);
    }
    private void showSetNotificationDialog() {
        DialogFragment df = new DialogFragment() {

            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.dialog_set_noti_title));
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_set_notification, null, false);
                builder.setView(view);

                mNumberPicker = (NumberPicker) view.findViewById(R.id.dialog_set_noti_numberPicker);

                mNumberPicker.setMinValue(0);
                mNumberPicker.setMaxValue(7);
                Task.TaskType taskType = mTask.getType();
                if (taskType == Task.TaskType.HOMEWORK) {
                    mNumberPicker.setValue(1);
                } else if (taskType == Task.TaskType.EXAM) {
                    mNumberPicker.setValue(2);
                } else {
                    mNumberPicker.setValue(0);
                }

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setNotification();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, null);

                return builder.create();
            }
        };

        df.show(getSupportFragmentManager(), "set notification");
    }

    private Notification getNotification(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle("StudyHelper reminder!");

        if(mNumberPicker.getValue() > 1) {
            builder.setContentText("This is your reminder that " + mTask.getName() + " is in " + mNumberPicker.getValue() + " days.");
        } else if(mNumberPicker.getValue() == 1) {
            builder.setContentText("This is your reminder that " + mTask.getName() + " is in " + mNumberPicker.getValue() + " day.");
        } else {
            builder.setContentText("This is your reminder that " + mTask.getName() + " is today.");
        }

        int UNUSED = 124234;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, UNUSED, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        return builder.build();
    }

    private void setNotification() {
        int daysBefore = mNumberPicker.getValue();

        Intent loginIntent = new Intent(this, LoginActivity.class);
        Notification notification = getNotification(loginIntent);

        Intent notificationIntent = new Intent(this, NotificationBroadcastReceiver.class);
        notificationIntent.putExtra(KEY_NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 17378269, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("Studyhelper", "setting the notification now");

        long notificationDay = SystemClock.elapsedRealtime() + 10000;

//        long millisecondsBefore = daysBefore * 86400000;
//        long notificationDay = (mTask.getDateLong() - millisecondsBefore) + (8 * 3600000);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationDay, pendingIntent);
    }
}
