package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.Comment;
import net.slipp.domain.CommentRepository;
import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.User;

@Controller
@RequestMapping("/questions/{questionId}/comments")
public class CommentController {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private QuestionRepository questionRepository;
	
	@PostMapping("")
	public String create(@PathVariable Long questionId, HttpSession session, String contents){
		//로그인확인
		if(!HttpSessionUtils.isLoginUser(session)){
			return "/users/loginForm";
		}
		//내용입력확인
		if(contents == null){
			throw new IllegalStateException("내용을 입력해주세요.");
		}
		
		User writer = HttpSessionUtils.getUserFromSession(session);
		Question question = questionRepository.findOne(questionId);
		Comment comment = new Comment(writer, question, contents);
		commentRepository.save(comment);
		return String.format("redirect:/questions/show/%d", questionId);
	}
}
