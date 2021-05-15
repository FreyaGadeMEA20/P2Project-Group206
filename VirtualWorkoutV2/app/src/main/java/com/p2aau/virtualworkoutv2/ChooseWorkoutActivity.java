package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.p2aau.virtualworkoutv2.classes.ExerciseConstant;

public class ChooseWorkoutActivity extends AppCompatActivity {


    private int exerciseLevel;
    private int exerciseType;


    androidx.gridlayout.widget.GridLayout workOutCategories;
    ImageView[] workOutTypes;
    LinearLayout workOutSubcategories;
    Button[] workoutLevels;
    int[] workoutColors = {R.color.cardio, R.color.strength, R.color.blitz, R.color.fat_burn};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);

        workOutCategories = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.workOuts);
        workOutCategories.setVisibility(View.VISIBLE);

        workOutSubcategories = (LinearLayout) findViewById(R.id.level_layout);

        workOutSubcategories.setVisibility(View.GONE);

        workOutTypes = new ImageView[]{(ImageView) findViewById(R.id.cardio),
                (ImageView) findViewById(R.id.strength),
                (ImageView) findViewById(R.id.blitz),
                (ImageView) findViewById(R.id.fat_burn)};

        workoutLevels = new Button[]{(Button) findViewById(R.id.button1),
                (Button) findViewById(R.id.button2),
                (Button) findViewById(R.id.button3),
                (Button) findViewById(R.id.button4)};

        for(int i = 0; i < workOutTypes.length; i++){
            workOutTypes[i].setOnClickListener(handler);
        }
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == workOutTypes[0]){
                workOutCategory(0);
            } else if (v == workOutTypes[1]){
                workOutCategory(1);
            } else if (v == workOutTypes[2]){
                workOutCategory(2);
            } else if (v == workOutTypes[3]){
                workOutCategory(3);
            }
        }
    };

    public void workOutCategory(int _int) {
        workOutCategories.setVisibility(View.GONE);

        exerciseType = _int;

        workOutSubcategories.setVisibility(View.VISIBLE);

        for(int i = 0; i < workoutLevels.length; i++){
            workoutLevels[i].setBackgroundColor(getResources().getColor(workoutColors[_int]));
       }
    }

    public void onWorkoutClick(View view){
        Intent intent = new Intent(ChooseWorkoutActivity.this, LobbyActivity.class);
        intent.putExtra("Uniqid", "choose_workout");
        ExerciseConstant.EXERCISE_TYPE  = exerciseType+1;

        Button button = (Button) findViewById(view.getId());

        String level = (String) button.getText();

        level = level.replace("Level ", "");

        exerciseLevel = Integer.parseInt(level);

        ExerciseConstant.EXERCISE_LEVEL = exerciseLevel;

        if(ExerciseConstant.EXERCISE_PROGRAMS[ExerciseConstant.EXERCISE_TYPE-1][ExerciseConstant.EXERCISE_LEVEL-1] == null){
            MakeAToast("This workout hasn't been implemented yet!");
        } else {
            startActivity(intent);
        }
    }

    // - Method for making it easier to make a toast - //
    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }
}