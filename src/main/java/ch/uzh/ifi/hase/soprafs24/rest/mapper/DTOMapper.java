package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationDate", target = "creationDate")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "id", target = "id")
  Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

  @Mapping(source = "id", target = "id")
  //@Mapping(source = "users", target = "users")
  LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "lobby.id", target = "lobbyId")
    VoiceChannelGetDTO convertEntityToVoiceChannelGetDTO(VoiceChannel voiceChannel);

    @Mapping(source = "lobbyId", target = "lobby.id")
    VoiceChannel convertVoiceChannelPostDTOtoEntity(VoiceChannelPostDTO voiceChannelPostDTO);
}
