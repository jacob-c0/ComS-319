package com.example.demo.inviteRequest;
import com.example.demo.user.User;
import jakarta.persistence.*;


@Entity
public class InviteRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User invitee;
    private String type; //friend or event
    private Long gotoId; //either friends id or events id
    @ManyToOne
    private User owner;
    private String message;
    // Constructors
    public InviteRequest() {
    }

    public InviteRequest(Long inviteeId, String type, Long gotoId, String message) {
        this.type = type;
        this.gotoId = gotoId;
        this.message = message;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerId(User id) {this.owner = id;}

    public User getOwnerId(Long id) {return this.owner;}

    public User getInviteeId() {
        return invitee;
    }

    public void setInviteeId(User inviteeId) {
        this.invitee = inviteeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getGotoId() {
        return gotoId;
    }

    public void setGotoId(Long gotoId) {
        this.gotoId = gotoId;
    }

    public void setMessage(String message) {this.message = message;}

    public String getMessage(){return this.message;}
    
}
