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

	//검증 메소드
	private boolean hasPermission(HttpSession session, Question question){
		if (!HttpSessionUtils.isLoginUser(session)) {
			throw new IllegalStateException("로그인이 필요합니다.");
		}
		
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if(!question.isSameWriter(loginUser)){
			throw new IllegalStateException("자신이 쓴 글만 수정, 삭제가 가능합니다.");
		}
		return true;
	}
	
	@GetMapping("/form")
	public String form(HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginForm";
		}

		return "/qna/form";
	}

	@PostMapping("")
	public String creat(String title, String contents, HttpSession session) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginForm";
		}

		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		Question newQuestion = new Question(sessionUser, title, contents);
		questionRepository.save(newQuestion);
		return "redirect:/";
	}

	@GetMapping("/show/{id}")
	public String show(@PathVariable Long id, HttpSession session, Model model) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginForm";
		}

		Question questionPost = questionRepository.findOne(id);
		model.addAttribute("question", questionPost);
		return "/qna/show";
	}

	@GetMapping("/form/{id}")
	public String update(@PathVariable Long id, HttpSession session, Model model) {
		try{
			Question question = questionRepository.findOne(id);
			hasPermission(session, question);
			model.addAttribute("question", questionRepository.findOne(id));
			return "/qna/updateForm";
		} catch (IllegalStateException e){
			model.addAttribute("errorMessage", e.getMessage());
			return "/users/loginForm";
		}
		// 모델에 수정하려는 게시글 데이터 저장
	}

	@PutMapping("/{id}")
	public String updatePost(@PathVariable Long id, Question updateQuestion, HttpSession session) {
		// 로그인 여부 확인
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginForm";
		}
		// 자신의 게시글 인지 확인
		Question question = questionRepository.findOne(id);
		if (!question.isSameWriter(HttpSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("자신의 게시글만 삭제할 수 있습니다.");
		}

		question.update(updateQuestion);
		questionRepository.save(question);
		return "redirect:/questions/show/{id}";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id, HttpSession session) {
		// 로그인 여부 확인
		if (!HttpSessionUtils.isLoginUser(session)) {
			return "/users/loginForm";
		}
		// 자신의 게시글 인지 확인
		Question question = questionRepository.findOne(id);
		if (!question.isSameWriter(HttpSessionUtils.getUserFromSession(session))) {
			throw new IllegalStateException("자신의 게시글만 삭제할 수 있습니다.");
		}
		questionRepository.delete(id);
		return "redirect:/";
	}
	
	

}
