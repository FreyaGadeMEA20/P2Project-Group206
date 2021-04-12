import java.util.Date;

public class Exercise {
	private String exerciseName;
	private String description;
	private Date timeToComplete;
	private int difficulty;
	private int amountToDo;
	private boolean completed;

	Exercise(String name, String desc, Date time, int diff, int amount) {
		this.exerciseName = name;
		this.description = desc;
		this.timeToComplete = time;
		this.difficulty = diff;
		this.amountToDo = amount;

	}

	public String getExercisenName() {
		return this.exerciseName;
	}

	public void setExercisenName(String name) {
		this.exerciseName = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String desc) {
		this.exerciseName = desc;
	}

	public Date getTimeToComplete() {
		return this.timeToComplete;
	}

	public void setTimeToComplete(Date time) {
		this.timeToComplete = time;
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public void setDifficulty(int diff) {
		this.difficulty = diff;
	}

	public int getAmountToDo() {
		return this.amountToDo;
	}

	public void setAmountToDo(int amount) {
		this.amountToDo = amount;
	}

	public boolean getCompleted() {
		return this.completed;
	}

	public void setCompleted(boolean x) {
		this.completed = x;
	}
}
