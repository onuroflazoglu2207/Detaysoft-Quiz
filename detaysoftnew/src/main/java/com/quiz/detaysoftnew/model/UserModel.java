package com.quiz.detaysoftnew.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class UserModel {

	public static final String[] values = { "identity", "name", "phone", "email", "password" };

	@Id
	@NotNull(message = "Identity cannot be null!")
	@Min(value = 10000000000L, message = "Identity length must be 11!")
	@Max(value = 99999999999L, message = "Identity length must be 11!")
	@Column(name = "identity")
	private Long identity;

	@NotBlank(message = "Name cannot be blank!")
	@Size(max = 50, message = "Name length cannot bigger then 50!")
	@Column(name = "name")
	private String name;

	@Pattern(regexp = "([0-9]{3,15})|(([+-]{1,1})([0-9]{3,15}))", message = "Phone number unacepptable!")
	@Size(min = 3, max = 16, message = "Phone length must be 3-16!")
	@Column(name = "phone")
	private String phone;

	@Email(message = "Unacceptable email!")
	@Size(max = 256, message = "Email length cannot bigger then 256!")
	@Column(name = "email")
	private String email;

	@NotEmpty(message = "Password cannot be empty!")
	@Size(min = 6, max = 20, message = "Password length must be 6-20!")
	@Column(name = "password")
	private String password;

	public UserModel() {
	}

	public UserModel(Long identity, String name, String phone, String email, String password) {
		this.identity = identity;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public Long getIdentity() {
		return identity;
	}

	public void setIdentity(Long identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

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
}