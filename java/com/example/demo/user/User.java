package com.example.demo.user;

import jakarta.persistence.*;


import java.util.Set;

@Entity
@Table(name="USERS")
public class User 
{
	public static long nextID = 1;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    @Transient
    public static long sessionId = 1;
	@Column(name="username")
	private String username = "username";
	@Column(name="email")
	private String email = "email";
	@Column(name="password")
	private String password = "password";
	@Column(name="fullname")
	private String fullName = "fullname";
	@Column(name="state")
	private String state = "state";
	@Column(name="city")
	private String city = "none";
    private String image = "";

   @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<User> friends;

	public User() 
	{
		
	}
	
	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
    
    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
	
}
