package edu.rose_hulman.kingmj1.studyhelper;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Created by kingmj1 on 1/17/2016.
 */
public class Course implements Parcelable {

    @JsonIgnore
    private String key;

    private String name;

    private String uid;
    //private TaskAdapter mTaskAdapter;

    public Course() {
        //required empty constructor for Firebase
    }

    public Course(String newName) {
        name = newName;
        //mTaskAdapter = new TaskAdapter();
    }

    protected Course(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


//    public TaskAdapter getTaskAdapter() {
//        return mTaskAdapter;
//    }
//
//    public void setTaskAdapter(TaskAdapter mTaskAdapter) {
//        this.mTaskAdapter = mTaskAdapter;
//    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Course inCourse) {
        name = inCourse.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
