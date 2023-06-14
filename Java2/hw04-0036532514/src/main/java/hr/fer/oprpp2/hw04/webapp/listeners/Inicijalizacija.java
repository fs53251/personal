package hr.fer.oprpp2.hw04.webapp.listeners;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.oprpp2.hw04.dao.DAOException;

@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		try(InputStream is = sce.getServletContext().getResourceAsStream("/WEB-INF/dbsettings.properties")) {
			Properties konfiguracija = new Properties();
			konfiguracija.load(is);

			String connectionURL = "";
			String host = konfiguracija.getProperty("host");
			String port = konfiguracija.getProperty("port");
			String name = konfiguracija.getProperty("name");
			if(host == null || port == null || name == null) {
				throw new DAOException();
			}

			connectionURL = "jdbc:derby://" + host + ":" + port + "/" + name;
			ComboPooledDataSource cpds = new ComboPooledDataSource();

			try {
				cpds.setDriverClass(konfiguracija.getProperty("driver"));
			} catch(PropertyVetoException e) {
				e.printStackTrace();
			}

			cpds.setJdbcUrl(connectionURL);

			cpds.setUser(konfiguracija.getProperty("user"));
			cpds.setPassword(konfiguracija.getProperty("password"));
			cpds.setInitialPoolSize(5);
			cpds.setMinPoolSize(5);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(20);

			sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

			try {
				populateData(cpds);
			} catch(SQLException e1) {
				e1.printStackTrace();
			}
		} catch(IOException e2) {
			e2.printStackTrace();
		}

	}

	private void populateData(ComboPooledDataSource cpds) throws SQLException {
		Connection con = null;
		try {
			con = cpds.getConnection();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Dohvatio konekciju.");
		DatabaseMetaData metaData = con.getMetaData();
		boolean poll = false;
		boolean pollOptions = false;

		ResultSet tables = metaData.getTables(null, null, null, new String[] { "TABLE" });
		while(tables.next()) {
			if(tables.getString("Table_NAME").equals("POLLS")) {
				poll = true;
			}

			if(tables.getString("Table_NAME").equals("POLLOPTIONS")) {
				pollOptions = true;
			}
		}

		if(!poll) {
			String query = "CREATE TABLE Polls\r\n" + "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n" + "title VARCHAR(150) NOT NULL,\r\n" + "message CLOB(2048) NOT NULL\r\n" + ")";
			createTable(con, query);
			System.out.println("Table created successfully.");
		}

		if(!pollOptions) {
			String query = "CREATE TABLE PollOptions\r\n" + "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n" + "optionTitle VARCHAR(100) NOT NULL,\r\n" + "optionLink VARCHAR(150) NOT NULL,\r\n" + "pollID BIGINT,\r\n" + "votesCount BIGINT,\r\n" + "dislikeCount BIGINT,\r\n" + "FOREIGN KEY (pollID) REFERENCES Polls(id)\r\n" + ")";
			createTable(con, query);
			System.out.println("Table created successfully.");
		}

		try(PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM Polls")) {
			try(ResultSet rset = pst.executeQuery()) {
				if(rset.next()) {
					//ne postoji niti jedan zapis tablice Poll
					if(rset.getInt(1) == 0) {
						System.out.println("Table Polls is empty. Inserting data.");
						unesiPodatke(con);
					}
				}
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void createTable(Connection con, String query) {
		try(var statement = con.createStatement()) {
			statement.executeUpdate(query);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private void unesiPodatke(Connection con) {
//																					1		2
		long idFirstRow = 0;
		long idSecondRow = 0;
		try(PreparedStatement pst = con.prepareStatement("insert into Polls (title, message) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, "Glasanje za omiljeni bend:");
			pst.setString(2, "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");
			pst.executeUpdate();

			try(ResultSet rset = pst.getGeneratedKeys()) {
				if(rset != null && rset.next()) {
					idFirstRow = rset.getLong(1);
					System.out.println("Unesena linija s id=" + idFirstRow + " u tablici Polls.");
				}
			}

			pst.setString(1, "Glasanje za omiljeni zbor:");
			pst.setString(2, "Od sljedećih zborova, koji Vam je zbor najdraži? Kliknite na link kako biste glasali!");
			pst.executeUpdate();

			try(ResultSet rset = pst.getGeneratedKeys()) {
				if(rset != null && rset.next()) {
					idSecondRow = rset.getLong(1);
					System.out.println("Unesena linija s id=" + idSecondRow + " u tablici Polls.");
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}

		if(idFirstRow != 0) {
//																							1           2          3        4			5
			try(PreparedStatement pst = con.prepareStatement("insert into PollOptions (optionTitle, optionLink, pollID, votesCount, dislikeCount) values (?, ?, ?, ?, ?)")) {
				pst.setString(1, "The Beatles");
				pst.setString(2, "https://www.youtube.com/watch?v=z9ypq6_5bsg");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();
				System.out.println("Uneseni Beatlesi");

				pst.setString(1, "The Platters");
				pst.setString(2, "https://www.youtube.com/watch?v=H2di83WAOhU");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();

				pst.setString(1, "The Beach Boys");
				pst.setString(2, "https://www.youtube.com/watch?v=2s4slliAtQU");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();

				pst.setString(1, "The Four Seasons");
				pst.setString(2, "https://www.youtube.com/watch?v=y8yvnqHmFds");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();

				pst.setString(1, "The Marcels");
				pst.setString(2, "https://www.youtube.com/watch?v=qoi3TH59ZEs");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();

				pst.setString(1, "The Everly Brothers");
				pst.setString(2, "https://www.youtube.com/watch?v=tbU3zdAgiX8");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();

				pst.setString(1, "The Mamas And The Papas");
				pst.setString(2, "https://www.youtube.com/watch?v=N-aK6JnyFmk");
				pst.setLong(3, idFirstRow);
				pst.setLong(4, 0);
				pst.setLong(5, 0);
				pst.executeUpdate();

			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
