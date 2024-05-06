package ch.uzh.ifi.hase.soprafs24.rest.dto;
import java.time.LocalDate;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

public class UserGetDTO {

  private Long id;
  private String name;
  private String username;
  private UserStatus status;
  private LocalDate creationDate;
  private LocalDate birthday;
  private int gamesPlayed;
  private int gamesWon;
  private double winRatio;
  private boolean ready;

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

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }


  public int getGamesPlayed() {
      return gamesPlayed;
  }

  public void setGamesPlayed(int gamesPlayed) {
      this.gamesPlayed = gamesPlayed;
  }

  public int getGamesWon() {
      return gamesWon;
  }

  public void setGamesWon(int gamesWon) {
      this.gamesWon = gamesWon;
  }

  public double getWinRatio() {
      return winRatio;
  }

  public void setWinRatio(double winRatio) {
      this.winRatio = winRatio;
  }
  public boolean isReady() { return ready; }
  public void setReady(boolean ready) { this.ready = ready; }

}
