package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import ch.uzh.ifi.hase.soprafs24.repository.VoiceChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional
public class VoiceChannelService {

    private final Logger log = LoggerFactory.getLogger(VoiceChannelService.class);

    private final VoiceChannelRepository voiceChannelRepository;
    private UserService userService;

    @Autowired
    public VoiceChannelService(VoiceChannelRepository voiceChannelRepository) {
        this.voiceChannelRepository = voiceChannelRepository;
        this.userService = userService;
    }
    /*

    public VoiceChannel createVoiceChannel(Lobby lobby) {
        VoiceChannel voiceChannel = new VoiceChannel();
        voiceChannel.setChannelId(generateChannelId()); // Hier kannst du eine Methode implementieren, die eine Channel-ID generiert
        voiceChannel.setLobby(lobby);

        return voiceChannelRepository.save(voiceChannel);
    }

    private String generateChannelId() {
        // Hier kannst du eine Logik implementieren, um eine eindeutige Channel-ID zu generieren
        return UUID.randomUUID().toString(); // Beispiel für eine einfache Generierung einer UUID als Channel-ID
    }
    public void addUserToVoiceChannel(Lobby lobby, Long userId) {
        User user = userService.getUserById(userId);

        // Add the user to the lobby's voice channel
        lobby.getVoiceChannel().addUser(user);

        // Save the updated lobby
        voiceChannelRepository.save(lobby.getVoiceChannel());

        log.debug("Adding user {} to voice channel of lobby {}", userId, lobby.getId());
    }

    public VoiceChannel getVoiceChannel(Lobby lobby) {
        // Retrieve the lobby's voice channel
        VoiceChannel voiceChannel = lobby.getVoiceChannel();

        if (voiceChannel == null) {
            // If the voice channel doesn't exist, create a new one
            voiceChannel = new VoiceChannel();
            lobby.setVoiceChannel(voiceChannel);
            voiceChannelRepository.save(voiceChannel);
        }

        return voiceChannel;
    }

    public void removeUserFromVoiceChannel(Lobby lobby, Long userId) {
        // Retrieve the lobby's current voice channel
        VoiceChannel voiceChannel = getVoiceChannel(lobby);

        // Retrieve the user to remove
        User user = userService.getUserById(userId);

        // Remove the user from the voice channel
        voiceChannel.removeUser(user);

        // Save the updated voice channel
        voiceChannelRepository.save(voiceChannel);

        log.debug("Removing user {} from voice channel of lobby {}", userId, lobby.getId());
    }

    public void clearVoiceChannel(Lobby lobby) {
        // Retrieve the lobby's current voice channel
        VoiceChannel voiceChannel = getVoiceChannel(lobby);

        // Clear the voice channel (remove all users)
        voiceChannel.clearUsers();

        // Save the updated voice channel
        voiceChannelRepository.save(voiceChannel);

        log.debug("Clearing voice channel of lobby {}", lobby.getId());
    }
    public void deleteVoiceChannelById(Long voiceChannelId) {
        VoiceChannel voiceChannel = voiceChannelRepository.findById(voiceChannelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voice Channel not found with id: " + voiceChannelId));

        voiceChannelRepository.delete(voiceChannel);
    }

     */
}
