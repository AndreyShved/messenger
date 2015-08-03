package ua.edu.sumdu.j2se.group5.messenger.client.view;

import org.apache.log4j.Logger;

import ua.edu.sumdu.j2se.group5.messenger.client.Controller;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Provides a GUI for user interaction with the client application.
 */
public class ClientGUI extends JFrame {

    private static Logger log = Logger.getLogger(ClientGUI.class);
   // public static String username = "Anonymous";
    //private Controller controller;
    private JTextField messageTF = new JTextField(20);
    private JButton send = new JButton();
    private JLabel loggedInAsBox = new JLabel();
    private JButton disconnect = new JButton();
    private JButton connect = new JButton();
    private JScrollPane scrollOnline = new JScrollPane();
    private JTextArea conversationTA = new JTextArea();
    
    public JTextArea getConversationTA() {
		return conversationTA;
	}

	public JScrollPane getScrollOnline() {
		return scrollOnline;
	}

	public JButton getDisconnectButton() {
		return disconnect;
	}

	public JButton getConnectButton() {
		return connect;
	}

	public JLabel getLoggedInAsBox() {
		return loggedInAsBox;
	}

	public JButton getSendButton() {
		return send;
	}

	public JTextField getMessageTF() {
		return messageTF;
	}

    
    public ClientGUI(final Controller controller) {
    	super();
    	this.setTitle(" Chat Box");
        this.setSize(450,500);
        this.setLocation(220,180);
        this.setResizable(false);
        this.setBackground(new Color(255, 255, 255));
    	this.setSize(500,320);
    	this.setLayout(null);

        JButton about = new JButton("About");
        JButton help = new JButton();
        JLabel message = new JLabel("Message: ");
        JLabel conversation = new JLabel();
        JScrollPane conversationSP = new JScrollPane();
        JLabel online = new JLabel();
        JLabel loggedAs = new JLabel();

        JFrame logInWindow = new JFrame();
        JTextField userNameBox = new JTextField(20);

        send.setBackground(new Color(0, 0, 255));
        send.setForeground(new Color(255, 255, 255));
        send.setText("Send");
        this.getContentPane().add(send);
        send.setBounds(250, 40, 81, 25);

        disconnect.setBackground(new Color(0, 0, 255));
        disconnect.setForeground(new Color(255, 255, 255));
        disconnect.setText("Disconnect");
        this.getContentPane().add(disconnect);
        disconnect.setBounds(10, 40, 110, 25);

        connect.setBackground(new Color(0, 0, 255));
        connect.setForeground(new Color(255, 255, 255));
        connect.setText("Connect");
        connect.setToolTipText("");
        this.getContentPane().add(connect);
        connect.setBounds(130, 40, 110, 25);

        about.setBackground(new Color(0, 0, 255));
        about.setForeground(new Color(255, 255, 255));
        about.setText("About");
        this.getContentPane().add(about);
        about.setBounds(340, 40, 75, 25);

        message.setText("Message: ");
        this.getContentPane().add(message);
        message.setBounds(10, 10, 60, 20);

        messageTF.setForeground(new Color(0, 0, 255));
        messageTF.requestFocus();
        this.getContentPane().add(messageTF);
        messageTF.setBounds(70, 4, 260, 30);

        conversation.setHorizontalAlignment(SwingConstants.CENTER);
        conversation.setText("Conversation");
        this.getContentPane().add(conversation);
        conversation.setBounds(100, 70, 140, 16);

        conversationTA.setColumns(20);
        conversationTA.setFont(new java.awt.Font("Tahoma", 0, 12));
        conversationTA.setForeground(new Color(0, 0, 255));
        conversationTA.setLineWrap(true);
        conversationTA.setRows(5);
        conversationTA.setEditable(false);

        conversationSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        conversationSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        conversationSP.setViewportView(conversationTA);
        this.getContentPane().add(conversationSP);
        conversationSP.setBounds(10, 90, 330, 180);

        online.setHorizontalAlignment(SwingConstants.CENTER);
        online.setText("Currently connected");
        online.setToolTipText("");
        this.getContentPane().add(online);
        online.setBounds(350, 70, 130, 16);

    	JList onlineList = new JList();
        onlineList.setForeground(new Color(0, 0, 255));

        scrollOnline.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollOnline.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollOnline.setViewportView(onlineList);
        this.getContentPane().add(scrollOnline);
        scrollOnline.setBounds(350, 90, 130, 180);

        loggedAs.setFont(new java.awt.Font("Tahoma", 0, 12));
        loggedAs.setText("Currently logged in as");
        this.getContentPane().add(loggedAs);
        loggedAs.setBounds(348,0, 140, 15);

        loggedInAsBox.setHorizontalAlignment(SwingConstants.CENTER);
        loggedInAsBox.setFont(new java.awt.Font("Tahoma", 0, 12));
        loggedInAsBox.setForeground(new Color(255, 0, 0));
        loggedInAsBox.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.getContentPane().add(loggedInAsBox);
        loggedInAsBox.setBounds(340, 17, 150, 20);
        
        send.addActionListener(controller);
        connect.addActionListener(controller);
        disconnect.addActionListener(controller);
       // help.addActionListener(controller);
        about.addActionListener(controller);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

     
        
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            //    try {
                	controller.closeApp();
                   // if (chatClient != null) {
                   //     controller.actionDisconnect();
                   // }
                  //  logInWindow.dispose();
            //    } catch (IOException e1) {
                //    log.error("Exception while disconnecting" + e1.getMessage());
             //   }
            }
        });
        
        
        

        send.setEnabled(false);
        disconnect.setEnabled(false);
        connect.setEnabled(true);
    }


	//public static JFrame mainWindow = new JFrame();



//    private static JButton about = new JButton("About");
  //  private static JButton help = new JButton();
  //  private static JLabel message = new JLabel("Message: ");
 //   private static JLabel conversation = new JLabel();
  //  private static JScrollPane conversationSP = new JScrollPane();
 //   private static JLabel online = new JLabel();
 
 //   private static JLabel loggedAs = new JLabel();


  //  private static JFrame logInWindow = new JFrame();
  


}
