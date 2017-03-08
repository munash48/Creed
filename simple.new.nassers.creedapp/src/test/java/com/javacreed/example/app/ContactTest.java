package com.javacreed.example.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.*;

import com.googlecode.flyway.core.Flyway;

public class ContactTest {
	DbHelper Helper2 = new DbHelper();
	public BasicDataSource ds;

	public ContactTest() {

		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/test");
		ds.setUsername("root");
		ds.setPassword("");

		try {

			Flyway flyway = new Flyway();
			flyway.setDataSource(ds);
			flyway.migrate();
		} catch (Exception e) {

		}

	}

	@Before
	public void init() throws SQLException {
		try (Connection connection = ds.getConnection()) {
			Statement stmt = connection.createStatement();

			stmt.execute("TRUNCATE TABLE contacts");
			System.out.println("Truncating the database ");

		}

	}

	@After
	public void close() throws SQLException {
		Helper2.close();
	}

	@Test
	public void testSave() throws SQLException {

		Contact c = new Contact();
		c.setName("Robert Muyaye");
		c.setContacts("Robertmuyaye@gmail.com");

		Helper2.getConnectionSave(c);

		// TODO Auto-generated method stub
		try (Connection connection = ds.getConnection()) {
			Statement stmt = connection.createStatement();
			try (ResultSet rs = stmt
					.executeQuery("SELECT COUNT(*) FROM contacts")) {
				Assert.assertTrue("Count should be greater than one", rs.next());
				Assert.assertEquals(1L, rs.getLong(1));

			}

		}
	}
}
