package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.entity.ChatRoom;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRoomRepository {
    private final MongoTemplate mongoTemplate;

    public void updateUsers(String roomId, List<String> users) {
        Query query = new Query(Criteria.where("room").is(roomId));
        Update update = new Update().set("users", users);
        mongoTemplate.updateFirst(query, update, ChatRoom.class);

    }
}
