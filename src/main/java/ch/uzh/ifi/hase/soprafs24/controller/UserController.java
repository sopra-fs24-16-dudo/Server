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
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<UserGetDTO> getUserById(@PathVariable Long userId) {
    User user = userService.getUserById(userId);
    
    // If user not found, return a 404 Not Found response
    if (user == null) {
      return ResponseEntity.notFound().build();
    }
    
    // Convert the user to DTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    
    // Return the DTO in the response body with status 200 OK
    return ResponseEntity.ok(userGetDTO);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody User loginUser) {

    User authenticatedUser = userService.checkUserCredentials(loginUser);

    if (authenticatedUser != null) {
      return ResponseEntity.ok(authenticatedUser);
    } else {
      // Return a 401 Unauthorized response
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
  }

  @PutMapping("/logout")
  public ResponseEntity<?> logout(@RequestBody User userId) {
    User statusUser = userService.updateUserStatus(userId);

    // Check if the user was successfully updated
    if (statusUser != null) {
      return ResponseEntity.ok().build();
    } else {
      // User not found, return 404 Not Found error
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found!");
    }
  }

  @PutMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public ResponseEntity<?> updateUserbyId(@PathVariable Long userId, @RequestBody User request) {

    User userToUpdate = userService.updateUserById(userId, request.getUsername(), request.getBirthday());
    
    // If user not found, return a 404 Not Found response
    if (userToUpdate == null) {
      return ResponseEntity.notFound().build();
    }
    // If user found, return a 204 No Content response    
    return ResponseEntity.noContent().build();  
  }
}
