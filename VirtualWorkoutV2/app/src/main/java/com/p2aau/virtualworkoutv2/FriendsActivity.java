package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.p2aau.virtualworkoutv2.classes.FriendListAdapter;

public class FriendsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] friends = {"Poul Poulsen", "Sarah Sarahsen", "Amon Goose", "Frille Frøsnapper", "Mike Æblemand", "Brede Gade"};
    int[] images = {R.drawable.friend_1, R.drawable.friend_2, R.drawable.friend_3, R.drawable.friend_4, R.drawable.friend_5, R.drawable.friend_6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);

        FriendListAdapter friendListAdapter = new FriendListAdapter(this, friends, images);
        recyclerView.setAdapter(friendListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onBackButtonClick(View view){
        Intent intent = new Intent(FriendsActivity.this, MainMenuActivity.class);
        intent.putExtra("Uniqid", "profile");
        startActivity(intent);
    }
}