package com.p2aau.virtualworkoutv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p2aau.virtualworkoutv2.classes.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    // -- Attributes -- //

    // - Attributes for firebase database - //
    private String userName;
    private DatabaseReference mRef;

    // - Attributes for navigation menu - //
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SetupDrawer();
        GetExtras();
        GenerateUser(userName);
    }

    // --- Methods --- //

    // -- Side menu -- //

    // - Method for setting up the side menu - //
    public void SetupDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainMenuLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // - Method for toggling the side menu from a button - //
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)) {
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

    // - Method for going to the profile page - //
    public void onProfileClick(MenuItem item){
        Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // - Method for going to the friends page - //
    public void onFriendsClick(MenuItem item){
        Intent intent = new Intent(MainMenuActivity.this, FriendsActivity.class);
        startActivity(intent);
    }

    // - Method for going to the workout page - //
    public void onWorkoutClick(MenuItem item){
        Intent intent = new Intent(MainMenuActivity.this, WorkoutActivity.class);
        startActivity(intent);
    }

    // -- Methods for lobby -- //
    // - Method for creating lobby button - //
    //TODO evaluate if need for extra
    public void onCreateLobbyClick(View view){
        Intent intent = new Intent(MainMenuActivity.this, CreateLobbyActivity.class);
        startActivity(intent);
    }

    // - Method for finding lobby button - //
    //TODO evaluate if need for extra
    public void onFindLobbyClick(View view){
        Intent intent = new Intent(MainMenuActivity.this, FindLobbyActivity.class);
        startActivity(intent);
    }

    // -- Backend methods -- //

    // - Get the extra information relayed from the previous activity (username) - //
    public void GetExtras(){
        userName = getIntent().getExtras().getString("userName");
    }

    // - Generating the user and adding it to the database - //
    public void GenerateUser(final String _userName){
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        //listen to the friend list in the database
        mRef.push();
        mRef.child(userName).setValue(new User(userName));
    }

    // -- Special methods -- //

    // - Method for making it easier to make a toast - //
    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }
}