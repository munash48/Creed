package com.javacreed.example.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

public class ContactsHelper {
	private static ContactsHelper INSTANCE = new ContactsHelper();
	public BasicDataSource ds;

	public static ContactsHelper getInstance() {
		return ContactsHelper.INSTANCE;

	}

	ContactsHelper() {

	}

	public List<Contact> getContacts() throws SQLException {
		final List<Contact> contacts = new ArrayList<>();
		final String sql = "SELECT * FROM contacts ORDER BY id";
		ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/test");
		ds.setUsername("root");
		ds.setPassword("");
		

		try (Connection connection = ds.getConnection()) {
			if (connection != null) {
				PreparedStatement psmt = connection.prepareStatement(sql);
				ResultSet rs = psmt.executeQuery();
				
				while (rs.next()){
					final Contact contact = new Contact();
					
					contact.setId(rs.getLong("id"));
					contact.setName(rs.getString("name"));
					contact.setContacts(rs.getString("contacts"));
					System.out.println(contact.id+" "+ contact.name+ " "+ contact.contacts);
					contacts.add(contact);
					
				}

			}

		}
		return contacts;
	}
}
