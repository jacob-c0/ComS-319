package com.example.demo.inviteRequest;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacob Callicott
 */
@Controller
@RequestMapping("/inviteRequests")

public class InviteRequestController
{
    @Autowired
    private InviteRequestRepository inviteRequestRepository;
    private UserRepository userRepository;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public InviteRequestController(InviteRequestRepository inviteRequestRepository)
    {
        this.inviteRequestRepository = inviteRequestRepository;
    }

    /**
     * @return Returns all inviteRequests. For developer purposes
     */
    @GetMapping("/trueViewAll")
    public ResponseEntity<List<InviteRequest>> viewAll() throws JsonProcessingException
    {
        Iterable<InviteRequest> invitesIterable = this.inviteRequestRepository.findAll();
        List<InviteRequest> inviteList = new ArrayList<>();
        invitesIterable.forEach(inviteList::add);
        return ResponseEntity.ok(inviteList);
    }

    /**
     * Returns all inviteRequests associated with the current session owner's ID
     *
     * @return Returns the inviteRequest, null if it does not exist.
     */
    @GetMapping("/viewAll")
    public ResponseEntity<List<InviteRequest>> view() throws JsonProcessingException
    {
        List<InviteRequest> i = inviteRequestRepository.findSent(User.sessionId);
        return ResponseEntity.ok(i);
    }

    @PostMapping("/add")
    public ResponseEntity<InviteRequest> addForm(@RequestBody InviteRequest i) throws JsonProcessingException
    {
        i.setOwnerId(userRepository.findById(User.sessionId)); // WORKAROUND FOR LACK OF SESSIONS, SETS LAST LOGGED IN USER
        inviteRequestRepository.save(i);
        return ResponseEntity.ok(i);
    }

    /**
     * @param id the id of the invite request we are looking for
     * @return the data of the inviteRequest in JSON form given back to the front end
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<InviteRequest> viewInviteRequest(@PathVariable long id) throws JsonProcessingException
    {
        InviteRequest i = inviteRequestRepository.findById(id);
        return ResponseEntity.ok(i);
    }

    /**
     * @param id the id of the invite request we want to edit
     * @return the edited invite request in JSON form
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<InviteRequest>  editForm(@PathVariable long id, @RequestBody InviteRequest message) throws JsonProcessingException
    {
        InviteRequest i = inviteRequestRepository.findById(id);
        i.setMessage(message.getMessage());
        inviteRequestRepository.save(i);
        return ResponseEntity.ok(i);
    }

    /**
     * @param id the id of the invite request we want to delete
     * @return a standard response entity with some integer
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProcess(@PathVariable Long id)
    {
        inviteRequestRepository.deleteById(id);
        return ResponseEntity.ok("Success");
    }
}