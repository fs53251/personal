package dao.jpa;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import dao.DAO;
import dao.DAOException;
import model.BlogComment;
import model.BlogEntry;
import model.BlogUser;
import utils.Converter;

public class JPADAOImpl implements DAO {

	//BlogUser methods
	@Override
	public BlogUser getBlogUserById(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}

	@Override
	public boolean createBlogUser(String firstName, String lastName, String nick, String email, String password) {

		//create BlogUser instance and fill data
		BlogUser blogUser = new BlogUser();

		blogUser.setFirstName(firstName);
		blogUser.setLastName(lastName);
		blogUser.setNick(nick);
		blogUser.setEmail(email);

		//password must be hashed
		String hashedPassword = hashPassword(password, "SHA-1");
		if(hashedPassword != null) {
			blogUser.setPasswordHash(hashedPassword);
		}

		//persist BlogUser instance
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(blogUser);

		try {
			em.getTransaction().commit();
		} catch(Exception e) {
			return false;
		}

		return true;
	}

	@Override
	public List<BlogUser> getAllBlogUsers() throws DAOException {
		EntityManager em = JPAEMProvider.getEntityManager();

		String query = "SELECT u FROM BlogUser u";
		return (List<BlogUser>) em.createQuery(query, BlogUser.class).getResultList();
	}

	@Override
	public BlogUser getBlogUserByNickByPassword(String nick, String password) throws DAOException {
		String query = "select user from BlogUser user where user.nick = :n";
		List<BlogUser> user = JPAEMProvider.getEntityManager().createQuery(query, BlogUser.class)
				.setParameter("n", nick)
				.getResultList();

		if(user.size() == 0) {
			return null;
		}
		BlogUser usr = user.get(0);
		if(usr.getPasswordHash() != null && usr.getPasswordHash().equals(hashPassword(password, "SHA-1"))) {
			return usr;
		}
		return null;
	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		String query = "select user from BlogUser user where user.nick = :n";

		List<BlogUser> user = JPAEMProvider.getEntityManager().createQuery(query, BlogUser.class)
				.setParameter("n", nick)
				.getResultList();
		if(user.size() == 0) {
			return null;
		}
		return user.get(0);
	}

	//BlogEntry methods
	@Override
	public BlogEntry getBlogEntryById(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	@Override
	public List<BlogEntry> getBlogEntriesByBlogUserId(Long userId) throws DAOException {
		return getBlogUserById(userId).getEntires();
	}

	@Override
	public boolean createBlogEntry(String title, String text, Long userId) {
		EntityManager em = JPAEMProvider.getEntityManager();
		System.out.println(title);
		System.out.println(text);
		System.out.println(userId);
		BlogUser user = getBlogUserById(userId);
		if(user == null) {
			return false;
		}

		BlogEntry blogEntry = new BlogEntry();
		Date dateOfCreation = new Date();
		blogEntry.setCreatedAt(dateOfCreation);
		blogEntry.setLastModifiedAt(dateOfCreation);
		blogEntry.setTitle(title);
		blogEntry.setText(text);
		blogEntry.setCreator(user);

		em.persist(blogEntry);

		user.getEntires().add(blogEntry);

		em.getTransaction().commit();

		return true;
	}

	@Override
	public BlogEntry getBlogEntryByIdByUserId(Long id, Long userId) {
		return getBlogEntryById(id);
	}

	@Override
	public BlogEntry editBlogEntry(String title, String text, Long userId, Long entryId) {

		BlogUser user = getBlogUserById(userId);
		if(user == null) {
			return null;
		}

		BlogEntry blogEntry = getBlogEntryByIdByUserId(entryId, userId);
		user.getEntires().remove(blogEntry);
		blogEntry.setTitle(title);
		blogEntry.setText(text);

		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(blogEntry);

		user.getEntires().add(blogEntry);

		em.getTransaction().commit();

		return blogEntry;
	}

	@Override
	public BlogEntry getBlogComment(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
	}

	private String hashPassword(String password, String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = messageDigest.digest();

			return Converter.bytesToHex(bytes);
		} catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean createBlogComment(String email, String message, Long entryId, Long userId) {
		BlogComment blogComment = new BlogComment();

		blogComment.setMessage(message);
		blogComment.setUsersEmail(email);
		Date dateOfCreation = new Date();
		blogComment.setPostedOn(dateOfCreation);

		BlogEntry blogEntry = getBlogEntryByIdByUserId(entryId, userId);
		if(blogEntry == null) {
			return false;
		}
		blogComment.setBlogEntry(blogEntry);

		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(blogComment);
		blogEntry.getComments().add(blogComment);

		em.getTransaction().commit();

		return true;
	}

}