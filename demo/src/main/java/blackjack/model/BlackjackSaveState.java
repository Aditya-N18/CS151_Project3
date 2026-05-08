package blackjack.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import blackjack.controller.BlackjackGameLogic;
import blackjack.controller.BlackjackGameLogic.Phase;
import utils.CipherUtil;

//{
//    "phase":   "PLAYER_TURN",
//    "human":  { "balance": 1000, "currentBet": 100, "cards": ["A-H","T-S"] },
//    "ai1":    { "balance": 1000, "currentBet": 100, "cards": [] },
//    "ai2":    { "balance": 1000, "currentBet": 100, "cards": [] },
//    "dealer": { "cards": [] }
//}

public final class BlackjackSaveState {

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    private BlackjackSaveState() {
    }

    public static String encode(BlackjackGameLogic g) {
        if (g == null) {
            throw new IllegalArgumentException("logic cannot be null");
        }
        Dto dto = new Dto();
        dto.phase = g.getPhase().name();
        dto.human = toPlayerDto(g.getHumanPlayer());
        dto.ai1 = toPlayerDto(g.getAiPlayer1());
        dto.ai2 = toPlayerDto(g.getAiPlayer2());
        dto.dealer = toDealerDto(g.getDealer());
        return CipherUtil.encrypt(GSON.toJson(dto));
    }

    public static void decodeInto(String token, BlackjackGameLogic g) {
        if (g == null) {
            throw new IllegalArgumentException("logic cannot be null");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Save token is empty");
        }

        String json;
        try {
            json = CipherUtil.decrypt(token.trim());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Invalid save token", ex);
        }
        if (json == null) {
            throw new IllegalArgumentException("Invalid save token");
        }

        Dto dto;
        try {
            dto = GSON.fromJson(json, Dto.class);
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException(
                    "Save token is not valid JSON", ex);
        }
        if (dto == null) {
            throw new IllegalArgumentException("Save token decoded to empty JSON");
        }

        Phase phase = parsePhase(dto.phase);
        requirePlayer("human", dto.human);
        requirePlayer("ai1", dto.ai1);
        requirePlayer("ai2", dto.ai2);
        requireDealer(dto.dealer);

        // apply state to game logic
        applyPlayer(dto.human, g.getHumanPlayer());
        applyPlayer(dto.ai1, g.getAiPlayer1());
        applyPlayer(dto.ai2, g.getAiPlayer2());
        applyHand(dto.dealer.cards, g.getDealer().getHand());
        g.setPhase(phase);
    }

    private static PlayerDto toPlayerDto(BlackjackPlayer p) {
        PlayerDto d = new PlayerDto();
        d.balance = p.balance;
        d.currentBet = p.currentBet;
        d.cards = handCardCodes(p.getHand());
        return d;
    }

    private static DealerDto toDealerDto(Dealer dealer) {
        DealerDto d = new DealerDto();
        d.cards = handCardCodes(dealer.getHand());
        return d;
    }

    private static List<String> handCardCodes(Hand hand) {
        List<Card> cards = hand.getCards();
        List<String> out = new ArrayList<>(cards.size());
        for (Card c : cards) {
            out.add(c.toCode());
        }
        return out;
    }

    private static Phase parsePhase(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Missing phase");
        }
        try {
            return Phase.valueOf(name);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown phase: " + name, ex);
        }
    }

    private static void requirePlayer(String tag, PlayerDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Missing '" + tag + "' object");
        }
        if (dto.balance < 0 || dto.currentBet < 0) {
            throw new IllegalArgumentException(
                    "Negative balance/bet on '" + tag + "'");
        }
    }

    private static void requireDealer(DealerDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Missing 'dealer' object");
        }
    }

    private static void applyPlayer(PlayerDto dto, BlackjackPlayer p) {
        p.balance = dto.balance;
        p.currentBet = dto.currentBet;
        applyHand(dto.cards, p.getHand());
    }

    private static void applyHand(List<String> codes, Hand hand) {
        hand.clear();
        if (codes == null) {
            return;
        }
        for (String code : codes) {
            if (code == null || code.isBlank()) {
                continue;
            }
            hand.addCard(Card.fromCode(code.trim()));
        }
    }

    private static final class Dto {
        String phase;
        PlayerDto human;
        PlayerDto ai1;
        PlayerDto ai2;
        DealerDto dealer;
    }

    private static final class PlayerDto {
        int balance;
        int currentBet;
        List<String> cards;
    }

    private static final class DealerDto {
        List<String> cards;
    }
}
