package com.hihi.square.domain.menu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hihi.square.domain.menu.dto.request.MenuCategoryRequestDto;
import com.hihi.square.domain.menu.dto.response.CommonResponseDto;
import com.hihi.square.domain.menu.dto.response.MenuCategoryResponseDto;
import com.hihi.square.domain.menu.entity.MenuCategory;
import com.hihi.square.domain.menu.service.MenuCategoryService;
import com.hihi.square.domain.user.entity.User;
import com.hihi.square.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/store/menucategory")
@RequiredArgsConstructor
@Slf4j
public class MenuCategoryController {
	private final MenuCategoryService menuCategoryService;
	private final UserService userService;

	@GetMapping
	public ResponseEntity<CommonResponseDto<?>> getAllMenuCategory(Authentication authentication) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();

		List<MenuCategory> menuCategoryList = menuCategoryService.findAllByUserId(user.getUsrId());
		List<MenuCategoryResponseDto> responseList = new ArrayList<>();

		for (MenuCategory menuCategory : menuCategoryList) {
			responseList.add(new MenuCategoryResponseDto(menuCategory));
		}
		log.info("responseList : {}", responseList);
		return ResponseEntity.ok(CommonResponseDto.success(responseList));
	}

	@GetMapping("/{id}")
	public ResponseEntity<CommonResponseDto<?>> getMenuCategoryById(@PathVariable Long id) {
		MenuCategory menuCategory = menuCategoryService.findById(id);
		if (menuCategory != null) {
			return ResponseEntity.ok(CommonResponseDto.success(new MenuCategoryResponseDto(menuCategory)));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<CommonResponseDto<?>> saveMenuCategory(Authentication authentication,
		@RequestBody MenuCategoryRequestDto request) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();
		request.setUser(user);

		MenuCategory menuCategory = request.toEntity();
		menuCategoryService.saveMenuCategory(menuCategory);
		return ResponseEntity.ok(CommonResponseDto.success("success"));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CommonResponseDto<?>> updateMenuCategory(
		Authentication authentication, @PathVariable Long id, @RequestBody MenuCategoryRequestDto request) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();

		request.setId(id);
		request.setUser(user);
		MenuCategory menuCategory = menuCategoryService.updateMenuCategory(request);
		return ResponseEntity.ok(CommonResponseDto.success(new MenuCategoryResponseDto(menuCategory)));
	}

	@PatchMapping("/list")
	public ResponseEntity<CommonResponseDto<?>> updateMenuCategoryList(Authentication authentication,
		@RequestBody MenuCategoryRequestDto request) {
		String uid = authentication.getName();
		User user = userService.findByUid(uid).get();
		List<MenuCategoryRequestDto> requestDtos = request.getData();
		menuCategoryService.updateMenuList(user, requestDtos);
		return ResponseEntity.ok(CommonResponseDto.success(null));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponseDto<?>> deleteMenuCategory(
		@PathVariable Long id) {
		MenuCategory menuCategory = menuCategoryService.findById(id);

		if (menuCategory == null) {
			return ResponseEntity.badRequest().build();
		}
		menuCategoryService.deleteMenuCategory(menuCategory);
		return ResponseEntity.ok(CommonResponseDto.success(new MenuCategoryResponseDto(menuCategory)));
	}
}
