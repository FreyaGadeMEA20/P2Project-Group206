package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.p2aau.virtualworkoutv2.classes.FriendListAdapter;

public class FriendsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] friends = {"Poul Poulsen", "Sarah Sarahsen", "Amon Goose", "Frille Frøsnapper", "Mike Æblemand", "Brede Gade"};
    int[] images = {R.drawable.friend_1, R.drawable.friend_2, R.drawable.friend_3, R.drawable.friend_4, R.drawable.friend_5, R.drawable.friend_6};

    // - Attributes for navigation menu - //
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        recyclerView = findViewById(R.id.recyclerView);

        FriendListAdapter friendListAdapter = new FriendListAdapter(this, friends, images);
        recyclerView.setAdapter(friendListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SetupDrawer();
    }

    // -- Side menu -- //

    // - Method for setting up the side menu - //
    public void SetupDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.friendLayout);
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
        intent.putExtra("Uniqid", "friends");
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