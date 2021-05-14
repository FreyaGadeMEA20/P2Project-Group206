package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        TextView textView = findViewById(R.id.username);

        Intent intent = getIntent();
        String message = intent.getStringExtra("user");

        textView.setText(message);

    }

    public void onBackButtonClick(View view){
        Intent intent = new Intent(ProfileActivity.this, MainMenuActivity.class);
        intent.putExtra("Uniqid", "profile");
        startActivity(intent);
    }
}