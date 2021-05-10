package com.p2aau.virtualworkoutv2.openvcall.ui;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p2aau.virtualworkoutv2.R;
import com.p2aau.virtualworkoutv2.propeller.Constant;

public class CallOptionsActivity extends AppCompatActivity {

    private final static Logger log = LoggerFactory.getLogger(CallOptionsActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agora_activity_call_options);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ab.setCustomView(R.layout.agora_ard_agora_actionbar_with_back_btn);
        }

        Switch debugSwitch = findViewById(R.id.debug_options);
        debugSwitch.setChecked(Constant.DEBUG_INFO_ENABLED);
        debugSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean withDebugInfo) {
                Constant.DEBUG_INFO_ENABLED = withDebugInfo;
            }
        });

        ((TextView) findViewById(R.id.ovc_page_title)).setText(R.string.label_options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(View view) {
        onBackPressed();
    }
}