package edu.rose_hulman.kingmj1.studyhelper;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
        progress = in.readInt();
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
        dest.writeInt(progress);
    }

    public enum TaskType {
        HOMEWORK,
        EXAM,
        MEETING;
    }

    @JsonIgnore
    private String key;


    private String name;
    private String courseKey;

    @JsonIgnore
    private TaskType type;

    private int typeInt;

    @JsonIgnore
    private Date dueDate;

    private long dateLong;
    private int progress;

    public Task() {
        //empty constructor for firebase
    }

    public Task(String newName, Date newDate, TaskType newType, int newProgress) {
        name = newName;
        dueDate = newDate;
        dateLong = dueDate.getTime();
        type = newType;
        typeInt = type.ordinal();
        progress = newProgress;
    }

    public String getName() { return name;}

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

    public String getDateString() {
        return DateFormat.getDateInstance().format(dueDate);
    }

    public void setValues(Task task) {
        name = task.getName();
        courseKey = task.getCourseKey();
        dateLong = task.getDateLong();
        dueDate = new Date(dateLong);
        typeInt = task.getTypeInt();
        type = TaskType.values()[typeInt];
        progress = task.getProgress();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int newProgress) {
        progress = newProgress;
    }

}
