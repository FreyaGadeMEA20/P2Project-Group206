package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.p2aau.virtualworkoutv2.classes.MyAdapter;

public class FriendsActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String[] friends = {"Poul Poulsen", "Sarah Sarahsen", "Bjørn Bjørnsen", "Ben Ben", "Mike Æ", "Rasmus Reje"};
    int[] images = {R.drawable.friend_1, R.drawable.friend_2, R.drawable.friend_3, R.drawable.friend_4, R.drawable.friend_5, R.drawable.friend_6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        recyclerView = findViewById(R.id.recyclerView);

        MyAdapter myAdapter = new MyAdapter(this, friends, images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}