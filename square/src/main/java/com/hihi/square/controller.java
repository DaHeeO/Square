package com.hihi.square;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
	@GetMapping
	public ResponseEntity<String> test(){
		System.out.println("들어왔다");
		System.out.println("다시 실행");
		return new ResponseEntity<>("성공", HttpStatus.OK);
	}
}
