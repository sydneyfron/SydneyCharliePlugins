package sydney.test.bs.invalid;

import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import sydney.client.BasicStrategy;

public class TestInvalidHandSize extends TestCase{

    public void test() {
        // Hand needs a hid which we can generate with a seat.
        Hand myHand = new Hand(new Hid(Seat.YOU));

        // Only add one card to hand which is not valid
        myHand.hit(new Card(7, Card.Suit.CLUBS));

        Card upCard = new Card(6,Card.Suit.HEARTS);

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // Check to make sure this hand is invalid
        assert !strategy.isValid(myHand);
    }
}
