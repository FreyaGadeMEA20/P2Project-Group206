package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);

        workOutCategories = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.workOuts);
        workOutCategories.setVisibility(View.VISIBLE);

        workOutSubcategories = (LinearLayout) findViewById(R.id.level_layout);

        workOutSubcategories.setVisibility(View.GONE);

        workOutTypes = new ImageView[]{(ImageView) findViewById(R.id.workOut1),
                (ImageView) findViewById(R.id.workOut2),
                (ImageView) findViewById(R.id.workOut3),
                (ImageView) findViewById(R.id.workOut4)};

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
        startActivity(intent);
    }

    // - Method for making it easier to make a toast - //
    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }
}