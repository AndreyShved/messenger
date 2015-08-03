package ua.edu.sumdu.j2se.group5.messenger.server;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;

public class ServerView extends JFrame {
	private JTextArea textArea;
	private static Logger log = Logger.getLogger(ServerView.class);
	
	public JTextArea getTextArea() {
		return textArea;
	}

	public ServerView(final ServerController contr) {
		super();
		this.getContentPane().setLayout(null);
		this.getContentPane().setPreferredSize(new Dimension(450,400));
		
		textArea = new JTextArea();
		textArea.setText("");
		textArea.setBounds(10, 11, 424, 278);
		getContentPane().add(textArea);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	ServerView.this.dispose();
            	contr.closeServer();
            }
        });
		
		this.setVisible(true);
		this.pack();
	}
}
