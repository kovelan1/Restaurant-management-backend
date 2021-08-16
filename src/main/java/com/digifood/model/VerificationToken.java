package com.digifood.model;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;


@Entity
public class VerificationToken {
	
	private static final int EXPIRATION = 5;
	 
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private int token;
  
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    
    private Date expiryDate;
    
    public VerificationToken() {
		// TODO Auto-generated constructor stub
	}
   
    public VerificationToken(int token, User user) {
		
		this.token = token;
		this.user = user;
		this.expiryDate =calculateExpiryDate(EXPIRATION);
	}
    
    public VerificationToken(Long id, int token, User user, Date expiryDate) {
		
		this.id = id;
		this.token = token;
		this.user = user;
		this.expiryDate = expiryDate;
	}

	private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public static int getExpiration() {
		return EXPIRATION;
	}
    
    

}
