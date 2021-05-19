package com.p2aau.virtualworkoutv2.classes;

import com.p2aau.virtualworkoutv2.R;

public class ExerciseConstant {
    public static String USERNAME = null;

    public static int EXERCISE_TYPE = 0;
    public static String EXERCISE_TYPE_NAME = null;

    public static final String[] EXERCISE_TYPES = {"Cardio", "Strength", "Yoga", "Fat Burn"};

    public static int EXERCISE_LEVEL = 0;

    public static int CURRENT_EXERCISE = 0;
    public static int MAX_EXERCISE = 0;

    public static ExerciseProgram EXERCISE_PROGRAM = null;

    public static Exercise EXERCISE = null;

    public static final ExerciseProgram[][] EXERCISE_PROGRAMS = new ExerciseProgram[4][4];

    static {
        //TODO add exercise programs here.

        // -- Cardio -- //
        // - Cardio level 1 - //
        ExerciseProgram c1 = new ExerciseProgram("Cardio Level 1", "A beginner-friendly workout stimulating heart rate");
        c1.addExercise(new Exercise("Jumping Jacks",/*"Jump to a position with legs spread and hands overhead, then jump back to original posture.",*/20000,1,15, R.raw.yumpingyacks));
        c1.addExercise(new Exercise("Squats",20000,1,15, R.raw.squats));
        c1.addExercise(new Exercise("Knee Push-Ups",20000,1,10, R.raw.pushups));
        c1.addExercise(new Exercise("Lunges",20000,1,10, R.raw.lunges));

        EXERCISE_PROGRAMS[0][0]= c1;

        // - Cardio level 2
        ExerciseProgram c2 = new ExerciseProgram("Cardio Level 2", "A moderately difficult workout stimulating heart rate");
        c2.addExercise(new Exercise("Run in Place",30000,2,30, R.raw.jumpingjack));
        c2.addExercise(new Exercise("Squats",30000,2,25, R.raw.feetinandoutplank));
        c2.addExercise(new Exercise("Feet in and out",20000,2,20, R.raw.squats));
        c2.addExercise(new Exercise("Push-Ups",20000,2,15, R.raw.pushups));
        c2.addExercise(new Exercise("Big sideways jumps",20000,2,10, R.raw.highknees));

        EXERCISE_PROGRAMS[0][1]= c2;

        // -- Strength -- //
        // - Strength level 1 - //
        ExerciseProgram s1 = new ExerciseProgram("Strength Level 1", "A Basic but diverse workout for increasing physical strength");
        s1.addExercise(new Exercise("Squats",25000,1,20, R.raw.squats));
        s1.addExercise(new Exercise("Plank Saw",20000,1,10, R.raw.normalplank));
        s1.addExercise(new Exercise("High Jumps",15000,1,6, R.raw.tuckjumps));
        s1.addExercise(new Exercise("Judo Push-Ups",20000,1,10, R.raw.highknees));

        EXERCISE_PROGRAMS[1][0]= s1;

        // - Strength level 2 - //
        ExerciseProgram s2 = new ExerciseProgram("Strength Level 1", "A moderately workout for increasing physical strength");
        s2.addExercise(new Exercise("Split jumps",20000,1,20, R.raw.squats));
        s2.addExercise(new Exercise("Explosive Push-Ups",20000,1,6, R.raw.explosivepushups));
        s2.addExercise(new Exercise("Runner touches",20000,1,16, R.raw.tuckjumps));
        s2.addExercise(new Exercise("Flying shoulder press",20000,1,10, R.raw.normalplank));
        s2.addExercise(new Exercise("Flying shoulder press",20000,1,10, R.raw.flyingshoulderpress));

        EXERCISE_PROGRAMS[1][1]= s2;
        // -- Yoga -- //
        // -- Yoga level 1 -- //
        ExerciseProgram y1 = new ExerciseProgram("Fat Burner Level 1", "An intense workout for burning fat over time");
        y1.addExercise(new Exercise("Walkouts",60000,1,12, R.raw.lunges));
        y1.addExercise(new Exercise("Walking Lunges",60000,1,40, R.raw.tuckjumps));
        y1.addExercise(new Exercise("Jumps",40000,1,60, R.raw.pushups));
        y1.addExercise(new Exercise("Knee Push-Ups",30000,1,15, R.raw.squats));
        y1.addExercise(new Exercise("Squats",40000,1,30, R.raw.lowboatcrunches));
        y1.addExercise(new Exercise("Boat Crunches",30000,1,6, R.raw.highplank));

        EXERCISE_PROGRAMS[2][0]= y1;

        // -- Fat burn -- //
        // - Fat burn level 1 - //
        ExerciseProgram f1 = new ExerciseProgram("Fat Burner Level 1", "An intense workout for burning fat over time");
        f1.addExercise(new Exercise("Walkouts",60000,1,12, R.raw.lunges));
        f1.addExercise(new Exercise("Walking Lunges",60000,1,40, R.raw.tuckjumps));
        f1.addExercise(new Exercise("Jumps",40000,1,60, R.raw.pushups));
        f1.addExercise(new Exercise("Knee Push-Ups",30000,1,15, R.raw.squats));
        f1.addExercise(new Exercise("Squats",40000,1,30, R.raw.lowboatcrunches));
        f1.addExercise(new Exercise("Boat Crunches",30000,1,6, R.raw.highplank));


        EXERCISE_PROGRAMS[3][0]= f1;

        // - Fat burn level 2 -- //
        ExerciseProgram f2 = new ExerciseProgram("Fat Burner Level 2", "An intense workout for burning fat over time");
        f2.addExercise(new Exercise("Alternating Backward Lunges",40000,1,12, R.raw.lunges));
        f2.addExercise(new Exercise("High Knees",20000,1,40, R.raw.highknees));
        f2.addExercise(new Exercise("Dolphin Push Ups",40000,1,60, R.raw.pushups));
        f2.addExercise(new Exercise("Lunges to Jumps",20000,1,15, R.raw.squats));
        f2.addExercise(new Exercise("Jump in square",40000,1,30, R.raw.burpee));
        f2.addExercise(new Exercise("2m Sidesteps",20000,1,6, R.raw.normalplank));

        EXERCISE_PROGRAMS[3][1]= f2;
    }
}
