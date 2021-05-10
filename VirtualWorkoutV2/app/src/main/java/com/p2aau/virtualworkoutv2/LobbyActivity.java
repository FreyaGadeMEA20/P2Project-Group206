package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.p2aau.virtualworkoutv2.classes.ExerciseProgram;
import com.p2aau.virtualworkoutv2.classes.Room;

public class LobbyActivity extends AppCompatActivity {

    Room room;
    ExerciseProgram exerciseProgram;
    int exerciseInt;
    private DrawerLayout afDrawerLayout;
    private ActionBarDrawerToggle afToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        SetupDrawer();
        //room = new Room();

        //GetExtra();
        //ChooseExercise();
    }

    public void SetupDrawer(){
        afDrawerLayout = (DrawerLayout) findViewById(R.id.mainMenuLayout);
        afToggle = new ActionBarDrawerToggle(this, afDrawerLayout, R.string.open, R.string.close);

        afDrawerLayout.addDrawerListener(afToggle);
        afToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onSelectWorkoutClick(View view){
        Intent intent = new Intent(LobbyActivity.this, ChooseWorkoutActivity.class);
        startActivity(intent);
    }

    public void onAddFriendToLobbyClick(View view){
        afDrawerLayout.openDrawer(GravityCompat.END);
    }


    public void GetExtra(){
        exerciseInt = getIntent().getExtras().getInt("room");
    }

    public void ChooseExercise(){
        //TODO long nested if else statement for choosing exerciseprogram :)
    }
}