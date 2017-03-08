package com.javacreed.example.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.flyway.core.Flyway;

public class DbHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	public BasicDataSource ds;

	public DbHelper() {
		LOGGER.debug("Creating DataScource");
		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/test");
		ds.setUsername("root");
		ds.setPassword("");

		try {
			LOGGER.debug("Ececuting flyway ");
			Flyway flyway = new Flyway();
			flyway.setDataSource(ds);
			flyway.migrate();
		} catch (Exception e) {
			LOGGER.debug("   >>><<<"+ds.toString());
			LOGGER.debug("Migrating database " + e.toString());
		}

	}

	public Connection getConnectionSave(Contact c) throws SQLException {
		try (Connection connection = ds.getConnection()) {
			if (connection != null) {
				if (c.id == -1) {

					final String sql = "INSERT INTO contacts (name,contacts) VALUES (?,?)";
					try {

						PreparedStatement pstm = connection
								.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

						pstm.setString(1, c.name);
						pstm.setString(2, c.contacts);
						pstm.executeUpdate();
						
						try(ResultSet rs = pstm.getGeneratedKeys()){
							rs.next();
							c.id= rs.getLong(1);
						}

					} catch (Exception e) {
						System.out.println("failed 2: " + e.toString());
					}
				}

				else {
					final String sql = "UPDATE contacts SET NAME =?, contacts =? WHERE id = ?";
					try {

						PreparedStatement pstm = connection
								.prepareStatement(sql);

						pstm.setString(1, c.name);
						pstm.setString(2, c.contacts);
						pstm.setLong(3, c.id);
						pstm.executeUpdate();
						
						try(ResultSet rs = pstm.getGeneratedKeys()){
							rs.next();
							c.id= rs.getLong(1);
						}

					} catch (Exception e) {
						System.out.println("failed 2: " + e.toString());
					}
					
					

				}
				return connection;
			} else
				return null;
			

		} catch (SQLException ex) {
			LOGGER.debug("connection not made " + ex.toString());
		}
		return null;

	}
	
	public void getConnectionDelete(Contact c) throws SQLException{
		if (c.id !=-1){
			final String sql = "DELETE FROM contacts WHERE id =?";
			try (Connection connection = ds.getConnection(); PreparedStatement pstm = connection.prepareStatement(sql)) {
				if (connection != null) {
					
					pstm.setLong(1, c.id);
					pstm.execute();
				
					
				}
			}
			
		}
	}

	public Connection getConnectionList() throws SQLException {
		String sql2 = "SELECT * FROM contacts";
		try (Connection connection = ds.getConnection()) {
			Statement stmt = connection.createStatement();
			try (ResultSet rs = stmt.executeQuery(sql2)) {
				
				while (rs.next()) {
					LOGGER.debug(
							"  >> [{}]  {}  ({})",
							new Object[] { rs.getInt("id"),
									rs.getString("name"),
									rs.getString("contacts") });
				}
			}

		} catch (SQLException e) {
			System.out.println("Kayanga" + e.toString());
		}

		return null;

	}

	public void close() throws SQLException {
		ds.close();
		LOGGER.debug("Closing database ");
	}
	
	public void registerShutDownHook(){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}));
	}

}
