#Snake and Blackjack Game Manager


##Overview
Snake and Blackjack Game Manager is a JavaFX-based application that demonstrates advanced Object-Oriented Programming (OOP)and Graphical User Interface (GUI) development.
The system features a centralized Manager that handles user authentication, score tracking, and game transitions, allowing users to switch between a logic-based Snake game and a probability-based Blackjack game.


##Core OOP Principles Demonstrated:
Encapsulation: Protecting game states and user data.
Inheritance: Extending base game logic and GUI components.
Abstraction: Using abstract models for game entities.
Interfaces: Implementing a Collidable interface for game object interactions.
Design Patterns: Implementing the Model-View-Controller (MVC) architecture.
Unit Testing: Ensuring logic reliability via JUnit 5.


##System Design
Main Modules & Classes
1. Manager (Core)
MainMenuController: Manages transitions between games.
UserAuth: Handles user login and account persistence.
ScoreManager: Tracks high scores for both games.
2. Snake Game
SnakeGameLogic: The engine managing movement, growth, and collision.
Snake & Food: Models representing game entities.
Collidable (Interface): Defines how objects interact within the grid.
3. Blackjack Game
BlackjackLogic: Manages card dealing, scoring, and dealer AI.
Deck & Card: Models for the game state.
Key Concepts Used
JavaFX: For a responsive, visual user interface.
MVC Architecture: Decoupling the game logic from the visual rendering.
Resource Management: Loading custom MP3 background music and image assets.
Maven: For dependency management and automated builds.


##Installation Instructions
1. Prerequisites: Java 17 or higher installed. Ensure Maven is installed and configured in your environment.
2. Clone the Repository:  git clone https://github.com/Aditya-N18/CS151_Project3.git 
3. Navigate to Project Directory:    cd CS151_Project3/demo    


##Usage
###To launch the application, run the following Maven command in your terminal:
mvn javafx:run

###To run the automated unit tests:
mvn test

##Contributions
Thy: Game Manager (entire) + Blackjack save/load + utils (incl. encryption)
Serife: Blackjack core logic + Blackjack JavaFX UI
Adithi: Snake core logic
Aditya: Snake JavaFX UI + game-loop driver + overlays
