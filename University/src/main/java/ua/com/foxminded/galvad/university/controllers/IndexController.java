package ua.com.foxminded.galvad.university.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("")
	public String showIndex() {
		return "index";
	}

}