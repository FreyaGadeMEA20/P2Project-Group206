package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class WorkoutsActivity extends AppCompatActivity {

    // - Attributes for navigation menu - //
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        SetupDrawer();

        Toast.makeText(this, "Page not yet implemented", Toast.LENGTH_SHORT).show();
    }

    // todo - content functionallity not implemented yet

    // -- Side menu -- //

    // - Method for setting up the side menu - //
    public void SetupDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.workoutLayout);
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

    // - Method for going to the home page - //
    public void onHomeClick(MenuItem item){
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("Uniqid", "workouts");
        startActivity(intent);
    }

    // - Method for going to the profile page - //
    public void onProfileClick(MenuItem item){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // - Method for going to the friends page - //
    public void onFriendsClick(MenuItem item){
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    // - Method for going to the workout page - //
    public void onWorkoutClick(MenuItem item){
        Intent intent = new Intent(this, WorkoutsActivity.class);
        startActivity(intent);
    }
}