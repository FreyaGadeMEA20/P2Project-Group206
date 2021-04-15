
public class RoomAdmin extends User{

	public RoomAdmin(String name, int age, int heightinCM, int weightinKG, int bmi) {
		super(name, age, heightinCM, weightinKG, bmi);
		// TODO Auto-generated constructor stub
	}
	int field;

	public void SetExercise(Exercise) {
	this.Exercise=Exercise;	
	}
	public void SetExercise(ExerciseProgram) {
	this.ExerciseProgram=ExerciseProgram;
	}
	public void StartExercise() {	
	Exercise.start();
	}
	public void EndExercise() {	
	Exercise.exit();
	}
	public void AddMember(User user) {
	roomMembers.add(user);	
	}
	public void RemoveMember(User user) {	
	roomMembers.remove(user);	
	}
	public void DisbandRoom() {	
	Room.exit();
	}
}

