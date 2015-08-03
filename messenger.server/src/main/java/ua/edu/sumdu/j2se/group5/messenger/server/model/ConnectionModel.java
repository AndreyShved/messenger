package ua.edu.sumdu.j2se.group5.messenger.server.model;

import java.net.Socket;

public class ConnectionModel {

    private Socket socket;
    private String name;


    public ConnectionModel(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

}
