
public class TestClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		User user1 = new User("dude42",21,176,83);
		User user2 = new User("WhackBoiii21",17,181,65);
		User user3 = new User("miSScommie",23,155,65);
		user1.addFriend(user2);
		user1.addFriend(user3);
		
		System.out.println(user1.getFriendlist());
		System.out.println(user2.getFriendlist());
		System.out.println("User1 friend names: " + user1.getFriendsNames());
		System.out.println(user2.getFriendsNames());
		
		System.out.println("user "+user1.getName()+" is "+ user1.getAge()+" years old, weighs "+user1.getWeight()+"kg and is "+user1.getHeight()+"cm tall.");
		System.out.println("his BMI therefore is:"+user1.getBmi());
		
		Exercise basicPushups = new Exercise("Armbøjninger", "Læg dig ned med fronten mod gulvet og brug armene til skiftevist at løfte og sænke din krop fra gulvet.", 30000,2,20);
		System.out.println("the exercise "+basicPushups.getExerciseName()+ " instructs:"+basicPushups.getEDescription()+"it lasts about "+basicPushups.getTimeToComplete()/1000+" seconds and requires "+basicPushups.getAmountToDo()+" repetitions and is rated for a difficulty of "+basicPushups.getDifficulty());
		

		user1.createRoom(user1);
		
		
	}
	
	public void createRoom(User user) { //måske burde denne metode ligge i User, da jeg tænker en User først bliver RoomAdmin, når en user laver et Room
		new Room(user);
		//noget typecasting, således at når en User laver et room, bliver han til en RoomAdmin
	}
}
