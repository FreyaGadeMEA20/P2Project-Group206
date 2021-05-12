package com.p2aau.virtualworkoutv2.openvcall.model;

public class User {
    public User(int uid, String name, boolean _readyState) {
        this.uid = uid;
        this.name = name;
        readyState = _readyState;
    }

    public final int uid;
    public final String name;
    public final boolean readyState;
}
