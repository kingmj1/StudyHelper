package edu.rose_hulman.kingmj1.studyhelper;

/**
 * Created by reynolpt on 1/18/2016.
 */
public class Classmates {

    private final String classmateName;
    private final String classmateEmail;

    public Classmates(String newName, String newEmail) {
        classmateEmail = newEmail;
        classmateName = newName;
    }

    public String getClassmateEmail() {
        return classmateEmail;
    }

    public String getName() {
        return classmateName;
    }

}
