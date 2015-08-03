package ua.edu.sumdu.j2se.group5.messenger.client.model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ua.edu.sumdu.j2se.group5.messenger.client.Controller;
import ua.edu.sumdu.j2se.group5.messenger.client.view.ClientGUI;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Holds a connection between client and server.
 * Supports related methods.
 *
 */
public class Client extends Thread {

    private static Logger log = Logger.getLogger(Client.class);
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private DocumentBuilder docBuilder;
    private MessageModel messageModel = new MessageModel();
    private String[] arrayUsers;
    private String username;
    private boolean connected;
    private Controller controller;
    private final String DISCON_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <query m=\"true\" d=\"true\"> <message>";
    private final String DISCON_2 = "</message> </query>";
    
    public boolean isConnected() {
    	return connected;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getArrayUsers() {
		return arrayUsers;
	}

    private  String HOST = null;
    
    public String getHOST() {
    	return HOST;
    }

    public Client(String host, Controller contr) {
        super();
        HOST = host;
        controller = contr;
        DocumentBuilderFactory docBuildFactor = DocumentBuilderFactory.newInstance();
    	try {
			docBuilder = docBuildFactor.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.warn(e);
		}
    }

    @Override
    public void run() {
        try {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream());
                out.flush();
                while (!this.isInterrupted()) {
                    receive();
                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            log.error("Exception occurred while opening I/O stream " + e.getMessage());
        }
    }
    /**
     * Disconnects connection with server
     * 
     * @throws IOException If a problem occurs when a socket is closing.
     */
    public void disconnect() throws IOException {
        try {
        	if (out != null) {
	        	out.println(DISCON_1+ username+DISCON_2);
	            out.flush();
        	}
        } finally {
        	if(socket != null)
        		socket.close();
        }
        JOptionPane.showMessageDialog(null, "Disconnected!");

	     connected = false;
    }
    /**
     * Method is for receiving messages and query from server.
     */
    public void receive() {
        if(in.hasNext()) {
            String query = in.nextLine();
	    	log.info("trying to Build document");
            log.info(query);
	    	try {
				Document doc = docBuilder.parse(new ByteArrayInputStream(query.getBytes(StandardCharsets.UTF_8)));
				if(Boolean.parseBoolean(doc.getDocumentElement().getAttribute("m"))) {
					NodeList nodeList = doc.getElementsByTagName("message");
					controller.addLine((nodeList.item(0)).getTextContent() + "\n");
				}


				if ( Boolean.parseBoolean(doc.getDocumentElement().getAttribute("l"))) {
                    NodeList nodeList = doc.getElementsByTagName("string");
                    ArrayList<String> currentUsers = new ArrayList<>();
                    for ( int i = 0; i < nodeList.getLength(); i++) {
                        if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) nodeList.item(i);
                            String name = element.getTextContent();
                            messageModel.setUser(name);
                            currentUsers.add(name);
                        }
                    }
                    arrayUsers = currentUsers.toArray(new String[currentUsers.size()]);
                    
                    controller.setOnlineList(arrayUsers);
				}
            } catch (SAXException | IOException e) {
				log.warn(e);
			}
        }
    }
    /**
     * Sending and XML query with MESSAGE model to server
     * @param X A <b>String</b>
     */
    public void send(String X) throws ParseException {

        messageModel.setMessage(X);
        messageModel.setDate(new Date());


        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <query m=\"true\" d=\"false\"> <models>" +
                "<user>" + messageModel.getUser() + "</user>" +
                "<message>" + messageModel.getMessage() + "</message>" +
                "<date>" + messageModel.getDate() + "</date>" +
                "</models></query>");
        out.flush();
        
        log.info(messageModel.getUser());
        log.info(messageModel.getMessage());
    }
    
	
	public void connect() throws UnknownHostException, IOException {
            final int PORT = 4444;
            this.socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(username);
            out.flush();

            connected = true;
    }
}
