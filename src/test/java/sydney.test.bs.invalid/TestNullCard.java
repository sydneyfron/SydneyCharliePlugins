package sydney.test.bs.invalid;

import charlie.card.Hid;
import charlie.dealer.Seat;
import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import sydney.client.BasicStrategy;


public class TestNullCard extends TestCase{
    public void test() {
        BasicStrategy strategy = new BasicStrategy();

        // Valid hand
        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(9, Card.Suit.DIAMONDS));
        myHand.hit(new Card(5, Card.Suit.DIAMONDS));

        // Invalid null upCard
        Card upCard = null;

        // The upCard is null, so isValid should return false, so negate to test
        assert !strategy.isValid(upCard);

        // Test the dual parameter version
        assert !strategy.isValid(myHand, upCard);
    }
}

