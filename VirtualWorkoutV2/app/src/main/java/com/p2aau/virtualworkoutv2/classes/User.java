package com.p2aau.virtualworkoutv2.classes;

import java.util.ArrayList;

public class User {

    // Important info
    String userName;
    Room currentRoom;

    // Personal info
    String name;
    int age;
    float heightinCM;
    float weightinKG;
    //float bmi;
    ArrayList<User> friendlist = new ArrayList<User>();
    // private int webcam;
    // private int microphone;

    public User(){}

    public User(String _userName){
        userName = _userName;
    }

    public String getUserName(){
        return userName;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return heightinCM;
    }

    public float getWeight() {
        return weightinKG;
    }

    //TODO issues with bmi calculations. Made it not able to be inserted into the database for some reason.
    // Will have to be updated
    /*public float getBmi() {
        this.bmi = this.weightinKG / ((this.heightinCM / 100) * (this.heightinCM / 100));
        return this.bmi;
    }*/

    public void setUserName(String _userName){
        userName = _userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(float height) {
        this.heightinCM = height;
    }

    public void setWeight(float weight) {
        this.weightinKG = weight;
    }

    public void addFriend(User user) {
        if (!friendlist.contains(user)) {
            friendlist.add(user);
            user.addFriend(this);
        }
        // else throw "Already friends with this person"-error
    }

    public void removeFriend(User user) {
        friendlist.remove(user);
    }

    public ArrayList<User> getFriendlist() {
        return this.friendlist;

    }

    public String getFriendsNames() { // seems to not work somehow... O.o
        String namelist = "";
        for (User friend : friendlist) {
            namelist = namelist + "\n" + friend.getName();
        }

        // namelist.concat(friend.getName());}

        /*
         * for (int i = 0; i < friendlist.size(); i++) { ;
         * namelist.concat(friendlist.get(i).getName()); namelist.concat(":)");
         */
        return namelist;

    }

    public void createRoom(User user) { // måske burde denne metode ligge i User, da jeg tænker en User først bliver
        // RoomAdmin, når en user laver et Room
        // new Room(user);
        // noget typecasting, således at når en User laver et room, bliver han til en
        // RoomAdmin
    }

    public void JoinRoom(Room _room) {
        currentRoom = _room;
    }

    public void LeaveRoom(Room _room) {
        currentRoom = null;
    }
}