/*
 * Copyright (c) Ron Coleman
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package sydney.test.shoe;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IUi;
import charlie.test.framework.Perfect;
import java.util.List;

/**
 * This class is the minimalist perfect  test case.
 * @author Ron.Coleman
 */
public class MyPerfectShoeTest extends Perfect implements IUi {
    private Hid you;
    private Hand playerHand;
    private Hand dealerHand;

    private static final int BET_AMT = 5;

    private boolean myTurn = false;

    /**
     * Runs the test.
     */
    public void test() throws Exception {
        // Set shoe property
        System.setProperty("charlie.shoe","sydney.plugin.MyShoe02");

        // Starts the server and logs in using only defaults
        go(this);

        // Now that the game server is ready, to start a game, we just need to
        // send in a bet which in the GUI is like pressing DEAL.
        final int SIDE_BET_AMT = 0;

        bet(BET_AMT,SIDE_BET_AMT);
        info("bet amt: "+BET_AMT+", side bet: "+SIDE_BET_AMT);

        ////////// All test logic at this point done by IUi implementation.

        // Wait for dealer to call end of game.
        assert await(20000);

        // End of scope closes sockets which shuts down client and server.
        info("DONE !");
    }

    /**
     * This method gets invoked whenever a card is dealt.
     * @param hid Target hand
     * @param card Card
     * @param handValues Hand value and soft value
     */
    @Override
    public void deal(Hid hid, Card card, int[] handValues) {

        if (card == null){
            return;
        }

        // add cards to hands
        if (hid.getSeat() == Seat.YOU){
            this.playerHand.hit(card);
        } else if (hid.getSeat() == Seat.DEALER) {
            this.dealerHand.hit(card);
        }

        // Part II, instruction 5
        if (myTurn && hid.getSeat() == Seat.YOU){
            play(hid);
        }



        info("DEAL: "+hid+" card: "+card+" hand values: "+handValues[0]+", "+handValues[1]);
    }

    /**
     * This method gets invoked only once whenever the turn changes.
     * @param hid New hand's turn
     */
    @Override
    public void play(Hid hid) {
        // When it's our turn, stand.
        if (hid.getSeat() == Seat.YOU) {
            myTurn = true;
            if (playerHand.size() < 5) {
                hit(you);
            }
        } else {
            myTurn = false;
            return;
        }
    }

    /**
     * This method gets invoked if a hand breaks.
     * @param hid Target hand
     */
    @Override
    public void bust(Hid hid) {
        // Possible if You or Dealer breaks but it will be one or the other.
        info("BREAK: "+hid);
    }

    /**
     * This method gets invoked for a winning hand.
     * @param hid Target hand
     */
    @Override
    public void win(Hid hid) {
        // Possible if You or Dealer wins, but it'll be one or ther other.
        info("WIN: "+hid);
    }

    /**
     * This method gets invoked for a losing hand.
     * @param hid Target hand
     */
    @Override
    public void lose(Hid hid) {
        // Possible if You or Dealer loses but it will be one or the other.
        info("LOSE: "+hid);
    }

    /**
     * This method gets invoke for a hand that pushes, ie, has same value as dealer's hand.
     * @param hid Target hand
     */
    @Override
    public void push(Hid hid) {
        // Possible if there's a push.
        info("PUSH: "+hid);
    }

    /**
     * This method gets invoked for a (natural) Blackjack hand, Ace+K, Ace+Q, etc.
     * @param hid Target hand
     */
    @Override
    public void blackjack(Hid hid) {
        // Possible if either You or Dealer has a blackjack.
        info("BLACKJACK: "+hid);
    }

    /**
     * This method gets invoked for a 5-card Charlie hand.
     * @param hid Target hand
     */
    @Override
    public void charlie(Hid hid) {
        // assert that hid-seat is YOU
        assert hid.getSeat() == Seat.YOU;

        // assert that player has 5 cards
        assert this.playerHand.size() == 5;

        // player won, so reward their bet
        this.you.setAmt(BET_AMT * 2);
    }

    /**
     * This method get invoked at the start of a game before any cards are dealt.
     * @param hids Hands in the game
     * @param shoeSize Current shoe size, ie, original shoe less cards dealt
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("game STARTING: ");

        // Asserts that the game has two players.
        assert hids.size() == 2;

        for(Hid hid: hids) {
            buffer.append(hid).append(", ");
            // make current hid playerhand if it is YOU
            if (hid.getSeat() == Seat.YOU) {
                this.you = hid;
                this.playerHand = new Hand(hid);
            } else if (hid.getSeat() == Seat.DEALER) {
                // assigns hand with hid to dealer if seat is DEALER
                this.dealerHand = new Hand(hid);
            }
        }
        buffer.append(" shoe size: ").append(shoeSize);
        info(buffer.toString());
    }

    /**
     * This method gets invoked after a game ends and before the start of a new game.
     * @param shoeSize Endind shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        // player's hand value is <= 21
        assert this.playerHand.getValue() <= 21;

        // player's hand is a Charlie
        assert this.playerHand.isCharlie();

        // ONLY the player's hand is a charlie
        assert !this.dealerHand.isCharlie();

        // player's main P&L is 2xBET_AMOUNT
        assert this.you.getAmt() == (2 * BET_AMT);

        // player's side P&L is zero
        assert this.you.getSideAmt() == 0;

        // dealer's hand has two cards
        assert this.dealerHand.size() == 2;

        signal();
        info("ENDING game shoe size: "+shoeSize);
    }

    /**
     * This method gets invoked when the burn card appears, it indicates a
     * re-shuffle is coming after the current game ends.
     */
    @Override
    public void shuffling() {
        info("SHUFFLING");
    }

    /**
     * This method sets the courier.
     * It's not used here because the base test case instantiates a courier for us.
     * @param courier Courier
     */
    @Override
    public void setCourier(Courier courier) {
    }

    /**
     * This method gets invoked when a player requests a split.
     * For instance, a 4+4 split results in two hands, each with two cards,
     * 4+x and 4+y where "x" and "y" are hits to each hand which the dealer
     * automatically performs, respectively.
     * @param newHid New hand split from the original.
     * @param origHid Original hand.
     */
    @Override
    public void split(Hid newHid, Hid origHid) {
        // Not possible for this test case.
        assert false;
    }

    /**
     * Handles insurance requests.
     */
    @Override
    public void insure() {
        // Insurance not supported.
        assert false;
    }
}