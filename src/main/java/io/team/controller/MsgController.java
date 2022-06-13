package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.Message;
import io.team.domain.dto.MessageReceiveDTO;
import io.team.jwt.JwtManager;
import io.team.service.logic.user.MsgServiceLogic;
import io.team.service.logic.user.UserServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MsgController {

	private final JwtManager jwtManager;
	private final MsgServiceLogic msgServiceLogic;
	private final UserServiceLogic userServiceLogic;

	@GetMapping("/messages/receive")
	public ResponseEntity<?> getReceiveMsgList(Pageable pageable, HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		HashMap<String, Object> result = new HashMap<>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			ArrayList<Message> messages = msgServiceLogic.findByReceiverId(mem_id);

			HashMap<Integer, String> memidNicknameHash = new HashMap<>();

			for (Message message : messages) {

				if (!memidNicknameHash.containsKey(message.getSenderId())) {
					String receiverNickname = userServiceLogic.findNicknameByMemid(message.getSenderId());
					memidNicknameHash.put(message.getSenderId(), receiverNickname);
				}
				if (!memidNicknameHash.containsKey(message.getReceiverId())) {
					String receiverNickname = userServiceLogic.findNicknameByMemid(message.getReceiverId());
					memidNicknameHash.put(message.getReceiverId(), receiverNickname);
				}
				message.setSenderNickname(memidNicknameHash.get(message.getSenderId()));
				message.setReceiverNickname(memidNicknameHash.get(message.getReceiverId()));
			}

			final int start = (int) pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), messages.size());
			final Page<Message> page = new PageImpl<>(messages.subList(start, end), pageable, messages.size());

			ArrayList<MessageReceiveDTO> messageReceiveDTOs = new ArrayList<>();

//			for (Message message : messages) {
//				messageReceiveDTOs.add(MessageReceiveDTO.messageReceiveDOTfomMessage(message));
//			}
//			final int start = (int) pageable.getOffset();
//			final int end = Math.min((start + pageable.getPageSize()), messageReceiveDTOs.size());
//			final Page<MessageReceiveDTO> page = new PageImpl<>(messageReceiveDTOs.subList(start, end), pageable,
//					messageReceiveDTOs.size());

			result.put("msg", page);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/messages/send")
	public ResponseEntity<?> getSendMsgList(Pageable pageable, HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		HashMap<String, Object> result = new HashMap<>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			ArrayList<Message> messages = msgServiceLogic.findBySenderId(mem_id);
			HashMap<Integer, String> memidNicknameHash = new HashMap<>();
			for (Message message : messages) {

				if (!memidNicknameHash.containsKey(message.getSenderId())) {
					String receiverNickname = userServiceLogic.findNicknameByMemid(message.getSenderId());
					memidNicknameHash.put(message.getSenderId(), receiverNickname);
				}
				if (!memidNicknameHash.containsKey(message.getReceiverId())) {
					String receiverNickname = userServiceLogic.findNicknameByMemid(message.getReceiverId());
					memidNicknameHash.put(message.getReceiverId(), receiverNickname);
				}
				message.setSenderNickname(memidNicknameHash.get(message.getSenderId()));
				message.setReceiverNickname(memidNicknameHash.get(message.getReceiverId()));

			}

			final int start = (int) pageable.getOffset();
			final int end = Math.min((start + pageable.getPageSize()), messages.size());
			final Page<Message> page = new PageImpl<>(messages.subList(start, end), pageable, messages.size());

//			ArrayList<MessageSendDTO> messageSendDTOs = new ArrayList<>();
//			for (Message message : messages) {
//				messageSendDTOs.add(MessageSendDTO.messageSendDOTfomMessage(message));
//			}
//			final int start = (int) pageable.getOffset();
//			final int end = Math.min((start + pageable.getPageSize()), messageSendDTOs.size());
//			final Page<MessageSendDTO> page = new PageImpl<>(messageSendDTOs.subList(start, end), pageable,
//					messageSendDTOs.size());

			result.put("msg", page);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@Transactional
	@GetMapping("/messages/{msgId}")
	public ResponseEntity<?> getMsgDetail(HttpServletRequest req, @PathVariable int msgId) {

		String token = req.getHeader("Authorization");
		HashMap<String, Object> result = new HashMap<>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			Message message = msgServiceLogic.findByMsgId(msgId);

			message.setSenderNickname(userServiceLogic.findNicknameByMemid(message.getSenderId()));
			message.setReceiverNickname(userServiceLogic.findNicknameByMemid(message.getReceiverId()));
			if (message.getReceiverDelete() == 1) {
				result.put("msg", "this message is deleted");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else if (message.getReceiverId() == mem_id) {
				// result.put("msg", MessageReceiveDTO.messageReceiveDOTfomMessage(message));
				result.put("msg", message);
				message.setIsRead(1);
				msgServiceLogic.sendMsg(message);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else if (message.getSenderId() == mem_id) {
				// result.put("msg", MessageSendDTO.messageSendDOTfomMessage(message));
				result.put("msg", message);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				result.put("msg", "not your message");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}

		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@PostMapping("/messages")
	public ResponseEntity<?> sendMsg(HttpServletRequest req, @RequestBody Message message) {

		String token = req.getHeader("Authorization");
		HashMap<String, Object> result = new HashMap<>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);

			message.setSenderId(mem_id);
			msgServiceLogic.sendMsg(message);
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@Transactional
	@DeleteMapping("/messages/{msgId}")
	public ResponseEntity<?> delet(HttpServletRequest req, @PathVariable int msgId) {

		String token = req.getHeader("Authorization");
		HashMap<String, Object> result = new HashMap<>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			Message message = msgServiceLogic.findByMsgId(msgId);
			System.out.println(message);
			if (message.getReceiverId() == mem_id) {
				msgServiceLogic.deleteReceiver(mem_id, msgId);
//				message.setReceiverDelete(1);
//				msgServiceLogic.sendMsg(message);
				result.put("msg", "OK");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else if (message.getSenderId() == mem_id) {
				msgServiceLogic.deleteSender(mem_id, msgId);
//				message.setReceiverDelete(1);
//				msgServiceLogic.sendMsg(message);
				result.put("msg", "OK");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			result.put("msg", "not your message");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}
}
