package manager.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import blackjack.view.BlackjackGameView;
import blackjack.view.BlackjackMenuView;
import manager.auth.AccountManager;
import manager.score.HighScoreManager;
import manager.view.LoginView;
import manager.view.MainMenuView;
import manager.view.ToolbarView;
import snake.view.SnakeGameView;
import utils.Constants;

//handle navigation between major screens like login, main menu and games
//also hold the toolbar
public class NavigationController {

    private static final double DEFAULT_WIDTH = 960;
    private static final double DEFAULT_HEIGHT = 640;

    private final Stage stage;
    private final AccountManager accounts;
    private final HighScoreManager scores;
    private final BorderPane shell = new BorderPane();
    private final ToolbarView toolbar = new ToolbarView();
    private Scene shellScene;
    private String currentUser;
    // Live blackjack game view, set on goToBlackjackGame() and drained by
    // goToMainMenu() so the human's final balance gets recorded as a score.
    private BlackjackGameView currentBlackjackView;

    public NavigationController(Stage stage, AccountManager accounts, HighScoreManager scores) {
        this.stage = stage;
        this.accounts = accounts;
        this.scores = scores;
        this.toolbar.setOnMainMenu(this::goToMainMenu);
    }

    public AccountManager getAccountManager() {
        return accounts;
    }

    public HighScoreManager getHighScoreManager() {
        return scores;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public Stage getStage() {
        return stage;
    }

    public void goToLogin() {
        currentUser = null;
        toolbar.setUserLabel(null);
        LoginView login = new LoginView(this, accounts);
        Scene loginScene = new Scene(login.getRoot(), DEFAULT_WIDTH, DEFAULT_HEIGHT);
        stage.setScene(loginScene);
    }

    // set toolbar and main menu on login success
    public void onLoginSuccess(String username) {
        this.currentUser = username;
        installShellSceneIfNeeded();
        toolbar.setUserLabel(username);
        goToMainMenu();
    }

    public void goToMainMenu() {
        recordBlackjackScoreIfActive();
        installShellSceneIfNeeded();
        toolbar.updateForScene("mainmenu");
        MainMenuController controller = new MainMenuController(this, scores);
        MainMenuView view = new MainMenuView(controller);
        controller.bind(view);
        controller.refreshLeaderboards();
        setRootWithToolbar(view.getRoot());
    }

    /**
     * If the user is leaving an active blackjack game, persist their final
     * balance as a high-score row before navigating away. Cleared after one
     * call so re-entry doesn't double-record.
     */
    private void recordBlackjackScoreIfActive() {
        if (currentBlackjackView == null) {
            return;
        }
        int balance = currentBlackjackView.getLogic().getHumanPlayer().getBalance();
        if (currentUser != null) {
            scores.recordScore(currentUser, Constants.GAME_BLACKJACK, balance);
        }
        currentBlackjackView = null;
    }

    public void goToBlackjackMenu() {
        installShellSceneIfNeeded();
        toolbar.updateForScene("blackjack-menu");
        BlackjackMenuView view = new BlackjackMenuView(this);
        setRootWithToolbar(view.getRoot());
    }

    public void goToBlackjackGame() {
        installShellSceneIfNeeded();
        toolbar.updateForScene("blackjack-game");
        BlackjackGameView view = new BlackjackGameView(this);
        this.currentBlackjackView = view;
        setRootWithToolbar(view.getRoot());
    }

    public void goToSnakeGame() {
        installShellSceneIfNeeded();
        toolbar.updateForScene("snake");
        SnakeGameView view = new SnakeGameView(this);
        setRootWithToolbar(view.getRoot());
    }

    public void setRootWithToolbar(Node content) {
        shell.setTop(toolbar.getNode());
        shell.setCenter(content);
    }

    private void installShellSceneIfNeeded() {
        if (shellScene == null) {
            shellScene = new Scene(shell, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
        if (stage.getScene() != shellScene) {
            stage.setScene(shellScene);
        }
    }
}
