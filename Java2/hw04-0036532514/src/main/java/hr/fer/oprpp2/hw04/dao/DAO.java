package hr.fer.oprpp2.hw04.dao;

import java.util.List;

import hr.fer.oprpp2.hw04.model.PollOptions;
import hr.fer.oprpp2.hw04.model.Polls;

public interface DAO {

	public List<Polls> getAllPolls() throws DAOException;

	public List<PollOptions> getAllPollOptions() throws DAOException;

	public Polls getPollById(long id) throws DAOException;

	public PollOptions getPollOptionById(long id) throws DAOException;

	public List<PollOptions> filterPollOtionsByPollId(long id) throws DAOException;

	public void changeVoteNumberForPollOption(long id);

	public void changeDislikeNumberForPollOption(long id);
}