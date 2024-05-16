package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column(nullable = false)
  private int gamesPlayed;


    @ManyToMany
    @JoinTable(
            name = "user_voice_channel",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "voice_channel_id")
    )
    private Set<VoiceChannel> voiceChannels;

    // getters and setters...


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
  public UserStatus getStatus() {
      return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public int getGamesPlayed() {
      return gamesPlayed;
  }
  public void incrementGamesPlayed() { this.gamesPlayed++; }
}