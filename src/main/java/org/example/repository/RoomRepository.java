package org.example.repository;

import org.example.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RoomRepository extends MongoRepository<ChatRoom,String> {

    List<ChatRoom> findByUsersContaining(String email);
    ChatRoom findByRoom(String roomId);
    boolean existsByRoom(String roomId);
}
