import java.util.ArrayList;

public class User {

	private String name;
	private int age;
	private float heightinCM;
	private float weightinKG;
	private float bmi;
	private ArrayList<User> friendlist = new ArrayList<User>();
	// private int webcam;
	// private int microphone;

	public User(String name, int age, float height, float weight) {
		this.name = name;
		this.age = age;
		this.heightinCM = height;
		this.weightinKG = weight;
		this.bmi = weight / ((height * height) / 100);
		// setBmi();
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

	public float getBmi() {
		
		System.out.println(this.weightinKG + " / " + this.heightinCM/100);
		this.bmi = this.weightinKG / ((this.heightinCM/100) * (this.heightinCM/100));
		return this.bmi;
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
			namelist = "\n" + namelist + friend.getName();
		}
		
		// namelist.concat(friend.getName());}

		/*
		 * for (int i = 0; i < friendlist.size(); i++) { ;
		 * namelist.concat(friendlist.get(i).getName()); namelist.concat(":)");
		 */
		return namelist;

	}

	public void JoinRoom() {
	}

	public void LeaveRoom() {
	}
}
