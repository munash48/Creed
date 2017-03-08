package com.javacreed.example.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.flyway.core.Flyway;

public class Contact {
	private static final Logger LOGGER = LoggerFactory.getLogger(Contact.class);
	public BasicDataSource ds;

	public long id =-1;
	public String name;
	public String contacts;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the contacts
	 */
	public String getContacts() {
		return contacts;
	}

	/**
	 */
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	
	@Override 
	public String toString(){
		final StringBuilder formatted = new StringBuilder();
		
		if (id == -1){
			formatted.append(" No id ");
		}
		else{
			formatted.append(" [").append(id).append("] ");
		}
		if (name==null){
			formatted.append(" [No Name] ");
		} else {
			formatted.append(name);
		}
		
	
		return formatted.toString();
		
	}

}
