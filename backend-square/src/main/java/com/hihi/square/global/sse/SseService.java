package com.hihi.square.global.sse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.hihi.square.domain.dm.dto.response.DMResponseDto;
import com.hihi.square.domain.dm.entity.DM;
import com.hihi.square.domain.user.entity.User;
import com.hihi.square.global.sse.dto.NotificationResponseDto;
import com.hihi.square.global.sse.entity.Notification;
import com.hihi.square.global.sse.entity.NotificationType;
import com.hihi.square.global.sse.repository.EmitterRepository;
import com.hihi.square.global.sse.repository.EmitterRepositoryImpl;
import com.hihi.square.global.sse.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {
	private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();
	private final NotificationRepository notificationRepository;

	private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

	public List<Notification> findAll() {
		List<Notification> notificationList = notificationRepository.findAll();
		return notificationList;
	}

	public SseEmitter subscribe(Long memberId, String lastEventId) {
		String emitterId = makeTimeIncludeId(memberId);
		SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
		emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
		emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

		// 503 에러를 방지하기 위한 더미 이벤트 전송
		String eventId = makeTimeIncludeId(memberId);

		Notification notification = createNotification(null, NotificationType.READY,
			"eventStream Created. [userId=" + memberId + "]", "");

		sendNotification(emitter, eventId, emitterId, "message", new NotificationResponseDto(notification));

		// sendNotification(emitter, eventId, emitterId, "message",
		// 	"eventStream Created. [userId=" + memberId + "]");

		// 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
		if (hasLostData(lastEventId)) {
			sendLostData(lastEventId, memberId, emitterId, emitter);
		}

		return emitter;
	}

	private String makeTimeIncludeId(Long memberId) {
		return memberId + "_" + System.currentTimeMillis();
	}

	private void sendNotification(SseEmitter emitter, String eventId, String emitterId, String type, Object data) {
		try {
			emitter.send(SseEmitter.event()
				.id(eventId)
				.name(type)    //front에서 onmessage로 받기 때문에 event 명 설정
				.data(data));
		} catch (IOException exception) {
			emitterRepository.deleteById(emitterId);
		}
	}

	private boolean hasLostData(String lastEventId) {
		return !lastEventId.isEmpty();
	}

	private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
		Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(
			String.valueOf(memberId));
		eventCaches.entrySet().stream()
			.filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
			.forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, "message", entry.getValue()));
	}

	// @TransactionalEventListener
	public void send(User receiver, NotificationType notificationType, String type, String content, String data) {
		Notification notification = notificationRepository.save(
			createNotification(receiver, notificationType, content, data));

		String receiverId = String.valueOf(receiver.getUsrId());
		String eventId = receiverId + "_" + System.currentTimeMillis();
		Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
		emitters.forEach(
			(key, emitter) -> {
				emitterRepository.saveEventCache(key, notification);
				sendNotification(emitter, eventId, key, type, new NotificationResponseDto(notification));
			}
		);
	}

	public void sendMessage(User receiver, String type, String content,
		String data, DM dm) {
		String receiverId = String.valueOf(receiver.getUsrId());
		String eventId = receiverId + "_" + System.currentTimeMillis();
		Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);

		//응답 쪽지 객체 생성
		DMResponseDto dmResponseDto = DMResponseDto.builder()
			.id(dm.getId())
			.content(dm.getContent())
			.user(data)
			.isRead(dm.getIsRead())
			.build();
		emitters.forEach(
			(key, emitter) -> {
				emitterRepository.saveEventCache(key, dm);
				sendNotification(emitter, eventId, key, type, dmResponseDto);
			}
		);
	}

	private Notification createNotification(User receiver, NotificationType notificationType, String content,
		String data) {
		return Notification.builder()
			.receiver(receiver)
			.notificationType(notificationType)
			.content(content)
			.data(data)
			.isRead(false)
			.build();
	}
}