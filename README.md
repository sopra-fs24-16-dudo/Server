![image](https://github.com/sopra-fs24-16-dudo/Server/assets/160472898/959584f6-4500-4d72-acd9-9a7ca002bf02)

## Introduction

Dudo is a multiplayer dice game that challenges players' bluffing and bidding skills. Inspired by the traditional Peruvian dice game, Dudo offers an exciting and strategic gaming experience where players attempt to outsmart each other with clever bids and daring bluffs.

The game is played with five dice, each depicting symbols from the standard deck of cards: 9, 10, J, Q, K, and A. Players take turns making bids on the total number of specific symbols (e.g., four 10s) present among all players' dice, while also having the option to challenge the previous bid if they believe it to be false.

Dudo combines elements of probability, psychology, and strategy, making each round unpredictable and engaging. Whether you're a seasoned strategist or a casual gamer looking for some fun, Dudo offers an immersive gaming experience for players of all skill levels.

## Technologies

- [Springboot](https://spring.io/projects/spring-boot) - Framework for creating Java applications
- [Gradle](https://gradle.org/) - Build automation tool
- [Google Cloud](https://cloud.google.com/appengine/docs/flexible) - Deployment
- [JPA](https://www.oracle.com/java/technologies/persistence-jsp.html) - Database for user management
- [RESTful](https://restfulapi.net/) - Web services for user control
- [Websocket](https://spring.io/guides/gs/messaging-stomp-websocket/) -  Real-time bidirectional communication between client and server
- [Agora](https://www.agora.io/en/) - Real-time voice communication
- [Mockito](https://site.mockito.org/) - Java framework for unit test

## High-level Components

The [Player](https://github.com/sopra-fs24-16-dudo/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Player.java) class represents real-time users in a game. It encapsulates essential user information such as user ID, username, and player-specific properties like chips and readiness status. It also manages the player's hand and handles actions like rolling dice and disqualification.

The [Lobby](https://github.com/sopra-fs24-16-dudo/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Lobby.java) class is responsible for managing players within a game session. It maintains a list of players currently in the lobby, handles player addition and removal, and facilitates the start of the game. Additionally, it handles the game actions and passes them.

The [Game](https://github.com/sopra-fs24-16-dudo/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Game.java) class represents the game itself. It orchestrates the overall game flow, including starting rounds, managing players, and determining the winner. It also encapsulates game-specific logic and maintains the state of the current round.

The [Round](https://github.com/sopra-fs24-16-dudo/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/entity/Round.java) class manages individual rounds within the game. It handles aspects such as bidding, tracking player actions, determining the winner of each round, and maintaining the state of the round, including the current bid and the suit counter.

The [GameController](https://github.com/sopra-fs24-16-dudo/Server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/controller/GameController.java) class serves as the entry point for controlling game-related functionality. It exposes REST endpoints for actions such as retrieving players, placing bids, starting rounds, and handling game events. Additionally, it interacts with the WebSocket connection to provide real-time updates to clients.

These high-level components work together to manage the game state, facilitate player interactions, and ensure smooth gameplay experience.

## Getting started with Spring Boot
-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: https://spring.io/guides/tutorials/rest/

## Setup with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
If you consider to use IntelliJ as your IDE of choice, you can make use of your free educational license [here](https://www.jetbrains.com/community/education/#students).
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs24` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

## Debugging
If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time

## Testing
Have a look here: https://www.baeldung.com/spring-boot-testing

## Contribution

Please read [CONTRIBUTIONS.md](https://github.com/sopra-fs24-16-dudo/Server/blob/main/contribution.md) for details on our code of conduct, and the process for submitting pull requests to us.

### Deployment

By pushing your commits to the main branch 3 actions run automated:
- SonarCloud Analysis: Checking the quality of your code and test coverage.
- Deploying Project to App Engine / Test and Sonarqube: Checking if building works.
- Deploying Project to App Engine / Deploying to Google Cloud: Deploying Project. 

## Roadmap
New developers who want to contribute could add the following features to our project:
- Guest Account: our game only supports registered users. Implementing a Guest User that can easy play some games would be a nice addition.
- Social Features: Friendslist, invite, private chat.
- Avatars: that are shown in the game.
- Mobile Adaptability: Our Game currently is designed for PC, which can be improved in terms of the layout and of the UI for mobile devices.

## Authors

* **Gianluca Nardone**  [GianlucaNardone](https://github.com/GianlucaNardone)
* **Katerina Kuneva**  [kkuneva](https://github.com/kkuneva)
* **Tomas Aguilar Lopez**  [tomasaguilar0710](https://github.com/tomasaguilar0710)
* **Jamin Sulic**  [Jamin-Sulic](https://github.com/Jamin-Sulic)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details

## Acknowledgments

If you want to say **thanks** or/and support active development of `Dudo`:

- Add a [GitHub Star](https://github.com/sopra-fs24-16-dudo) to the project.
- Contact us ;)
