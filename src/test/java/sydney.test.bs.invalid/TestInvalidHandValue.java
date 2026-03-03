package sydney.test.bs.invalid;


import charlie.card.Hid;
import charlie.dealer.Seat;
import junit.framework.TestCase;
import charlie.card.Card;
import charlie.card.Hand;
import sydney.client.BasicStrategy;


public class TestInvalidHandValue extends TestCase{
    public void test() {
        BasicStrategy strategy = new BasicStrategy();

        // Invalid hand because it is more than 21
        Hand myHand = new Hand(new Hid(Seat.YOU));
        myHand.hit(new Card(10, Card.Suit.DIAMONDS));
        myHand.hit(new Card(9, Card.Suit.DIAMONDS));
        myHand.hit(new Card(8, Card.Suit.DIAMONDS));

        // Valid upCard
        Card upCard = new Card(10, Card.Suit.DIAMONDS);

        // myHand value is too high, so isValid should return false, so negate to test
        assert !strategy.isValid(myHand);
    }
}

