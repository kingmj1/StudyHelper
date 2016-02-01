package edu.rose_hulman.kingmj1.studyhelper;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by kingmj1 on 2/1/2016.
 */
public class StudyHelperApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
