package com.example.demo.event;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * @author Jacob Callicott
 */
@Controller
@RequestMapping("/events")
public class EventController
{
    @Autowired
    private EventRepository eventRepository;
    private UserRepository userRepository;
    
    @Autowired
    public EventController(EventRepository eventRepository)
    {
        this.eventRepository = eventRepository;
    }

    /**
	 * @return Returns all events. For developer purposes
	 */
    @GetMapping("/trueViewAll")
    public ResponseEntity<List<Event>> viewAll()
    {
        Iterable<Event> eventsIterable = eventRepository.findAll();
        List<Event> eventList = new ArrayList<>();
        eventsIterable.forEach(eventList::add);
        return ResponseEntity.ok(eventList);
    }

    /**
     * @return Returns all events associated with the user ID
     */
    @GetMapping("/viewAll")
	public ResponseEntity<List<Event>> view()
    {
        List<Event> e = eventRepository.findByOwnerId(User.sessionId);
        return ResponseEntity.ok(e);
    }

    /**
     *
     * Creates a new user using a JSON requestBody
     * @return returns the new user that was created in JSON form
     */
    @PostMapping("/add")
    public ResponseEntity<Event> addForm(@RequestBody Event e)
    {
        e.setOwner(userRepository.findById(User.sessionId)); // WORKAROUND FOR LACK OF SESSIONS, SETS LAST LOGGED IN USER
        eventRepository.save(e);
        return ResponseEntity.ok(e);
    }

    /**
     * @param id the id of the user that we are looking for
     * @return Returns the user that was requested from id
     */
    @GetMapping("/view/{id}")
    public ResponseEntity<Event> viewEvent(@PathVariable Long id)
    {
        Event e = getEventById(id);
        return ResponseEntity.ok(e);
    }

    /**
     * @return Converts what is retrieved by the repository automatically from Optional<Event> to Event
     */
    public Event getEventById(Long eventId)
    {
        return eventRepository.findById(eventId)
                .orElse(null);
    }

    /**
     * @param id id for the user that is intended for updating
     * @param newEvent the new data for the event that will be overwridden onto the old event
     * @return the data for the new user that was updated in JSON form
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity<Event> editForm(@PathVariable Long id, @RequestBody Event newEvent)
    {
        Event e = getEventById(id);
        e.setTitle(newEvent.getTitle());
        e.setDescription(newEvent.getDescription());
        e.setVisibilityType(newEvent.getVisibilityType());
        e.setEventDate(newEvent.getEventDate());
        e.setEventTime(newEvent.getEventTime());

        eventRepository.save(e);
        return ResponseEntity.ok(e);
    }

    /**
     * @param id id for the user that is intended for deleting
     * @return a response body ok that is standard for our controllers. Give some default value
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProcess(@PathVariable Long id)
    {
        eventRepository.deleteById(id);
        return ResponseEntity.ok("Valid");
    }

}