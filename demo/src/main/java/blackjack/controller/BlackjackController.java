package blackjack.controller;

import blackjack.controller.BlackjackGameLogic.Phase;
import blackjack.model.BlackjackSaveState;
import blackjack.view.BlackjackGameView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

public class BlackjackController {

    private final BlackjackGameLogic logic;
    private final BlackjackGameView view;

    public BlackjackController(BlackjackGameView view, BlackjackGameLogic logic) {
        this.view = view;
        this.logic = logic;
    }

    public BlackjackGameLogic getLogic() {
        return logic;
    }

    /**
     * Show a modal dialog with the encrypted save token in a non-editable but
     * selectable {@link TextArea}, ready for the user to copy.
     */
    public void saveGame() {
        String token = BlackjackSaveState.encode(logic);

        TextArea area = new TextArea(token);
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefRowCount(6);
        area.setPrefColumnCount(48);
        area.setStyle("-fx-font-family: 'monospace';");

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Save State");
        dialog.setHeaderText("Copy this save string to keep your progress.");
        dialog.getDialogPane().setContent(area);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        // Pre-select the whole token so Ctrl+C is one keystroke.
        area.requestFocus();
        area.selectAll();
        dialog.showAndWait();
    }

    public void handleHit() {
        if (logic.getPhase() != Phase.PLAYER_TURN) {
            return;
        }

        logic.humanHit();

        if (logic.getPhase() == Phase.GAME_OVER) {
            view.setStatus("Game Over - You're out of money. Use the toolbar to return to Main Menu.");
        } else if (logic.getPhase() == Phase.ROUND_OVER) {
            if (logic.isHumanBust()) {
                view.setStatus("Player Bust! Round finished.");
            } else {
                view.setStatus("Round finished.");
            }
        }

        view.refresh();
    }

    public void handleStand() {
        if (logic.getPhase() != Phase.PLAYER_TURN) {
            return;
        }

        logic.humanStand();

        if (logic.getPhase() == Phase.GAME_OVER) {
            view.setStatus("Game Over - You're out of money. Use the toolbar to return to Main Menu.");
        } else {
            view.setStatus("Round finished.");
        }

        view.refresh();
    }

    public void handleNextRound() {
        if (logic.getPhase() != Phase.ROUND_OVER) {
            return;
        }
        logic.nextRound();
        view.setStatus("Place your bet to start the next round.");
        view.refresh();
    }
}
