package hr.fer.oprpp2.hw04.dao;

import java.sql.Connection;

public class SQLConnectionProvider {
	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	public static void setConnection(Connection con) {
		if(con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	public static Connection getConnection() {
		return connections.get();
	}
}
