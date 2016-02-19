package edu.rose_hulman.kingmj1.studyhelper;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkManagerActivity extends AppCompatActivity implements TaskAdapter.TaskCallback {

    private static final int RC_PERMISSIONS = 123;
    private static final int DEFAULT_SCREEN_BRIGHTNESS = 200;

    private TaskAdapter mTaskAdapter;
    private boolean isInWorkMode;
    private AudioManager mAudioManager;
    private Window mWindow;
    private int previousScreenBrightness;
    private String mUID;
    private ArrayList<Classmate> mShareClassmates = new ArrayList<>();
    private boolean canWrite;
    private boolean hasPreppedForStudyMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasPreppedForStudyMode = false;
        setContentView(R.layout.activity_work_manager);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if(Build.VERSION.SDK_INT >= 23) {
            canWrite = Settings.System.canWrite(this);
        } else {
            canWrite = true;
        }
        Intent intent = getIntent();
        mUID = intent.getStringExtra(Constants.UID_EXTRA_KEY);
        mWindow = getWindow();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        isInWorkMode = !(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.work_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter(this, recyclerView, this, null, mUID);
        recyclerView.setAdapter(mTaskAdapter);

        prepForStudyMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_manager, menu);
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

        if (id == R.id.action_study_mode) {
//            Toast placeholder = Toast.makeText(this, "Upcoming Feature", Toast.LENGTH_SHORT);
//            placeholder.show();
            if(hasPreppedForStudyMode) {
                float windowBrightness;
                int settingsBrightness;
                if (isInWorkMode) {
                    //brightness of <0 makes it default to user preferences
                    windowBrightness = -1.0f;
                    settingsBrightness = previousScreenBrightness;
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    windowBrightness = 0.05f;
                    settingsBrightness = 15;
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }


                WindowManager.LayoutParams layoutpars = mWindow.getAttributes();
                layoutpars.screenBrightness = windowBrightness;
                mWindow.setAttributes(layoutpars);

                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                        settingsBrightness);

                isInWorkMode = !isInWorkMode;
            } else {
                prepForStudyMode();
            }

            return true;
        } else {
            prepForStudyMode();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onEdit(Task task) {
        Firebase shareFirebase = new Firebase(Constants.CLASSMATES_PATH);
        shareFirebase.keepSynced(true);
        Query shareQuery = shareFirebase.orderByChild("courseKey").equalTo(task.getCourseKey());
        //Log.d("SH", task.getCourseKey());
        shareQuery.addChildEventListener(new TaskShareChildEventListener());
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showShareDialog(task);
        mShareClassmates.clear();
    }

    private void showShareDialog(final Task task) {
        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                final int numberOfClassmates = mShareClassmates.size();
                final String[] classmateNames = new String[numberOfClassmates];
                final ArrayList<String> selectedEmails = new ArrayList<>();

                for(int i = 0; i < numberOfClassmates; i++) {
                    classmateNames[i] = mShareClassmates.get(i).getName();
                    Log.d("SH", mShareClassmates.get(i).getName());
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkManagerActivity.this);
                builder.setTitle(getString(R.string.share_dialog_title));
                builder.setMultiChoiceItems(classmateNames, null
                        , new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                String selectedEmail = mShareClassmates.get(which).getEmailAddress();
                                if(isChecked) {
                                    selectedEmails.add(selectedEmail);
                                } else {
                                    selectedEmails.remove(selectedEmail);
                                }
                            }
                        });

                builder.setPositiveButton(R.string.share_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] emails = new String[selectedEmails.size()];
                        sendShareEmail(selectedEmails.toArray(emails), task);
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, null);

                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "Share");
    }

    private void sendShareEmail(String[] recicpients, Task task) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recicpients);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject_line));
        intent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_message_format), task.getName(), task.getKey()));
        startActivity(intent);
    }

    class TaskShareChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Classmate classmate = dataSnapshot.getValue(Classmate.class);
            classmate.setKey(dataSnapshot.getKey());
            mShareClassmates.add(classmate);
            Log.d("listener", classmate.getName());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            //not needed
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            //not needed
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //not needed
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("SH", firebaseError.getMessage());
        }
    }

    @TargetApi(23)
    private void getWritePermission() {
        Intent writePermissionIntent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        startActivityForResult(writePermissionIntent, RC_PERMISSIONS);
    }

    private void prepForStudyMode() {
        if(hasPreppedForStudyMode) {
            return;
        }
        if(canWrite) {
            try {
                previousScreenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            hasPreppedForStudyMode = true;
        } else {
            getWritePermission();
        }
    }
    @TargetApi(23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_PERMISSIONS) {
            if(Settings.System.canWrite(this)) {
                canWrite = true;
                prepForStudyMode();
            }
        } else {
            Toast.makeText(this, getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();
        }
    }
}
