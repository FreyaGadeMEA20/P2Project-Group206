package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.p2aau.virtualworkoutv2.classes.ExerciseConstant;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    /*
            FUNCTIONALLY THE SAME AS THE LOGINACTIVITY.JAVA
            VERY MINOR DIFFERENCES
     */

    // Sign up button
    public void onSignUpNextClick(View view){
        EditText userNameEditText = findViewById(R.id.login_user_name_field);
        String userName = userNameEditText.getText().toString();

        /*
         * Checks first if the user remembered to enter a username, and only username.
         * For the first real implementation, password and the like are minor details and will be left out.
         */
        if(userName.equals(null) || userName.equals("")) {
            // If the username is empty, it will tell the user.
            Toast.makeText(this, "user name cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            // If it is not empty, it will continue with that username to the next page/activity.
            Intent intent = new Intent(this, MainMenuActivity.class);
            ExerciseConstant.USERNAME = userName; //Adds the username to a constant for the program to work with.
            intent.putExtra("Uniqid", "signup");
            startActivity(intent);
        }
    }

    public void onLoginNextClick(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}