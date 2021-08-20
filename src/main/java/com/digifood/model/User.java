package com.digifood.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.digifood.DTO.AuditModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class User extends AuditModel implements Serializable{
	

    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private boolean enabled;
    
    private String fileName;
    private String fileType; 
    
    @JsonIgnore
    @Lob
    private byte[] file;
    
//    @JsonIgnore
//    @OneToOne(mappedBy = "user")
//    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Waiter waiter;
    
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Cook cook;
    
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Cashier cashier;
    
    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "user")
    private Customer customer;
    
    @JsonIgnore
    @OneToMany(mappedBy = "actionBy")
    private List<Notification> actionByNotifications;
    
    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<Notification> receivingNotifications;
    
    public User(){
    	
    }
    
    
	public User(String username, String password, String firstName, String lastName, String role, boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.enabled = enabled;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public Waiter getWaiter() {
		return waiter;
	}


	public void setWaiter(Waiter waiter) {
		this.waiter = waiter;
	}


	public Cook getCook() {
		return cook;
	}


	public void setCook(Cook cook) {
		this.cook = cook;
	}


	public Cashier getCashier() {
		return cashier;
	}


	public void setCashier(Cashier cashier) {
		this.cashier = cashier;
	}


	public Customer getCustomer() {
		return customer;
	}


	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public byte[] getFile() {
		return file;
	}


	public void setFile(byte[] file) {
		this.file = file;
	}


	public List<Notification> getActionByNotifications() {
		return actionByNotifications;
	}


	public void setActionByNotifications(List<Notification> actionByNotifications) {
		this.actionByNotifications = actionByNotifications;
	}


	public List<Notification> getReceivingNotifications() {
		return receivingNotifications;
	}


	public void setReceivingNotifications(List<Notification> receivingNotifications) {
		this.receivingNotifications = receivingNotifications;
	}

	
	
    
}
