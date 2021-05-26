import java.util.ArrayList;

public class Room {
	private User roomAdmin;
	private ExerciseProgram roomProgram;
	private Exercise roomExercise;
	private ArrayList<User> roomMembers;

	Room(User user) {
		this.roomAdmin = user;
		new RoomAdmin(user);
	}

	public void addExercise( Exercise exe) {
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
