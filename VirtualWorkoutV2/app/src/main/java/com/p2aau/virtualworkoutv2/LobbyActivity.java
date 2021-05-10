package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.p2aau.virtualworkoutv2.classes.ExerciseProgram;
import com.p2aau.virtualworkoutv2.classes.Room;

public class LobbyActivity extends AppCompatActivity {

    Room room;
    ExerciseProgram exerciseProgram;
    int exerciseInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        //room = new Room();

        //GetExtra();
        //ChooseExercise();
    }

    public void onSelectWorkoutClick(View view){
        Intent intent = new Intent(LobbyActivity.this, ChooseWorkoutActivity.class);
        startActivity(intent);
    }


    public void GetExtra(){
        exerciseInt = getIntent().getExtras().getInt("room");
    }

    public void ChooseExercise(){
        //TODO long nested if else statement for choosing exerciseprogram :)
    }
}