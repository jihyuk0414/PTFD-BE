package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.entity.Chatting;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomChatRepository {

    private final MongoTemplate mongoTemplate;

    public void updateChatDetails(String beforeNickName, String newNickName, String newProfileImage) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sender.nick_name").is(beforeNickName));
        Update update = new Update();
        update.set("sender.nick_name", newNickName);
        update.set("sender.profile_image", newProfileImage);
        mongoTemplate.updateFirst(query, update, Chatting.class);
    }

}
