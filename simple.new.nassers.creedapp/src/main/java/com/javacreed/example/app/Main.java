package com.javacreed.example.app;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		DbHelper Helper2 = new DbHelper();
		Helper2.registerShutDownHook();
		
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				Application app = null;
				try {
					app = new Application();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				app.setTitle("Simple java Databse Application ");
				app.setSize(new Dimension (800, 600));
				app.setLocationRelativeTo(null);
				app.setDefaultCloseOperation(app.EXIT_ON_CLOSE);
				app.setVisible(true);
				
	/*			app.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						LOGGER.debug("done");
						try {
							Helper2.close();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}); */
				


				
			}
			
		});
		
	}

}
