package edu.rose_hulman.kingmj1.studyhelper;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class Course {

    private String name;
    private TaskAdapter mTaskAdapter;

    public Course(String newName) {
        name = newName;
        mTaskAdapter = new TaskAdapter();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public TaskAdapter getTaskAdapter() {
        return mTaskAdapter;
    }

    public void setTaskAdapter(TaskAdapter mTaskAdapter) {
        this.mTaskAdapter = mTaskAdapter;
    }


}
