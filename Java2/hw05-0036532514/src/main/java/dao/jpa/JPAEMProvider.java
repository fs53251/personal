package dao.jpa;

import javax.persistence.EntityManager;

import dao.DAOException;

public class JPAEMProvider {

	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em == null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}

		return em;
	}

	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if(em == null) {
			return;
		}
		try {
			em.close();
		} catch(Exception ex) {

		}
		locals.remove();
	}

}
