package hr.fer.oprpp2.hw04.model;

public class Polls {

	private long id;
	private String title;
	private String message;

	public Polls() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Polls [id=" + id + "]";
	}
}
