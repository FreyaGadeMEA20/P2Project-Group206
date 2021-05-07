package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

public class CreateLobbyActivity extends AppCompatActivity {

    GridLayout workOutStage1 = (GridLayout) findViewById(R.id.workOuts);

    ImageView[] workOutTypes = {(ImageView) findViewById(R.id.workOut1),
            (ImageView) findViewById(R.id.workOut2),
            (ImageView) findViewById(R.id.workOut3),
            (ImageView) findViewById(R.id.workOut4)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        for(int i = 0; i < workOutTypes.length; i++){
            workOutTypes[i].setOnClickListener(handler);
        }

    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == workOutTypes[0]){
                workOutStage1.setVisibility(View.GONE);
            } else if (v == workOutTypes[1]){
                workOutStage1.setVisibility(View.GONE);
            } else if (v == workOutTypes[2]){
                workOutStage1.setVisibility(View.GONE);
            } else if (v == workOutTypes[3]){
                workOutStage1.setVisibility(View.GONE);
            }
        }
    };

    public void onChosenWorkoutClick(View view) {

    }
}