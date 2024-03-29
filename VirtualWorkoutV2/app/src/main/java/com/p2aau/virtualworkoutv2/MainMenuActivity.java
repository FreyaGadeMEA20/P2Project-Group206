package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p2aau.virtualworkoutv2.classes.ActivityAdapter;
import com.p2aau.virtualworkoutv2.classes.ExerciseConstant;
import com.p2aau.virtualworkoutv2.classes.FriendListAdapter;
import com.p2aau.virtualworkoutv2.classes.User;
import com.p2aau.virtualworkoutv2.openvcall.model.ConstantApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainMenuActivity extends AppCompatActivity {

    // -- Attributes -- //

    // - Attributes for firebase database - //
    private String userName;
    private DatabaseReference mRef;

    // - Attributes for navigation menu - //
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ActionBarDrawerToggle fToggle;

    // - Attributes for the recycler view - //
    String[] friends = {"Poul Poulsen", "Sarah Sarahsen", "Amon Goose", "Frille Frøsnapper", "Mike Æblemand", "Brede Gade"};

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // generates activity
        GenerateRecyclerView();

        // sets up the drawer
        SetupDrawer();

        // checks what the previous activity was
        String previousIntent = getIntent().getExtras().getString("Uniqid");
        if(previousIntent.equals("login") || previousIntent.equals("signup")) {
            // generates the user if the user logged in or signed up
            GenerateUser();
        } else {

        }
    }

    // --- Methods --- //'

    // -- Friend activity -- //
    public void GenerateRecyclerView() {
        // finds the view
        recyclerView = findViewById(R.id.recyclerViewMain);

        // generates a list of 10 elements
        int[] images = new int[10];
        // gives each element a random image
        for (int i = 0; i < images.length; i++){
            int[] tempImages = {R.drawable.star_icon, R.drawable.smiley_icon};
            Random rand = new Random();
            images[i] = tempImages[rand.nextInt(2)];
        }

        // gets the 4 exercise types
        String[] workouts = ExerciseConstant.EXERCISE_TYPES;

        // generates the same size array for the text
        String[] text = new String[images.length];
        // gives each element a text based on the icon
        for (int i = 0; i < images.length; i++){
            Random rand = new Random();
            // adds a random friend name
            String temp = friends[rand.nextInt(friends.length)];
            // checks which icon is at the elemtn
            if(images[i] == R.drawable.star_icon){
                temp = temp + " leveled up to Level " + (rand.nextInt(3)+1);
            } else if (images[i] == R.drawable.smiley_icon){
                temp = temp +" finished a " + workouts[rand.nextInt(workouts.length)] + " Workout Level";
            }
            // adds it to that list element
            text[i] = temp;
        }

        //generates the friend activity based on the two arrays
        ActivityAdapter activityAdapter = new ActivityAdapter(this, text, images);
        recyclerView.setAdapter(activityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // -- Side menu -- //

    // - Method for setting up the side menu - //
    public void SetupDrawer(){
        // gets the main layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainMenuLayout);
        // gives the two menus a function
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        fToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        // adds the functionality to the layout
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.addDrawerListener(fToggle);
        mToggle.syncState();
        fToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // - Method for toggling the side menu from a button - //
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        } else if(fToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // - Creates the menu with the buttons - //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    // - Method for going to the home page - //
    public void onHomeClick(MenuItem item){
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("Uniqid", "home");
        startActivity(intent);
    }

    // - Method for going to the profile page - //
    public void onProfileClick(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // - Method for going to the friends page - //
    public void onFriendsClick(MenuItem item) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    // - Method for going to the workout page - //
    public void onWorkoutClick(MenuItem item) {
        Intent intent = new Intent(this, WorkoutsActivity.class);
        startActivity(intent);
    }

    // -- Methods for lobby -- //
    // - Method for creating lobby button - //
    public void onCreateLobbyClick(View view){
        Intent intent = new Intent(MainMenuActivity.this, LobbyActivity.class);
        ConstantApp.ACTION_KEY_CHANNEL_NAME = "test";    // Name of the channel for AGORA. For testing it is "test"
        //intent.putExtra("user", ExerciseConstant.USERNAME);                              // Username for getting the user from the DB
        intent.putExtra("Uniqid", "create_lobby");                // ID to tell the program what to do
        startActivity(intent);
    }

    // - Method for finding lobby button - //
    public void onFindLobbyClick(View view) {
        mDrawerLayout.openDrawer(GravityCompat.END);
}

    // - Method for going to the lobby - //
    public void onLobbyClick(MenuItem item){
        Intent intent = new Intent(MainMenuActivity.this, LobbyActivity.class);
        intent.putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME,"test");    // Name of the channel for AGORA. For testing it is "test"
        //intent.putExtra("user", ExerciseConstant.USERNAME);                              // Username for getting the user from the DB
        intent.putExtra("Uniqid", "find_lobby");                   // ID to tell the program what to do
        startActivity(intent);
    }

    // - Generating the user and adding it to the database - //
    public void GenerateUser(){
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        //listen to the friend list in the database
        mRef.push();
        mRef.child(ExerciseConstant.USERNAME).setValue(new User(ExerciseConstant.USERNAME));
    }

    // -- Special methods -- //

    // - Method for making it easier to make a toast - //
    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }
}