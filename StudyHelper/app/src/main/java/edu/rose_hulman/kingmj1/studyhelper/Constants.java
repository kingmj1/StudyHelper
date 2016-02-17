package edu.rose_hulman.kingmj1.studyhelper;

/**
 * Created by kingmj1 on 2/16/2016.
 */
public class Constants {
    //Firebase URL stuff
    public static final String FIREBASE_REPO = "study-helper-rose";
    public static final String FIREBASE_URL = "https://" + FIREBASE_REPO + ".firebaseio.com";
    public static final String TASKS_PATH = FIREBASE_URL + "/tasks";
    public static final String COURSES_PATH = FIREBASE_URL + "/courses";
    public static final String CLASSMATES_PATH = FIREBASE_URL + "/classmates";

    //Extra keys
    public static final String UID_EXTRA_KEY = "uid_key";
    public static final String COURSE_KEY_EXTRA_KEY = "course_key";

    //Request Code
    public static final int RC_DID_LOGOUT = 123;
}
