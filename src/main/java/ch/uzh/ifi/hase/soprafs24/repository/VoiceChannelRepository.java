package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.VoiceChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoiceChannelRepository extends JpaRepository<VoiceChannel, Long> {
    Optional<VoiceChannel> findById(Long id);
}
