package sydney.test.bs.section2;

import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import sydney.client.BasicStrategy;

/**
 * Tests my 11 vs dealer 6 which should be DOUBLE DOWN.
 * @author Jenna and Sydney
 */
public class Test_11_6_00 extends TestCase {
    /**
     * Runs the test.
     */
    public void test() {
        // Hand needs a hid which we can generate with a seat.
        Hand myHand = new Hand(new Hid(Seat.YOU));

        // Put two cards in the hand, only rank matters, not suit.
        myHand.hit(new Card(6, Card.Suit.SPADES));
        myHand.hit(new Card(5, Card.Suit.HEARTS));

        // Again, only up-card rank matters, not suit.
        Card upCard = new Card(6,Card.Suit.CLUBS);

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // This throws an exception if play is not the expected Play.
        assert play == Play.DOUBLE_DOWN;
    }
}
