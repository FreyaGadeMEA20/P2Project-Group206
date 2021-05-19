package com.p2aau.virtualworkoutv2.classes;

public class Exercise {
    private String exerciseName;
   // private String edescription;
    private int timeToComplete;
    private int pauseTime;
    private int difficulty;
    //private int amountToDo;
    private boolean completed;

    private int video;

    Exercise(String name, /*String desc,*/ int time, int diff, int amount, int _video) {
        this.exerciseName = name;
        //this.edescription = desc;
        this.timeToComplete = time;
        this.difficulty = diff;
        //this.amountToDo = amount;
        video = _video;

    }

    Exercise(String name, int time, int diff, int _video) {
        this.exerciseName = name;
        this.timeToComplete = time;
        this.difficulty = diff;
        video = _video;

    }

    public String getExerciseName() {
        return this.exerciseName;
    }

    public void setExerciseName(String name) {
        this.exerciseName = name;
    }

    public int getVideo(){
        return video;
    }

    public int getTimeToComplete() {
        return this.timeToComplete;
    }

    public void setTimeToComplete(int time) {
        this.timeToComplete = time;
    }

    public int getPauseTime() {
        return this.pauseTime;
    }

    public void setPauseTime(int time) {
        this.pauseTime = time;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int diff) {
        this.difficulty = diff;
    }

    /*public int getAmountToDo() {
        return this.amountToDo;
    }*/

    /*public void setAmountToDo(int amount) {
        this.amountToDo = amount;
    }*/

    public boolean getCompleted() {
        return this.completed;
    }

    public void startExercise() {
        //play animations and start timers and what not...s
    }

    public void setCompleted(boolean x) {
        this.completed = x;
    }
}