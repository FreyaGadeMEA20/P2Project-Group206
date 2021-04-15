import java.util.ArrayList;

public class User {
	
	public String name;
	public int age;
	public int heightinCM;
	public int weightinKG;
	public float bmi;
	public ArrayList<User> friendlist=new ArrayList<User>();
	public int webcam;
	public int microphone;

public User(String name, int age, int heightinCM, int weightinKG, int bmi) {
	//setName(name);
	//setAge(age);
	//setHeight(heightinCM);
	//setWeight(weightinKG);
	//setBmi(bmi);
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
	return bmi;
}
public void setName(String name) {
	this.name=name;
}
public void setAge(int age) {
	this.age=age;
}
public void setHeight(int heightinCM) {
	this.heightinCM=heightinCM;
}
public void setWeight(int weightinKG) {
	this.weightinKG=weightinKG;
}
public void setBmi(float bmi) {
	this.bmi=bmi;
}

public void addFriend(User user) {
	//friendlist.add(User.name);
	//friendlist.add(age);
	//friendlist.add(heightinCM);
	//friendlist.add(weightinKG);
	//friendlist.add(bmi);
	friendlist.add(user);	
}
public void removeFriend(User user) {
	//friendlist.remove(name);
	//friendlist.remove(age);
	//friendlist.remove(heightinCM);
	//friendlist.remove(weightinKG);
	//friendlist.remove(bmi);
	friendlist.remove(user);
}
//@Override
//public String toString()
//{
  //return String.format( "Name: %s\nStudent Age: %s\nStudent", name, age, heightinCM, weightinKG, bmi);
//}

}



//public void JoinRoom() {

//}

//public void LeaveRoom() {
	
//	}
//}
