package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setName("name");
    userPostDTO.setUsername("username");

    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.getName(), user.getName());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");

    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getName(), userGetDTO.getName());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
  }

  @Test
  public void testCreateLobby_fromLobbyPostDTO_toLobby_success() {
      // Assuming there's a method to map from LobbyPostDTO to Lobby
      LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
      lobbyPostDTO.setId(123);

      Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

      assertEquals(lobbyPostDTO.getId(), lobby.getId());
  }

  @Test
  public void testGetLobby_fromLobby_toLobbyGetDTO_success() {
      Lobby lobby = new Lobby();
      lobby.setId(123L);

      // Setup users for the lobby
      User user1 = new User();
      user1.setId(1L);
      User user2 = new User();
      user2.setId(2L);
      User[] users = { user1, user2 };
      lobby.setUsers(List.of(users));

      LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

      assertEquals(lobby.getId(), lobbyGetDTO.getId());
      assertEquals(2, lobbyGetDTO.getUsers().length);  // Ensure users are transferred correctly
      assertEquals(user1.getId(), lobbyGetDTO.getUsers()[0].getId());
      assertEquals(user2.getId(), lobbyGetDTO.getUsers()[1].getId());
  }
}
