package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.slipp.domain.Comment;
import net.slipp.domain.CommentRepository;
import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.User;

@RestController
@RequestMapping("/api/questions/{questionId}/comments")
public class ApiCommentController {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private QuestionRepository questionRepository;
	
	@PostMapping("")
	public Comment create(@PathVariable Long questionId, HttpSession session, String contents){
		//로그인확인
		if(!HttpSessionUtils.isLoginUser(session)){
			return null;
		}
		//내용입력확인
		if(contents == null){
			throw new IllegalStateException("내용을 입력해주세요.");
		}
		
		User writer = HttpSessionUtils.getUserFromSession(session);
		Question question = questionRepository.findOne(questionId);
		Comment comment = new Comment(writer, question, contents);
		return commentRepository.save(comment);
//		return String.format("redirect:/questions/show/%d", questionId);
	}
}
