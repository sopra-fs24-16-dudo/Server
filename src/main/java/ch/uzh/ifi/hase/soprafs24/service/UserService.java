package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User checkUserCredentials(User loginUser) {
    User existingUser = userRepository.findByUsername(loginUser.getUsername());

    if (existingUser != null && loginUser.getName().equals(existingUser.getName())) {
      existingUser.setStatus(UserStatus.ONLINE);
      return existingUser; // Credentials are valid
    } else {
      return null;
    }
  }

  public User getUserById(Long userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    User foundUser = optionalUser.orElse(null);
    if (foundUser != null){
      return foundUser;
    } else {
      return null;
    }
  }

  public User updateUserById(Long Id, String newUsername, LocalDate newBirthday) {
    Optional<User> optionalUser = userRepository.findById(Id);
    User userToUpdate = optionalUser.orElse(null);

    if (userToUpdate == null) {
      return null;
    }

    if (newUsername != "") {
      userToUpdate.setUsername(newUsername);
    }

    return userRepository.save(userToUpdate);
  }

  public User updateUserStatus(User userId) {
    Optional<User> optionalUser = userRepository.findById(userId.getId());
    User statusUser = optionalUser.orElse(null);
    if (statusUser != null) {
      // Update the user's status
      statusUser.setStatus(UserStatus.OFFLINE);
      return statusUser;
    } else {
      return null;
    }
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    checkIfUserExists(newUser);
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
    }
  }

  public void clearAllUsers() {
      userRepository.deleteAll();
  }
}
