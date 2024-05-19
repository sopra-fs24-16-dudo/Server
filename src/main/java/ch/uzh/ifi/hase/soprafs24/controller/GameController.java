package ch.uzh.ifi.hase.soprafs24.controller;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
public class GameController {

    private final LobbyService lobbyService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    GameController(LobbyService lobbyService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/games/players/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayers(@PathVariable Long lobbyId) {
        List<Player> players = lobbyService.getPlayersInLobby(lobbyId);
        if (players != null){
            return players;
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No players in game");
        }
    }
    @PostMapping("/games/hand/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Hand rollHand(@PathVariable Long lobbyId, @RequestBody Long userId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player player = lobby.getPlayerById(userId);
        if (player.hasRolled()){
            return player.getHand();
        }
        player.roll();
        return player.getHand();
    }

    @GetMapping("/games/currentBid/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getBid(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        messagingTemplate.convertAndSend("/topic/game/currentbid/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        return lobby.getCurrentBid().toString();
    }

    @GetMapping("/games/nextBid/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getNextBid(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        if (lobby.getNextBid() == null){
            return "Null";
        }
        return lobby.getNextBid().toString();
    }

    @GetMapping("/games/validBids/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Bid> getValidBids(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        if (lobby.getValidBids() == null){
            return new ArrayList<Bid>();
        }
        return lobby.getValidBids();
    }

    @PostMapping("/games/placeBid/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void placeBid(@PathVariable Long lobbyId, @RequestBody String bid) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        bid = bid.substring(1, bid.length() - 1);
        Bid newBid = new Bid(bid);
        lobby.placeBid(newBid);
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }

    @GetMapping("/games/currentPlayer/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int getCurrentPlayer(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player currentPlayer = lobby.getRound().getCurrentPlayer();
        return (int) currentPlayer.getId();
    }

    @GetMapping("/games/lastPlayer/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public int getLastPlayer(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player lastPlayer = lobby.getRound().getLastPlayer();
        return (int) lastPlayer.getId();
    }

    @PutMapping("/games/dudo/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void dudo(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        lobby.dudo();
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }

    @GetMapping("/games/winner/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getWinner(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player winner = lobby.getWinner();
        return winner;
    }

    @GetMapping("/games/winnerCheck/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkWinner(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobby.checkWinner();
    }

    @GetMapping("/games/loser/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Player getLoser(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player loser = lobby.getLoser();
        return loser;
    }

    @PutMapping("/games/round/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void startRound(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        lobby.startRound();
        messagingTemplate.convertAndSend("/topic/lobby/" + lobbyId, DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
    }

    @GetMapping("/games/counter/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getSuitCounter(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobby.getRound().getSuitCounter().toString();
    }

    @GetMapping("/games/hands/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getHands(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobby.getHands().toString();
    }
    @PutMapping("/games/end/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void endGame(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        Player winner = lobby.getWinner();
        lobby.updatePoints(winner);
    }

    @GetMapping("/games/fijoCheck/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean checkFijo(@PathVariable Long lobbyId) {
        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        return lobby.isFijo();
    }

}