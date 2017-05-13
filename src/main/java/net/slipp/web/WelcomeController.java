package net.slipp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.slipp.domain.QuestionRepository;

@Controller
@RequestMapping("/")
public class WelcomeController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("")
	public String welcome(Model model) {
		model.addAttribute("questions", questionRepository.findAll());
		return "index";
	}
}
