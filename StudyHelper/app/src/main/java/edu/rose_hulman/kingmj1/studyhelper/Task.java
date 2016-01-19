package edu.rose_hulman.kingmj1.studyhelper;

import java.util.Date;

/**
 * Created by kingmj1 on 1/17/2016.
 */
public class Task {

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
