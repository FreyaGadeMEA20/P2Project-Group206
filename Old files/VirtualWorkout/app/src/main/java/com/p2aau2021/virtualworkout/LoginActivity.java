package com.p2aau2021.virtualworkout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginNextClick(View view){
        EditText userNameEditText = findViewById(R.id.et_login_user_name);
        String userName = userNameEditText.getText().toString();

        if(userName == null || userName == "") {
            Toast.makeText(this, "user name cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, VideoCallActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
    }

    public void onLoginBackClick(View view){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}