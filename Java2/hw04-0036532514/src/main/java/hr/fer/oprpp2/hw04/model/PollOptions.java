package hr.fer.oprpp2.hw04.model;

public class PollOptions {
	private long id;
	private String optionTitle;
	private String optionLink;
	private long pollID;
	private long votesCount;
	private long dislikeCount;

	public PollOptions() {}

	public long getId() {
		return id;
	}

	public long getRazlika() {
		return votesCount - dislikeCount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getOptionLink() {
		return optionLink;
	}

	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	public long getPollID() {
		return pollID;
	}

	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	public long getVotesCount() {
		return votesCount;
	}

	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

	public long getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(long dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	@Override
	public String toString() {
		return "PollOptions [id=" + id + "]";
	}
}
