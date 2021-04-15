package com.dh.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.dh.security1.config.oauth.PrincipalOauth2UserService;

@Configuration

//스프링 시큐리티 필터가 스프링 필터체인에 등록됩니다.
@EnableWebSecurity	

//secured 어노테이션 활성화
//preAuthorize, postAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)	
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()	//인증만 되면 들어갈 수 있는 주소
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")	//login주소가 호출되면 시큐리티가 낚아채서 로그인진행
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService);	
		//구글 로그인 완료 후처리 필요 
		//1. 코드받기(인증) 
		//2.엑세스토큰 받기 (권한) 
		//3. 사용자프로필 정보 조회 
		//4. 정보를 토대로 회원가입 
	}
}
