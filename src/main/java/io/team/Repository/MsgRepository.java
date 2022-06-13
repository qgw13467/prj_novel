package io.team.Repository;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.team.domain.Message;

@Repository
public interface MsgRepository extends JpaRepository<Message, Integer> {
	
	Message findByMsgId(int msgId);
	
	@Query("SELECT m FROM Message m WHERE m.senderDelete = 0 AND m.senderId = :senderId" )
	Page<Message> findBySenderId(int senderId, Pageable pageable);

	@Query("SELECT m FROM Message m WHERE m.senderDelete = 0 AND m.senderId = :senderId")
	Collection<Message> findBySenderId(int senderId);

	@Query("SELECT m FROM Message m WHERE m.receiverDelete = 0 AND m.receiverId = :receiverId")
	Page<Message> findByReceiverId(int receiverId, Pageable pageable);

	@Query("SELECT m FROM Message m WHERE m.receiverDelete = 0 AND m.receiverId = :receiverId")
	Collection<Message> findByReceiverId(int receiverId);

	@Modifying
	@Query("UPDATE Message m SET m.isRead = 1 WHERE m.msgId = :msgId")
	int msgReadCheck(int msgId);

	@Modifying
	@Query("UPDATE Message m SET m.receiverDelete = 1 WHERE m.receiverId = :receiverId AND m.msgId = :msgId")
	int receiverDeleteMsg(int receiverId, int msgId);

	@Modifying 
	@Query("UPDATE Message m SET m.senderDelete = 1 WHERE m.senderId = :senderId AND m.msgId = :msgId")
	int senderDeleteMsg(int senderId, int msgId);
}
