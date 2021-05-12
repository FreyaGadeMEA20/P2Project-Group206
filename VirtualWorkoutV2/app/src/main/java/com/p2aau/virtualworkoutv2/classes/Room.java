package com.p2aau.virtualworkoutv2.classes;


import java.util.List;

public class Room {
    private String roomName;
    private ExerciseProgram roomProgram;
    private Exercise roomExercise;
    private List<User> roomMembers;

    Room(){}

    public Room(String _roomName, List<User> _users){
        roomName = _roomName;
        roomMembers = _users;
    }

    public void setRoomName(String _name){
        roomName=_name;
    }

    public String getRoomName(){
        return roomName;
    }

    public void addUsers(User _user){
        roomMembers.add(_user);
    }

    public List<User> getUsers() {
        return roomMembers;
    }

    public User getAUser(int i){
        return roomMembers.get(i);
    }

    public void addExercise(Exercise exe) {
        this.roomExercise =exe;
    }

    public void addExerciseProgram(ExerciseProgram prog) {
        this.roomProgram=prog;
    }

    public void startExercise() {
        if(roomExercise==null) {
            roomExercise.startExercise();
        }
    }

    public void exerciseController() {
        //idk what this does
    }

}