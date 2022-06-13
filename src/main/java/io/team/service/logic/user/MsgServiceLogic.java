package io.team.service.logic.user;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import io.team.Repository.MsgRepository;
import io.team.domain.Message;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MsgServiceLogic {

	private final MsgRepository msgRepository;
	public Page<Message> findByReceiverId(int receiverId, Pageable pageable) {
//		ArrayList<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
//		for (Message message : messages) {
//			messageDTOs.add(MessageDTO.messageDOTfomMessage(message));
//		}
//		int start = (int) pageable.getOffset();
//		int end = Math.min((start + pageable.getPageSize()), messageDTOs.size());
//		Page<MessageDTO> page = new PageImpl<>(messageDTOs.subList(start, end), pageable, messageDTOs.size());
		Page<Message> messages = msgRepository.findByReceiverId(receiverId, pageable);

		return messages;
	}
	
	public ArrayList<Message> findByReceiverId(int receiverId) {

		return (ArrayList<Message>) msgRepository.findByReceiverId(receiverId);
	}

	public Page<Message> findBySenderId(int senderId, Pageable pageable) {

//		ArrayList<MessageDTO> messageDTOs = new ArrayList<MessageDTO>();
//		for (Message message : messages) {
//			messageDTOs.add(MessageDTO.messageDOTfomMessage(message));
//		}
//		int start = (int) pageable.getOffset();
//		int end = Math.min((start + pageable.getPageSize()), messageDTOs.size());
//		Page<MessageDTO> page = new PageImpl<>(messageDTOs.subList(start, end), pageable, messageDTOs.size());

		return msgRepository.findBySenderId(senderId, pageable);

	}
	
	public ArrayList<Message> findBySenderId(int senderId) {
		return (ArrayList<Message>) msgRepository.findBySenderId(senderId);

	}

	public Message findByMsgId(int msgId) {
		return msgRepository.findByMsgId(msgId);
	}
	
	public int readMsgCheck(int msgId) {
		return msgRepository.msgReadCheck(msgId);
	}

	public int countNewMsg(int receiverId) {
		ArrayList<Message> messages = (ArrayList<Message>) msgRepository.findByReceiverId(receiverId);
		int newMsg = 0;
		for (Message message : messages) {
			long day = ChronoUnit.DAYS.between(message.getDatetime(), LocalDateTime.now());
			if (day < 1 && message.getIsRead() == 0) {
				newMsg++;
			}
		}
		return newMsg;
	}

	public Message sendMsg(Message message) {
		return msgRepository.save(message);
	}
	

	public int deleteSender(int senderId, int msgId) {
		return msgRepository.senderDeleteMsg(senderId, msgId);
	}

	public int deleteReceiver(int receiverId, int msgId) {
		return msgRepository.receiverDeleteMsg(receiverId, msgId);
	}

}
