package com.hihi.square.domain.menu.repository;

import java.util.List;
import com.hihi.square.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hihi.square.domain.menu.entity.MenuCategory;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
	@Transactional
	@Modifying
	@Query(value = "select * from menu_category where usr_id = :userId", nativeQuery = true)
	List<MenuCategory> findAllByUserId(@Param("userId") Integer userId);

    List<MenuCategory> findByUser(User user);

}
