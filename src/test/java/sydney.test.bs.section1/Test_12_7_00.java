package sydney.test.bs.section1;

import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import sydney.client.BasicStrategy;

/**
 * Tests my 12 vs dealer 7 which should be HIT.
 * @author Jenna and Sydney
 */
public class Test_12_7_00 extends TestCase {
    /**
     * Runs the test.
     */
    public void test() {
        // Hand needs a hid which we can generate with a seat.
        Hand myHand = new Hand(new Hid(Seat.YOU));

        // Put two cards in the hand, only rank matters, not suit.
        myHand.hit(new Card(4, Card.Suit.DIAMONDS));
        myHand.hit(new Card(8, Card.Suit.HEARTS));

        // Again, only up-card rank matters, not suit.
        Card upCard = new Card(7,Card.Suit.CLUBS);

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // This throws an exception if play is not the expected Play.
        assert play == Play.HIT;
    }
}
