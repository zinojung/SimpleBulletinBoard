package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.Comment;
import net.slipp.domain.CommentRepository;
import net.slipp.domain.Question;
import net.slipp.domain.QuestionRepository;
import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("/questions")
public class QuestionController {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommentRepository commentRepository;
	
	private final String PERMISSION_LOGIN = "로그인이 필요합니다.";
	private final String PERMISSION_OWN = "자신이 쓴 글만 수정, 삭제가 가능합니다.";
	// 검증 메소드들
	
	private void hasPermissionLogin(HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			throw new IllegalStateException(PERMISSION_LOGIN);
		} 
	}
	
	private void hasPermissionOwn(HttpSession session, Question question){
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!question.isSameWriter(loginUser)) {
			throw new IllegalStateException(PERMISSION_OWN);
		}
	}

	@GetMapping("/form")
	public String form(HttpSession session, Model model) {
		try{
			hasPermissionLogin(session);
			return "/qna/form";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/user/login";
		}

	}

	@PostMapping("")
	public String creat(String title, String contents, HttpSession session, Model model) {
		try{
			hasPermissionLogin(session);
			User sessionUser = HttpSessionUtils.getUserFromSession(session);
			Question newQuestion = new Question(sessionUser, title, contents);
			questionRepository.save(newQuestion);
			return "redirect:/";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "/user/login";
		}
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable Long id, HttpSession session, Model model) {
		Question questionPost = questionRepository.findOne(id);
		model.addAttribute("question", questionPost);
		return "/qna/show";
	}

	@GetMapping("/form/{id}")
	public String update(@PathVariable Long id, HttpSession session, Model model) {
		try {
			hasPermissionLogin(session);
			Question question = questionRepository.findOne(id);
			hasPermissionOwn(session, question);
			model.addAttribute("question", questionRepository.findOne(id));
			return "/qna/updateForm";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			if(e.getMessage().equals(PERMISSION_LOGIN)){
				return "/user/login";
			} else{
				return "/qna/show";
						//String.format("redirect:/questions/show/%d", id);
			}
		}
	}

	@PutMapping("/{id}")
	public String updatePost(@PathVariable Long id, Question updateQuestion, HttpSession session, Model model) {
		try {
			hasPermissionLogin(session);
			Question question = questionRepository.findOne(id);
			hasPermissionOwn(session, question);
			question.update(updateQuestion);
			questionRepository.save(question);
			return String.format("redirect:/questions/%d", id);
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			if(e.getMessage().equals(PERMISSION_LOGIN)){
				return "/user/login";
			} else{
				return String.format("redirect:/questions/show/%d", id);
			}
		}
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session, Model model) {
		try {
			hasPermissionLogin(session);
			Question question = questionRepository.findOne(id);
			hasPermissionOwn(session, question);
			questionRepository.delete(id);
			return "redirect:/";
		} catch (IllegalStateException e) {
			model.addAttribute("errorMessage", e.getMessage());
			if(e.getMessage().equals(PERMISSION_LOGIN)){
				return "/user/login";
			} else{
				return "/qna/show";
				//return String.format("redirect:/questions/show/%d", id);
			}
		}
	}

}
