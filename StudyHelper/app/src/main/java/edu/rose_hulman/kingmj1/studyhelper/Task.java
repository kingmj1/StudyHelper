package edu.rose_hulman.kingmj1.studyhelper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class Task implements Parcelable {

    protected Task(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public enum TaskType {
        HOMEWORK,
        EXAM,
        MEETING;
    }


    private String name;
    private TaskType type;
    private Date dueDate;

    public Task(String newName, Date newDate, TaskType newType) {
        name = newName;
        dueDate = newDate;
        type = newType;
    }

    public String getName() { return name;}

    public String getTypeString() {
        switch (type) {
            case EXAM:
                return "Exam";
            case HOMEWORK:
                return "Homework";
            case MEETING:
                return "Meeting";
            default:
                return "Unknown Type";
        }
    }
}
