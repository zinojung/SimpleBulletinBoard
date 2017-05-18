package net.slipp.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Comment {
	@Id
	@GeneratedValue
	@JsonProperty
	private Long id;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_writer"))
	@JsonProperty
	private User writer;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_question"))
	@JsonProperty
	private Question question;
	@Lob
	@JsonProperty
	private String contents;
	@JsonProperty
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", writer=" + writer + ", question=" + question + ", contents=" + contents
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Comment() {}
	
	public Comment(User writer, Question question, String contents){
		super();
		this.writer = writer;
		this.question = question;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
	}
	
	public void update(Comment comment){
		this.writer = comment.writer;
		this.contents = comment.contents;
		this.updateDate = LocalDateTime.now();
	}
	
	public String getFormattedCreateDate(){
		if(createDate == null) {
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public boolean isSameWriter(User loginUser){
		return this.writer.equals(loginUser);
	}
	
	
	
}
