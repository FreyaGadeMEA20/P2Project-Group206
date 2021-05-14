package com.p2aau.virtualworkoutv2.classes;

public class ExerciseConstant {
    public static int EXERCISE_TYPE = 0;
    public static int EXERCISE_LEVEL = 0;
    public static String USERNAME = null;

    public static ExerciseProgram EXERCISE_PROGRAM = null;

    public static Exercise[] EXERCISES = null;

    public static final ExerciseProgram[][] EXERCISE_PROGRAMS = null;

    static {
        //TODO add exercise programs here.

        ExerciseProgram c1 = new ExerciseProgram("Cardio Level 1", "A beginner-friendly workout stimulating heart rate");
        c1.addExercise(new Exercise("Jumping Jacks",/*"Jump to a position with legs spread and hands overhead, then jump back to original posture.",*/20000,1,15));
        c1.addExercise(new Exercise("Squats",20000,1,15));
        c1.addExercise(new Exercise("Knee Push-Ups",20000,1,10));
        c1.addExercise(new Exercise("Lunges",20000,1,10));

        EXERCISE_PROGRAMS[0][0]= c1;
    }
}
