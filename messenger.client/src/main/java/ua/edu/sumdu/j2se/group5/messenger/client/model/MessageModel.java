package ua.edu.sumdu.j2se.group5.messenger.client.model;

import java.util.Date;

public class MessageModel {
    private String user;
    private String message;
    private Date date;

    public MessageModel() {

    }
    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
