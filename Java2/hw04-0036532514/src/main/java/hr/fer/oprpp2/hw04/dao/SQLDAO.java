package hr.fer.oprpp2.hw04.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import hr.fer.oprpp2.hw04.model.PollOptions;
import hr.fer.oprpp2.hw04.model.Polls;

public class SQLDAO implements DAO {

	@Override
	public List<Polls> getAllPolls() throws DAOException {
		List<Polls> polls = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
//		                                                          1    2       3
		try(PreparedStatement pst = con.prepareStatement("select id, title, message from Polls order by id")) {
			try(ResultSet rset = pst.executeQuery()) {
				while(rset != null && rset.next()) {
					Polls poll = new Polls();
					poll.setId(rset.getLong(1));
					poll.setTitle(rset.getString(2));
					poll.setMessage(rset.getString(3));

					polls.add(poll);
				}
			}
		} catch(SQLException e) {
			throw new DAOException("Error while fetching data.");
		}

		return polls;
	}

	@Override
	public List<PollOptions> getAllPollOptions() throws DAOException {
		LinkedList<PollOptions> pollOptions = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
//		                                                          1       2           3         4          5    
		try(PreparedStatement pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount from PollOptions order by id")) {
			try(ResultSet rset = pst.executeQuery()) {
				while(rset != null && rset.next()) {
					PollOptions pollOption = new PollOptions();
					pollOption.setId(rset.getLong(1));
					pollOption.setOptionTitle(rset.getString(2));
					pollOption.setOptionLink(rset.getString(3));
					pollOption.setPollID(rset.getLong(4));
					pollOption.setVotesCount(rset.getLong(5));

					pollOptions.add(pollOption);
				}
			}
		} catch(SQLException e) {
			throw new DAOException("Error while fetching data.");
		}

		return pollOptions;
	}

	@Override
	public Polls getPollById(long id) throws DAOException {
		Polls poll = null;
		Connection con = SQLConnectionProvider.getConnection();
//		                                                          1    2       3
		try(PreparedStatement pst = con.prepareStatement("select id, title, message from Polls where id=?")) {
			pst.setLong(1, id);
			try(ResultSet rset = pst.executeQuery()) {
				while(rset != null && rset.next()) {
					poll = new Polls();
					poll.setId(rset.getLong(1));
					poll.setTitle(rset.getString(2));
					poll.setMessage(rset.getString(3));
				}
			}
		} catch(SQLException e) {
			throw new DAOException("Error while fetching data.");
		}

		return poll;
	}

	@Override
	public PollOptions getPollOptionById(long id) throws DAOException {
		PollOptions pollOption = null;
		Connection con = SQLConnectionProvider.getConnection();
//		                                                          1       2           3         4          5           6
		try(PreparedStatement pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount, dislikeCount from PollOptions where id=?")) {
			pst.setLong(1, id);
			try(ResultSet rset = pst.executeQuery()) {
				while(rset != null && rset.next()) {
					pollOption = new PollOptions();
					pollOption.setId(rset.getLong(1));
					pollOption.setOptionTitle(rset.getString(2));
					pollOption.setOptionLink(rset.getString(3));
					pollOption.setPollID(rset.getLong(4));
					pollOption.setVotesCount(rset.getLong(5));
					pollOption.setDislikeCount(rset.getLong(6));
				}
			}
		} catch(SQLException e) {
			throw new DAOException("Error while fetching data.");
		}

		return pollOption;
	}

	@Override
	public List<PollOptions> filterPollOtionsByPollId(long id) throws DAOException {
		LinkedList<PollOptions> pollOptions = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
//		                                                          1       2           3         4          5    
		try(PreparedStatement pst = con.prepareStatement("select id, optionTitle, optionLink, pollID, votesCount, dislikeCount from PollOptions where pollID=? order by id")) {
			pst.setLong(1, id);
			try(ResultSet rset = pst.executeQuery()) {
				while(rset != null && rset.next()) {
					PollOptions pollOption = new PollOptions();
					pollOption.setId(rset.getLong(1));
					pollOption.setOptionTitle(rset.getString(2));
					pollOption.setOptionLink(rset.getString(3));
					pollOption.setPollID(rset.getLong(4));
					pollOption.setVotesCount(rset.getLong(5));
					pollOption.setDislikeCount(rset.getLong(6));

					pollOptions.add(pollOption);
				}
			}
		} catch(SQLException e) {
			throw new DAOException("Error while fetching data.");
		}

		return pollOptions;
	}

	@Override
	public void changeVoteNumberForPollOption(long id) {
		Connection con = SQLConnectionProvider.getConnection();
		try(PreparedStatement pst = con.prepareStatement("update PollOptions set votesCount= votesCount + 1 where id=?")) {
			pst.setLong(1, id);
			pst.executeUpdate();
		} catch(SQLException e) {
			throw new DAOException();
		}
	}

	@Override
	public void changeDislikeNumberForPollOption(long id) {
		Connection con = SQLConnectionProvider.getConnection();
		try(PreparedStatement pst = con.prepareStatement("update PollOptions set dislikeCount= dislikeCount +1 where id=?")) {
			pst.setLong(1, id);
			pst.executeUpdate();
		} catch(SQLException e) {
			throw new DAOException();
		}
	}

}
