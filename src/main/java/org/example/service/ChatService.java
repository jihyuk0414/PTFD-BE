package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.Message;
import org.example.entity.Chatting;
import org.example.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public void saveChat(Chatting chatting){
        chatRepository.save(chatting);
    }
    public List<Message> getChat(String room){
        return chatRepository.findByChatRoomId(room).stream().map(Chatting::toDto).toList();
    }
}
