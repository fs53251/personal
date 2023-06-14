package dao;

import java.util.List;

import model.BlogEntry;
import model.BlogUser;

public interface DAO {

	//BlogUser methods
	public BlogUser getBlogUserById(Long id) throws DAOException;

	public boolean createBlogUser(String firstName, String lastName, String nick, String email, String password) throws DAOException;

	public List<BlogUser> getAllBlogUsers() throws DAOException;

	public BlogUser getBlogUserByNickByPassword(String nick, String password) throws DAOException;

	public BlogUser getBlogUserByNick(String nick) throws DAOException;

	//BlogEntry methods

	public BlogEntry getBlogEntryById(Long id) throws DAOException;

	public List<BlogEntry> getBlogEntriesByBlogUserId(Long userId) throws DAOException;

	public boolean createBlogEntry(String title, String text, Long userId);

	public BlogEntry getBlogEntryByIdByUserId(Long id, Long userId);

	public BlogEntry editBlogEntry(String title, String text, Long userId, Long entryId);

	//BlogComment methods

	public BlogEntry getBlogComment(Long id) throws DAOException;

	public boolean createBlogComment(String email, String message, Long entryId, Long userId);
}
