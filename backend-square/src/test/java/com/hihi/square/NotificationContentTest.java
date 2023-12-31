package com.hihi.square;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationContentTest {

	@Test
	@DisplayName("알림 내용이 50자 이내일 경우 성공한다.")
	public void test1() throws Exception {
		//given

		//when, then
		// Assertions.assertDoesNotThrow(() -> new NotificationContent("hi".repeat(2)));
	}

	@Test
	@DisplayName("알림 내용이 50자 이상일 경우 실패한다.")
	public void test2() throws Exception {
		//given

		//when, then
		// Assertions.assertThrows(InvalidNotificationContentException.class,
		// 	() -> new NotificationContent("hi".repeat(31)));
	}

	@Test
	@DisplayName("알림 내용이 공백일 경우 실패한다.")
	public void test3() throws Exception {
		//given

		//when, then
		// Assertions.assertThrows(InvalidNotificationContentException.class, () -> new NotificationContent(" "));
		// Assertions.assertThrows(Exception.class, () -> new NotificationContent(" "));
	}
}