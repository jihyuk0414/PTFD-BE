package org.example.repository;

import org.example.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface RoomRepository extends MongoRepository<ChatRoom,String> {

    List<ChatRoom> findByUsersContaining(String email);
    Integer findUserCountByRoom(String room);
    List<String> findUsersByRoom(String roomId);
    ChatRoom findByRoom(String roomId);
}
