package com.p2aau2021.virtualworkout;

import com.p2aau2021.virtualworkout.adapter.FriendListRecyclerViewAdapter;
import com.p2aau2021.virtualworkout.adapter.MessageAdapter;
import com.p2aau2021.virtualworkout.adapter.ShowFriendListRecyclerViewAdapter;
import com.p2aau2021.virtualworkout.layout.GridVideoViewContainer;
import com.p2aau2021.virtualworkout.layout.RecyclerItemClickListener;
import com.p2aau2021.virtualworkout.model.DBUser;
import com.p2aau2021.virtualworkout.model.MessageBean;
import com.p2aau2021.virtualworkout.model.MessageListBean;
import com.p2aau2021.virtualworkout.model.User;
import com.p2aau2021.virtualworkout.model.UserStatusData;
import com.p2aau2021.virtualworkout.rtm.AGApplication;
import com.p2aau2021.virtualworkout.rtm.ChatManager;
import com.p2aau2021.virtualworkout.utils.Constant;
import com.p2aau2021.virtualworkout.utils.MessageUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;

public class VideoCallActivity extends AppCompatActivity {
    private String userName;
    private String channelName;
    private User user;

    private String localState;

    private List<String> DBFriend;

    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        userName = getIntent().getExtras().getString("userName");
        //channelName = userName;
        user = new User();

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        mRef.push();
        mRef.child(userName).setValue(new DBUser(userName, user.getAgoraUid(), localState, DBFriend));
    }
}