package ua.edu.sumdu.j2se.group5.messenger.client;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;

import ua.edu.sumdu.j2se.group5.messenger.client.model.Client;
import ua.edu.sumdu.j2se.group5.messenger.client.view.ClientGUI;
import ua.edu.sumdu.j2se.group5.messenger.client.view.ClientLogInView;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class Controller implements ActionListener {

	private static Logger log = Logger.getLogger(ClientGUI.class);
    private Client chatClient;
    private ClientGUI view;
    private ClientLogInView logInView;
    
	public static void main(String[] args) {
		Controller controller = new Controller();
		ClientGUI view = new ClientGUI(controller);
		controller.view = view;
		File propertiesFile = new File("log4j.properties");
		PropertyConfigurator.configure(propertiesFile.toString());
		File settingsFile = new File("settings.xml");
		DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder docBuilder;
		try {
			docBuilder = docBuildFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(settingsFile);
			controller.chatClient = new Client(doc.getDocumentElement().getAttribute("ip"),controller);
		} catch (Exception e) {
			log.error(e);
		}
	}

   /* private void actionHelp() {
        JOptionPane.showMessageDialog(null, "Chat Program");
    }*/

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case "About":
			actionAbout();
			break;
		case "Send":
			if(!view.getMessageTF().getText().equals("")) {
	            try {
					chatClient.send(view.getMessageTF().getText());
					view.getMessageTF().setText("");
				} catch (ParseException e1) {
					log.error(e1);
				}
	            view.getMessageTF().requestFocus();

	        }
			break;
		case "Connect":
			logInView = new ClientLogInView(this,view);
			break;
		case "Disconnect":
			try {
                actionDisconnect();
            } catch (IOException e1) {
               log.error("Exception while disconnecting" + e1.getMessage());
            }
			break;
		case "Enter":
			String name = logInView.getUserNameBox().getText();
			if(!name.equals("")) {
				try {
				actionEnterName(name,logInView.getParent());
				} catch (UnknownHostException ex) {
					log.error("Unknown Host" + ex.getMessage());
					JOptionPane.showMessageDialog(null, "connect failed");
				} catch (IOException ex) {
					log.error("Can't create socket" + ex.getMessage());
					JOptionPane.showMessageDialog(null, "connect failed");
				}
				logInView.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Please, enter your name");
			}
			break;
		}			
	}
	
	private void connect() throws UnknownHostException, IOException {
            chatClient.connect();
            chatClient.start();
    }
	
	 private void actionDisconnect() throws IOException {
	        disconnect();
	        view.getDisconnectButton().setEnabled(false);
	        view.getConnectButton().setEnabled(true);
	        view.getSendButton().setEnabled(false);
	        JList onlineList = new JList();
	        onlineList.setForeground(new Color(0, 0, 255));
	        view.getScrollOnline().setViewportView(onlineList);
	 }
	 
	 private void actionEnterName(String name,ClientGUI view) throws UnknownHostException, IOException {
	     chatClient.setUsername(name.trim());
	     connect();
	     view.getLoggedInAsBox().setText(chatClient.getUsername());
	     view.setTitle(chatClient.getUsername() + "'s ChatBox");
	            
	     view.getSendButton().setEnabled(true);
	     view.getDisconnectButton().setEnabled(true);
	     view.getConnectButton().setEnabled(false);
	 }
	 
	 public void closeApp() {
		 if(chatClient != null) {
	    	try {
	        	//controller.closeApp();
	            	//  logInWindow.dispose();
	            if (chatClient.isConnected()) {
	            	disconnect();
	            }
          
	        } catch (IOException e1) {
	            log.error("Exception while disconnecting" + e1.getMessage());
	            if (!chatClient.isInterrupted()) {
	                chatClient.interrupt();
	            }
	        }
		 }
         for(Frame frame : Frame.getFrames()) frame.dispose();
	 }
	 
	 private void disconnect() throws IOException {
		 String host = chatClient.getHOST();
		 chatClient.disconnect();
	     chatClient.interrupt();
	     chatClient = new Client(host,this);
	 }
	 
	 private void actionAbout() {
	     JOptionPane.showMessageDialog(null, "Developers: Andrey_Shved & Artem_Taranovskiy\nexample@mail for questions");
	 }
	 
	 public void setOnlineList(String[] onlineUsers) {
		 JList onlineList = new JList(onlineUsers);
	        onlineList.setForeground(new Color(0, 0, 255));
	        view.getScrollOnline().setViewportView(onlineList);
	 }
	 
	 public void addLine(String line) {
		view.getConversationTA().append(line);
	 }
}
