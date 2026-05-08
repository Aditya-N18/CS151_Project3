package manager.controller;

import manager.score.HighScoreManager;
import manager.view.MainMenuView;
import utils.Constants;

// Handle naviagation in the main menu and show leaderboard 
public class MainMenuController {

    private static final int TOP_N = 5;

    private final NavigationController nav;
    private final HighScoreManager scores;
    private MainMenuView view;

    public MainMenuController(NavigationController nav, HighScoreManager scores) {
        this.nav = nav;
        this.scores = scores;
    }

    public void bind(MainMenuView view) {
        this.view = view;
    }

<<<<<<< HEAD
    // public void launchBlackjack() {
    // nav.goToBlackjackMenu();
    // }
=======
    public void launchBlackjack() {
        nav.goToBlackjackMenu();
    }
>>>>>>> 71465882ff13ea2dfe1315322534b88c7f760337

    public void launchSnake() {
        nav.goToSnakeGame();
    }

    public void refreshLeaderboards() {
        if (view == null) {
            return;
        }
        view.setBlackjackTop(scores.getTopScores(Constants.GAME_BLACKJACK, TOP_N));
        view.setSnakeTop(scores.getTopScores(Constants.GAME_SNAKE, TOP_N));
    }
}
