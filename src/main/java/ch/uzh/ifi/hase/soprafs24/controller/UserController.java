package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    User createdUser = userService.createUser(userInput);
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<UserGetDTO> getUserById(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    return ResponseEntity.ok(userGetDTO);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User loginUser) {
    User authenticatedUser = userService.checkUserCredentials(loginUser);
    if (authenticatedUser != null) {
      return ResponseEntity.ok(authenticatedUser);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
  }

  @PutMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody User userId) {
    User statusUser = userService.updateUserStatus(userId);
    if (statusUser != null) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found!");
    }
  }


}
