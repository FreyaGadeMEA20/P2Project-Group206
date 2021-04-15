import java.util.ArrayList;

public class User {

	private String name;
	private int age;
	private int heightinCM;
	private int weightinKG;
	private float bmi;
	private ArrayList<User> friendlist = new ArrayList<User>();
	// private int webcam;
	// private int microphone;

	public User(String name, int age, int height, int weight) {
		this.name = name;
		this.age = age;
		this.heightinCM = height;
		this.weightinKG = weight;
		this.bmi = weight/height*height/100;
		//setBmi();
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public int getHeight() {
		return heightinCM;
	}

	public int getWeight() {
		return weightinKG;
	}

	public float getBmi() {
		return this.bmi;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setHeight(int height) {
		this.heightinCM = height;
	}

	public void setWeight(int weight) {
		this.weightinKG = weight;
	}
	
	public void setBmi() {
		this.bmi = this.weightinKG/this.heightinCM*this.heightinCM/100;
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

	public String getFriendsNames() { //seems to not work somehow... O.o
		String namelist= "dumbshit";
		for (User friend : friendlist) {
			namelist.concat(friend.getName());
		}
		return namelist;
	}

	public void JoinRoom() {
	}

	public void LeaveRoom() {
	}
}
