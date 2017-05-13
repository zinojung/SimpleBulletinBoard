package net.slipp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Question {
	@Id
	@GeneratedValue
	private Long id;
	
	private String writer;
	private String title;
	private String contents;
	private Date dateTime;
	
	public Question() {}
	
	public Question(String writer, String title, String contents, Date dateTime){
		super();
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.dateTime = dateTime;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
}
