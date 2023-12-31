package com.hihi.square.domain.user.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.hihi.square.domain.user.dto.response.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hihi.square.domain.dibs.respository.DibsRepository;
import com.hihi.square.domain.user.dto.request.CustomerRegisterRequestDto;
import com.hihi.square.domain.user.dto.request.CustomerUpdateRequestDto;
import com.hihi.square.domain.user.dto.request.UserChangePasswordDto;
import com.hihi.square.domain.user.dto.request.UserFindIdRequestDto;
import com.hihi.square.domain.user.dto.request.UserLoginRequestDto;
import com.hihi.square.domain.user.entity.Customer;
import com.hihi.square.domain.user.entity.EmdAddress;
import com.hihi.square.domain.user.entity.User;
import com.hihi.square.domain.user.entity.UserSocialLoginType;
import com.hihi.square.domain.user.service.CustomerService;
import com.hihi.square.domain.user.service.EmdAddressService;
import com.hihi.square.domain.user.service.UserService;
import com.hihi.square.global.common.CommonResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;
	private final CustomerService customerService;
	private final PasswordEncoder passwordEncoder;
	private final DibsRepository dibsRepository;
	private final EmdAddressService emdAddressService;

	// 내 위치 수정
	@PatchMapping("/address/{bCode}")
	public ResponseEntity updateMyAddress(Authentication authentication, @PathVariable(name = "bCode") Long bCode) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();
		// if (user instanceof Store) {
		// 	return new ResponseEntity(CommonResponseDto.builder().statusCode(400).message("ONLY_CUSTOMER").build(),
		// 		HttpStatus.BAD_REQUEST);
		// }
		userService.updateUserAddress(user, bCode);
		return new ResponseEntity(CommonResponseDto.builder().statusCode(200).message("SUCCESS").build(),
			HttpStatus.OK);
	}

	// 내 정보 보기
	@GetMapping
	public ResponseEntity viewMyInfo(Authentication authentication) {
		String uid = authentication.getName();
		Optional<User> optionalUser = userService.findByUid(uid);
		if (optionalUser.isEmpty()) {
			return new ResponseEntity(CommonResponseDto.builder().message("NOT_EXISTS_USER").statusCode(400).build(),
				HttpStatus.BAD_REQUEST);
		}
		// if (optionalUser.get() instanceof Store) {
		// 	return new ResponseEntity(CommonResponseDto.builder().message("NOT_CUSTOMER_USER").statusCode(400).build(),
		// 		HttpStatus.BAD_REQUEST);
		// }
		Customer customer = (Customer)optionalUser.get();
		UserInfoDto userInfo = userService.getMyInfo(uid);
		Integer dibs = dibsRepository.countByCustomer(customer);
		CustomerInfoResponseDto response = CustomerInfoResponseDto.builder()
			.statusCode(200)
			.info(userInfo)
			.rank(customerService.getRankName(customer.getRank()))
			.social(customer.getSocial())
			.point(customer.getPoint())
			.dibsCount(dibs)
			.build();
		return new ResponseEntity(response, HttpStatus.OK);
	}

	//회원가입
	@PostMapping
	public ResponseEntity<CommonResponseDto> singup(@RequestBody @Valid CustomerRegisterRequestDto request) {
		log.info("request : {}", request);
		Customer customer = request.toEntity();
		CommonResponseDto response = CommonResponseDto.builder()
			.statusCode(409)
			.message("ALREADY_EXISTS_UID")
			.build();
		//아이디 중복 체크
		if (userService.validateDuplicateUid(customer.getUid())) {
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
		//닉네임 중복 체크
		if (userService.validateDuplicateNickname(customer.getNickname())) {
			response.setMessage("ALREADY_EXISTS_NICKNAME");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
		// 이메일 중복 체크
		if (userService.validateDuplicateEmail(customer.getEmail())) {
			response.setMessage("ALREADY_EXISTS_EMAIL");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
		//저장
		EmdAddress mainAddress = emdAddressService.findByBCode(request.getBCode()).get();
		customer.updateMainAddress(mainAddress);
		customerService.save(customer);
		response.setStatusCode(201);
		response.setMessage("SIGNUP_SUCCESS");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	// 아이디 중복확인
	@GetMapping("/id/{id}")
	public ResponseEntity<CommonResponseDto> validateUid(@PathVariable String id) {
		CommonResponseDto response = CommonResponseDto.builder()
			.statusCode(200)
			.message("INVALID")
			.build();
		if (userService.validateDuplicateUid(id)) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.setMessage("VALID");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequestDto request) {
		CommonResponseDto fResponse = CommonResponseDto.builder().statusCode(400).build();
		Optional<User> optionalUser = userService.findByUid(request.getUid());
		// 아이디 존재하지 않음
		if (!optionalUser.isPresent()) {
			fResponse.setMessage("INVALID_UID");
			return new ResponseEntity<>(fResponse, HttpStatus.BAD_REQUEST);
		}
		// 비밀번호 틀림
		User user = optionalUser.get();
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			fResponse.setMessage("INVALID_PASSWORD");
			return new ResponseEntity<>(fResponse, HttpStatus.BAD_REQUEST);
		}
		// 접근 권한이 없음 || // 로그인 방법 상이
		if (!request.getAuthenticate().toString().equals(user.getDecriminatorValue()) || ((user instanceof Customer)
			&& !((Customer)user).getSocial().equals(
			UserSocialLoginType.DEFAULT))) {
			fResponse.setMessage("NOT_AUTHENTICATED");
			return new ResponseEntity<>(fResponse, HttpStatus.BAD_REQUEST);
		}
		// 로그인
		UserLoginResponseDto sResponse = userService.updateRefreshToken(user);

		return new ResponseEntity<>(sResponse, HttpStatus.OK);
	}

	// 아이디 찾기
	@PostMapping("/find/id")
	public ResponseEntity<?> findUserId(@RequestBody @Valid UserFindIdRequestDto request) {
		Optional<User> optionalUser = userService.findUserId(request);
		if (!optionalUser.isPresent()) {
			return new ResponseEntity<>(CommonResponseDto.builder().statusCode(400).message("INVALID").build(),
				HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(UserFindIdResponseDto.builder()
				.statusCode(200)
				.message("VALID")
				.uid(optionalUser.get().getUid())
				.build(), HttpStatus.OK);
		}
	}

	// 비밀번호 변경
	@PatchMapping("/password")
	public ResponseEntity<CommonResponseDto> changePassword(Authentication authentication,
		@RequestBody @Valid UserChangePasswordDto request) {
		String uid = authentication.getName();
		Optional<User> optionalUser = userService.findByUid(uid);
		if (!optionalUser.isPresent()) {
			return new ResponseEntity<>(
				CommonResponseDto.builder().statusCode(400).message("INVALID_ACCESS_TOKEN").build(),
				HttpStatus.BAD_REQUEST);
		}
		User user = optionalUser.get();
		if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			return new ResponseEntity<>(
				CommonResponseDto.builder().statusCode(400).message("INCORRECT_PASSWORD").build(),
				HttpStatus.BAD_REQUEST);
		}
		userService.updatePassword(uid, request.getNewPassword());
		return new ResponseEntity<>(CommonResponseDto.builder().statusCode(200).message("CHANGED_PASSWORD").build(),
			HttpStatus.OK);
	}

	// 로그아웃
	@GetMapping("/logout")
	public ResponseEntity<CommonResponseDto> logout(Authentication authentication) {
		userService.logout(authentication.getName());
		return new ResponseEntity<>(CommonResponseDto.builder().statusCode(200).message("SUCCESS_LOGOUT").build(),
			HttpStatus.OK);
	}

	// 구매자 정보 수정
	@PatchMapping
	@Transactional
	public ResponseEntity updateCustomer(Authentication authentication,
		@Valid @RequestBody CustomerUpdateRequestDto request) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();
		// 닉네임은 유니크 !
		if (!user.getNickname().equals(request.getNickname()) && userService.validateDuplicateNickname(
			request.getNickname())) {
			return new ResponseEntity<>(
				CommonResponseDto.builder().statusCode(409).message("ALREADY_EXISTS_NICKNAME").build(),
				HttpStatus.CONFLICT);
		}
		userService.updateUserInfo(uid, request);
		return new ResponseEntity(CommonResponseDto.builder().statusCode(200).message("SUCCESS").build(),
			HttpStatus.OK);
	}

	@PostMapping("/profile")
	public ResponseEntity setProfileImage(Authentication authentication, @RequestPart MultipartFile profile,
		@RequestPart MultipartFile thumb) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();
		userService.updateUserProfile(user, profile, thumb);
		return new ResponseEntity(CommonResponseDto.builder().statusCode(200).message("SUCCESS").build(),
			HttpStatus.OK);
	}

	@DeleteMapping("/profile")
	public ResponseEntity deleteProfileImage(Authentication authentication) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();
		userService.deleteUserProfile(user);
		return new ResponseEntity(CommonResponseDto.builder().statusCode(200).message("SUCCESS").build(),
			HttpStatus.OK);

	}

	// 내 등급 정보 ( 구매자)
	@GetMapping("/rank")
	public ResponseEntity<?> getRankInfo(Authentication authentication) {
		String uid = authentication.getName();
		Customer customer = (Customer)userService.findByUid(uid).get();
		CustomerRankInfoResponseDto response = customerService.getRankInfo(customer);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
