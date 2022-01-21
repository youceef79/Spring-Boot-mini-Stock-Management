package org.sid.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@SuppressWarnings("serial")
@Entity
public class User implements Serializable{
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public long getId() {
		return id;
	}

	@Id @GeneratedValue
	private long id;
	
	@NotEmpty
	private String email;
	
	@Column(nullable = false)
	@NotEmpty
	private String username;
	
	@Column(nullable = false)
	@NotEmpty
	private String password;
	
	
	public User() {
		super();
	}
	private String roles = "";
	
	public User(String usern ,String email ,String roles, String password) {
		super();
		this.username = usern;
		this.email = email;
		this.password = password;
	     this.roles = roles; 
	}
	  
	public List<String> getRoleList(){
	        if(this.roles.length() > 0){
	            return Arrays.asList(this.roles.split(","));
	        }
	        return new ArrayList<>();
	    }
	
}
