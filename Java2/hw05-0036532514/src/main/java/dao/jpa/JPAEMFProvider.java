package dao.jpa;

import javax.persistence.EntityManagerFactory;

public class JPAEMFProvider {

	public static EntityManagerFactory emf;

	public static EntityManagerFactory getEmf() {
		return emf;
	}

	public static void setEmf(EntityManagerFactory e) {
		emf = e;
	}
}
