package com.dh.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dh.security1.config.auth.PrincipalDetails;
import com.dh.security1.model.User;
import com.dh.security1.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	//일반적인 로그인의 경우 Authentication 객체에 UserDetails객체가 담김
	@GetMapping("/test/login")
	public @ResponseBody String testLogin(Authentication authentication,
			@AuthenticationPrincipal PrincipalDetails userDetails) {
		System.out.println("/test/login-=--------------------");
		System.out.println("authentication : " + authentication.getPrincipal());
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println(principalDetails.getUsername());
		System.out.println("userDetails : " + userDetails.getUser());
		return "세션정보확인";
	}
	
	//oauth 로그인의 경우 Authentication 객체에 OAuth2User 객체가 담김
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) {
		System.out.println("/test/oauth/login-=--------------------");
		System.out.println("authentication : " + authentication.getPrincipal());
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("oAuth2 authentication : " + oAuth2User.getAttributes());
		System.out.println("oAuth2User : " + oauth.getAttributes());
		return "oAuth2 세션정보확인";
	}
	
	@GetMapping({"","/"})
	public String index() {
		return "index";
	}
	
	//일반 로그인, OAuth 로그인 전부 다 PrincipalDetails로 받을 수 있음
	//@AuthenticationPrincipal
	@GetMapping("/user")
	public @ResponseBody String user(
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println(principalDetails.getUser());
		return "user";
	}
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user.toString());
		user.setRole("ROLE_USER");
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
}
