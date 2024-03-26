package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LobbyService {


    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public Lobby getLobbyById(Long lobbyId) {
        Optional<Lobby> optionalLobby = lobbyRepository.findById(lobbyId);
        Lobby foundLobby = optionalLobby.orElse(null);
        if (foundLobby != null){
            return foundLobby;
        } else {
            return null;
        }
    }

    public List<Lobby> getAllLobbies() {
        return lobbyRepository.findAll();
    }

    public Lobby createLobby() {

        Lobby newLobby = new Lobby();
        checkIfLobbyExists(newLobby.getId());
        lobbyRepository.save(newLobby);
        lobbyRepository.flush();
        log.debug("Created Information for Lobby: {}", newLobby.getId());
        return newLobby;
    }

    //check if lobby with id already exists
    private void checkIfLobbyExists(Long lobbyId) {
        Optional<Lobby> lobby = lobbyRepository.findById(lobbyId);
        if (lobby.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Lobby with id does not exist");
        }

    }
}
