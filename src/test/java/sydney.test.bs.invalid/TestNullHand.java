package sydney.test.bs.invalid;

import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import sydney.client.BasicStrategy;


public class TestNullHand extends TestCase{
    public void test() {
        BasicStrategy strategy = new BasicStrategy();


        // Makes a player hand that is null
        Hand hand = null;
        Card upCard = new Card(10, Card.Suit.DIAMONDS); // Valid up-card


        // The hand is null, so isValid should return false, so negate to test
        assert !strategy.isValid(hand);


        // Test the dual parameter version
        assert !strategy.isValid(hand, upCard);
    }
}

