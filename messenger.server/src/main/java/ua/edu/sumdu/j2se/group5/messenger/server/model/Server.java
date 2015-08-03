package ua.edu.sumdu.j2se.group5.messenger.server.model;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * Is an  implementation of server logic.
 */
public class Server {

    private static Logger log = Logger.getLogger(Server.class);
    public static ArrayList<ConnectionModel> connections = new ArrayList<>();
    public static ConnectionModel connectionModel;

	public static LinkedList<Thread> serverThreads = new LinkedList<Thread>();


    /**
     * Adds new user
     *
     * @param socket A <b>Socket</b> related to connection with
     *               specific user.
     */
    public static void addUser(Socket socket) {
        Scanner in;
        try {
            in = new Scanner(socket.getInputStream());

            String name = in.nextLine();
            log.info("User " + name + " connected");
            connectionModel = new ConnectionModel(socket, name);
            connections.add(connectionModel);
            sendCurrentUserList();

        } catch (IOException e) {
            log.error("Exception occurred while opening I/O stream " + e.getMessage());
        }
    }

    /**
     * Removes specified user
     *
     * @param user A <b>String</b> of user name.
     */
    public static void removeUser(ConnectionModel user) throws IOException {
        boolean t = connections.remove(user);
        if (t) log.info("User " + user.getName() + " disconnected");

        sendCurrentUserList();
        for (ConnectionModel conn : connections) {
            log.info(conn.getName());
        }
    }

    /**
     * Sending list of currently connected users
     */
    public static synchronized void sendCurrentUserList() {
        StringBuilder sb = new StringBuilder();
        for (ConnectionModel connection : connections) {
            sb.append("<string>").append(connection.getName()).append("</string>");
        }
        for (ConnectionModel tempSocket : connections) {
            PrintWriter PW;
            try {
                synchronized (tempSocket.getSocket().getOutputStream()) {
                    PW = new PrintWriter(tempSocket.getSocket().getOutputStream());
                    for (int i = 0; i < connections.size(); i++) {
                        PW.println(Constants.CURRENT_USERS_1 + sb + Constants.CURRENT_USERS_2);
                        PW.flush();
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }
}