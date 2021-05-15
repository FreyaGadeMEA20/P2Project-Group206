package com.p2aau.virtualworkoutv2.classes;

public class ExerciseConstant {
    public static int EXERCISE_TYPE = 0;
    public static int EXERCISE_LEVEL = 0;
    public static String USERNAME = null;

    public static ExerciseProgram EXERCISE_PROGRAM = null;

    public static Exercise[] EXERCISES = null;

    public static final ExerciseProgram[][] EXERCISE_PROGRAMS = new ExerciseProgram[4][4];

    static {
        //TODO add exercise programs here.

        // -- Cardio -- //
        // - Cardio level 1 - //
        ExerciseProgram c1 = new ExerciseProgram("Cardio Level 1", "A beginner-friendly workout stimulating heart rate");
        c1.addExercise(new Exercise("Jumping Jacks",/*"Jump to a position with legs spread and hands overhead, then jump back to original posture.",*/20000,1,15));
        c1.addExercise(new Exercise("Squats",20000,1,15));
        c1.addExercise(new Exercise("Knee Push-Ups",20000,1,10));
        c1.addExercise(new Exercise("Lunges",20000,1,10));

        EXERCISE_PROGRAMS[0][0]= c1;

        // - Cardio level 2
        ExerciseProgram c2 = new ExerciseProgram("Cardio Level 2", "A moderately difficult workout stimulating heart rate");
        c2.addExercise(new Exercise("Run in Place",30000,2,30));
        c2.addExercise(new Exercise("Squats",30000,2,25));
        c2.addExercise(new Exercise("Feet in and out",20000,2,20));
        c2.addExercise(new Exercise("Push-Ups",20000,2,15));
        c2.addExercise(new Exercise("Big sideways jumps",20000,2,10));

        EXERCISE_PROGRAMS[0][1]= c2;

        // -- Strength -- //
        // - Strength level 1 - //
        ExerciseProgram s1 = new ExerciseProgram("Strength Level 1", "A Basic but diverse workout for increasing physical strength");
        s1.addExercise(new Exercise("Squats",25000,1,20));
        s1.addExercise(new Exercise("Plank Saw",20000,1,10));
        s1.addExercise(new Exercise("High Jumps",15000,1,6));
        s1.addExercise(new Exercise("Judo Push-Ups",20000,1,10));
        s1.addExercise(new Exercise("Split jumps",20000,1,20));
        s1.addExercise(new Exercise("Explosive Push-Ups",20000,1,6));
        s1.addExercise(new Exercise("Runner touches",20000,1,16));
        s1.addExercise(new Exercise("Flying shoulder press",20000,1,10));

        EXERCISE_PROGRAMS[1][0]= s1;

        // -- Blitz -- //
        // - WHOOPS NONE IMPLEMENTED - //

        // -- Fat burn -- //
        // - Fat burn level 1 - //
        ExerciseProgram f1 = new ExerciseProgram("Fat Burner Level 1", "An intense workout for burning fat over time");
        f1.addExercise(new Exercise("Walkouts",60000,1,12));
        f1.addExercise(new Exercise("Walking Lunges",60000,1,40));
        f1.addExercise(new Exercise("Jumps",40000,1,60));
        f1.addExercise(new Exercise("Knee Push-Ups",30000,1,15));
        f1.addExercise(new Exercise("Squats",40000,1,30));
        f1.addExercise(new Exercise("Boat Crunches",30000,1,6));
        f1.addExercise(new Exercise("Reverse Lunge reaches",40000,1,16));
        f1.addExercise(new Exercise("High Plank",40000,1,10));

        EXERCISE_PROGRAMS[3][0]= f1;

        // - Fat burn level 2 -- //
        ExerciseProgram f2 = new ExerciseProgram("Fat Burner Level 2", "An intense workout for burning fat over time");
        f2.addExercise(new Exercise("Alternating Backward Lunges",40000,1,12));
        f2.addExercise(new Exercise("High Knees",20000,1,40));
        f2.addExercise(new Exercise("Dolphin Push Ups",40000,1,60));
        f2.addExercise(new Exercise("Lunges to Jumps",20000,1,15));
        f2.addExercise(new Exercise("Jump in square",40000,1,30));
        f2.addExercise(new Exercise("2m Sidesteps",20000,1,6));
        f2.addExercise(new Exercise("3m Low Lateral Walks",40000,1,16));
        f2.addExercise(new Exercise("180 Burpees",20000,1,10));

        EXERCISE_PROGRAMS[3][1]= f2;
    }
}
