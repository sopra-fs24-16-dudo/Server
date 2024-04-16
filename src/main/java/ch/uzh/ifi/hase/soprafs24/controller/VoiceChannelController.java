package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import ch.uzh.ifi.hase.soprafs24.rest.dto.VoiceChannelGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.VoiceChannelPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.VoiceChannelService;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VoiceChannelController {
    private final VoiceChannelService voiceChannelService;
    private final LobbyService lobbyService;

    public VoiceChannelController(VoiceChannelService voiceChannelService, LobbyService lobbyService) {
        this.voiceChannelService = voiceChannelService;
        this.lobbyService = lobbyService;
    }

    @PostMapping("/lobby/{lobbyId}/voice-channel/join")
    public ResponseEntity<String> joinVoiceChannel(@PathVariable Long lobbyId, @RequestBody Long userId) {
        try {
            voiceChannelService.addUserToVoiceChannel(lobbyService.getLobbyById(lobbyId), userId);
            return ResponseEntity.ok("Joined voice channel successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to join voice channel");
        }
    }

    @PostMapping("/lobby/{lobbyId}/voice-channel/leave")
    public ResponseEntity<String> leaveVoiceChannel(@PathVariable Long lobbyId, @RequestBody Long userId) {
        try {
            voiceChannelService.removeUserFromVoiceChannel(lobbyService.getLobbyById(lobbyId), userId);
            return ResponseEntity.ok("Left voice channel successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to leave voice channel");
        }
    }

    @DeleteMapping("/voice-channel/{voiceChannelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVoiceChannel(@PathVariable Long voiceChannelId) {
        // Delete the voice channel
        voiceChannelService.deleteVoiceChannelById(voiceChannelId);
    }
}
