package com.p2aau.virtualworkoutv2.classes;

import java.util.ArrayList;
//import java.util.Date;

public class ExerciseProgram {
    private ArrayList<Exercise> listOfExercises;
    private Exercise currentExercise;
    private int currentExerciseCounter;
    private String programName;
    private String pdescription;
    private float pdifficulty;
    private int ppauseTime;
    private int ptimeToComplete;

    ExerciseProgram(String name, String desc) {
        this.programName=name;
        this.pdescription=desc;
        this.listOfExercises = new ArrayList<Exercise>();
    }

    public ArrayList<Exercise> getListOfExercises(){
        return listOfExercises;
    }

    public Exercise getCurrentExercise(int _exercise){
        return listOfExercises.get(_exercise);
    }

    public String getExerciseProgramName() {
        return this.programName;
    }

    public void setExerciseProgramName(String name) {
        this.programName = name;
    }

    public String getPDescription() {
        return this.pdescription;
    }

    public void setPDescription(String desc) {
        this.pdescription = desc;
    }

    public void addExercise(Exercise exe) {
        listOfExercises.add(exe);
    }

    public void removeExercise(Exercise exe) {
        listOfExercises.remove(exe);
    }

    public int getCurrentExerciseCounter() {return this.currentExerciseCounter;}

    // Gets the total amount of pause time, which is how much time is inbetween each exercise in total.
    public int getPPauseTime() {
        int ptime = 0;
        for (Exercise e : listOfExercises)
            ptime = +e.getPauseTime();
        this.ppauseTime=ptime;
        return ptime;
    }

    // Same as above, just for time to complete instead
    public int getPTimeToComplete() {
        int ttime = 0;
        for (Exercise e : listOfExercises)
            ttime = +e.getTimeToComplete();
        this.ptimeToComplete = ttime+getPPauseTime();
        return ttime + getPPauseTime();
    }

    // Function to call if we wanted to make the program able to change an exercise in the application
    public void editExercise(Exercise exe) {
        //what attribute we wanna edit?
        // don't think we need this for our app. More relevant in future work.
    }

    // starts exercise
    // TODO change the app to use this instead
    public void startProgram() {
        this.currentExercise = listOfExercises.get(0);
        this.currentExerciseCounter = 1;
        this.currentExercise.startExercise();
    }

    // choses next exercise.
    // TODO change the app to use this instead
    public void nextExercise() {
        currentExercise = listOfExercises.get(currentExerciseCounter);
        currentExerciseCounter++;
    }

}