package com.p2aau.virtualworkoutv2;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.p2aau.virtualworkoutv2.classes.ExerciseConstant;
import com.p2aau.virtualworkoutv2.openvcall.model.AGEventHandler;
import com.p2aau.virtualworkoutv2.openvcall.model.ConstantApp;
import com.p2aau.virtualworkoutv2.openvcall.model.DuringCallEventHandler;
import com.p2aau.virtualworkoutv2.openvcall.ui.BaseActivity;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.GridVideoViewContainer;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.SmallVideoViewAdapter;
import com.p2aau.virtualworkoutv2.openvcall.ui.layout.SmallVideoViewDecoration;
import com.p2aau.virtualworkoutv2.propeller.UserStatusData;
import com.p2aau.virtualworkoutv2.propeller.ui.RecyclerItemClickListener;
import com.p2aau.virtualworkoutv2.propeller.ui.RtlLinearLayoutManager;

import java.util.HashMap;
import java.util.Iterator;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

public class ChooseWorkoutActivity extends BaseActivity implements DuringCallEventHandler {

    // --- Attributes --- //
    // -- Attributes for category and level -- //
    private int exerciseLevel;
    private int exerciseType;

    androidx.gridlayout.widget.GridLayout workOutCategories;
    ImageView[] workOutTypes;
    LinearLayout workOutSubcategories;
    Button[] workoutLevels;
    int[] workoutColors = {R.color.cardio, R.color.strength, R.color.yoga, R.color.fat_burn};

    // -- Attributes for webcam -- //
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

    // 0 as we just needed the volume part
    private double height = 0; // Hard coded due to not able to find the right attribute to the view.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workout);

        // Grid layout of the workout categories
        workOutCategories = (androidx.gridlayout.widget.GridLayout) findViewById(R.id.workOuts);
        workOutCategories.setVisibility(View.VISIBLE); // Sets it to visible, just to be safe

        // Array of the 4 different imageviews of the categories
        workOutTypes = new ImageView[]{(ImageView) findViewById(R.id.cardio),
                (ImageView) findViewById(R.id.strength),
                (ImageView) findViewById(R.id.yoga),
                (ImageView) findViewById(R.id.fat_burn)};

        // Sets an on click listener to each of the images in the workout type
        for(int i = 0; i < workOutTypes.length; i++){
            workOutTypes[i].setOnClickListener(handler);
        }

        // LinearLayout of the level buttons
        workOutSubcategories = (LinearLayout) findViewById(R.id.level_layout);
        workOutSubcategories.setVisibility(View.GONE); // Sets it to gone, just to be safe

        // Array of each of the levels the user can choose
        // - For expansion, this should be changed to be a for loop, so it can be easily expandable.
        workoutLevels = new Button[]{(Button) findViewById(R.id.button1),
                (Button) findViewById(R.id.button2),
                (Button) findViewById(R.id.button3),
                (Button) findViewById(R.id.button4)};


