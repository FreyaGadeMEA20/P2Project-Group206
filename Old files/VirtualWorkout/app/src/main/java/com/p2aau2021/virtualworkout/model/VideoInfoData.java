package com.p2aau2021.virtualworkout.model;

public class VideoInfoData {

    public int mWidth;
    public int mHeight;
    public int mDelay;
    public int mFrameRate;
    public int mBitRate;
    public int mCodec;

    public VideoInfoData(int width, int height, int delay, int frameRate, int bitRate, int codec) {
        this.mWidth = width;
        this.mHeight = height;
        this.mDelay = delay;
        this.mFrameRate = frameRate;
        this.mBitRate = bitRate;
        this.mCodec = codec;
    }

    @Override
    public String toString() {
        return "VideoInfoData{" +
                "mWidth=" + mWidth +
                ", mHeight=" + mHeight +
                ", mDelay=" + mDelay +
                ", mFrameRate=" + mFrameRate +
                ", mBitRate=" + mBitRate +
                ", mCodec=" + mCodec +
                '}';
    }
}