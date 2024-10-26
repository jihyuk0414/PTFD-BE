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

    public void updateUserNickNameInUsers(String beforeNickName, String newNickName) {
        Query query = new Query(Criteria.where("users").is(beforeNickName));
        Update update = new Update().set("users.$", newNickName);
        mongoTemplate.updateMulti(query, update, ChatRoom.class);

    }

    public void updateUserNickNameInPost(String beforeNickName, String newNickName, String newUserProfile) {
        Query query = new Query(Criteria.where("post.nickName").is(beforeNickName));
        Update update = new Update()
                .set("post.nickName", newNickName)
                .set("post.userProfile", newUserProfile);
        mongoTemplate.updateMulti(query, update, ChatRoom.class);
    }

    public void addUserToRoom(String roomId, String nickName) {
        Query query = new Query(Criteria.where("room").is(roomId));
        Update update = new Update().addToSet("users", nickName);
        mongoTemplate.updateFirst(query, update, ChatRoom.class);
    }

    public void updatePostInfo(String beforePostName, String newPostName, int newPrice, String newPostImg, String newPostInfo) {
        Query query = new Query(Criteria.where("post.postName").is(beforePostName));
        Update update = new Update()
                .set("post.postName", newPostName)
                .set("post.price", newPrice)
                .set("post.imagePost", newPostImg)
                .set("post.postInfo", newPostInfo);
        mongoTemplate.updateMulti(query, update, ChatRoom.class);
    }
}