        // Add a callback to the activity, so when they press the back button, they always go back to the lobby
        // This was done in order to not have the webcam freeze once they click on the back button.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                deInitUIandEvent();
                Intent intent = new Intent(ChooseWorkoutActivity.this, LobbyActivity.class);
                intent.putExtra("Uniqid", "choose_workout_cancel");
                startActivity(intent);
            }
        };
        // Adds the back button to the activity
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Gets run before the onCreate above, as it comes from the super class "BaseActivity".
    // This is from the Agora code example for adding webcam.
    // Even though there is no webcam on the activity, we still needed the audio.
    // As it was right before testing, we did not spend time looking for what was the webcam and what was the audio.
    // So we just made the webcam practically invisible. Should be looked at for minor optimization.
    @Override
    protected void initUIandEvent() {
        addEventHandler(this); // Tells the program it is this activity that gets worked on.
        String channelName = ConstantApp.ACTION_KEY_CHANNEL_NAME; // The channel name which the user joins for webcam

        // Finds the view in which the program has to confine in
        mGridVideoViewContainer = (GridVideoViewContainer) findViewById(R.id.grid_video_view_container_own);

        // Generates the local video and the view at which the webcam will place
        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        preview(true, surfaceV, 0);
        surfaceV.setZOrderOnTop(false);
        surfaceV.setZOrderMediaOverlay(false);

        mUidsList.put(0, surfaceV); // get first surface view

        // Initializes the container with all the views
        mGridVideoViewContainer.initViewContainer(this, 0, mUidsList, mIsLandscape, false, height); // first is now full view

        // Connects to the server and joins the channel
        joinChannel(channelName, config().mUid);

        // Runs anything optional that is missing.
        optional();
    }

    // - Destroys the UI and the event to leave the channel, to remove the video call from the screen - //
    @Override
    protected void deInitUIandEvent() {
        optionalDestroy();
        doLeaveChannel();
        removeEventHandler(this);
        mUidsList.clear();
    }

    // - Leaves the channel - //
    private void doLeaveChannel() {
        leaveChannel(config().mChannel);
        preview(false, null, 0);
    }

    /*private void doHideTargetView(int targetUid, boolean hide) {
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
    }*/

    // - Generates what the handler will do when the user clicks on the screen.
    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Gets which of the four images that gets clicked on and runs a command based on what images that is clicked on
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

    // - What happens when an image is clicked on - //
    public void workOutCategory(int _int) {
        // Sets the categories to be gone, so the user can click on the levels instead
        workOutCategories.setVisibility(View.GONE);

        // Saves their choice
        exerciseType = _int;

        // Makes the level buttons visible again
        workOutSubcategories.setVisibility(View.VISIBLE);

        // Sets the color of the buttons to be the same as the image pressed on for visual feedback to the user
        for(int i = 0; i < workoutLevels.length; i++){
            // Gets the color based on which exercise has been clicked on
            workoutLevels[i].setBackgroundColor(getResources().getColor(workoutColors[exerciseType]));
       }
    }

    // - Method for when the user clicks on a button - //
    public void onWorkoutClick(View view){
        // Saves their type choice (cardio, strength, etc.) in a static variable so it can be used later on.
        ExerciseConstant.EXERCISE_TYPE  = exerciseType;

        // Saves the name of the exercise type in a static variable.
        ExerciseConstant.EXERCISE_TYPE_NAME = ExerciseConstant.EXERCISE_TYPES[exerciseType];

        // Finds the button that is clicked on
        Button button = (Button) findViewById(view.getId());
        // Gets the text of the button
        String level = (String) button.getText();
        // Removes the "level " part of it to only get the number
        level = level.replace("Level ", "");
        // Parses the number to an int
        exerciseLevel = Integer.parseInt(level);

        // Sets the constant static variable to be the level that has been chosen
        ExerciseConstant.EXERCISE_LEVEL = exerciseLevel;

        // Checks if the chosen workout is null. If it is, it shows a toast it hasn't been implemented yet
        // - for future it should instead make the buttons be a dull version of the color instead.
        if(ExerciseConstant.EXERCISE_PROGRAMS[ExerciseConstant.EXERCISE_TYPE][ExerciseConstant.EXERCISE_LEVEL-1] == null){
            MakeAToast("This workout hasn't been implemented yet!");
        } else {
            // If it isn't null, it will instead start up the lobby activity.
            Intent intent = new Intent(ChooseWorkoutActivity.this, LobbyActivity.class);
            intent.putExtra("Uniqid", "choose_workout");
            deInitUIandEvent();
            startActivity(intent);
        }
    }

    // - Method for making it easier to make a toast - //
    public void MakeAToast(String _toast){
        Toast.makeText(this, _toast, Toast.LENGTH_SHORT).show();
    }

    // - Code from Agora - //
    private void optional() {
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
    }

    // - Code from Agora - //
    private void optionalDestroy() {
    }

    // - Code from Agora - //
    @Override
    public void onUserJoined(int uid) {

    }

    // - Code from Agora - //
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
        doRenderRemoteUi(uid);
    }

    // - Code from Agora - //
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

    // - Code from Agora - //
    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    // - Code from Agora - //
    @Override
    public void onUserOffline(int uid, int reason) {
        doRemoveRemoteUi(uid);
    }

    // - Code from Agora - //
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

    // - Code from Agora - //
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

    // - Code from Agora - //
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

                //doHideTargetView(peerUid, muted);

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

    // - Code from Agora - //
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

    // - Code from Agora - //
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

    // - Code from Agora - //
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

    // - Code from Agora - //
    private void onSmallVideoViewDoubleClicked(View view, int position) {
        switchToDefaultVideoView();
    }

    // - Code from Agora - //
    public void notifyHeadsetPlugged(final int routing) {
        mAudioRouting = routing;
    }
}