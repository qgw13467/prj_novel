package io.team.domain.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.team.domain.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageReceiveDTO {
	private int msgId;
	private int senderId;
	private int receiverId;
	private String title;
	private String content;
	private int isRead;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime Datetime;
	
	public static MessageReceiveDTO messageReceiveDOTfomMessage(Message message) {

		return MessageReceiveDTO.builder()
				.msgId(message.getMsgId())
				.senderId(message.getSenderId())
				.receiverId(message.getReceiverId())
				.title(message.getTitle())
				.content(message.getContent())
				.isRead(message.getIsRead())
				.Datetime(message.getDatetime())
				.build();
				
	}
}
