package com.quiz.detaysoftnew.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "todos")
public class TodoModel {

	public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

	public static final String[] values = { "identity", "who", "title", "explan", "success", "date" };

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "identity")
	private Long identity;

	@NotNull(message = "Who cannot be null!")
	@Pattern(regexp = "[0-9]+", message = "Unacceptable number!")
	@Size(min = 11, max = 11, message = "Who length must be 11!")
	@Column(name = "who")
	private String who;

	@Column(name = "title")
	private String title;

	@Column(name = "explan")
	private String explan;

	@NotNull(message = "Success cannot be null!")
	@Column(name = "success")
	private Boolean success;

	@DateTimeFormat(iso = ISO.DATE_TIME)
	@JsonFormat(pattern = dateFormat)
	@Column(name = "date")
	private LocalDateTime date;

	public TodoModel() {
	}

	public TodoModel(String who, String title, String explan, Boolean success, LocalDateTime date) {
		this.who = who;
		this.title = title;
		this.explan = explan;
		this.success = success;
		this.date = date;
	}

	public Long getIdentity() {
		return identity;
	}

	public void setIdentity(Long identity) {
		this.identity = identity;
	}

	public String getWho() {
		return who;
	}

	public void setWho(String who) {
		this.who = who;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExplan() {
		return explan;
	}

	public void setExplan(String explan) {
		this.explan = explan;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}
}