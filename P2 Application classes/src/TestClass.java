
public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		User user1 = new User("dude42",21,176,83);
		User user2 = new User("WhackBoiii21",17,181,65);
		user1.addFriend(user2);
		
		System.out.println(user1.getFriendlist());
		System.out.println(user2.getFriendlist());
		System.out.println("User1 friend name: " + user1.getFriendsNames());
		System.out.println(user2.getFriendsNames());
		
		System.out.println("user "+user1.getName()+" is "+ user1.getAge()+" years old, weighs "+user1.getWeight()+"kg and is "+user1.getHeight()+"cm tall.");
		System.out.println("his BMI therefore is:"+user1.getBmi());
		
		
		
	}
	
	public void createtRoom(User user) { //måske burde denne metode ligge i User, da jeg tænker en User først bliver RoomAdmin, når en user laver et Room
		new Room(user);
		//noget typecasting, således at når en User laver et room, bliver han til en RoomAdmin
	}
}
