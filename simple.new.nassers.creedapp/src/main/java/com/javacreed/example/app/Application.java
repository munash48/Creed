package com.javacreed.example.app;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Application extends JFrame {
	

	private static final long serialVersionUID = 13425678654324567L;
	private JList<Contact> contactsList;

	JTextField idTextField = new JTextField();
	JTextField nameTextField = new JTextField();
	JTextArea contactsTextArea = new JTextArea();

	private DefaultListModel<Contact> contactsListModel;

	private Action refreshAction;
	private Action newAction;
	private Action saveAction;
	private Action deleteAction;

	private Contact selected;

	public Application() throws SQLException {
		
		initComonents();
		initActions();
		refreshData();
	}

	private JToolBar createToolBar() {
		final JToolBar toolBar = new JToolBar();
		ImageIcon reficon = new ImageIcon(getClass().getResource(
				"/icons/Refresh.png"));

		JButton refreshb = new JButton(reficon);

		refreshb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					refreshData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		ImageIcon delicon = new ImageIcon(getClass().getResource(
				"/icons/Delete.png"));
		JButton deleteb = new JButton(delicon);
		
		deleteb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					delete();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		});
		ImageIcon newicon = new ImageIcon(getClass().getResource(
				"/icons/New.png"));
		JButton newb = new JButton(newicon);
		
		newb.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				createNew();
				
			}
			
		});
		
		ImageIcon savicon = new ImageIcon(getClass().getResource(
				"/icons/Save.png"));
		JButton saveb = new JButton(savicon);

		saveb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
					refreshData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		toolBar.add(refreshb);
		toolBar.addSeparator();
		toolBar.add(deleteb);
		toolBar.addSeparator();
		toolBar.add(newb);
		toolBar.addSeparator();

		toolBar.add(saveb);
		toolBar.addSeparator();

		toolBar.add(refreshAction);
		toolBar.addSeparator();
		toolBar.add(deleteAction);
		toolBar.addSeparator();
		toolBar.add(newAction);
		toolBar.addSeparator();
		toolBar.add(saveAction);
		toolBar.addSeparator();

		return toolBar;

	}

	private ImageIcon load(final String name) {
		return new ImageIcon(getClass().getResource("/icons/Refresh.png"));

	}

	private void refreshData() throws SQLException {

		try {
			contactsListModel.removeAllElements();

			ContactsHelper helper = new ContactsHelper();

			List<Contact> contacts = helper.getContacts();
			for (Contact contact : contacts) {
				contactsListModel.addElement(contact);

			}
		} catch (SQLException e)

		{
			JOptionPane.showMessageDialog(this,
					"Faild to refresh the contacts", "Refresh",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	private void createNew() {
		Contact contact = new Contact();
		contact.setName(" New Name");
		contact.setContacts("new Contacts");
		setSelectedContact(contact);

	}

	private void save() throws SQLException {
		if (selected != null) {
			selected.setName(nameTextField.getText());
			selected.setContacts(contactsTextArea.getText());
			try {
				DbHelper dbh = new DbHelper();
				dbh.getConnectionSave(selected);
			} catch (SQLException e) {
				System.out.println(" failed to save selected" + e.toString());
				JOptionPane.showMessageDialog(this,
						" Failed to save selected Contact", " Save Selected"
								+ JOptionPane.WARNING_MESSAGE,
						getDefaultCloseOperation(), null);
				;

			} finally {
				refreshData();
			}
		}

	}

	private void delete() throws SQLException {
		if (selected != null) {
			if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
					"Delete? "+ selected.getName(), "Delete", JOptionPane.YES_NO_OPTION)) {

				try {
					DbHelper dbh = new DbHelper();
					dbh.getConnectionDelete(selected);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this,
							" Failed Delete oparation " + " Delete Opps"
									+ JOptionPane.WARNING_MESSAGE);
				} finally{
					setSelectedContact(null);
					refreshData();
				}
			}

		}

	}

	private void initActions() {
		refreshAction = new AbstractAction("Refresh", load("Refresh")) {
			private static final long serialVersionUID = 4678987658790876678L;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					refreshData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		};

		newAction = new AbstractAction("New", load("New")) {
			private static final long serialVersionUID = 4678675785096809878L;

			@Override
			public void actionPerformed(ActionEvent e) {
				createNew();

			}

		};

		saveAction = new AbstractAction("Save", load("Save")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2467898006543L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		};

		deleteAction = new AbstractAction("Delete", load("Delete")) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 567890987654L;

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					delete();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		};

	}

	private void initComonents() {
		add(createListPane(), BorderLayout.WEST);
		add(createEditor(), BorderLayout.CENTER);
		add(createToolBar(), BorderLayout.SOUTH);

	}

	private JComponent createListPane() {
		contactsListModel = new DefaultListModel<>();

		contactsList = new JList<>(contactsListModel);

		contactsList.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							Contact selected = contactsList.getSelectedValue();
							setSelectedContact(selected);
						}

					}

				});
		return new JScrollPane(contactsList);

	}

	private void setSelectedContact(Contact selected) {
		this.selected = selected;
		if (selected == null) {
			idTextField.setText("");
			nameTextField.setText("");
			contactsTextArea.setText("");
		} else {
			idTextField.setText(String.valueOf(selected.getId()));
			nameTextField.setText(selected.getName());
			contactsTextArea.setText(selected.getContacts());
		}

	}

	private JComponent createEditor() {
		final JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("ID"), constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.weightx = 1;
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.fill = GridBagConstraints.BOTH;
		idTextField = new JTextField();
		idTextField.setEditable(false);
		panel.add(idTextField, constraints);

		// name
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Name"), constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 1;
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.fill = GridBagConstraints.BOTH;
		nameTextField = new JTextField();
		panel.add(nameTextField, constraints);

		// contacts
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(new JLabel("Contacts"), constraints);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.fill = GridBagConstraints.BOTH;
		contactsTextArea = new JTextArea();
		panel.add(new JScrollPane(contactsTextArea), constraints);

		return panel;

	}

}
