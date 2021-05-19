package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p2aau.virtualworkoutv2.classes.ExerciseConstant;

import java.util.Random;

public class ExerciseCompleteActivity extends AppCompatActivity {

    // --- Attributes --- //
    // -- Views -- //
    ImageView[] imageViews;
    TextView titleText;
    TextView xpText;

    int[] reactions = {R.drawable.emoji_laugh, R.drawable.emoji_heart, R.drawable.emoji_star_eyes, R.drawable.emoji_thumps_up, R.drawable.emoji_cry};
    String[] exerciseTypes = {"Cardio", "Strength", "Yoga", "Fat Burn"};
    int[] icons = {R.drawable.cardio_icon_color, R.drawable.strength_icon_color, R.drawable.yoga_icon_color, R.drawable.fat_burn_icon_color};
    ImageView workoutIcon;

    int level = ExerciseConstant.EXERCISE_LEVEL;

    String exerciseName = exerciseTypes[ExerciseConstant.EXERCISE_TYPE-1];;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_complete);

        Random rand = new Random();

        // - Workout icon - //
        workoutIcon = (ImageView) findViewById(R.id.exercise_logo);
        workoutIcon.setImageResource(icons[ExerciseConstant.EXERCISE_TYPE-1]);

        // - Title text - //
        titleText = (TextView) findViewById(R.id.CompleteTextView);
        String titleString = exerciseName + " - Level " + level + "\nComplete!";
        titleText.setText(titleString);

        // - XP text - //
        xpText = (TextView) findViewById(R.id.XPandCalories);
        String xpString = "+ " + (rand.nextInt(10)*10) + " " + exerciseName + " XP";
        String xpString2 = "\n+ " + (rand.nextInt(10)*10) + " User XP\n";
        String caloryString = (rand.nextInt(5)*100+rand.nextInt(10)*10)+ " Calories burned";
        String xpAndCaloryString = xpString+xpString2+caloryString;
        xpText.setText(xpAndCaloryString);

        // - Reactions - //
        imageViews = new ImageView[]{(ImageView) findViewById(R.id.Reaction1),
                (ImageView) findViewById(R.id.Reaction2),
                (ImageView) findViewById(R.id.Reaction3)};

        for(int i = 0; i < imageViews.length; i++){
            imageViews[i].setImageResource(reactions[rand.nextInt(reactions.length)]);
        }
    }

    public void onBackToLobbyClick(View view){
        Intent intent = new Intent(ExerciseCompleteActivity.this, LobbyActivity.class);
        intent.putExtra("Uniqid", "end_screen");
        startActivity(intent);
    }
}