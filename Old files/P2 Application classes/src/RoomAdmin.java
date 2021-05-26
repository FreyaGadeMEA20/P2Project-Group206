
public class RoomAdmin extends User {

	Room room;

	public RoomAdmin(String name, int age, float height, float weight) {
		super(name, age, height, weight);
	}

	RoomAdmin(User user, Room room) {
		super(user.getName(), user.getAge(), user.getHeight(), user.getWeight());
		this.room = room;
	}

	public void setExercise() {
	}

	public void setExerciseProgram() {

	}

	public void startExercise() {

	}

	public void endExercise() {

	}

	public void addMember() {
		// søg blandt user's friendlist
	}

	public void removeMember() {
		// søg blandt room's members
	}

	public void disbandRoom() {

	}

}
