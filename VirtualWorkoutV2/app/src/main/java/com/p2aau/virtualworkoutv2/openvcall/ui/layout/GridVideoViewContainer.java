package com.p2aau.virtualworkoutv2.openvcall.ui.layout;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.p2aau.virtualworkoutv2.propeller.UserStatusData;
import com.p2aau.virtualworkoutv2.propeller.VideoInfoData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import com.p2aau.virtualworkoutv2.propeller.ui.RecyclerItemClickListener;

public class GridVideoViewContainer extends RecyclerView {

    private final static Logger log = LoggerFactory.getLogger(GridVideoViewContainer.class);

    private GridVideoViewContainerAdapter mGridVideoViewContainerAdapter;

    public GridVideoViewContainer(Context context) {
        super(context);
    }

    public GridVideoViewContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridVideoViewContainer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemEventHandler(RecyclerItemClickListener.OnItemClickListener listener) {
        this.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), listener));
    }

    private boolean initAdapter(Activity activity, int localUid, HashMap<Integer, SurfaceView> uids, double _height) {
        if (mGridVideoViewContainerAdapter == null) {
            mGridVideoViewContainerAdapter = new GridVideoViewContainerAdapter(activity, localUid, uids, _height);
            mGridVideoViewContainerAdapter.setHasStableIds(true);
            return true;
        }
        return false;
    }

    public void initViewContainer(Activity activity, int localUid, HashMap<Integer, SurfaceView> uids, boolean isLandscape, boolean grid, double _height) {
        boolean newCreated = initAdapter(activity, localUid, uids, _height);

        if (!newCreated) {
            mGridVideoViewContainerAdapter.setLocalUid(localUid);
            mGridVideoViewContainerAdapter.customizedInit(uids, true, _height);
        }

        this.setAdapter(mGridVideoViewContainerAdapter);

        int orientation = isLandscape ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;

        int count = uids.size();
        if (!grid) { // only local full view or or with one peer
            this.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext(), orientation, false));
        } else if (grid) {
            int itemSpanCount = getNearestSqrt(count);
            this.setLayoutManager(new GridLayoutManager(activity.getApplicationContext(), itemSpanCount, orientation, false));
        }

        mGridVideoViewContainerAdapter.notifyDataSetChanged();
    }

    private int getNearestSqrt(int n) {
        return (int) Math.sqrt(n);
    }

    public void notifyUiChanged(HashMap<Integer, SurfaceView> uids, int localUid, HashMap<Integer, Integer> status, HashMap<Integer, Integer> volume) {
        if (mGridVideoViewContainerAdapter == null) {
            return;
        }
        mGridVideoViewContainerAdapter.notifyUiChanged(uids, localUid, status, volume);
    }

    public void addVideoInfo(int uid, VideoInfoData video) {
        if (mGridVideoViewContainerAdapter == null) {
            return;
        }
        mGridVideoViewContainerAdapter.addVideoInfo(uid, video);
    }

    public void cleanVideoInfo() {
        if (mGridVideoViewContainerAdapter == null) {
            return;
        }
        mGridVideoViewContainerAdapter.cleanVideoInfo();
    }

    public UserStatusData getItem(int position) {
        return mGridVideoViewContainerAdapter.getItem(position);
    }

}
