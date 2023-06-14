package dao;

import dao.jpa.JPADAOImpl;

public class DAOProvider {
	private static DAO dao = new JPADAOImpl();

	public static DAO getDao() {
		return dao;
	}
}