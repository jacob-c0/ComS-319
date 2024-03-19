package com.example.demo.message;

import com.example.demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MessageController
{
    private final MessageRepository messageRepository;
    @Autowired
    public MessageController(MessageRepository messageRepository)
    {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/message/adminViewAll")
    public ResponseEntity<List<Message>> viewAll()
    {
        return ResponseEntity.ok(messageRepository.findAll());
    }

    @GetMapping("/message/findById/{id}")
    public ResponseEntity<Message> viewAll(@PathVariable long id)
    {
        return ResponseEntity.ok(messageRepository.findById(id));
    }

    @GetMapping("/message/{rId}")
    public ResponseEntity<List<Message>> viewSent(@PathVariable long rId)
    {
        return ResponseEntity.ok(messageRepository.findSent(User.sessionId, rId));
    }

    @PostMapping("/message/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message request)
    {
        Message toSave = new Message
                (request.getSender(),request.getReceiver(), request.getContent());
        if(toSave.getContent() == null){
            throw new RuntimeException("No message");
        }
        messageRepository.save(toSave);
        return ResponseEntity.ok(toSave);
    }

    @PutMapping("/message/update/{chatId}")
    public ResponseEntity<Message> editMessage(@PathVariable long chatId,
                                               @RequestBody Message request)
    {
        Message toEdit = messageRepository.findById(chatId);
        if(toEdit == null)
        {
            throw new RuntimeException("Message does not exist");
        }
        if(request.getSender() != toEdit.getSender() ||
                request.getReceiver() != toEdit.getReceiver()){
            throw new RuntimeException("Cannot edit message");
        }
        toEdit.setContent(request.getContent());
        messageRepository.save(toEdit);
        return ResponseEntity.ok(toEdit);
    }
    @DeleteMapping("/message/delete/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable long id)
    {
        Message toDelete = messageRepository.findById(id);
        if(toDelete == null){
            throw new RuntimeException("Cannot delete message");
        }
        messageRepository.deleteById(toDelete.getId());
        return ResponseEntity.ok("Deleted");
    }

}
