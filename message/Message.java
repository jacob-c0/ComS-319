package com.example.demo.message;

import com.example.demo.user.User;
import jakarta.persistence.*;

@Entity
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    private String content;

    public Message(User sId, User rId, String content){
        this.sender = sId;
        this.receiver = rId;
        this.content = content;
    }

    public Message() {

    }

    //Getters and Setters
    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public User getSender() { return this.sender; }

    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }

    public void setReceiver(User receiver) { this.receiver = receiver; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }
}
