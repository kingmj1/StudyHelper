package edu.rose_hulman.kingmj1.studyhelper;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by reynolpt on 1/18/2016.
 */
public class Classmate {

    public static final String COURSE_KEY = "courseKey";

    private String name;
    private String emailAddress;
    private String courseKey;

    @JsonIgnore
    private String key;

    public Classmate() {
        //required empty constructor
    }

    public Classmate(String newName, String newEmail, String newCourseKey) {
        emailAddress = newEmail;
        name = newName;
        courseKey = newCourseKey;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String newAddress) {
        emailAddress = newAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String newKey) {
        key = newKey;
    }

    public void setValues(Classmate newClassmate) {
        name = newClassmate.getName();
        emailAddress = newClassmate.getEmailAddress();
    }

}
