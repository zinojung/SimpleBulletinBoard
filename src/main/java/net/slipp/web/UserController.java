package net.slipp.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.slipp.domain.User;
import net.slipp.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/login";
	}
	
	@RequestMapping("/login")
	public String login(){
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session){
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			System.out.println("Login Failure!");
			return "redirect:/users/login";
		}
		if (!user.matchPassword(password)) {
			System.out.println("Login Failure!");
			return "redirect:/users/loginForm";
		}
		
		System.out.println("Login Success!");
		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/";
	}

	@PostMapping("")
	public String create(User user){
		System.out.println("user : " + user);
		
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model){
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}
	
	@GetMapping("/form")
	public String form(){
		return "/user/form";
	}
	
	@GetMapping("/{id}/form")
	public String updateform(@PathVariable Long id, Model model, HttpSession session){
		if(HttpSessionUtils.isLoginUser(session)){
			return "redirect:/users/loginForm";
		}
	
		User sessionedUser = HttpSessionUtils.getUserFromSession(session); 
		if(!sessionedUser.matchId(id)){
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		
		model.addAttribute("user", userRepository.findOne(id)); //사용자 정보를 찾아옴
		return "/user/updateForm";
	}
	
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updateUser, HttpSession session){
		Object tempUser = session.getAttribute("sessionedUser");
		if(tempUser == null){
			return "redirect:/users/loginForm";
		}
		
		User sessionedUser = (User)tempUser;
		if(!sessionedUser.matchId(id)){
			throw new IllegalStateException("자신의 정보만 수정할 수 있습니다.");
		}
		User user = userRepository.findOne(id);
		user.update(updateUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
