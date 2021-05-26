package com.p2aau2021.virtualworkout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SigninActivity extends AppCompatActivity {
    EditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        userNameEditText = findViewById(R.id.et_user_name);
    }

    public void onSignUpBackClick(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onSignInNextClick(View view){
        String name = userNameEditText.getText().toString();
        if (name != null && !name.equals("")) {
            Intent intent = new Intent(this, VideoCallActivity.class);
            intent.putExtra("userName", name);
            startActivity(intent);
        } else {
            Toast.makeText(this, "user name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}