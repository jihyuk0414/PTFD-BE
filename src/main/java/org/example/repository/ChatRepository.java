package org.example.repository;

import org.example.entity.Chatting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chatting, String> {
    List<Chatting> findByRoomId(String room);
}
