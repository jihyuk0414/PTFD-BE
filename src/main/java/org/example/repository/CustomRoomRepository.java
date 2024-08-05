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

    public void updateUsers(String roomId, List<String> users,int userCount) {
        Query query = new Query(Criteria.where("id").is(roomId));
        Update update = new Update().set("users", users);
        Update update1 = new Update().set("userCount",userCount);
        mongoTemplate.updateFirst(query, update, ChatRoom.class);
        mongoTemplate.updateFirst(query,update1, ChatRoom.class);
    }
}
