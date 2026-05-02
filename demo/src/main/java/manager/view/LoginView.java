package manager.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import manager.auth.AccountManager;
import manager.controller.NavigationController;


public class LoginView {

    private final NavigationController nav;
    private final AccountManager accounts;
    private final VBox root;
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label statusLabel = new Label(" ");

    public LoginView(NavigationController nav, AccountManager accounts) {
        this.nav = nav;
        this.accounts = accounts;
        this.root = build();
    }

    public Region getRoot() {
        return root;
    }

    private VBox build() {
        Label title = new Label("CS151 \u2014 Project 3");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Sign in to play Blackjack or Snake");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #555;");

        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(260);

        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(260);

        Button loginBtn = new Button("Log In");
        loginBtn.setDefaultButton(true);
        loginBtn.setOnAction(e -> handleLogin());

        Button createBtn = new Button("Create Account");
        createBtn.setOnAction(e -> handleCreate());

        HBox buttons = new HBox(10, loginBtn, createBtn);
        buttons.setAlignment(Pos.CENTER);

        statusLabel.setStyle("-fx-text-fill: #b00020;");

        VBox box = new VBox(12, title, subtitle, usernameField, passwordField, buttons, statusLabel);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        return box;
    }

    private void handleLogin() {
        String u = usernameField.getText().trim();
        String p = passwordField.getText();
        if (u.isEmpty() || p.isEmpty()) {
            setError("Username and password required.");
            return;
        }
        if (accounts.login(u, p)) {
            statusLabel.setText(" ");
            nav.onLoginSuccess(u);
        } else {
            setError("Invalid username or password.");
        }
    }

    private void handleCreate() {
        String u = usernameField.getText().trim();
        String p = passwordField.getText();
        if (u.isEmpty() || p.isEmpty()) {
            setError("Username and password required.");
            return;
        }
        if (u.contains("|") || p.contains("|")) {
            setError("'|' is not allowed in usernames or passwords.");
            return;
        }
        if (accounts.userExists(u)) {
            setError("That username is taken.");
            return;
        }
        if (accounts.createAccount(u, p)) {
            setInfo("Account created \u2014 you are now signed in.");
            nav.onLoginSuccess(u);
        } else {
            setError("Could not create account.");
        }
    }

    private void setError(String msg) {
        statusLabel.setStyle("-fx-text-fill: #b00020;");
        statusLabel.setText(msg);
    }

    private void setInfo(String msg) {
        statusLabel.setStyle("-fx-text-fill: #1b5e20;");
        statusLabel.setText(msg);
    }
}
