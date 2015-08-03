package ua.edu.sumdu.j2se.group5.messenger.client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ua.edu.sumdu.j2se.group5.messenger.client.Controller;

public class ClientLogInView extends JFrame {
	
	private JTextField userNameBox;
	private ClientGUI parent;
	
	public ClientGUI getParent() {
		return parent;
	}

	public ClientLogInView(Controller controller, ClientGUI parentFrame) {
		super();
		parent = parentFrame;
	    this.setTitle("What's your name? ");
	    this.setSize(400,100);
	    this.setLocation(250,200);
	    JPanel logIn = new JPanel();
	    
	    JButton enter = new JButton("Enter");
	    enter.addActionListener(controller);
	    userNameBox = new JTextField(20);
	    
	    logIn.add(new JLabel("Enter your name: "));
	    logIn.add(userNameBox);
	    logIn.add(enter);
	    this.add(logIn);

	    this.setVisible(true);
	}

	public JTextField getUserNameBox() {
		return userNameBox;
	}
}
