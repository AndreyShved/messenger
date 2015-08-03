package ua.edu.sumdu.j2se.group5.messenger.server.model;

import java.util.Date;

public class MessageModel {
    private String message;
    private String user;
    private Date date;

    public MessageModel() {

    }

    public MessageModel(String message, String user, Date date) {
        this.message = message;
        this.user = user;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
