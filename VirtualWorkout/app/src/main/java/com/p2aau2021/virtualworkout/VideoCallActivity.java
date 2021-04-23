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

    private List<DBUser> searchFriendList = new ArrayList<>();
    private String userName;
    private String channelName;
    private User user;
    private static final String TAG = VideoCallActivity.class.getName();
    private boolean isMuted = false;

    private boolean isLocalCall = true;
    private EditText mSearchFriendEditText;
    private RecyclerView mFriendListRecyclerView;
    private FriendListRecyclerViewAdapter mFriendListRecyclerViewAdapter;
    private LinearLayout mShowFriendLinearLayout;
    private RecyclerView mShowFriendListRecyclerView;
    private ShowFriendListRecyclerViewAdapter mShowFriendListRecyclerViewAdapter;
    private ChildEventListener joinFriendChildEventListener;

    private ImageView mCallBtn, mMuteBtn, mSwitchCameraBtn;
    private LinearLayout mAddFriendLinearLayout;
    private boolean isAddingFriend = false;
    private boolean isShowingFriend = false;

    private String localState;

    private List<String> DBFriend;

    private DatabaseReference mRef;
    private ChildEventListener childEventListener;
    private ChildEventListener chatSearchChildEventListener;

    private static final int PERMISSION_REQ_ID = 22;
    RtcEngine mRtcEngine;

    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    private boolean mIsLandscape = false;
    private GridVideoViewContainer mGridVideoViewContainer;
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>();
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast("User: " + uid + " join!");
                    user.setAgoraUid(uid);
                    SurfaceView localView = mUidsList.remove(0);
                    mUidsList.put(uid, localView);
                    mRef.child(userName).setValue(new DBUser(userName, user.getAgoraUid(), localState, DBFriend));
                }
            });
        }

        @Override
        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of a remote user is received and decoded after the remote user successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        // Listen for the onUserOffline callback.
        // This callback occurs when the remote user leaves the channel or drops offline.
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast("User: " + uid + " left the room.");
                    onRemoteUserLeft(uid);
                }
            });
        }
    };

    // Ask for Android device permissions at runtime.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.hide();
        }

        setContentView(R.layout.activity_video_call);
        getExtras();
        initUI();
        generateUser(userName);
        //startVideoCall();

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }

    private void initUI() {
        mCallBtn = findViewById(R.id.start_call_end_call_btn);
        mMuteBtn = findViewById(R.id.audio_mute_audio_unmute_btn);
        mSwitchCameraBtn = findViewById(R.id.switch_camera_btn);
        mAddFriendLinearLayout = findViewById(R.id.layout_add_friends);
        mGridVideoViewContainer = findViewById(R.id.grid_video_view_container);
        mSearchFriendEditText = findViewById(R.id.et_search_friends);
        mFriendListRecyclerView = findViewById(R.id.rv_friendList);
        mShowFriendLinearLayout = findViewById(R.id.layout_show_friends);
        mShowFriendListRecyclerView = findViewById(R.id.rv_show_friendList);
        /*mChatLayout = findViewById(R.id.layout_chat);
        mRecyclerView = findViewById(R.id.message_list);
        mMsgEditText = findViewById(R.id.message_edittiext);
        mTitleTextView = findViewById(R.id.message_title);*/

        mGridVideoViewContainer.setItemEventHandler(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //can add single click listener logic
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //can add long click listener logic
            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onBigVideoViewDoubleClicked(view, position);
            }
        });
    }

    public void startVideoCall() throws Exception {
        mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
    }

    public void getExtras(){
        userName = getIntent().getExtras().getString("userName");
        channelName = userName;
        user = new User();
    }

    public void generateUser(final String _userName){
        mRef = FirebaseDatabase.getInstance().getReference("Users");

        //listen to the friend list in the database
        mRef.push();
        mRef.child(this.userName).child("friend").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DBFriend = (List<String>) dataSnapshot.getValue();
                if (DBFriend == null) {
                    DBFriend = new ArrayList<>();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast(databaseError.getMessage());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRef.child(userName).setValue(new DBUser(userName, user.getAgoraUid(), localState, DBFriend));
            }
        }, 1500);

        //mRef.push();
        //mRef.child(userName).setValue(new DBUser(userName, user.getAgoraUid(), localState, DBFriend));
    }

    public void showToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }

    private void setupRemoteVideo(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SurfaceView mRemoteView = RtcEngine.CreateRendererView(getApplicationContext());

                mUidsList.put(uid, mRemoteView);
                mRemoteView.setZOrderOnTop(true);
                mRemoteView.setZOrderMediaOverlay(true);
                mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));

                switchToDefaultVideoView();
            }
        });
    }

    private void switchToDefaultVideoView() {

        mGridVideoViewContainer.initViewContainer(VideoCallActivity.this, user.getAgoraUid(), mUidsList, mIsLandscape);

        boolean setRemoteUserPriorityFlag = false;

        mLayoutType = LAYOUT_TYPE_DEFAULT;

        int sizeLimit = mUidsList.size();
        if (sizeLimit > 5) {
            sizeLimit = 5;
        }

        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (user.getAgoraUid() != uid) {
                if (!setRemoteUserPriorityFlag) {
                    setRemoteUserPriorityFlag = true;
                    mRtcEngine.setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH);
                } else {
                    mRtcEngine.setRemoteUserPriority(uid, Constants.USER_PRIORITY_NORANL);
                }
            }
        }
    }

    private void onRemoteUserLeft(int uid) {
        removeRemoteVideo(uid);
    }

    private void removeRemoteVideo(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }
                switchToDefaultVideoView();
            }
        });

    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        joinChannel();
        setupLocalVideo();
    }

    private void searchFriends(final String searchFriendName) {
        //search for a new friend in the database
        searchFriendList.clear();
        mFriendListRecyclerViewAdapter = new FriendListRecyclerViewAdapter(searchFriendList);
        mFriendListRecyclerViewAdapter.setOnItemClickListener(new FriendListRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                addFriend(searchFriendList.get(position).getName());
                mSearchFriendEditText.setText("");
                searchFriendList.clear();
                mFriendListRecyclerView.setAdapter(mFriendListRecyclerViewAdapter);
            }
        });
        RecyclerView.LayoutManager manager = new GridLayoutManager(getBaseContext(), 1);
        mFriendListRecyclerView.setLayoutManager(manager);

        mFriendListRecyclerView.setAdapter(mFriendListRecyclerViewAdapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DBUser result = dataSnapshot.getValue(DBUser.class);

                searchFriendList.add(result);
                mRef.orderByChild("name").startAt(searchFriendName).endAt(searchFriendName + "\uf8ff").removeEventListener(childEventListener);

                mFriendListRecyclerViewAdapter = new FriendListRecyclerViewAdapter(searchFriendList);
                mFriendListRecyclerViewAdapter.setOnItemClickListener(new FriendListRecyclerViewAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        addFriend(searchFriendList.get(position).getName());
                        mSearchFriendEditText.setText("");
                        searchFriendList.clear();
                        mFriendListRecyclerView.setAdapter(mFriendListRecyclerViewAdapter);
                    }
                });
                RecyclerView.LayoutManager manager = new GridLayoutManager(getBaseContext(), 1);
                mFriendListRecyclerView.setLayoutManager(manager);

                mFriendListRecyclerView.setAdapter(mFriendListRecyclerViewAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.orderByChild("name").startAt(searchFriendName).endAt(searchFriendName + "\uf8ff").addChildEventListener(childEventListener);
    }

    private void onBigVideoViewDoubleClicked(View view, int position) {
        if (mUidsList.size() < 2) {
            return;
        }

        final UserStatusData user = mGridVideoViewContainer.getItem(position);

        if (user.mUid != this.user.getAgoraUid()) {

            chatSearchChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DBUser result = dataSnapshot.getValue(DBUser.class);
                    //startMessaging(result.getName());

                    mRef.orderByChild("uid").startAt(user.mUid).endAt(user.mUid + "\uf8ff").removeEventListener(chatSearchChildEventListener);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            mRef.orderByChild("uid").startAt(user.mUid).endAt(user.mUid + "\uf8ff").addChildEventListener(chatSearchChildEventListener);
        }
    }

    public void addFriend(String userName) {
        DBFriend.add(userName);
        mRef.child(this.userName).setValue(new DBUser(this.userName, user.getAgoraUid(), localState, DBFriend));
    }

    public void onShowFriendListClick(View view) {
        if (isAddingFriend) {
            isAddingFriend = !isAddingFriend;
            mAddFriendLinearLayout.setVisibility(isAddingFriend ? View.VISIBLE : View.GONE);
        }

        isShowingFriend = !isShowingFriend;
        mShowFriendLinearLayout.setVisibility(isShowingFriend ? View.VISIBLE : View.GONE);

        mShowFriendListRecyclerViewAdapter = new ShowFriendListRecyclerViewAdapter(DBFriend);
        mShowFriendListRecyclerViewAdapter.setOnItemClickListener(new ShowFriendListRecyclerViewAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {

                if (v.getId() == R.id.btn_join_friend) {
                    joinFriendChildEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            DBUser result = dataSnapshot.getValue(DBUser.class);
                            joinFriend(DBFriend.get(position));
                            mShowFriendLinearLayout.setVisibility(View.GONE);
                            /*}else {
                                showToast(DBFriend.get(position) + "'s room is locked. You can message him to say hi!");
                            }*/

                            mRef.orderByChild("name").startAt(DBFriend.get(position)).endAt(DBFriend.get(position) + "\uf8ff").removeEventListener(joinFriendChildEventListener);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    mRef.orderByChild("name").startAt(DBFriend.get(position)).endAt(DBFriend.get(position) + "\uf8ff").addChildEventListener(joinFriendChildEventListener);
                }else if (v.getId() == R.id.btn_chat_friend){
                    //startMessaging(DBFriend.get(position));
                }
            }
        });
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mShowFriendListRecyclerView.setLayoutManager(manager);
        mShowFriendListRecyclerView.setAdapter(mShowFriendListRecyclerViewAdapter);
    }

    public void onSearchButtonClick(View view) {
        String searchFriendName = mSearchFriendEditText.getText().toString();
        if (searchFriendName == null || searchFriendName.equals("")) {
            showToast("Name can not be empty!");
        }else {
            searchFriends(searchFriendName);
        }
    }

    public void onAddFriendClick(View view) {
        if (isShowingFriend) {
            isShowingFriend = !isShowingFriend;
            mShowFriendLinearLayout.setVisibility(isShowingFriend ? View.VISIBLE : View.GONE);
        }
        isAddingFriend = !isAddingFriend;
        mAddFriendLinearLayout.setVisibility(isAddingFriend ? View.VISIBLE : View.GONE);
    }

    public void joinFriend(String friendName){
        channelName = friendName;
        finishCalling();
        joinChannel();
        startCalling();
    }

    public void onLockRoomClick(View view) {
        if (isLocalCall) {
            //when the user is in his own room
            if (localState.equals(Constant.USER_STATE_LOCK)) {
                //set the room to public
                localState = Constant.USER_STATE_OPEN;
                mRef.child(this.userName).setValue(new DBUser(this.userName, user.getAgoraUid(), localState, DBFriend));
                showToast("Room set to public");
            }else {
                //set the room to private so that no one can join the room
                localState = Constant.USER_STATE_LOCK;
                mRef.child(this.userName).setValue(new DBUser(this.userName, user.getAgoraUid(), localState, DBFriend));
                showToast("Room set to private");
            }
        }else {
            //when user is joining other people's room
            //leave that room and come back to user's own room
            isLocalCall = true;
            finishCalling();
            channelName = userName;
            startCalling();
            localState = Constant.USER_STATE_OPEN;
            //update user's room state
            mRef.child(userName).setValue(new DBUser(userName, user.getAgoraUid(), localState, DBFriend));
        }
    }

    private void finishCalling() {
        mRtcEngine.leaveChannel();
        mUidsList.clear();
    }

    private void startCalling() {
        //set up local video canvas
        mRtcEngine.enableVideo();
        mRtcEngine.enableInEarMonitoring(true);
        mRtcEngine.setInEarMonitoringVolume(80);

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        surfaceView.setZOrderOnTop(false);
        surfaceView.setZOrderMediaOverlay(false);

        //join the channel
        //mRtcEngine.joinChannel(null, channelName, "Extra Optional Data", 0);
    }

    private void initializeEngine() {
        try {
            startVideoCall();
            //mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupLocalVideo() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRtcEngine.enableVideo();
                mRtcEngine.enableInEarMonitoring(true);
                mRtcEngine.setInEarMonitoringVolume(80);

                SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
                mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
                surfaceView.setZOrderOnTop(false);
                surfaceView.setZOrderMediaOverlay(false);

                mUidsList.put(0, surfaceView);

                mGridVideoViewContainer.initViewContainer(VideoCallActivity.this, 0, mUidsList, mIsLandscape);

            }
        });
    }

    private void joinChannel() {
        // Join a channel with a token, token can be null.
        mRtcEngine.joinChannel(null, channelName, "Extra Optional Data", 0);
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    public void onLocalAudioMuteClicked(View view) {
        isMuted = !isMuted;
        mRtcEngine.muteLocalAudioStream(isMuted);
        int res = isMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }
}