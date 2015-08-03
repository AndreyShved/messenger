package ua.edu.sumdu.j2se.group5.messenger.server.model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * This class processes messages from client and generates an answer.
 * Holds connection and supports related methods. 
 *
 */
public class ServerReturn implements Runnable {
    private static Logger log = Logger.getLogger(ServerReturn.class);
    Socket socket;
	String message = "";
    PrintWriter PW;
    private DocumentBuilder docBuilder;
	ArrayList<MessageModel> messages = new ArrayList<>();

    
    public ServerReturn(Socket X) {
        this.socket = X;
        DocumentBuilderFactory docBuildFactor = DocumentBuilderFactory.newInstance();
    	try {
			docBuilder = docBuildFactor.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			log.error(e);
		}
    }

	public ConnectionModel getConnectionModel() {
		for (int i = 0; i < Server.connections.size(); i++) {
			Socket currentSocket = Server.connections.get(i).getSocket();
			if (currentSocket.getPort() == socket.getPort()) {
				return Server.connections.get(i);
			}
		}
		return null;
	}
    
    public boolean checkConnection() {
		if (!socket.isConnected() || socket.isClosed()) {
			for (int i = 0; i < Server.connections.size(); i++) {
				Socket currentSocket = Server.connections.get(i).getSocket();
				if (currentSocket.getPort() == socket.getPort()) {
					Server.connections.remove(i);
				}
			}
			for (ConnectionModel tempSocket : Server.connections) {
				try {
					OutputStream tempOutputStream = tempSocket.getSocket().getOutputStream();
					synchronized (tempOutputStream) {
						PrintWriter tempOut;
						tempOut = new PrintWriter(tempOutputStream);
						tempOut.flush();
						//log.info(tempSocket.getSocket().getLocalAddress().getHostName() + " disconnected!");
					}
				} catch (IOException e) {
					log.error("Exception occurred while opening I/O stream" + e.getMessage());
				}
			}
			return false;
		}
		return true;
    }

    public void run() {
		Scanner in = null;
		try {
			in = new Scanner(socket.getInputStream());
		} catch (IOException e1) {
			log.error(e1);
		}
		while (checkConnection() && (in != null && in.hasNext())) {
			ConnectionModel connectionModel = getConnectionModel();
			MessageModel messageModel = new MessageModel();
			messageModel.setUser( connectionModel.getName() );
			messageModel.setMessage(in.nextLine());
			try {
				Document doc = docBuilder.parse(new ByteArrayInputStream(messageModel.getMessage().getBytes(StandardCharsets.UTF_8)));
				if(Boolean.parseBoolean(doc.getDocumentElement().getAttribute("m"))) {
					NodeList messageList = doc.getElementsByTagName("message");
					messageModel.setMessage((messageList.item(0)).getTextContent());
					messageModel.setDate(new Date());
					message = messageModel.getUser() + ": " + (messageList.item(0)).getTextContent();

				} else {
					messageModel.setMessage("");
				}
				if(Boolean.parseBoolean(doc.getDocumentElement().getAttribute("d"))) {
					Iterator<ConnectionModel> it = Server.connections.iterator();
					while(it.hasNext()) {
						ConnectionModel conn = it.next();
						if (conn.getSocket().equals(socket)) {
							it.remove();
							Server.removeUser(conn);
						}
					}
					try {
						socket.close();
					} catch (IOException e) {
						log.error(e);
					}
					message += messageModel.getUser() + " - Disconnected";
				}
			} catch (SAXException | IOException e) {
				log.error(e);
			}
			messages.add(messageModel);
			log.info("Client: " + messageModel.getMessage());

			for (ConnectionModel tempSocket : Server.connections) {
				OutputStream tempOutputStream;
				try {
					tempOutputStream = tempSocket.getSocket().getOutputStream();
					synchronized (tempOutputStream) {
						PW = new PrintWriter(tempOutputStream);
						PW.println(Constants.MESSAGE_1 + message + Constants.MESSAGE_2);
						PW.flush();
					}
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
    }
}
