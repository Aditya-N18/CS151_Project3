package manager.app;

import javafx.application.Application;
import javafx.stage.Stage;

import manager.auth.AccountManager;
import manager.controller.NavigationController;
import manager.score.HighScoreManager;

// App entry point
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        AccountManager accounts = new AccountManager();
        accounts.loadAccounts();

        HighScoreManager scores = new HighScoreManager();
        scores.loadScores();
        scores.initializeDefaultScores();

        NavigationController nav = new NavigationController(stage, accounts, scores);
        nav.goToLogin();

        stage.setTitle("CS151 - Project 3");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
