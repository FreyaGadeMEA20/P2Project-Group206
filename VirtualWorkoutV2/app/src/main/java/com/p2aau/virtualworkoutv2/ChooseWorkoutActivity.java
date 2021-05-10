package com.p2aau.virtualworkoutv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChooseWorkoutActivity extends AppCompatActivity {


    androidx.gridlayout.widget.GridLayout workOutCategories;
    ImageView[] workOutTypes;
    LinearLayout[] workOutSubcategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);

        workOutCategories = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.workOuts);
        workOutCategories.setVisibility(View.VISIBLE);

        workOutSubcategories = new LinearLayout[]{(LinearLayout) findViewById(R.id.cardio_layout),
                (LinearLayout) findViewById(R.id.strength_layout),
                (LinearLayout) findViewById(R.id.blitz_layout),
                (LinearLayout) findViewById(R.id.fat_burn_layout)};

        for(int i = 0; i < workOutSubcategories.length; i++){
            workOutSubcategories[i].setVisibility(View.GONE);
        }

        workOutTypes = new ImageView[]{(ImageView) findViewById(R.id.workOut1),
                (ImageView) findViewById(R.id.workOut2),
                (ImageView) findViewById(R.id.workOut3),
                (ImageView) findViewById(R.id.workOut4)};

        for(int i = 0; i < workOutTypes.length; i++){
            workOutTypes[i].setOnClickListener(handler);
        }
    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == workOutTypes[0]){
                workOutCategory(0);
            } else if (v == workOutTypes[1]){
                workOutCategory(1);
            } else if (v == workOutTypes[2]){
                workOutCategory(2);
            } else if (v == workOutTypes[3]){
                workOutCategory(3);
            }
        }
    };

    public void workOutCategory(int _int) {
        workOutCategories.setVisibility(View.GONE);

        workOutSubcategories[_int].setVisibility(View.VISIBLE);
    }

    // - Method for making it easier to make a toast - //
    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }
}