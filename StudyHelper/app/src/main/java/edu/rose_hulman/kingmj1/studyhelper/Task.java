package edu.rose_hulman.kingmj1.studyhelper;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class Task implements Parcelable {

    protected Task(Parcel in) {
        name = in.readString();
        dueDate = new Date(in.readLong());
        type = (TaskType)in.readSerializable();
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
        dest.writeLong(dueDate.getTime());
        dest.writeSerializable(type);
    }

    public enum TaskType {
        HOMEWORK,
        EXAM,
        MEETING;
    }


    private String name;
    private TaskType type;
    private Date dueDate;
    private int progress;

    public Task(String newName, Date newDate, TaskType newType, int newProgress) {
        name = newName;
        dueDate = newDate;
        type = newType;
        progress = newProgress;
    }

    public String getName() { return name;}

    public void setName(String newName) {
        name = newName;
    }

    public String getTypeString() {
        if(type == null) {
            return "null type?";
        }
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

    public void setType(TaskType newType) {
        type = newType;
    }

    public String getDateString() {
        return DateFormat.getDateInstance().format(dueDate);
    }

    public void setDueDate(Date newDate) {
        dueDate = newDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int newProgress) {
        progress = newProgress;
    }
}
