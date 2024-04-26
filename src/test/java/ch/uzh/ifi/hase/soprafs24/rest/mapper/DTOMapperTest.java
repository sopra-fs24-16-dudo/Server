package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Player;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setId(123L);

        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        assertNotNull(lobby);
        assertEquals(123L, (int) lobby.getId());
    }

    @Test
    public void testGetLobby_fromLobby_toLobbyGetDTO_success() {
        // Setup lobby with two players
        Lobby lobby = new Lobby();
        lobby.setId(123L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Player player1 = new Player(user1);
        Player player2 = new Player(user2);

        lobby.addPlayer(player1);
        lobby.addPlayer(player2);

        // Perform mapping
        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        // Assertions to check the mapping and the structure of the resulting DTO
        assertNotNull(lobbyGetDTO, "LobbyGetDTO should not be null");
        assertEquals(123L, lobbyGetDTO.getId(), "Lobby IDs should match");

        // Checking users inside the DTO
        //assertNotNull(lobbyGetDTO.getUsers(), "Users array should not be null");
        //assertEquals(2, lobbyGetDTO.getUsers().length, "There should be two users in the DTO");

        // Checking individual user IDs
        //assertEquals(user1.getId(), lobbyGetDTO.getUsers()[0].getId(), "User IDs should match for the first user");
        //assertEquals(user2.getId(), lobbyGetDTO.getUsers()[1].getId(), "User IDs should match for the second user");
    }
    @Test
    public void testGetVoiceChannel_fromVoiceChannel_toVoiceChannelGetDTO_success() {
        // Create VoiceChannel entity
        VoiceChannel voiceChannel = new VoiceChannel();
        voiceChannel.setId(1L);
        Lobby lobby = new Lobby();
        lobby.setId(100L);
        voiceChannel.setLobby(lobby);

        // Perform mapping
        VoiceChannelGetDTO voiceChannelGetDTO = DTOMapper.INSTANCE.convertEntityToVoiceChannelGetDTO(voiceChannel);

        // Assertions to check the mapping
        assertNotNull(voiceChannelGetDTO, "VoiceChannelGetDTO should not be null");
        assertEquals(voiceChannel.getId(), voiceChannelGetDTO.getId(), "IDs should match");
        assertEquals(voiceChannel.getLobby().getId(), voiceChannelGetDTO.getLobbyId(), "Lobby IDs should match");
    }
    @Test
    public void testCreateVoiceChannel_fromVoiceChannelPostDTO_toVoiceChannel_success() {
        // Create VoiceChannelPostDTO
        VoiceChannelPostDTO voiceChannelPostDTO = new VoiceChannelPostDTO();
        voiceChannelPostDTO.setLobbyId(100L);

        // MAP -> Create VoiceChannel entity
        VoiceChannel voiceChannel = DTOMapper.INSTANCE.convertVoiceChannelPostDTOtoEntity(voiceChannelPostDTO);

        // Check content
        assertNotNull(voiceChannel, "VoiceChannel should not be null");
        assertEquals(voiceChannelPostDTO.getLobbyId(), voiceChannel.getLobby().getId(), "Lobby IDs should match");
    }
}
