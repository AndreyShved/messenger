package ua.edu.sumdu.j2se.group5.messenger.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ua.edu.sumdu.j2se.group5.messenger.server.model.Server;
import ua.edu.sumdu.j2se.group5.messenger.server.model.ServerReturn;

public class ServerController {
	
	
	private static Logger log = Logger.getLogger(ServerController.class);
	private ServerView view;
	
	public static void main(String[] args) {
		ServerController controller = new ServerController();
		controller.view = new ServerView(controller);
        File propertiesFile = new File("log4j.properties");
        PropertyConfigurator.configure(propertiesFile.toString());
        
        String s;
        try {
            final int PORT = 4444;

            ServerSocket serverSocket = new ServerSocket(PORT);
            s = "Waiting for clients...";
            log.info("Waiting for clients...");
            controller.addConsoleLine(s);

            while (true) {
                Socket socket = serverSocket.accept();
                s = "Client connected from: " + socket.getLocalAddress().getHostName();

                log.info(s);
                controller.addConsoleLine(s);

                Server.addUser(socket);

                ServerReturn chat = new ServerReturn(socket);
                Thread X = new Thread(chat);
                X.start();
                Server.serverThreads.add(X);
            }
        } catch (IOException e) {
        	s = "Exception occurred while socket opening: " + e.getMessage();
            log.error(s);
            controller.addConsoleLine(s);
        }
    }
	
	public void addConsoleLine(String s) {
		String tmp = view.getTextArea().getText();
		if(tmp != null) s = tmp + "\n" + s;
		view.getTextArea().setText(s);
	}
	
	public void closeServer() {
		for(Thread thread : Server.serverThreads) {
			thread.interrupt();
		}
		Thread.currentThread().interrupt();
		System.exit(1);
	}
}
