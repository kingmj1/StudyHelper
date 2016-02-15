package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class WorkManagerActivity extends AppCompatActivity implements TaskAdapter.TaskCallback {

    private TaskAdapter mTaskAdapter;

    private boolean isSilent;
    private AudioManager mAudioManager;
    private Window mWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_manager);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        mWindow = getWindow();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        isSilent = !(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL);

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
            float brightness;
            if(isSilent) {
                //brightness of <0 makes it default to user preferences
                brightness = -1.0f;
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else {
                brightness = 0.05f;
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            }
            WindowManager.LayoutParams layoutpars = mWindow.getAttributes();
            //Set the brightness of this window
            layoutpars.screenBrightness = brightness;
            //Apply attribute changes to this window
            mWindow.setAttributes(layoutpars);
            isSilent = !isSilent;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onEdit(Task task) {

    }
}
