package com.hihi.square.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hihi.square.domain.user.repository.UserRepository;
import com.hihi.square.domain.user.service.LoginService;
import com.hihi.square.global.jwt.JwtAuthenticationProcessingFilter;
import com.hihi.square.global.jwt.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final LoginService loginService;
	private final JwtService jwtService;
	private final UserRepository userRepository;

	private final String[] allowedUrls = {
		"/store", // 가게 회원 정보 등록
		"/store/business-license/*", // 사업자등록번호 중복확인
		"/user/id/*", // 회원 아이디 중복확인
		"/user/login", // 회원 로그인
		"/user/find/**", // 회원 아이디/비밀번호 찾기
		"/user" // 소비자 회원가입

	};    // 로그인 안해도 되는 URL들

	// BCryptPasswordEncoder는 Spring Security에서 제공하는 비밀번호 암호화 객체입니다.
	// Service에서 비밀번호를 암호화할 수 있도록 Bean으로 등록합니다.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//				.authorizeHttpRequests(requests -> requests.request("/*").permitAll().anyRequest().authenticated())
		// rest api만을 고려하여 기본설정 해제
		http
			.httpBasic()
			.disable()
			// 서버에 인증정보를 저장하지 않기 때문에 csrf를 사용하지 않음
			.csrf()
			.disable()
			.cors()
			.and()
			// session 기반의 인증을 하지 않기 때문에 stateless로 바꿔줌
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			// 기본 로그인페이지 없애기
			.formLogin()
			.disable()
			//인증, 권한 처리 필터 적용
			.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
			//요청에 대한 권한 설정
			.authorizeRequests()
			//1. 회원가입 모두 허용
			.antMatchers("/user/**", "/api/v1/user")
			.permitAll()
			.antMatchers(HttpMethod.DELETE, "/store/**").permitAll()
			.antMatchers(HttpMethod.POST, "/store")
			.permitAll()
			.antMatchers(HttpMethod.GET, "/review/**").permitAll()
			//2. 인가된 사용자만 허용(구매자, 판매자, 관리자)
			.antMatchers("/community/**").authenticated()
			//2. 가게에 대한 권한 설정
			.antMatchers(HttpMethod.GET, "/store", "/store/business-license/**", "/scb/store", "/store/daily/list",
				"/order/store/**", "/coupon/store/issue", "/coupon/store")
			.hasRole("UA02")
			.antMatchers(HttpMethod.POST, "/store/**", "/scb/**", "/coupon/store")
			.hasRole("UA02")
			.antMatchers(HttpMethod.PATCH, "/store/**", "/order/store-acceptance/**", "/order/store-denied/**",
				"/order/store-pickup/**",
				"/scb/**", "/coupon/store")
			.hasRole("UA02")
			.antMatchers(HttpMethod.DELETE, "/store/**", "/scb/**")
			.hasRole("UA02")
			.antMatchers(HttpMethod.GET, "/review/*").hasRole("UA02")
			//3. 회원에 대한 권한 설정
			.antMatchers("/user/address", "/review", "/order/**", "/dibs/**").hasRole("UA01")
			.antMatchers(HttpMethod.GET, "/store/daily/dibs", "/coupon/store/*")
			.hasRole("UA01")
			//4. 관리자에 대한 권한 설정
			// .antMatchers("/store/**").hasAuthority("UA02")
			// .antMatchers("/user/login", "/user", "/store", "/user/find/id").permitAll()
			.antMatchers("/**")
			.permitAll()
		// 위 3가지(로그인, 구매자/가게 회원가입)을 제외한 POST 요청을 막아둠
		// .antMatchers(HttpMethod.POST, "/**").authenticated()
		// 토큰을 활용하는 경우 모든 요청에 대해 인가에 대해서 적용
		// .authorizeHttpRequests(requests ->
		// 	requests.anyRequest().permitAll()
		// )
		;

		return http.build();
	}

	@Bean
	public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
		JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService,
			userRepository);
		return jwtAuthenticationFilter;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(loginService);
		return new ProviderManager(provider);
	}
}
