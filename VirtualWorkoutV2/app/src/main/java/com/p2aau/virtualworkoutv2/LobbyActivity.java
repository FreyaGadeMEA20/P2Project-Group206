package com.p2aau.virtualworkoutv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p2aau.virtualworkoutv2.classes.ExerciseConstant;
import com.p2aau.virtualworkoutv2.classes.ExerciseProgram;
import com.p2aau.virtualworkoutv2.classes.Room;
import com.p2aau.virtualworkoutv2.classes.User;
import com.p2aau.virtualworkoutv2.openvcall.ui.BaseActivity;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.GridVideoViewContainer;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.SmallVideoViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import io.agora.rtc.Constants;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p2aau.virtualworkoutv2.openvcall.model.AGEventHandler;

import java.util.Iterator;
import java.util.List;

import com.p2aau.virtualworkoutv2.openvcall.model.ConstantApp;
import com.p2aau.virtualworkoutv2.openvcall.model.DuringCallEventHandler;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.GridVideoViewContainer;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.SmallVideoViewAdapter;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.SmallVideoViewDecoration;
import com.p2aau.virtualworkoutv2.propeller.Constant;
import com.p2aau.virtualworkoutv2.propeller.UserStatusData;
import com.p2aau.virtualworkoutv2.propeller.VideoInfoData;
import com.p2aau.virtualworkoutv2.propeller.ui.RecyclerItemClickListener;
import com.p2aau.virtualworkoutv2.propeller.ui.RtlLinearLayoutManager;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class LobbyActivity extends BaseActivity implements DuringCallEventHandler {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    public static final int LAYOUT_TYPE_DEFAULT = 0;
    public static final int LAYOUT_TYPE_SMALL = 1;

    // should only be modified under UI thread
    private final HashMap<Integer, SurfaceView> mUidsList = new HashMap<>(); // uid = 0 || uid == EngineConfig.mUid
    public int mLayoutType = LAYOUT_TYPE_DEFAULT;
    private GridVideoViewContainer mGridVideoViewContainer;
    private RelativeLayout mSmallVideoViewDock;

    private volatile boolean mVideoMuted = false;
    private volatile boolean mAudioMuted = false;

    private volatile int mAudioRouting = Constants.AUDIO_ROUTE_DEFAULT;

    private boolean mIsLandscape = false;

    private SmallVideoViewAdapter mSmallVideoViewAdapter;

    private double height = 0.7; // Hard coded due to not able to find the right attribute to the view.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
    }

    // Gets run before the onCreate above, as it comes from the super class "BaseActivity".
    @Override
    protected void initUIandEvent() {
        addEventHandler(this);
        String channelName = ConstantApp.ACTION_KEY_CHANNEL_NAME; // TODO fix to "test" for testing

        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container_own);
        mGridVideoViewContainer.setItemEventHandler(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onBigVideoViewClicked(view, position);
                MakeAToast(position+"");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onBigVideoViewDoubleClicked(view, position);
            }
        });

        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        preview(true, surfaceV, 0);
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);

        mUidsList.put(0, surfaceV); // get first surface view

        mGridVideoViewContainer.initViewContainer(this, 0, mUidsList, mIsLandscape, false, height); // first is now full view

        joinChannel(channelName, config().mUid);

        //SetupDrawer();

        String previousIntent = getIntent().getExtras().getString("Uniqid");
        if(previousIntent.equals("create_lobby")){
            // Would create the room in the database
            // As well as add the user to the room in the database
        } else if (previousIntent.equals("find_lobby")){
            // Only adds the user to the room in the database
        } else if (previousIntent.equals("choose_workout")){
            ChooseExerciseLevel(ExerciseConstant.EXERCISE_TYPE, ExerciseConstant.EXERCISE_LEVEL); // Gets the exercise
        } else if (previousIntent.equals("end_screen")){
            // TODO?
        }

        optional();
    }

    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();
        doLeaveChannel();
        removeEventHandler(this);
        mUidsList.clear();
    }

    private void doLeaveChannel() {
        leaveChannel(config().mChannel);
        preview(false, null, 0);
    }

    public void onHangupClicked(View view) {
        finish();
    }

    public void onVideoMuteClicked(View view) {
        if (mUidsList.size() == 0) {
            return;
        }

        SurfaceView surfaceV = getLocalView();
        ViewParent parent;
        if (surfaceV == null || (parent = surfaceV.getParent()) == null) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        mVideoMuted = !mVideoMuted;

        if (mVideoMuted) {
            rtcEngine.disableVideo();
        } else {
            rtcEngine.enableVideo();
        }

        ImageView iv = (ImageView) view;

        iv.setImageResource(mVideoMuted ? R.drawable.agora_btn_camera_off : R.drawable.agora_btn_camera);

        hideLocalView(mVideoMuted);
    }

    private SurfaceView getLocalView() {
        for (HashMap.Entry<Integer, SurfaceView> entry : mUidsList.entrySet()) {
            if (entry.getKey() == 0 || entry.getKey() == config().mUid) {
                return entry.getValue();
            }
        }

        return null;
    }

    private void hideLocalView(boolean hide) {
        int uid = config().mUid;
        doHideTargetView(uid, hide);
    }

    private void doHideTargetView(int targetUid, boolean hide) {
        HashMap<Integer, Integer> status = new HashMap<>();
        status.put(targetUid, hide ? UserStatusData.VIDEO_MUTED : UserStatusData.DEFAULT_STATUS);
        if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
            mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
        } else if (mLayoutType == LAYOUT_TYPE_SMALL) {
            UserStatusData bigBgUser = mGridVideoViewContainer.getItem(0);
            if (bigBgUser.mUid == targetUid) { // big background is target view
                mGridVideoViewContainer.notifyUiChanged(mUidsList, targetUid, status, null);
            } else { // find target view in small video view list
                mSmallVideoViewAdapter.notifyUiChanged(mUidsList, bigBgUser.mUid, status, null);
            }
        }
    }

    public void onVoiceMuteClicked(View view) {
        if (mUidsList.size() == 0) {
            return;
        }

        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        iv.setImageResource(mAudioMuted ? R.drawable.agora_btn_microphone_off : R.drawable.agora_btn_microphone);
    }

    /*public void SetupDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.lobbyLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    public void onSelectWorkoutClick(View view){
        Intent intent = new Intent(LobbyActivity.this, ChooseWorkoutActivity.class);
        startActivity(intent);
    }

    public void onReadyUpClick(View view){
        // This snippet of code would be for checking if all the people are ready. However, we were not able to implement it to work well.
        // This meant it was ignored, as it was not a large part of what we had to test.
        /*boolean allReady = false;

        mGridVideoViewContainer.getItem(0).setReadyState(true);

        if (mUidsList.size() < 2) {
            allReady = true;
        } else {
            for(int i = 0; i < mUidsList.size(); i ++){
                UserStatusData user = mGridVideoViewContainer.getItem(i);

                MakeAToast(user.getReadyState()+"");

                if(user.getReadyState()) {
                    allReady = true;
                } else {
                    allReady = false;
                    break;
                }
            }
        }*/

        if(checkWorkoutStatus()) {
            deInitUIandEvent();
            Intent intent = new Intent(LobbyActivity.this, StartingWorkoutActivity.class);
            ExerciseConstant.MAX_EXERCISE = ExerciseConstant.EXERCISE_PROGRAM.getListOfExercises().size(); // Getting the size
            ExerciseConstant.CURRENT_EXERCISE = 1;
            startActivity(intent);
        } else {
            MakeAToast("Please choose a workout.");
        }
    }

    public boolean checkWorkoutStatus(){
        boolean bool;

        if(ExerciseConstant.EXERCISE_PROGRAM == null){
            bool = false;
        } else {
            bool = true;
        }

        return bool;
    }

    public void onAddFriendToLobbyClick(View view){
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    public void ChooseExerciseLevel(int _type, int _level){
        ExerciseConstant.EXERCISE_PROGRAM = ExerciseConstant.EXERCISE_PROGRAMS[_type-1][_level-1];
    }

    private void optional() {
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    private void optionalDestroy() {
    }

    private void onBigVideoViewClicked(View view, int position) {
        //toggleFullscreen();
    }

    private void onBigVideoViewDoubleClicked(View view, int position) {
        /*if (mUidsList.size() < 2) {
            return;
        }

        UserStatusData user = mGridVideoViewContainer.getItem(position);
        int uid = (user.mUid == 0) ? config().mUid : user.mUid;

        if (mLayoutType == LAYOUT_TYPE_DEFAULT && mUidsList.size() != 1) {
            switchToSmallVideoView(uid);
        } else {
            switchToDefaultVideoView();
        }*/
    }

    @Override
    public void onUserJoined(int uid) {

    }

    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }

    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mUidsList.containsKey(uid)) {
                    return;
                }

                /*
                  Creates the video renderer view.
                  CreateRendererView returns the SurfaceView type. The operation and layout of the
                  view are managed by the app, and the Agora SDK renders the view provided by the
                  app. The video display view must be created using this method instead of
                  directly calling SurfaceView.
                 */
                SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                mUidsList.put(uid, surfaceV);

                boolean useDefaultLayout = mLayoutType == LAYOUT_TYPE_DEFAULT;

                surfaceV.setZOrderOnTop(true);
                surfaceV.setZOrderMediaOverlay(true);

                /*
                  Initializes the video view of a remote user.
                  This method initializes the video view of a remote stream on the local device. It affects only the video view that the local user sees.
                  Call this method to bind the remote video stream to a video view and to set the rendering and mirror modes of the video view.
                 */
                rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));

                if (useDefaultLayout) {
                    switchToDefaultVideoView();
                } else {
                    int bigBgUid = mSmallVideoViewAdapter == null ? uid : mSmallVideoViewAdapter.getExceptedUid();
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {
        doRemoveRemoteUi(uid);
    }

    private void doRemoveRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                Object target = mUidsList.remove(uid);
                if (target == null) {
                    return;
                }

                int bigBgUid = -1;
                if (mSmallVideoViewAdapter != null) {
                    bigBgUid = mSmallVideoViewAdapter.getExceptedUid();
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT || uid == bigBgUid) {
                    switchToDefaultVideoView();
                } else {
                    switchToSmallVideoView(bigBgUid);
                }
            }
        });
    }

    @Override
    public void onExtraCallback(int type, Object... data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> status = new HashMap<>();
                    status.put(peerUid, muted ? UserStatusData.AUDIO_MUTED : UserStatusData.DEFAULT_STATUS);
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, status, null);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_MUTED:
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                doHideTargetView(peerUid, muted);

                break;

            case AGEventHandler.EVENT_TYPE_ON_USER_VIDEO_STATS:
                IRtcEngineEventHandler.RemoteVideoStats stats = (IRtcEngineEventHandler.RemoteVideoStats) data[0];

                mGridVideoViewContainer.cleanVideoInfo();

                break;

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS:
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];

                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                if (mLayoutType == LAYOUT_TYPE_DEFAULT) {
                    HashMap<Integer, Integer> volume = new HashMap<>();

                    for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                        peerUid = each.uid;
                        int peerVolume = each.volume;

                        if (peerUid == 0) {
                            continue;
                        }
                        volume.put(peerUid, peerVolume);
                    }
                    mGridVideoViewContainer.notifyUiChanged(mUidsList, config().mUid, null, volume);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR:
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_CONNECTION_ERROR) {
                    String msg = getString(R.string.msg_connection_error);
                    showLongToast(msg);
                }

                break;

            case AGEventHandler.EVENT_TYPE_ON_DATA_CHANNEL_MSG:

                peerUid = (Integer) data[0];
                final byte[] content = (byte[]) data[1];

                break;

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];


                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED:
                notifyHeadsetPlugged((int) data[0]);

                break;

        }
    }

    private void switchToDefaultVideoView() {
        if (mSmallVideoViewDock != null) {
            mSmallVideoViewDock.setVisibility(View.GONE);
        }
        mGridVideoViewContainer.initViewContainer(this, config().mUid, mUidsList, mIsLandscape, false, height);

        mLayoutType = LAYOUT_TYPE_DEFAULT;
        boolean setRemoteUserPriorityFlag = false;
        int sizeLimit = mUidsList.size();
        if (sizeLimit > ConstantApp.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantApp.MAX_PEER_COUNT + 1;
        }
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                if (!setRemoteUserPriorityFlag) {
                    setRemoteUserPriorityFlag = true;
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH);
                } else {
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_NORMAL);
                }
            }
        }
    }

    private void switchToSmallVideoView(int bigBgUid) {
        HashMap<Integer, SurfaceView> slice = new HashMap<>(1);
        slice.put(bigBgUid, mUidsList.get(bigBgUid));
        Iterator<SurfaceView> iterator = mUidsList.values().iterator();
        while (iterator.hasNext()) {
            SurfaceView s = iterator.next();
            s.setZOrderOnTop(true);
            s.setZOrderMediaOverlay(true);
        }

        mUidsList.get(bigBgUid).setZOrderOnTop(false);
        mUidsList.get(bigBgUid).setZOrderMediaOverlay(false);

        mGridVideoViewContainer.initViewContainer(this, bigBgUid, slice, mIsLandscape, false, height);

        bindToSmallVideoView(bigBgUid);

        mLayoutType = LAYOUT_TYPE_SMALL;

        //requestRemoteStreamType(mUidsList.size());
    }

    private void bindToSmallVideoView(int exceptUid) {
        if (mSmallVideoViewDock == null) {
            ViewStub stub = (ViewStub) findViewById(R.id.small_video_view_dock);
            mSmallVideoViewDock = (RelativeLayout) stub.inflate();
        }

        boolean twoWayVideoCall = mUidsList.size() == 2;

        RecyclerView recycler = (RecyclerView) findViewById(R.id.small_video_view_container);

        boolean create = false;

        if (mSmallVideoViewAdapter == null) {
            create = true;
            mSmallVideoViewAdapter = new SmallVideoViewAdapter(this, config().mUid, exceptUid, mUidsList, height);
            mSmallVideoViewAdapter.setHasStableIds(true);
        }
        recycler.setHasFixedSize(true);

        if (twoWayVideoCall) {
            recycler.setLayoutManager(new RtlLinearLayoutManager(getApplicationContext(), RtlLinearLayoutManager.HORIZONTAL, false));
        } else {
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
        recycler.addItemDecoration(new SmallVideoViewDecoration());
        recycler.setAdapter(mSmallVideoViewAdapter);
        recycler.addOnItemTouchListener((RecyclerView.OnItemTouchListener) new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void onItemDoubleClick(View view, int position) {
                onSmallVideoViewDoubleClicked(view, position);
            }
        }));

        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        if (!create) {
            mSmallVideoViewAdapter.setLocalUid(config().mUid);
            mSmallVideoViewAdapter.notifyUiChanged(mUidsList, exceptUid, null, null);
        }
        for (Integer tempUid : mUidsList.keySet()) {
            if (config().mUid != tempUid) {
                if (tempUid == exceptUid) {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_HIGH);
                } else {
                    rtcEngine().setRemoteUserPriority(tempUid, Constants.USER_PRIORITY_NORMAL);
                }
            }
        }
        recycler.setVisibility(View.VISIBLE);
        mSmallVideoViewDock.setVisibility(View.VISIBLE);
    }

    private void onSmallVideoViewDoubleClicked(View view, int position) {
        switchToDefaultVideoView();
    }

    public void notifyHeadsetPlugged(final int routing) {
        mAudioRouting = routing;
    }

    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }
}