
public class RoomAdmin extends User {

	Room room;

	public void setExercise() {
	}

	public void setExerciseProgram() {

	}

	public void startExercise() {

	}

	public void endExercise() {

	}

	public void addMember() {
		// søg blandt user's frindlist
	}

	public void removeMember() {
		// søg blandt room's members
	}

	public void createtRoom(User This) { //måske burde denne metode ligge i User, da jeg tænker en User først bliver RoomAdmin, når en user laver et Room
		new Room(this);
		//noget typecasting, således at når en User laver et room, bliver han til en RoomAdmin
	}

	public void disbandRoom() {

	}

}
