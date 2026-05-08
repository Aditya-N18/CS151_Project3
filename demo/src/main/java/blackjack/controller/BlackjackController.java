package blackjack.controller;

import blackjack.controller.BlackjackGameLogic.Phase;
import blackjack.view.BlackjackGameView;

public class BlackjackController {

    private final BlackjackGameLogic logic;
    private final BlackjackGameView view;

    public BlackjackController(BlackjackGameView view, BlackjackGameLogic logic) {
        this.view = view;
        this.logic = logic;
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
