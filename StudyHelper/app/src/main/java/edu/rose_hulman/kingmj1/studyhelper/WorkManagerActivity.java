package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class WorkManagerActivity extends AppCompatActivity implements TaskAdapter.TaskCallback {

    private static final int DEFAULT_SCREEN_BRIGHTNESS = 200;

    private TaskAdapter mTaskAdapter;
    private boolean isInWorkMode;
    private AudioManager mAudioManager;
    private Window mWindow;
    private int previousScreenBrightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mWindow = getWindow();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        isInWorkMode = !(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);
        try {
            previousScreenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            previousScreenBrightness = DEFAULT_SCREEN_BRIGHTNESS;
        }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.work_task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter(this, recyclerView, this, null);
        recyclerView.setAdapter(mTaskAdapter);
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
            float windowBrightness;
            int settingsBrightness;
            if(isInWorkMode) {
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
            //Set the brightness of this window
            layoutpars.screenBrightness = windowBrightness;
            //Apply attribute changes to this window
            mWindow.setAttributes(layoutpars);

            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, settingsBrightness);

            isInWorkMode = !isInWorkMode;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onEdit(Task task) {

    }
}
