package blackjack.controller;

import blackjack.view.BlackjackGameView;

public class BlackjackController {

    private final BlackjackGameLogic logic;
    private final BlackjackGameView view;

    public BlackjackController(BlackjackGameView view, BlackjackGameLogic logic) {
        this.view = view;
        this.logic = logic;
    }

    public void handleHit() {
        logic.humanHit();
        view.refresh();

        if (logic.isHumanBust()) {
            view.setStatus("Player Bust!");
        }
    }

    public void handleStand() {
        logic.humanStand();
        view.setStatus("Round Finished");
        view.refresh();
    }
}
