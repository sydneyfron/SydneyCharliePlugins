package sydney.test.client;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IUi;
import charlie.test.framework.Perfect;
import java.util.List;
import java.lang.reflect.Field;

/**
 * Automated integration test for the side bet plugin.
 */
public class PerfectSideBet extends Perfect implements IUi {
    private Hid you;
    private Hand playerHand;
    private Hand dealerHand;

    private int gameNumber = 0;
    private double bankroll = 1000.0;
    private boolean myTurn = false;

    /**
     * Uses reflection to reset the framework's wait condition so it can be reused.
     */
    private void resetWait() {
        try {
            Field field = Perfect.class.getDeclaredField("trucking");
            field.setAccessible(true);
            field.set(this, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the test.
     */
    public void test() throws Exception {
        // Set properties for the side bet rule and side bet shoe
        System.setProperty("charlie.shoe", "charlie.sidebet.test.Shoe");
        System.setProperty("charlie.sidebet.rule", "charlie.sidebet.rule.SideBetRule");

        // Start Server
        go(this);

        // === GAME 0 (Push) ===
        bet(25, 0);
        assert await(20000);
        resetWait();

        // === GAMES 1 through 9 ===
        for (int i = 1; i <= 9; i++) {
            bet(25, 10);
            assert await(20000);
            resetWait();
        }

        // Final assertion from Lab 9
        info("Final Bankroll: $" + bankroll);
        assert bankroll == 1435.0;
        info("DONE!");
    }

    @Override
    public void deal(Hid hid, Card card, int[] handValues) {
        if (card == null) return;

        if (hid.getSeat() == Seat.YOU) {
            this.playerHand.hit(card);
        } else if (hid.getSeat() == Seat.DEALER) {
            this.dealerHand.hit(card);
        }

        if (myTurn && hid.getSeat() == Seat.YOU) {
            play(hid);
        }
    }

    @Override
    public void play(Hid hid) {
        if (hid.getSeat() == Seat.YOU) {
            myTurn = true;

            // Games 0-4 involve 3-card hands (e.g. 7+9+3), Games 5-9 involve 2-card hands.
            int expectedSize = (gameNumber <= 4) ? 3 : 2;

            if (playerHand.size() < expectedSize) {
                hit(you);
            } else {
                stay(you);
            }
        } else {
            myTurn = false;
        }
    }

    @Override
    public void push(Hid hid) {
        assert hid.getSeat() == Seat.YOU;

        // Game 0: Main bet Push, Side bet none
        if (gameNumber == 0) {
            assert hid.getAmt() == 0.0;
            assert hid.getSideAmt() == 0.0;
            bankroll += 0;
        } else {
            assert false;
        }
    }

    @Override
    public void win(Hid hid) {
        assert hid.getSeat() == Seat.YOU;

        if (gameNumber == 1) {
            // Game 1: Win/Win (7+9+3 vs K+8)
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == 30.0;
            bankroll += 55.0;
        } else if (gameNumber == 2) {
            // Game 2: Win/Lose (9+7+3 vs K+8)
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == -10.0;
            bankroll += 15.0;
        } else if (gameNumber == 5) {
            // Game 5: Win/Win (K+Q suited vs K+8)
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == 250.0; // Royal Match pays 25:1
            bankroll += 275.0;
        } else if (gameNumber == 6) {
            // Game 6: Win/Lose (K+Q unsuited vs K+8)
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == -10.0;
            bankroll += 15.0;
        } else if (gameNumber == 7) {
            // Game 7: Win/Win (8+5 vs K+6+K) Exactly 13 pays 1:1
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == 10.0;
            bankroll += 35.0;
        } else if (gameNumber == 8) {
            // Game 8: Win/Win (7+6 vs K+6+K) Super 7 pays 3:1
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == 30.0;
            bankroll += 55.0;
        } else if (gameNumber == 9) {
            // Game 9: Win/Lose (6+8 vs K+6+K)
            assert hid.getAmt() == 25.0;
            assert hid.getSideAmt() == -10.0;
            bankroll += 15.0;
        } else {
            assert false;
        }
    }

    @Override
    public void lose(Hid hid) {
        assert hid.getSeat() == Seat.YOU;

        if (gameNumber == 3) {
            // Game 3: Lose/Win (7+9+3 vs K+10)
            assert hid.getAmt() == -25.0;
            assert hid.getSideAmt() == 30.0;
            bankroll += 5.0;
        } else if (gameNumber == 4) {
            // Game 4: Lose/Lose (9+7+3 vs K+10)
            assert hid.getAmt() == -25.0;
            assert hid.getSideAmt() == -10.0;
            bankroll -= 35.0;
        } else {
            assert false;
        }
    }

    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        assert hids.size() == 2;

        for (Hid hid : hids) {
            if (hid.getSeat() == Seat.YOU) {
                this.you = hid;
                this.playerHand = new Hand(hid);
            } else if (hid.getSeat() == Seat.DEALER) {
                this.dealerHand = new Hand(hid);
            }
        }
    }

    @Override
    public void endGame(int shoeSize) {
        gameNumber++;
        signal();
    }

    // Unused callbacks
    @Override public void bust(Hid hid) { }
    @Override public void blackjack(Hid hid) { }
    @Override public void charlie(Hid hid) { }
    @Override public void shuffling() { }
    @Override public void setCourier(Courier courier) { }
    @Override public void split(Hid newHid, Hid origHid) { assert false; }
    @Override public void insure() { assert false; }
}