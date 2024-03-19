package com.example.demo.user;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;

@Controller
public class UserController
{

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/index")
	public ResponseEntity<List<User>> index()
	{
		Iterable<User> usersIterable = userRepository.findAll();
		List<User> userList = new ArrayList<>();
		usersIterable.forEach(userList::add);
		return ResponseEntity.ok(userList);
	}

	@PostMapping("/register")
	public ResponseEntity<User> create(@RequestBody User user)
	{
		userRepository.save(user);
		return getUser(user.getId());
	}

	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestBody User user) throws JsonProcessingException {
		User loginUser = userRepository.findByUsername(user.getUsername());
		if(loginUser == null)
		{
			User.sessionId = 1;
			return ResponseEntity.ok(userRepository.findById(1));
		}
		if(user.getPassword().equals(loginUser.getPassword()))
		{
			User.sessionId = loginUser.getId();
			return ResponseEntity.ok(loginUser);
		}
		else
		{
			User.sessionId = 1;
			return ResponseEntity.ok(userRepository.findById(1));
		}
	}

	@GetMapping("/getSessionUser")
	ResponseEntity<User> giveUser()
	{
		User user = userRepository.findById(User.sessionId);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/users/{id}")
	ResponseEntity<User> getUser(@PathVariable long id)
	{
		User user = userRepository.findById(id);
		return ResponseEntity.ok(user);
	}

	@PutMapping("/users/update/{id}")
	ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User user)
	{
		User ogUser = userRepository.findById(id);
		ogUser.setUsername(user.getUsername());
		ogUser.setPassword(user.getPassword());
		ogUser.setEmail(user.getEmail());
		ogUser.setState(user.getState());
		ogUser.setCity(user.getCity());
		userRepository.save(ogUser);

		return ResponseEntity.ok(ogUser);
	}

	@DeleteMapping("/users/delete")
	ResponseEntity<String> deleteUser()
	{
		userRepository.deleteById(User.sessionId);
		return ResponseEntity.ok("It worked");
	}

	@DeleteMapping("/users/adminDelete/{id}")
	ResponseEntity<String> deleteUser(@PathVariable long id)
	{
		userRepository.deleteById(id);
		return ResponseEntity.ok("It worked");
	}

}
